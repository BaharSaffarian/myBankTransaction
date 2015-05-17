import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.math.BigDecimal;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class Client extends Thread {
    private static int threadNumber=0;
    InputStream xmlInput;
    private String terminalId;
    private String terminalType;
    private String serverIp;
    private int serverPort;
    private String outLogPath;
    private Socket socket;
    ArrayList <Transaction> transactionArrayList= new ArrayList<Transaction>();
    public Client() {
        super(""+ ++threadNumber);
        try {
            xmlInput=new FileInputStream("src\\main\\resources\\terminal"+this.getName()+".xml");
        }catch (IOException e){
            e.printStackTrace();
        }
        SaxParser saxParser=new SaxParser();
        saxParser.extractObjects();
        start();
    }

    public void run(){
        try {
            socket=new Socket(serverIp,serverPort);
            ObjectOutputStream out=new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in=new ObjectInputStream(socket.getInputStream());

            Iterator <Transaction> iterator= transactionArrayList.iterator();

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder =dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            Element transactionsElement = doc.createElement("transactions");
            doc.appendChild(transactionsElement);
            while(iterator.hasNext()){
                out.writeObject(iterator.next());
                Transaction responseTransaction=(Transaction) in.readObject();

                Element transactionElement=doc.createElement("transaction");

                Attr idAttr=doc.createAttribute("id");
                idAttr.setValue(responseTransaction.getId());
                transactionElement.setAttributeNode(idAttr);

                Attr typeAttr=doc.createAttribute("type");
                typeAttr.setValue(responseTransaction.getType());
                transactionElement.setAttributeNode(typeAttr);

                Attr amountAttr=doc.createAttribute("amount");
                amountAttr.setValue(responseTransaction.getAmount().toString());
                transactionElement.setAttributeNode(amountAttr);

                Attr depositAttr=doc.createAttribute("deposit");
                depositAttr.setValue(responseTransaction.getDepositId());
                transactionElement.setAttributeNode(depositAttr);

                Attr successAttr=doc.createAttribute("success");
                successAttr.setValue((responseTransaction.isSuccess()==true ? "succeed" :"fail"));
                transactionElement.setAttributeNode(successAttr);

                Attr balanceAttr=doc.createAttribute("newBalance");
                balanceAttr.setValue(responseTransaction.getDepositNewBalance()!=null ?
                                        responseTransaction.getDepositNewBalance().toString() :
                                        "");
                transactionElement.setAttributeNode(balanceAttr);

                transactionsElement.appendChild(transactionElement);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("src\\main\\resources\\response.xml"));
            transformer.transform(source, result);

        }catch (IOException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }catch (ParserConfigurationException e){
            e.printStackTrace();
        }catch (TransformerException e){
            e.printStackTrace();
        }

    }

    public class SaxParser {

        class SaxHandler extends DefaultHandler {
            Transaction currentTransaction=new Transaction();
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                if ("terminal".equals(qName)) {
                    terminalId=attributes.getValue("id");
                    terminalType=attributes.getValue("type");
                } else if ("server".equals(qName)) {
                    serverIp=attributes.getValue("ip");
                    serverPort=Integer.parseInt(attributes.getValue("port"));
                } else if ("outLoge".equals(qName)) {
                    outLogPath=attributes.getValue("path");
                } else if ("transaction".equals(qName)) {
                    currentTransaction.setId(attributes.getValue("id"));
                    currentTransaction.setType(attributes.getValue("type"));
                    currentTransaction.setAmount(new BigDecimal(attributes.getValue("amount")));
                    currentTransaction.setDepositId(attributes.getValue("deposit"));
                    transactionArrayList.add(currentTransaction);
                }

            }
        }

        void extractObjects() {

            try {
                SAXParserFactory spf = SAXParserFactory.newInstance();
                SAXParser sp = spf.newSAXParser();
                sp.parse(xmlInput, new SaxHandler());

            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
        }


    }

    public static void main(String args[]){
        new Client();
    }

}
