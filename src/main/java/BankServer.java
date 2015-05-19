import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Logger;


public class BankServer implements Runnable {
    public static BankServer server = new BankServer();
    private HashMap<String, Deposit> depositHashMap;
    private Logger logger;
    private ServerSocket serverSocket;

    private BankServer() {
        JsonParser jsonParser = new JsonParser();
        jsonParser.pars();
        depositHashMap = jsonParser.getDepositArrayList();
        try {
            FileHandler fileHandler = new FileHandler("src\\main\\resources\\" + jsonParser.getOutLog());
            logger = Logger.getLogger("server");
            logger.addHandler(fileHandler);
            serverSocket = new ServerSocket(jsonParser.getPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Deposit getDepositById(String id) {

        if (depositHashMap.containsKey(id)) {
            return depositHashMap.get(id);
        } else
            return null;
    }

    public Logger getLogger() {
        return logger;
    }

    public void run() {
        try {
            serverSocket.setSoTimeout(10000);
            while (true) {
                new BankServerHandler(serverSocket.accept());
            }
        } catch (SocketTimeoutException e) {
            logger.info("Server job terminates");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {

        BankServer.server.run();
    }
}
