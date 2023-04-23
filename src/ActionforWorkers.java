import java.io.*;
import java.net.*;
import java.util.ArrayList;



public class ActionforWorkers extends Thread{

    ObjectInputStream in;
    ObjectOutputStream out;
    waypointsList wp;


    public ActionforWorkers(Socket connection){

        try {
				
			in = new ObjectInputStream(connection.getInputStream());
			out = new ObjectOutputStream(connection.getOutputStream());

            wp = (waypointsList) in.readObject();

			System.out.println("One of workers has " + temp.size() + " waypoints inside");	

		} catch (IOException e) {
			e.printStackTrace();
		}         
       
    }

    public void run(ArrayList<Waypoint> chunk){   
             
    }
}