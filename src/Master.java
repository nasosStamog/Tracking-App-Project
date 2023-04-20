import java.io.*;


public class Master {
    //Workers Initialization
    static int workers = 3;

    public static void main(String args[]) throws IOException {
      Server a = new Server();
      Parser prs = new Parser();
      //a.openClientServer();
      a.openWorkerServer(workers);
      //prs.parseGpx("C:/Users/Martinisk/Desktop/Tracking-App/gpx_files_in_master/route1.gpx");
      //System.out.println(prs.getWaypoints());
		}
    
}
