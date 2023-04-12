public class Waypoint{
    //Object that contains the data of gpx's waypoints
    protected double lat;
    protected double lon;
    protected double ele;
    protected String time;
    protected String user;


    public Waypoint(double lat,double lon,double ele, String time, String user) {
        this.lat = lat;
        this.lon = lon;
        this.ele = ele;
        this.time = time;
        this.user = user;
    }

    //Dokimastikoi Constructors
    public Waypoint(double lat){
        this.lat = lat;
    }
    
    public void print() {
        System.out.println("Latitude: " + lat);
        System.out.println("Longitude: " + lon);
        System.out.println("Elevation: " + ele);
        System.out.println("Time: " + time);
        System.out.println("User: " + user);
    }

}