import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class ActionforWorkers extends Thread {
    ObjectInputStream in;
    ObjectOutputStream out;
    waypointsList<Waypoint> wp;


    public ActionforWorkers(Socket connection){
        try {				
			in = new ObjectInputStream(connection.getInputStream());
			out = new ObjectOutputStream(connection.getOutputStream());

		} catch (IOException e) {
			e.printStackTrace();
		}       
       
    }

    public void set(waypointsList<Waypoint> wp){
        this.wp = wp;
    }

    public void run(){ 
       try {
        out.writeObject(wp);
    } catch (IOException e) {
        
        e.printStackTrace();
    } 
       //in.readObject();

       //Reduce

       //Fill an arraylist with reduced results and the client server will take it to clients

             
    }
}