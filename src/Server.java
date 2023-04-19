import java.io.*;
import java.net.*;

public class Server {

    public Server(){};

    public void openServer() throws IOException {
        try {

        ServerSocket serverSocket = new ServerSocket(4321);
        System.out.println("Server started. Waiting for client to connect...");

        Socket clientSocket = serverSocket.accept();
        System.out.println("Client connected.");
        
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

        String inputLine;

        while ((inputLine = in.readLine()) != "test") {
            System.out.println("Received message from client: " + inputLine);
            out.println("Echo: " + inputLine);
            System.out.println("makabou");
        }

        in.close();
        out.close();

        clientSocket.close();
        serverSocket.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}