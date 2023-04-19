import java.io.*;
import java.net.*;

public class kinito{

    public static void main(String args[]) {
		new kinito().run();
	}

    public void run(){

        Socket requestsocket = null;
        ObjectInputStream in = null;
		ObjectOutputStream out = null;

        try {

			requestsocket = new Socket("127.0.0.1",4321);
			in = new ObjectInputStream(requestsocket.getInputStream());
			out = new ObjectOutputStream(requestsocket.getOutputStream());

			GPXFile t = new GPXFile("route 1", "C:/Users/Martinisk/Desktop/Tracking-App/gpxs/route1.gpx");
            out.writeObject(t);
			out.writeObject("test test");
            out.flush();

			/* Print the received result from server */
			System.out.println("Server>" + in.readInt());

		} catch (UnknownHostException unknownHost) {
			System.err.println("You are trying to connect to an unknown host!");
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} finally {
			try {
				in.close();	out.close();
				requestsocket.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
    }    
}
