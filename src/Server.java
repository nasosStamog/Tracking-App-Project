import java.io.*;
import java.net.*;
import java.io.File;

public class Server {

    //Socket that receives requests from clients
    ServerSocket requestClientSocket;
    
    //Socket that handles the connection with clients
    Socket providerClientSocket;

    //Socket that receives requests
    ServerSocket requestWorkerSocket;
    
    //Socket that handles the connection 
    Socket providerWorkerSocket;


    public void openClientServer() throws IOException {

        
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

                File gpxFile = new File("C:/Users/Martinisk/Desktop/Tracking-App/gpx_files_in_master/" + Filename);
                counter += 1;
                FileOutputStream fos = new FileOutputStream(gpxFile);
                fos.write(gpxBytes);
                fos.close();
               
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
    
    public void openWorkerServer(int workers) throws IOException {

        try {

            requestWorkerSocket = new ServerSocket(4320);
            System.out.println("Server started. Waiting for worker to connect...");
            
            int connectedWorkers = 0;
            
            while (connectedWorkers < workers) {
                providerWorkerSocket = requestWorkerSocket.accept();
                System.out.println("Worker connected.");
                //Actions for workers
            

                connectedWorkers+=1;
            }
            System.out.println("All workers are connected successfully");
            
            }catch (IOException e){
                e.printStackTrace();
            } finally {
                try {
                    providerWorkerSocket.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
        }
    }
}
