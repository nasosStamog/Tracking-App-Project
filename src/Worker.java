import java.io.*;
import java.net.*;

public class Worker {
    //Class Worker: This is the class that runs on the worker machine. Receives request from master and starts threads that proccess the data. 
    //Sends back the results of computation.
    int port;

    public static void main(String args[]) {
        new Worker(4320).openServer();

    }

    /* Define the socket that receives requests */
    ServerSocket s;
    /* Define the socket that is used to handle the connection */
    Socket providerSocket;

    public Worker(int port){
        this.port = port;
    }


    void openServer() {
        try {

            /* Create Server Socket */
            s = new ServerSocket(port);

            while (true) {
                /* Accept the connection */
                providerSocket = s.accept();
                System.out.println("Master sent new request");     

                /* Handle the request */
                Thread d = new ActionsForWorkers(providerSocket);
                d.start();
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