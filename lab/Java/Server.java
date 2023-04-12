import java.io.*;
import java.net.*;

public class Server {

	public static void main(String args[]) {
		new Server().openServer();
	}
	
	/* Define the socket that receives requests */
	//Server socket
	ServerSocket providerSocket;
	/* Define the socket that is used to handle the connection */
	//Socket that communicates with client
	Socket socket2;
	void openServer() {
		try {

			/* Create Server Socket */
			providerSocket = new ServerSocket(6789);

			while (true) {
				/* Accept the connection */
				providerSocket.accept();
				socket2 = providerSocket.accept();
				/* Handle the request */
				ActionsForClients a1 = new ActionsForClients(socket2);
				a1.start();
			}

		} catch (IOException ioException) {
			ioException.printStackTrace();
		} finally {
			try {
				providerSocket.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}
}
