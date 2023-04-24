import java.io.*;
import java.net.*;
import java.util.ArrayList;


public class workerServer extends Thread { //Our server must be thread to run 
                                           //concurently with the other server which is responsible for client

    //Custom ArrayList for waypoints
    private waypointsList<Waypoint> wp ;
    //ArrayList that contains ActionforWorkers objects-threads
    private ArrayList<ActionforWorkers> wl;
    //Socket that handles the connection 
    Socket providerWorkerSocket;
    //Socket that receives requests
    ServerSocket requestWorkerSocket; 
    //Sum of Workers
    private int workers;

    //Constructor
    public workerServer(int workers, waypointsList<Waypoint> wp) {
        this.workers = workers;
        this.wp = wp;
        //The "Waypoint list" is common memory space between our two main servers
        //Client Server fills it with waypoint-objects and Worker Server works on them
	}

	public void run() {

        int port = 4320;
            
        try { 

            requestWorkerSocket = new ServerSocket(port);           
            System.out.println("Server started. Waiting for worker to connect...");            
            
            int connectedWorkers = 0;            
            while (connectedWorkers < workers) {                
                providerWorkerSocket = requestWorkerSocket.accept();
                System.out.println("Worker connected."); 
                connectedWorkers+=1;
                wl.add(new ActionforWorkers(providerWorkerSocket));

            }

            System.out.println("All workers are connected successfully");
            

            //Round robin, the threads that have been created in the while above 
            //start with specific order

            int order = 0;

            while (true){
                //wl.get(order).set(wp.returnNWaypoints);
                //wl.get(order).start();
                order+=1;
                if(order > wl.size()){order = 0;}
            }
            

            /*
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
             */
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
