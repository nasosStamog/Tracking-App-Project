import java.io.*;


public class Master {
    //Workers Initialization
    static int workers = 3;
   

    public static void main(String args[]) throws IOException {
      waypointsList<Waypoint> list = new waypointsList<Waypoint>();

      System.out.println(list.size());

      clientServer a = new clientServer(list);
      workerServer b = new workerServer(workers, list);
      //Parser prs = new Parser();
      b.start();
      a.start();
      
      
      //b.openWorkerServer(workers);
      //prs.parseGpx("C:/Users/Martinisk/Desktop/Tracking-App/gpx_files_in_master/route1.gpx");
      //System.out.println(prs.getWaypoints());
		}
    
}
