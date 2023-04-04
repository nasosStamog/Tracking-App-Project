public class main {

    public static void main(String[] args){

        String filePath = "D:/Users/nasos/Desktop/TrackingApp/gpxs/route1.gpx";

        Parser prs = new Parser();
        prs.parseGpx(filePath);
        

    }
    
}
