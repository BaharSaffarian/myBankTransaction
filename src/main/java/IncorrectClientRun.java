/**
 * Created by DOTIN SCHOOL 3 on 5/18/2015.
 */
public class IncorrectClientRun {
    public static void main(String args[]) {

        try {
            new Client().join();
            new Client();
            new Client();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
