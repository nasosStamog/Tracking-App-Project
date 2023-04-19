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
        //Receive Gpx file on server
        
        
        InputStream is = clientSocket.getInputStream();
        DataInputStream dis = new DataInputStream(is);
        int length = dis.readInt();
        byte[] gpxBytes = new byte[length];
        dis.readFully(gpxBytes);
        dis.close();
        
        
        //Save GPX file on server
        File gpxFile = new File("C:/Users/Martinisk/Desktop/Tracking-App/gpx files in master");
        FileOutputStream fos = new FileOutputStream(gpxFile);
        fos.write(gpxBytes);
        fos.close();

        clientSocket.close();
        serverSocket.close();
        
        
        
        
        /*
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

        String inputLine;

        while ((inputLine = in.readLine()) != "test") {
            System.out.println("Received message from client: " + inputLine);
            out.println("Echo: " + inputLine);
            System.out.println("test");
        }

        in.close();
        out.close();

        clientSocket.close();
        serverSocket.close();*/
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}