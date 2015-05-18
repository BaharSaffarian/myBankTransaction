import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Logger;


public class BankServer implements Runnable{
    public static BankServer server=new BankServer();
    private HashMap<String, Deposit> depositHashMap;
    private int port;
    FileHandler fileHandler;
    Logger logger;
    private ServerSocket serverSocket;

    private BankServer(){
        JsonParser jsonParser= new JsonParser();
        jsonParser.pars();
        port=jsonParser.getPort();
        depositHashMap=jsonParser.getDepositArrayList();
        try {
            fileHandler=new FileHandler("src\\main\\resources\\"+jsonParser.getOutLog());
            logger=Logger.getLogger("server");
            logger.addHandler(fileHandler);
            serverSocket = new ServerSocket(port);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    Deposit getDepositById(String id){

        if(depositHashMap.containsKey(id)) {
            return depositHashMap.get(id);
        }else
            return null;
    }

    public Logger getLogger() {
        return logger;
    }

    public void run(){
        System.out.println(depositHashMap);
        while(true){
            try {
                new BankServerHandler(serverSocket.accept());
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public static void main(String args[]){
        BankServer.server.run();
    }
}
