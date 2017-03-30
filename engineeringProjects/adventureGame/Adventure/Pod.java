import ecs100.*;
import java.awt.Color;
import java.util.*;
import java.io.*;
/**
 * Write a description of class Pod here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Pod
{
    // instance variables - replace the example below with your own
    private String name;
    private boolean hasRecoveryKit,hasDataCache;
    private Trap trap;      // at most one trap. if the trap is disable,then keep it disable;
    private ArrayList<Item>items;
    private ArrayList<Pod>portalToPod;
    /**
     * Constructor for objects of class Pod
     */
    public Pod(String name,boolean hasRecoveryKit,boolean hasDataCache)
    {
        // initialise instance variables
        this.name=name;
        this.hasRecoveryKit=hasRecoveryKit;
        this.hasDataCache=hasDataCache;
        portalToPod= new ArrayList<Pod>();
        items=new ArrayList<Item>();
    }
    
    /**get if has recoveryKit in this Pod*/
    public boolean getHasRecoveryKit(){
        return  hasRecoveryKit;
    }
    /**use the recovery kit*/
    public void recoveryKitUsed(){
        hasRecoveryKit=false;
    }
    /**get hasDataCache*/
    public boolean getHasDataCache(){
        return hasDataCache;
    }
    /**set hasDataCache*/
    public void setHasDataCache(){
        hasDataCache=true;
    }
    
    /**  the current pod add  a portal to other  pods
     */
    public void addPortalTo(Pod p){      
        portalToPod.add(p);
    }

    /** 
     * get the portalToPod back.
     */
    public ArrayList<Pod> getPortalToPod(){
       return  portalToPod;
    }

    /** return trap, if the pod has one.
     *  else return null;
     */
    public Trap getTrap(){ return trap; }

    /** 
     * creat a trap in a pod which doesn't have a trap.
     */
    public void setTrap(Trap t){
        trap=t;
    }

    /**
     *add item to items 
     */
    public void addItem(Item it){
        if(it!=null){
            items.add(it);
        } 
    }

    /**
     * remove a item from Pod
     */
    public void removeItem(Item it){
        if(it!=null)items.remove(it);
    }

    /**
     * return a item from items in the Pod
     */

    public Item getItemFromPod(){
        
        if(items.size()>0){
            UI.println("item size "+items.size());
            Item lastItem=items.get(items.size()-1);
            return lastItem;
        }else{
            UI.println("there is not item on the Pod ");
            return null;
        }

    }
    
    public ArrayList<Item> getItems() {
        return items;
    }

    /**describe
     * the name of the Pod, dsecrible if exist a trap. if so, whether it is active. 
     * the number of portals of the Pod
     */
    public void describe(){
        UI.println("name of the pod:  "+name);
        Trap t=this.getTrap();
        if(t!=null){ 
            UI.println("there is a trap in the pod");
            boolean ac=t.getActivated();
            if(ac){UI.println("this trap is active");}
            else{UI.println("this trap is not active");}
        }
        else{UI.println("there is not a trap in the pod");}
        int numberOfPortal=portalToPod.size();  
        UI.println("current pod has "+numberOfPortal+"  portals");
    }

    public String toString() {
        return this.name;
    }
}
