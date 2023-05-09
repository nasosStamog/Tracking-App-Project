import java.io.Serializable;

public class Waypoint implements Serializable{
    //Object that contains the data of gpx's waypoints
    protected double lat;
    protected double lon;
    protected double ele;
    protected String time;
    protected String user;

    //Simple Constructor
    public Waypoint(double lat,double lon,double ele, String time, String user) {
        this.lat = lat;
        this.lon = lon;
        this.ele = ele;
        this.time = time;
        this.user = user;
    }

}