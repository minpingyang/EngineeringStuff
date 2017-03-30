import ecs100.*;
import java.awt.Color;
import java.util.*;
import java.io.*;
/**
 * Write a description of class Player here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Player
{
    // instance variables - replace the example below with your own
    // private String name;
    private ArrayList<Item> pack;
    private double weightOfPack;
    private int health;
    private Pod position;
    /**
     * Constructor for objects of class Player
     */
    public Player()
    {
        weightOfPack=0;
        health=100;
        pack=new ArrayList<Item>();
    }

    /**Collect items to a pack while searching through the pods
     * Each item has a weight. the pack has a limit of 10kg.
     * 
     */
    public void putItemsToPack(Item it)
    {
        if(it!=null){
            weightOfPack+=it.getWeightOfItem();
            if(weightOfPack<=10){
                pack.add(it);
            }
        }
    }

    /**
     * remove a item from pack, if pack is not empty
     */

    public Item removeItemFromPack(){
        if(!pack.isEmpty()){
            return pack.remove(pack.size()-1);
        }else{
            return null;
        }

    }

    /**
     * get the weight of pack
     */
    public double getWeightOfPack(){
        return weightOfPack;
    }

    /**
     * return pack
     */
    public ArrayList<Item> getPack(){
        return pack;
    }

    /**
     * set player postion which is pod
     */
    public void setPosition(Pod p){
        this.position= p;
    }
    /** player's health is damaged by 15 */
    public void damageHealth(){
      health-=15;
      if(health<=0){health=0;}
    }
    /**use recoveryKit increase health by 20*/
    public void recoverHealth(){
      health+=20;
      if(health>=100){health=100;}
    }
    
    /**got player's health*/
    public int getHealth(){
     return health;
    }
    
     
    /**
     * getPosition  return Pod
     */

    public Pod getPosition(){
        return this.position;
    }
}
