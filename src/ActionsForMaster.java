import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
public class ActionsForMaster extends Thread{
    //Action for Master Class: This class extends thread and allows master to handle a lot of requests at the same time.
    //Action for Master Method separates the waypoint list from a client into n chunks, send them to workers, receive the intermediate results, 
    //perform the reduce method, and finally send the results back to clients.
    int requestcode;
    //Streams for communication with Clients
    ObjectInputStream cin;
    ObjectOutputStream cout;

    //Streams for communications with Workers
    ObjectInputStream win;
    ObjectOutputStream wout;

    //Necessary Sockets
    Socket requestSocket;
    Socket clientSocket;

    ArrayList<Object> configList;

    //Hashmap that stores user data
    //For each user
    static ArrayList<HashMap<String, Double>> userDistance1 = new ArrayList<HashMap<String, Double>>();
    HashMap<String, Double> userDistance = new HashMap<String, Double> ();
    static ArrayList<HashMap<String, Double>> userTime1= new ArrayList<HashMap<String, Double>>();
    HashMap<String, Double> userTime = new HashMap<String, Double> ();
    static ArrayList<HashMap<String, Double>> userEle1= new ArrayList<HashMap<String, Double>>();
    HashMap<String, Double> userEle = new HashMap<String, Double> ();
    static HashMap<String, Double> avgUsersDist = new HashMap<>();
    static HashMap<String, Double> avgUsersElev = new HashMap<>();
    static HashMap<String, Double> avgUsersTime = new HashMap<>();
    static ArrayList<String> differentUsers = new ArrayList<String>();
    //for all users
    static double allUserDist = 0;
    static double allUserElev = 0;
    static double allUserTime = 0;

//Constructor of the class
public ActionsForMaster(Socket clientSocket, ArrayList<Object> configList) {
    this.clientSocket = clientSocket; 
    this.configList = configList;
    try {
        cout = new ObjectOutputStream(clientSocket.getOutputStream());
        cin = new ObjectInputStream(clientSocket.getInputStream());

    } catch (IOException e) {
        e.printStackTrace();
    }
}

public void run() {
    try {
        String user = (String) cin.readObject();
        if(!differentUsers.contains(user)){differentUsers.add(user);}
        requestcode =(int) cin.readObject();

        if (requestcode==0){
            //Reads the file from client and stores it temporary in masters memory.
            InputStream is = clientSocket.getInputStream();
            DataInputStream dis = new DataInputStream(is);
            int length = dis.readInt();
            byte[] gpxBytes = new byte[length];
            dis.readFully(gpxBytes);

            String Filename = "route" +Integer.toString((int)this.getId()) + ".gpx";
            Filename = "gpxs_in_master/" + Filename;

            File gpxFile = new File(Filename);
            try (FileOutputStream fos = new FileOutputStream(gpxFile)) {
                fos.write(gpxBytes);
            } 

            //Parse the file to get information
            Parser prs = new Parser();
            prs.parseGpx(Filename);

            //Deletes file
            gpxFile.delete();

            //String user = prs.getUser();
            waypointsList<Waypoint> temp = prs.getWaypoints();
            waypointsList<Waypoint> temp2 = prs.getWaypoints();  
            // chunk  creationv
            int n = temp.size();
            
            //Size of chunk is determind by the number of workers. Every worker gets the same number of waypoints except the last one in the configList
            //The last one take all the waypoints that are in the list.

            int chunkSize = n /(configList.size()/2) ; 

            ArrayList<HashMap<String, Double>> unreducedResults = new ArrayList<HashMap<String, Double>>();        
            waypointsList<Waypoint> chunk;
            ArrayList<waypointsList<Waypoint>> chunks = new ArrayList<waypointsList<Waypoint>>();


            for(int i = 0; i < configList.size()/2; i++){
                chunks.add(new waypointsList<>());

            }
            
            
            int counter = 1;
            while(temp2.size()>0){

                chunks.get((counter-1)).add(temp2.get(0));
                temp2.remove(0);
                
                if (counter==configList.size()/2){
                    counter=1;
                }
                else{
                    counter+=1;
                }
                

            }
            
           
            
            for(int i = 0; i < configList.size()/2; i++){
                requestSocket = new Socket((String)configList.get(i+configList.size()/2), Integer.parseInt((String) configList.get(i)));

                /* Create the streams to send and receive data from worker*/
                wout = new ObjectOutputStream(requestSocket.getOutputStream());
                win = new ObjectInputStream(requestSocket.getInputStream());
                
                

                wout.writeObject(chunks.get(i));
                wout.flush();
                @SuppressWarnings("unchecked")
                HashMap<String, Double> w12 = (HashMap<String, Double>) win.readObject();
                unreducedResults.add(w12);
                wout.close();
                win.close();
            }
            
            Double totalDistance = reduce("Total Distance", unreducedResults);

            //Calculates Average Distance for each user
            userDistance.put(user, totalDistance);
            userDistance1.add(userDistance);
            avgUsersDist = userCalc(userDistance1);
            
            ArrayList<Double> valuesdistance = new ArrayList<>();
            //Calculates Average Distance for all users
            for (HashMap<String, Double> hashMap : userDistance1) {
                valuesdistance.addAll(hashMap.values());
                
            }
           
            allUserDist = 0;
            for (double value : valuesdistance) {
               
                allUserDist += value;
            }
            
            allUserDist = allUserDist/differentUsers.size();
            
        
            Double averageSpeed = reduce("Average Speed", unreducedResults);
            
            Double elevation = reduce("Total Elevation", unreducedResults);

            //Calculates Average Elevation for each user
            userEle.put(user, elevation);
            userEle1.add(userEle);
            avgUsersElev = userCalc(userEle1);
            ArrayList<Double> valueselev = new ArrayList<>();
            //Calculates Average Elevation for all users
            for (HashMap<String, Double> hashMap : userEle1) {
                valueselev.addAll(hashMap.values());
            }
            allUserElev = 0;
            for (double value : valueselev) {
                
                allUserElev += value;
            }
            
            allUserElev = allUserElev/differentUsers.size();
            
            Double totalTime = reduce("Total Time", unreducedResults);
            //Calculates Average Time for each user
            userTime.put(user, totalTime);
            userTime1.add(userTime);
            avgUsersTime = userCalc(userTime1);
            ArrayList<Double> valuestime = new ArrayList<>();
            //Calculates Average Time for all users
            for (HashMap<String, Double> hashMap : userTime1) {
                valuestime.addAll(hashMap.values());
                
            }
            allUserTime = 0;
            for (double value : valuestime) {
                allUserTime += value;
            }
            allUserTime = allUserTime/differentUsers.size();
            //Send results back to client
            
            cout.writeInt(1);
            cout.writeObject(totalDistance);
            cout.writeObject(averageSpeed);
            cout.writeObject(elevation);
            cout.writeObject(totalTime);
            cout.writeObject(user);
        
        
            cout.writeObject(avgUsersDist);
            cout.writeObject(avgUsersElev);
            cout.writeObject(avgUsersTime);
        }
            //Returns Average Users Data in double
            if (requestcode==1){
            cout.writeObject(allUserDist);
            cout.writeObject(allUserElev);
            cout.writeObject(allUserTime);
        }
        cout.flush();
    } catch (IOException e) {
        e.printStackTrace();
    } catch (ClassNotFoundException e) {
        throw new RuntimeException(e);
    } finally {
        try {
            if (requestcode==0){
            win.close();
            wout.close();}
        
            if(requestcode==1){
            cin.close();
            cout.close();
        }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
}
    

private static HashMap<String, Double> userCalc(ArrayList<HashMap<String, Double>> list){
    HashMap<String, Double> result = new HashMap<>();
    HashMap<String, Integer> keyCounts = new HashMap<>(); // keep track of counts for each key
    for (HashMap<String, Double> map : list) {
        // iterate over each key-value pair in the HashMap
        for (Map.Entry<String, Double> entry : map.entrySet()) {
            String key = entry.getKey();
            Double value = entry.getValue();

            // update the count for this key
            int count = keyCounts.getOrDefault(key, 0);
            keyCounts.put(key, count + 1);

            // if the key is already in the result HashMap, add the value to its current total
            if (result.containsKey(key)) {
                result.put(key, result.get(key) + value);
                
            } else {
                // otherwise, create a new key-value pair in the result HashMap
                result.put(key, value);
            }
        }
    }

    // create a new HashMap with the updated keys and values
    HashMap<String, Double> newMap = new HashMap<>();
    for (Map.Entry<String, Double> entry : result.entrySet()) {
        String key = entry.getKey();
        Double value = entry.getValue() / keyCounts.get(key); // divide by count for this key
        newMap.put(key, value);
    }

    return newMap;
}

    //Reduce method
    private Double reduce(String valueToBeCalculated,  ArrayList<HashMap<String, Double>> unreducedResults){
        double returnResult = -1;
        switch (valueToBeCalculated){

            
            case "Total Distance":
                Double totalDistance = 0.0;
                for(HashMap<String, Double> hm: unreducedResults){
                    totalDistance += hm.get("Total Distance");                
                }
                returnResult = totalDistance;
                break;
            
            case "Average Speed":
                Double totalavgspeed = 0.0;
                for(HashMap<String, Double> hm: unreducedResults){
                    totalavgspeed += hm.get("Average Speed");                
                }
                returnResult = totalavgspeed / unreducedResults.size(); 
                break;
            
            case "Total Elevation":
                Double totalElevation = 0.0;
                for(HashMap<String, Double> hm: unreducedResults){
                    totalElevation += hm.get("Total Elevation");                
                }
                returnResult = totalElevation;
                break; 


            case "Total Time":
                Double totalTime = 0.0;
                for(HashMap<String, Double> hm: unreducedResults){
                    totalTime += hm.get("Total Time")/60;                
                }
                returnResult = totalTime;
                break;
        }        
        return returnResult; 
    }

}