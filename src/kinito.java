import java.io.*;
import java.net.*;

public class kinito{

    public static void main(String args[]) {
		new kinito().run();
	}
	Socket requestsocket = null;
    /*ObjectInputStream in = null;
	ObjectOutputStream out = null;*/

    public void run(){

    
		try {
			requestsocket = new Socket("127.0.0.1",4321);
			// Read GPX file into a byte array
			File gpxFile = new File("C:/Users/Martinisk/Desktop/Tracking-App/gpxs/route1.gpx");
			byte[] gpxBytes = new byte[(int) gpxFile.length()];
			FileInputStream fis = new FileInputStream(gpxFile);
			fis.read(gpxBytes);
			fis.close();

			// Send GPX file to server
			OutputStream os = requestsocket.getOutputStream();
			DataOutputStream dos = new DataOutputStream(os);
			dos.writeInt(gpxBytes.length);
			dos.write(gpxBytes);
			dos.flush();
			dos.close();

			requestsocket.close();

		} catch (UnknownHostException unknownHost) {
			System.err.println("You are trying to connect to an unknown host!");
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}  finally {
			try {
				//in.close();	out.close();
				requestsocket.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
		
	

        /*try {
			
			
			requestsocket = new Socket("127.0.0.1",4321);
			in = new ObjectInputStream(requestsocket.getInputStream());
			out = new ObjectOutputStream(requestsocket.getOutputStream());
			System.out.println("eftase 2 ");
			//Read Gpx file into a Byte array
			File gpx = new File("C:/Users/Martinisk/Desktop/Tracking-App/gpxs/route1.gpx");
			byte [] gpxBytes = new byte[(int) gpx.length()];
			FileInputStream fis = new FileInputStream(gpx);
			System.out.println("mehri edo");
			fis.read(gpxBytes);
			fis.close();


			/*GPXFile t = new GPXFile("route 1", "C:/Users/Martinisk/Desktop/Tracking-App/gpxs/route1.gpx");
            out.writeObject(t);
			out.writeObject("test test");
            out.flush();*/

			/* Print the received result from server */

			//Send GPX file to server
			/*OutputStream os = requestsocket.getOutputStream();
			DataOutputStream dos = new DataOutputStream(os);
			
			dos.writeInt(gpxBytes.length);
			dos.write(gpxBytes);
			dos.flush();
			dos.close();
			os.close();
			requestsocket.close();	
			System.out.println("to esteile");




			System.out.println("Server>" + in.readInt());

		} catch (UnknownHostException unknownHost) {
			System.err.println("You are trying to connect to an unknown host!");
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}  finally {
			try {
				in.close();	out.close();
				requestsocket.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
		
    }    */
	}
}
