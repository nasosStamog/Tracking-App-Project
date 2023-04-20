import java.io.*;
import java.net.*;

public class kinito extends Thread{
	String filePath;

	kinito(String filePath){
		this.filePath = filePath;
	}

	Socket requestsocket = null;
    /*ObjectInputStream in = null;
	ObjectOutputStream out = null;*/

    public void run(){
    
		try {
			requestsocket = new Socket("127.0.0.1",4321);
			// Read GPX file into a byte array
			File gpxFile = new File(filePath);
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
	}


	public static void main(String args[]) {
		new kinito("C:/Users/Martinisk/Desktop/Tracking-App/gpxs/route1.gpx").start();
		new kinito("C:/Users/Martinisk/Desktop/Tracking-App/gpxs/route2.gpx").start();
		new kinito("C:/Users/Martinisk/Desktop/Tracking-App/gpxs/route3.gpx").start();
		new kinito("C:/Users/Martinisk/Desktop/Tracking-App/gpxs/route4.gpx").start();
		new kinito("C:/Users/Martinisk/Desktop/Tracking-App/gpxs/route5.gpx").start();
	}
}



