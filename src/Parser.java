import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Parser{
    //Parser Class helps us to parse the gpx file, and make its content managable.
    //input: a gpx file
    //output: arraylist with waypoint objects
    
    waypointsList<Waypoint> waypoints = new waypointsList<>();
    
    //Constructor
    protected double lat;
    protected double lon;
    protected double ele;
    protected String time;
    protected String user;

    //gpx input
    public void parseGpx(String path){

        try(BufferedReader br = new BufferedReader( new FileReader(path)) ) {
            
            String ln;
            
            while ((ln = br.readLine()) != null) {
                
                if (ln.contains("<wpt")) {
                    lat = Double.parseDouble(ln.substring(ln.indexOf("lat=\"") + 5, ln.indexOf("\"", ln.indexOf("lat=\"") + 5)));
                    lon = Double.parseDouble(ln.substring(ln.indexOf("lon=\"") + 5, ln.indexOf("\"", ln.indexOf("lon=\"")+5)));
                }
                
                if(ln.contains("<ele")) {
                    ele = Double.parseDouble(ln.substring(ln.indexOf("ele")+4, ln.indexOf("</", ln.indexOf("<ele"))));
                }

                if(ln.contains("<time")) {
                    String time1 = ln.substring(ln.indexOf("time")+5, ln.indexOf("</", ln.indexOf("<time")));

                    //Time Format Change
                    DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
                    LocalDateTime localDateTime = LocalDateTime.parse(time1, inputFormatter);
                    DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    time = localDateTime.format(outputFormatter);
                    //End of Time Formatting
                    
                }

                if(ln.contains("creator")) {
                    user = ln.substring(ln.indexOf("creator=\"") + 9, ln.indexOf("\"", ln.indexOf("creator=\"") + 9));                    
                }

                if (ln.contains("</wpt>")){
                    waypoints.add(new Waypoint(lat,lon,ele,time,user));
                }          
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUser(){
        return user;
    }

    public waypointsList<Waypoint> getWaypoints(){
        return waypoints ;
    }
}
