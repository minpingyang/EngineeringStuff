import ecs100.*;
import java.awt.Color;
import java.util.*;
import java.io.*;
/**
 * Write a description of class Item here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Item
{
    // instance variables - replace the example below with your own
    private String name;    //Torch
    private double weight;
    /**
     * Constructor for objects of class Item
     */
    public Item(String name,double weight)
    {
        // initialise instance variables
        this.name=name;
        this.weight=weight;
    }

    public double getWeightOfItem(){
        return weight;
    }
    public String getName(){
    return name;
    }
    
    public String toString(){
        return this.name;
    }
}
