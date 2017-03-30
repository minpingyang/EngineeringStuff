import java.util.*;
import ecs100.*;


public class DrillingSystem {
    private List<Location> locations;
    private Location currentLocation;

    public DrillingSystem(){
	UI.addButton("AddLocation",this::addLocation);
	UI.addButton("ListLocations",this::listLocations);
	UI.addButton("SelectLocation",this::selectLocation);
	UI.addButton("AddSample",this::addSample);
	UI.addButton("ListSamples",this::listSamples);
	UI.addButton("DisplayAll",this::displayRecords);
	UI.setDivider(1.0);
    }

    public void listLocations(){
    }
    public void selectLocation(){
    }
    public void addLocation(){
    }
    public void addSample(){
    }
    public void listSamples(){
    }
    public void displayRecords(){
    }


    public static void main(String[] args){
	new DrillingSystem();
    }

}

