import java.io.*;
import java.net.*;

public class ActionsForClients extends Thread {
ObjectInputStream in;
ObjectOutputStream out;

	public ActionsForClients(Socket connection) {
		try {
				
			in = new ObjectInputStream(connection.getInputStream());
			out = new ObjectOutputStream(connection.getOutputStream());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		try {				
			int x = in.readInt();
			int y = in.readInt();
			int sum = x + y;
			out.writeInt(sum);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
				out.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}
}
