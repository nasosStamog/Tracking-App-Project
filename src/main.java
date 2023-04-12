public class main {

    public static void main(String[] args){

        String filePath = "C:/Users/Martinisk/Desktop/Tracking-App/gpxs/route1.gpx";

        Parser prs = new Parser();
        prs.parseGpx(filePath);

        //user = prs.getUser();
        //user.setRoute(prs.getRoute);
        

    }
    
}
