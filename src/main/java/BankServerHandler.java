import java.net.Socket;

/**
 * Created by DOTIN SCHOOL 3 on 5/13/2015.
 */
public class BankServerHandler extends Thread {
    private Socket socket;

    public BankServerHandler(Socket socket) {
        this.socket = socket;
        start();
    }

    public void run(){

    }
}
