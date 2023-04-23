import java.io.*;
import java.net.*;
import java.util.ArrayList;


public class workerServer extends Thread {
    //Custom ArrayList for waypoints
    private waypointsList<Waypoint> wp ;
    //ArrayList that contains ActionforWorkers objects
    private ArrayList<ActionforWorkers> wl;
    //Socket that receives requests
    ServerSocket requestWorkerSocket;
    
    //Socket that handles the connection 
    Socket providerWorkerSocket;

    ObjectInputStream in;
	ObjectOutputStream out;
    


    //Sum of Workers
    private int workers;
    //Constructor
    public workerServer(int workers, waypointsList<Waypoint> wp) {
        this.workers = workers;
        this.wp = wp;
	}


	public void run() {

        int port = 4320;
            
        try {

            
            System.out.println("Server started. Waiting for worker to connect...");
            
            int connectedWorkers = 0;
            
            while (connectedWorkers < workers) {

                requestWorkerSocket = new ServerSocket(port);
                providerWorkerSocket = requestWorkerSocket.accept();
                System.out.println("Worker connected."); 
                connectedWorkers+=1;
                port += 1;

                wl.add(new ActionforWorkers(providerWorkerSocket));

            }

            System.out.println("All workers are connected successfully");    
            //useful counters
            int waypointsInAL;
            int waypointsPerWorker;
            //Temporary ArrayList for Waypoints
            ArrayList<Waypoint> temp = new ArrayList<Waypoint>();

            while(true){
                waypointsInAL = wp.size();
                waypointsPerWorker = waypointsInAL / workers;
                for(int i = 0; i < wl.size() - 1; i++){
                    temp = wp.getNWaypoints(waypointsPerWorker);
                    wl.get(i).run(temp);
                }

                temp = wp.getAll();
                wl.get(wl.size()).run(temp);                             
            }
            
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
