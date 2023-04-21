import java.io.*;
import java.net.*;



public class Worker extends Thread {

    public Worker(){}

	public static void main(String args[]) {
		new Worker().start();
		new Worker().start();
		new Worker().start();		
	}

    public void run(){

        Socket acceptSocket = null;
        ObjectInputStream in = null;
		ObjectOutputStream out = null;

        try {

            /* Create socket for contacting the server on port 4320*/
			acceptSocket = new Socket("127.0.0.1",4320);
            /* Create the streams to send and receive data from server */
			//in = new ObjectInputStream(acceptSocket.getInputStream());
			//out = new ObjectOutputStream(acceptSocket.getOutputStream());           
			

		} catch (UnknownHostException unknownHost) {
			System.err.println("You are trying to connect to an unknown host!");
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} finally {
			try {
				//in.close();	out.close();
				acceptSocket.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}
        

}
    

