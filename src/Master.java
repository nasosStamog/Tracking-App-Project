import java.io.*;
import java.net.*;
import java.util.Properties;
import java.util.ArrayList;
import java.util.Enumeration;

public class Master {
    //Master Class: This the class that runs on th Master Machine. It is responsible for take requests from clients
    //send them in workers and then returns the results back to clients.
    //Master also keeps a record with the stats of every user

    public static void main(String args[]) throws ClassNotFoundException {
        new Master().openServer();
    }

    /* Define the socket that receives requests */
    ServerSocket s;
    /* Define the socket that is used to handle the connection */
    Socket providerSocket;
    Socket requestSocket;
    Socket requestSocket1;
    Socket requestSocket2;
    ObjectInputStream in;
    ObjectOutputStream out;
    ObjectInputStream win;
    ObjectOutputStream wout;

    //Array that stores worker's ips and ports
    ArrayList<Object> configList = new ArrayList<Object>();

    //Array that stores worker's ports
    ArrayList<Object> configList2 = new ArrayList<Object>();
    
    //Constructor of Master. Loads important information from config file
    public Master(){
        
        try (InputStream input = new FileInputStream("config/config.properties")) {

            Properties prop = new Properties();
            
            // load the properties file
            prop.load(input);

            //configList load
            @SuppressWarnings("unchecked")
            Enumeration<String> e = (Enumeration<String>) prop.propertyNames();
            while (e.hasMoreElements())
            {
                String key = e.nextElement().toString();
                configList2.add(prop.getProperty(key));              
                
            }
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }





    //Main method of Master.
    //Receives requests from clients and starts threads that communicate with Workers to do the computation.
    //Master threads (ActionsForMasterClass) do requests to Workers that are stored in config file.
    void openServer() throws ClassNotFoundException {
        try {

            /* Create Server Socket */
            s = new ServerSocket(4321);
            System.out.println("Waiting For Clients...");

            while (true) {
                /* Accept the connection */
                providerSocket = s.accept();
                System.out.println("New Client request");
                Thread d = new ActionsForMaster(providerSocket, configList2);
                d.start();
            }

        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            try {
                providerSocket.close();
                requestSocket.close();
                requestSocket1.close();
                requestSocket2.close();
                
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}