import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;


public class BankServer implements Runnable{
    public static BankServer server=new BankServer();
    private HashMap<String, Deposit> depositHashMap;
    private int port;
    private String logFileName;
    private ServerSocket serverSocket;

    private BankServer(){
        JsonParser jsonParser= new JsonParser();
        jsonParser.pars();
        port=jsonParser.getPort();
        logFileName=jsonParser.getOutLog();
        depositHashMap=jsonParser.getDepositArrayList();
        try {
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

    public void run(){
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
