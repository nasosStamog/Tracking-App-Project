import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.io.File;

public class clientServer extends Thread {
    private waypointsList<Waypoint> wp ;


    public clientServer(waypointsList<Waypoint> waypointsList){
        this.wp = waypointsList;
    }

    //Socket that receives requests from clients
    ServerSocket requestClientSocket;
    
    //Socket that handles the connection with clients
    Socket providerClientSocket;

    //shared waypoints list
    
    //Create the parser object
    Parser prs = new Parser();

    public void run() {

        
        int counter = 0;
        String Filename;

        try {

            requestClientSocket = new ServerSocket(4321);
            System.out.println("Server started. Waiting for client to connect...");


            while (true) {
                providerClientSocket = requestClientSocket.accept();
                System.out.println("Client connected.");
                //Actions for kinito
                InputStream is = providerClientSocket.getInputStream();
                DataInputStream dis = new DataInputStream(is);
                int length = dis.readInt();
                byte[] gpxBytes = new byte[length];
                dis.readFully(gpxBytes);
                dis.close();
                
                Filename = "route" +Integer.toString(counter) + ".gpx";
                Filename = "C:/Users/Martinisk/Desktop/Tracking-App/gpx_files_in_master/" + Filename;
                
                File gpxFile = new File(Filename);
                counter += 1;
                FileOutputStream fos = new FileOutputStream(gpxFile);
                fos.write(gpxBytes);
                fos.close();
                
                prs.parseGpx(Filename);
                ArrayList<Waypoint> temp = prs.getWaypoints();

                for(int i = 0; i < temp.size(); i++ ){
                    wp.add(temp.get(i));
                }
                temp.clear();

                System.out.println("Size of wp : " + wp.size());            
               
            }
        
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            try {
                providerClientSocket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    } 
}
