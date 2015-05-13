import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by DOTIN SCHOOL 3 on 5/13/2015.
 */
public class JsonParser {
    private final static String filePath="src\\main\\resources\\core.json";
    private ArrayList <Deposit> depositArrayList;
    private int port;
    private String outLog;

    private String depositCustomer;
    private String depositId;
    BigDecimal depositInitBalance;
    BigDecimal depositUpperBound;

    public void pars(){
        try {
            depositArrayList=new ArrayList<Deposit>();
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
                depositArrayList.add(new Deposit(depositCustomer
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
    public ArrayList<Deposit> getDepositArrayList(){
        return depositArrayList;
    }
    public int getPort(){
        return port;
    }
    public String getOutLog(){
        return outLog;
    }
}
