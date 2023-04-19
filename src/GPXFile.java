import java.io.Serializable;

public class GPXFile implements Serializable {
    String name;
    String filepath;

    public GPXFile(String name, String filepath){
        this.name = name;
        this.filepath = filepath;
    }

    public String getName(){
        return name;
    }   
    
    public String getPath(){
        return filepath;
    }  
    
}
