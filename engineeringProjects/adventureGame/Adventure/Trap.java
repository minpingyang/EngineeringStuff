import ecs100.*;
import ecs100.*;
import java.awt.Color;
import java.util.*;
import java.io.*;
/**
 * Write a description of class Trap here.
 * 
 * @author (minping Yang) 
 * @version (24 Aug)
 */
public class Trap
{
    // field
    private String name;
    private ArrayList<Item> itemsDisTrap;
    private boolean activated;
    /**
     * Constructor for objects of class Trap
     */
    public Trap(String name,ArrayList<Item> itemsDisTrap)
    {
        // initialise instance variables
        this.name=name;
        this.itemsDisTrap=itemsDisTrap;
        activated=true;
    }
    
    /** 
     * 
     */
    public void setActivated(boolean act){
        this.activated=act;
    }
    public boolean getActivated(){
        return activated;
    }
    public ArrayList<Item> getItemsDisTrap(){
        return itemsDisTrap;
    }
    
    public void disableTrap(){
        activated=false;
        UI.println("disble the trap  successfully");
    }
    
    public String toString(){
        return this.name;
    }
}
