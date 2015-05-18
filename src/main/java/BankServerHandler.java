import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Logger;

public class BankServerHandler extends Thread {
    private Socket socket;

    public BankServerHandler(Socket socket) {
        this.socket = socket;
        start();
    }

    public void run(){

        try{
            ObjectOutputStream out=new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in=new ObjectInputStream(socket.getInputStream());
            String clientTerminalId= in.readUTF();
            Logger logger=BankServer.server.getLogger();
            logger.info("Connected to "+clientTerminalId);
            while(true){
                Transaction receivedTransaction=(Transaction) in.readObject();
                logger.info("Received: client Terminal "+clientTerminalId+" , "+ receivedTransaction.toString());
                receivedTransaction.setSuccess(false);
                Deposit deposit=BankServer.server.getDepositById(receivedTransaction.getDepositId());
                if(deposit!=null && "deposit".equals(receivedTransaction.getType())){
                    synchronized(deposit){

                        if(deposit.validateDepositOperand(receivedTransaction.getAmount())){
                            receivedTransaction.setDepositNewBalance(deposit.deposit(receivedTransaction.getAmount()));
                            receivedTransaction.setSuccess(true);
                            logger.info("Deposit Operation performs: client terminal " +clientTerminalId+" , "+ receivedTransaction.toString());
                        }else{
                            logger.info("Client Terminal: "+clientTerminalId+" , Transaction "+ receivedTransaction.getId()+" : The deposit operand violates the upper bound");
                            receivedTransaction.setFailMessage("The deposit operand violates the upper bound");
                        }

                    }
                }else if(deposit!=null && "withdraw".equals(receivedTransaction.getType())){
                    synchronized(deposit){

                        if(deposit.validateWithdrawOperand(receivedTransaction.getAmount())){
                            //sleep(1);
                            receivedTransaction.setDepositNewBalance(deposit.withdraw(receivedTransaction.getAmount()));
                            receivedTransaction.setSuccess(true);
                            logger.info("Withdraw Operation performs: client terminal "+ clientTerminalId+" , " + receivedTransaction.toString());
                        }else {
                            logger.info("Client Terminal: "+clientTerminalId+" , Transaction "+ receivedTransaction.getId()+ " : The deposit balance is lower than the requested amount");
                            receivedTransaction.setFailMessage("The deposit balance is lower than the requested amount");
                        }
                    }
                }else{
                    logger.info("client terminal "+clientTerminalId+" transaction "+receivedTransaction.getId()+" Request is not valid");
                    receivedTransaction.setFailMessage("Request is not valid");
                }
                out.writeObject(receivedTransaction);
                logger.info("The results for Transaction "+ receivedTransaction.getId()+ " sends back to terminal " + clientTerminalId);
            }
        }catch (IOException e){
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }/*catch (InterruptedException e){
            e.printStackTrace();
        }*/
    }
}
