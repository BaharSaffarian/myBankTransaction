import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;

public class JsonParser {
    private final static String filePath="src\\main\\resources\\core.json";
    private HashMap<String,Deposit> deposits;
    private int port;
    private String outLog;

    private String depositCustomer;
    private String depositId;
    BigDecimal depositInitBalance;
    BigDecimal depositUpperBound;

    public void pars(){
        try {
            deposits=new HashMap<String, Deposit>();
            FileReader fileReader = new FileReader(filePath);
            JSONParser jsonParser=new JSONParser();
            JSONObject jsonObject=(JSONObject) jsonParser.parse(fileReader);
            port=Integer.parseInt(jsonObject.get("port").toString());
            outLog=jsonObject.get("outLog").toString();
            JSONArray jsonDepositArray=(JSONArray)jsonObject.get("deposits");
            Iterator <JSONObject> jsonDepositArrayIterator=jsonDepositArray.iterator();
            while (jsonDepositArrayIterator.hasNext()){
                JSONObject jsonDepositObject=jsonDepositArrayIterator.next();
                depositCustomer=jsonDepositObject.get("customer").toString();
                depositId=jsonDepositObject.get("id").toString();
                depositInitBalance=new BigDecimal(jsonDepositObject.get("initialBalance").toString());
                depositUpperBound=new BigDecimal(jsonDepositObject.get("upperBound").toString());
                //System.out.println(depositCustomer+","+depositId+","+depositInitBalance+","+depositUpperBound);
                deposits.put(depositId, new Deposit(depositCustomer
                                                    ,depositId
                                                    ,depositInitBalance
                                                    ,depositUpperBound));
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }catch (ParseException e){
            e.printStackTrace();
        }
    }
    public HashMap <String,Deposit> getDepositArrayList(){
        return deposits;
    }
    public int getPort(){
        return port;
    }
    public String getOutLog(){
        return outLog;
    }
}
