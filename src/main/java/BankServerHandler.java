import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

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
            while(true){
                Transaction receivedTransaction=(Transaction) in.readObject();
                receivedTransaction.setSuccess(false);
                Deposit deposit=BankServer.server.getDepositById(receivedTransaction.getDepositId());
                if(deposit!=null && receivedTransaction.getType()=="deposit"){
                    synchronized(deposit){
                        if(deposit.validateDepositOperand(receivedTransaction.getAmount())){
                            receivedTransaction.setDepositNewBalance(deposit.deposit(receivedTransaction.getAmount()));
                            receivedTransaction.setSuccess(true);
                        }
                    }
                }else if(deposit!=null && receivedTransaction.getType()=="withdraw"){
                    synchronized(deposit){
                        if(deposit.validateWithdrawOperand(receivedTransaction.getAmount())){
                            receivedTransaction.setDepositNewBalance(deposit.withdraw(receivedTransaction.getAmount()));
                            receivedTransaction.setSuccess(true);
                        }
                    }
                }
                out.writeObject(receivedTransaction);
            }
        }catch (IOException e){
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}
