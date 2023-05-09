import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.lang.Math;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;



public class ActionsForWorkers extends Thread {
    //This class allows worker to be multithreaded and compute a lot of chunks simoultaneously.
    ObjectInputStream in;
    ObjectOutputStream out;

    //Constructor method that initializes in-out streams
    public ActionsForWorkers(Socket connection) {
        try {
            out = new ObjectOutputStream(connection.getOutputStream());
            in = new ObjectInputStream(connection.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            @SuppressWarnings("unchecked")
            waypointsList<Waypoint> chunk = (waypointsList<Waypoint>) in.readObject();


            ArrayList<Double> chunkDistances = DistanceCalc(chunk);    
            ArrayList<Double> chunkavgSpeed = avgspeedCalc(chunk);
            ArrayList<Double> chunkElevation = totalElevation(chunk);
            ArrayList<Double> chunktotalTime = totalTime(chunk);
            
            HashMap<String, Double> chunkResults = new HashMap<String, Double>();
            ArrayList<ArrayList<Double>> chunkData = new ArrayList<>(); 


            chunkData.add(chunkDistances);
            chunkData.add(chunkavgSpeed);
            chunkData.add(chunkElevation);
            chunkData.add(chunktotalTime);

            chunkResults.put("Total Distance", map(chunkData, "Total Distance"));
            chunkResults.put("Average Speed", map(chunkData, "Average Speed"));
            chunkResults.put("Total Elevation", map(chunkData, "Total Elevation"));
            chunkResults.put("Total Time", map(chunkData, "Total Time"));
            
            out.writeObject(chunkResults);
            out.flush();
            

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                in.close();
                out.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    //Calculation of distance
    private ArrayList<Double> DistanceCalc(waypointsList<Waypoint> chunk) {

        ArrayList<Double> distances = new ArrayList<>();

        for (int i = 1; i < chunk.size(); i++) {

            double lat1 = chunk.get(i - 1).lat;
            double lon1 = chunk.get(i - 1).lon;

            double lat2 = chunk.get(i).lat;
            double lon2 = chunk.get(i).lon;

            // Havershine Distance Method

            final double R = 6371.0;
            Double latDistance = toRad(lat2 - lat1);
            Double lonDistance = toRad(lon2 - lon1);
            Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
            Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            Double distance = R * c;

            distances.add(distance);
        }
        

        return distances;
    }

    private static Double toRad(Double value) {
        return value * Math.PI / 180;
    }



    //Clculation of total time
    private ArrayList<Double> totalTime(waypointsList<Waypoint> chunk){

        ArrayList<Double> timeBetweenWaypoints = new ArrayList<>();


        for (int i = 1; i < chunk.size(); i++) {

            String time1 = chunk.get(i - 1).time;
            String time2 = chunk.get(i).time;
            LocalDateTime dateTime1 = LocalDateTime.parse(time1, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            LocalDateTime dateTime2 = LocalDateTime.parse(time2, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            Duration total_time = Duration.between(dateTime1, dateTime2);
            double final_duration_time = total_time.toSeconds();

            timeBetweenWaypoints.add(final_duration_time);
        }

        return  timeBetweenWaypoints; 
    }  




    //Calculation of Average Speed
    private ArrayList<Double> avgspeedCalc(waypointsList<Waypoint> chunk){

        ArrayList<Double> avgSpeed = new ArrayList<>();

        ArrayList<Double> time = totalTime(chunk);

        ArrayList<Double> distances = DistanceCalc(chunk);

        for(int i = 0; i < distances.size(); i++){
            avgSpeed.add(distances.get(i)/(time.get(i)/3600));
        }

        return avgSpeed; 
    }

    //Calculation of elevation
    private ArrayList<Double> totalElevation(waypointsList<Waypoint> chunk){

        ArrayList<Double> elevation = new ArrayList<>();

        for (int i = 1; i < chunk.size(); i++) {
            Double el1 = chunk.get(i - 1).ele;
            Double el2 = chunk.get(i).ele;

            elevation.add(el2 - el1);
        }

        return elevation; 
    }

    //Map method
    private Double map(ArrayList<ArrayList<Double>> chunkData, String valueToBeCalculated){
        double returnResult = -1;
        switch (valueToBeCalculated){

            
            case "Total Distance":
                Double totalDistance = 0.0;
                for(Double value: chunkData.get(0)){
                    totalDistance += value;                
                }
                returnResult = totalDistance;        
                break;
            
            case "Average Speed":
                Double totalavgspeed = 0.0;
                for(Double value: chunkData.get(1)){
                    totalavgspeed += value;                
                }
                returnResult = totalavgspeed / chunkData.get(1).size(); 
                break;
            
            case "Total Elevation":
                Double totalElevation = 0.0;
                for(Double value: chunkData.get(2)){
                    totalElevation += value;                
                }
                returnResult = totalElevation;
                break; 


            case "Total Time":
                Double totalTime = 0.0;
                for(Double value: chunkData.get(3)){
                    totalTime += value;                
                }
                returnResult = totalTime;
                break; 



        }        
        return returnResult;    
        
    }
    
}


