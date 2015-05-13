import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;


public class BankServer {
    private ArrayList <Deposit> depositArrayList;
    private int port;
    private String logFileName;
    private ServerSocket serverSocket;
    public BankServer(){
        JsonParser jsonParser= new JsonParser();
        jsonParser.pars();
        port=jsonParser.getPort();
        logFileName=jsonParser.getOutLog();
        depositArrayList=jsonParser.getDepositArrayList();
        try {
            serverSocket = new ServerSocket(port);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public void startServicing(){
        while(true){
            try {
                new BankServerHandler(serverSocket.accept());
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public static void main(String args[]){
        System.out.println(new BankServer().depositArrayList);
    }
}
