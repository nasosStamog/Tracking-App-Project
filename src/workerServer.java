import java.io.*;
import java.net.*;


public class workerServer extends Thread {
    private waypointsList<Waypoint> wp ;

     //Socket that receives requests
    ServerSocket requestWorkerSocket;
    
    //Socket that handles the connection 
    Socket providerWorkerSocket;

    //Sum of Workers
    private int workers;
    //Constructor
    public workerServer(int workers, waypointsList<Waypoint> wp) {
        this.workers = workers;
        this.wp = wp;
	}


	public void run() {

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
