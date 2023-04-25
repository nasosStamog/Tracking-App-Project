import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Worker extends Thread {
	
	public static void main(String args[]) {
		new Worker().start();		
	}

    public void run(){

        Socket acceptSocket = null;
        ObjectInputStream in = null;
		ObjectOutputStream out = null;

		waypointsList<Waypoint> wp;

        try {

            /* Create socket for contacting the server on port 4320*/
			acceptSocket = new Socket("127.0.0.1",4320);

            /* Create the streams to send and receive data from server with help of actionForWorkers */
			in = new ObjectInputStream(acceptSocket.getInputStream());
			out = new ObjectOutputStream(acceptSocket.getOutputStream());

			wp = (waypointsList<Waypoint>) in.readObject();

			//map waypoints, return array with keys 


			

		} catch (UnknownHostException unknownHost) {
			System.err.println("You are trying to connect to an unknown host!");
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}finally {
			try {
				in.close();	
				out.close();
				acceptSocket.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}       

}
    

