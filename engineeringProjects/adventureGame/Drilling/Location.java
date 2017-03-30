import java.util.*;
import ecs100.*;

public class Location{
    private String name;
    private double latitude;
    private double longitude;
    private List<CoreSample> samples ;

    public Location(){}
    

    public void setName(String name){this.name = name;}
    public String getName(){return this.name;}

    public void setLatitude(double latitude){this.latitude = latitude;}
    public double getLatitude(){return this.latitude;}

    public void setLongitude(double longitude){this.longitude = longitude;}
    public double getLongitude(){return this.longitude;}


    public void addSample(CoreSample sample){
    }
    
    public void printNameAndPosition(){
    }
    
    public void printList(){
    }
}


