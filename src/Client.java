import java.io.*;
import java.net.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

public class Client {
    //Client Class: This class runs on every client
    //Sends gpx files and receives the calculated results

    String filePath;
    ArrayList<Object> configList = new ArrayList<Object>();


    public Client(String filePath) {

        this.filePath = filePath; 
        try (InputStream input = new FileInputStream("config/configc.properties")) {

            Properties prop2 = new Properties();
            // load the properties file
            prop2.load(input);
            configList.add(prop2.getProperty("ipc1"));
            configList.add(Integer.parseInt(prop2.getProperty("portc1")));
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
    }


    public void run() {
        ObjectOutputStream out= null ;
        ObjectInputStream in = null ;
        Socket requestSocket= null ;
        FileInputStream fis = null; 
		DataOutputStream dos = null;


        try {
            /* Create socket for contacting the server on port 4321*/
            requestSocket = new Socket((String) configList.get(0), (Integer) configList.get(1));

            /* Create the streams to send and receive data from server */
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            in = new ObjectInputStream(requestSocket.getInputStream());

            // Read GPX file into a byte array
			File gpxFile = new File(filePath);
			byte[] gpxBytes = new byte[(int) gpxFile.length()];
			fis = new FileInputStream(gpxFile);
			fis.read(gpxBytes);

			// Send GPX file to server
			OutputStream os = requestSocket.getOutputStream();
			dos = new DataOutputStream(os);
			dos.writeInt(gpxBytes.length);
			dos.write(gpxBytes);
			dos.flush();

            Double totalDistance = (Double) in.readObject();
            Double averageSpeed = (Double) in.readObject();
            Double elevation = (Double) in.readObject();
            Double totalTime = (Double) in.readObject();
            String user = (String) in.readObject();

            DecimalFormat df = new DecimalFormat("#.##");
            System.out.println(user +": ");
            System.out.println("    Total Distance " + df.format(totalDistance) + " kilometers.");
            System.out.println("    Average Speed " + df.format(averageSpeed) + " km/h.");
            System.out.println("    Total Elevation " + df.format(elevation) + " meters.");
            System.out.println("    Total Time " + df.format(totalTime) + " minutes.");

        } catch (UnknownHostException unknownHost) {
            System.err.println("You are trying to connect to an unknown host!");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close(); out.close();
                requestSocket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    public static void main(String args[]) throws InterruptedException {
        Scanner in = new Scanner(System.in);
        String inGpx;

        ArrayList<Client> clients = new ArrayList<>();

        while (true) {
            System.out.print("Please give your GPX route file: ");
            inGpx = in.nextLine();
            Client client = new Client("gpxs/"+inGpx);
            client.run();
            
            clients.add(client);

            // Wait for all running threads to finish            
            System.out.print("Do you want to add another GPX file? (Y/N) ");
            String choice = in.nextLine();
            if (choice.equalsIgnoreCase("N")) {
                break;
            }
        }
        in.close();
    }
}




