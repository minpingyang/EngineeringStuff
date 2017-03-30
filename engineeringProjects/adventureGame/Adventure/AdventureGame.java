// This program is copyright VUW.
// You are granted permission to use it to construct your answer to an ENGR110 assignment.
// You may not distribute it in any other way without permission.
/* Code for COMP110 Assignment

 * Name:
 * Usercode:
 * ID:
 */

import ecs100.*;
import java.awt.*;
import java.util.*;
import java.io.*;
import javax.swing.Timer;
import java.text.SimpleDateFormat;
import java.awt.event.*;
/** AdventureGame   */
public class AdventureGame{
    //----------Fields -------------------
    /*# YOUR CODE HERE */
    private Player player=new Player();
    private ArrayList<Pod> allPods=new ArrayList<Pod>();
    private Pod dockingPod;
    private Pod currentPod;
    private Pod arrivePod;
    private int health=100;
    private ArrayList<Item> pack;
    private boolean containsData;
    private Timer timer;
    private long startTime = -1;

    private long duration=300000;     
    private boolean stopTimer=true;
    //---------- Constructor and interface ------------------
    /** Construct a new AdventureGame object and initialise the interface */
    public AdventureGame(){
        UI.initialise();
        UI.addButton("List Pack", this::doList);
        UI.addButton("Portal A", ()->{this.goPortal(0);});
        UI.addButton("Portal B", ()->{this.goPortal(1);});
        UI.addButton("Portal C", ()->{this.goPortal(2);});
        UI.addButton("Disable Trap", this::doDisable);
        UI.addButton("Look", this::doLook);
        UI.addButton("Search", this::doSearch);
        UI.addButton("Pickup", this::doPickUp);
        UI.addButton("PutDown", this::doPutDown);
        UI.addButton("Use Kit", this::useRecoveryKit);
        UI.addButton("Quit", UI::quit);
        UI.setMouseMotionListener(this::doMouse);
        UI.setDivider(0.5);  //show only the text pane
        this.initialiseGame();

        UI.setFontSize(14);
        UI.setColor(Color.red);
        UI.drawString("Before start the Game,read the instructions: ",10,20);
        UI.setColor(Color.blue);
        UI.drawString("intial given time is 5 minutes",10,40);
        UI.drawString("Using recovery kit can increase timer 20 seconds",10,60);
        UI.drawString("Triggering traps will decrease timer 10 seconds",10,80);
        UI.drawString("Disabling traps successfully will increase 30 sec",10,100);
        UI.drawString("You will lose once time run out or player's health<0",10,120);
        UI.drawString("You will win once you return to docking pod with datacache",10,140);
        UI.setColor(Color.red);
        UI.drawString("Click here to start countdown timer!!",10,160); 

        timer = new Timer(10, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (startTime < 0) {
                        startTime = System.currentTimeMillis();
                    }
                    long now = System.currentTimeMillis();
                    long clockTime = now - startTime;
                    SimpleDateFormat df = new SimpleDateFormat("mm:ss:SSS");
                    if (clockTime >= duration) {
                        UI.setColor(Color.red);
                        clockTime = duration;
                        UI.clearGraphics();
                        UI.drawString(df.format(duration - clockTime),40,40);
                        timer.stop();
                        stopTimer=false;
                        player=null;
                        UI.drawString("GAME OVER",40,20);
                    }else{
                        UI.clearGraphics();
                        UI.drawString(df.format(duration - clockTime),40,40);
                    }

                }
            });
        timer.setInitialDelay(0);

        // UI.setMouseMotionListener(this::doMouse);
    }

    //----------- Methods to respond to buttons ----------------

    public void doMouse(String mouseAction, double x, double y) {
        if (mouseAction.equals("pressed")){
            if (!timer.isRunning()&&stopTimer) {
                startTime = -1;
                timer.start();
            };
        }

    }

    /** List the items in the player's pack */
    public void doList(){
        if (player==null){ return;}
        /*# YOUR CODE HERE */
        pack=player.getPack();
        if(!pack.isEmpty()){
            for(int i=0;i<pack.size();i++){
                Item it=pack.get(i);
                UI.println(it);
            }  
            UI.println("pack current size:   "+pack.size());
        }else{
            UI.println("pack is empty");
        }
    }

    /** Exit the current pod going through the specified portal number */
    public void goPortal(int num){
        if (player==null){ return; }
        /*# YOUR CODE HERE */
        currentPod=player.getPosition();
        ArrayList<Pod> portalToPod=currentPod.getPortalToPod();
        pack=player.getPack();

        if(num==0&&!portalToPod.isEmpty()){
            arrivePod=portalToPod.get(0);          
        }
        if(num==1&&portalToPod.size()>1){
            arrivePod=portalToPod.get(1);
        }
        if(num==2&&portalToPod.size()>2){
            arrivePod=portalToPod.get(2);
        }
        if(arrivePod!=null){
            player.setPosition(arrivePod); 
            UI.println("player's current position: "+arrivePod);   

            if(arrivePod.equals("Docking Pod")){
                for(Item i:pack){
                    String itemName=i.getName();
                    if(itemName.equals("DataCache")){
                        containsData=true;
                        break;
                    }
                }
                if(containsData){
                    UI.setColor(Color.red);
                    UI.setFontSize(15);
                    UI.println("!!!!!!!!!!YOU WIN!!!!!!!!!!!!");
                }
            }
        }
    }

    /** Look around at the pod and report what's there (except for datacache)*/
    public void doLook(){
        if (player==null||checkTrap()){return;}

        /*# YOUR CODE HERE */
        currentPod=player.getPosition();
        currentPod.describe();
    }

    /** Search for the data cache, and pick it up if it is found.
     *  If the player has a torch, then there is a higher probability of
     *  finding the datacache (assuming it is in the pod) than if the player
     *  doesn't have a torch.
     */
    public void doSearch(){
        if (player==null || checkTrap()){ return;}
        /*# YOUR CODE HERE */
        pack=player.getPack();
        currentPod=player.getPosition();
        boolean isDataCache=false;
        boolean containsTorch=false;
        for(Item i:pack){
            String itemName=i.getName();
            if(itemName.equals("Torch")){
                containsTorch=true;
                break;
            }
        }

        if(containsTorch){
            double highPorba=Math.random();
            if(highPorba<0.9){
                //UI.println("test highPorba Torch work!!!!!!!!!");
                currentPod.setHasDataCache();  
            }
            isDataCache=currentPod.getHasDataCache();
            if(isDataCache){
                UI.println(" Torch help you to improve the probablity to find dataCache in the Pod!!!!!!!!!!!!!!!");
                player.putItemsToPack(new Item("DataCache", 0));
                UI.println("pick up dataCache into pack");
            }else{
                UI.println("there is not dataCache in the Pod");
            }
        }else{
            isDataCache=currentPod.getHasDataCache();
            if(isDataCache){
                UI.println("find dataCache in the Pod!!!!!!!!!!!!!");
                player.putItemsToPack(new Item("DataCache", 0));
                UI.println("pick up dataCache into pack");
            }else{
                UI.println("there is not dataCache in the Pod");
            }
        }
    }

    /** Attempt to pick up an item from the pod and put it in the pack.
     *  If item makes the pack too heavy, then puts the item back in the pod */
    public void doPickUp(){
        if (player==null || checkTrap()){ return;}
        /*# YOUR CODE HERE */
        currentPod=player.getPosition();
        Item it=currentPod.getItemFromPod(); 
        if(it!=null){
            double itemWeight=it.getWeightOfItem();
            double packWeight=player.getWeightOfPack();
            double totalWeight=itemWeight+packWeight;
            if(totalWeight<=10){
                player.putItemsToPack(it);
                currentPod.removeItem(it);
            }
        }
        else{
            UI.println("current pod doesn't have a item");
        }
    }

    /** Attempt to put down an item from the pack. */
    public void doPutDown(){
        if (player==null || checkTrap()){ return;}
        /*# YOUR CODE HERE */ 
        pack=player.getPack();    
        if(!pack.isEmpty()){
            Item it=player.removeItemFromPack();
            Pod currentPod=player.getPosition();
            currentPod.addItem(it);
        }else{
            UI.println("pack is empty");
        }

    }     

    /** Attempt to disable the trap in the current pod.
     * If there is no such trap, or it is already disabled, return immediately.
     * If disabling the trap with the players current pack of items doesn't work,
     *  the player is damaged. If their health is now <=0, then the game is over
     */
    public void doDisable(){
        if (player==null){ return;}
        /*# YOUR CODE HERE */
        currentPod=player.getPosition();
        Trap checkExist= currentPod.getTrap();
        if(checkExist==null){return;}
        else{
            boolean checkActivate=checkExist.getActivated();
            if(checkActivate){
                pack=player.getPack();
                ArrayList<Item> itemDisTrap=checkExist.getItemsDisTrap();
                for(int i=0;i<pack.size();i++){
                    for(int j=0;j<itemDisTrap.size();j++){
                        Item packItem=pack.get(i);
                        Item disTrap=itemDisTrap.get(j);                        
                        if(packItem.equals(disTrap)){
                            UI.println("pack has the item which can disble the trap");
                            checkExist.disableTrap();
                            if(duration>0&&duration<=300000){
                                duration+=30000; // increase 10 seconds
                                UI.println("********************************************************");
                                UI.println("Disabling the trap successfully to increase 30 seconds!");

                            }
                            return;
                        }                    
                    }               
                }
                UI.println("pack doesn't has the item which can disable the trap");
                checkTrap();
            }else{
                return;
            }
        }
    }

    /** If there is a recovery kit in the pod that hasn't been already used on
     *  this visit, then use it (increase the player's health) and remember that
     *  the kit has now been used.
     */
    public void useRecoveryKit(){
        if (player==null || checkTrap()){ return;}
        /*# YOUR CODE HERE */
        currentPod=player.getPosition();
        boolean isKit=currentPod.getHasRecoveryKit();
        if(isKit){
            UI.println("use the kit to recovery health by 20");
            player.recoverHealth();
            int health=player.getHealth();
            UI.println("player's health is "+health);          
            if(duration>0&&duration<=300000){
                duration+=20000; // increase 10 seconds
                UI.println("*****************************************");
                UI.println("use recovery kit to increase 20 seconds!");

            }
            currentPod.recoveryKitUsed();
            UI.println("the recovery kit had been used");
        }else{
            UI.println("The pod doesn't have recoveryKit");
        }
    }

    // ------------ Utility methods ---------------------------
    /** Check if there is an active trap. If so, set it off and damage the player.
     *  Returns true if the player got damaged. 
     */
    private boolean checkTrap(){
        /*# YOUR CODE HERE */
        currentPod=player.getPosition();
        Trap checkExist= currentPod.getTrap();
        if(checkExist!=null){
            boolean checkActivated=checkExist.getActivated();
            if(checkActivated){
                health=player.getHealth();
                if(health>10){
                    player.damageHealth();
                    UI.println("player's health decrease by 15 since trap");
                    health=player.getHealth();
                    UI.println("player's health is "+health);
                    if(duration>10000){
                        duration-=10000; // increase 10 seconds
                        UI.println("trigger traps to decrease 10 seconds!");
                    }
                    return true;
                }else{
                    player.damageHealth();
                    player=null;
                    duration=0;
                    UI.println("player's health is <0");

                    UI.println("!!!!!!!!Player dies,GAME OVER!!!!!!!!!!!");
                    return true;
                }
            }
        }
        return false;
    }

    // ---------- Initialise -------------------------
    /** Intialise all the pods in the game and the player
     *  YOU DO NOT NEED TO USE THIS METHOD - YOU CAN REPLACE IT WITH YOUR OWN
     *  The code provided is a pretty simple initialisation process.
     *  
     *  It makes assumptions about the constructors 
     *  
     *  and some methods for other classes.
     *  You will need to change it if it doesn't fit with the rest of your design
     *  
     *  1.It reads the pod descriptions from the game-data.txt file, 
     *  2. and connects them in a circle, with random cross-links. 
     *  
     *  3.It then reads descriptions of the traps (and the items that disable them),
     *  
     *  4. makes Trap objects and Item objects,
     *   
     *  5.   puts the Traps and the items in random pods 
     *  6. Puts a torch Item into one of the pods
     *  Makes a player
     *  Assumes constructors for Pod, Player, Item, and Trap
     *  Assumes allPods field, and several methods on traps, pods, and items
     *   You will need to modify the code if you have different constructors and methods.
     */
    public void initialiseGame(){
        Scanner data = null;
        try{
            //create pods from game-data file: 
            data = new Scanner(new File("game-data.txt"));
            //ignore comment lines, (starting with '# ')
            while (data.hasNext("#")){data.nextLine();}
            //read number of pods
            int numPods = data.nextInt(); data.nextLine();
            //read  name, has-recovery-kit,  has-data-cache
            for (int i=0; i<numPods; i++){
                // ASSUMES a Pod constructor!!!
                String podName = data.nextLine().trim();
                boolean hasRecoveryKit = data.nextBoolean();
                boolean hasDataCache = data.nextBoolean();
                Pod pod = new Pod(podName, hasRecoveryKit, hasDataCache);                //**MAY NEED TO CHANGE***               
                allPods.add(pod);              
                data.nextLine();
            }
            dockingPod = allPods.get(0);
            player.setPosition(dockingPod);
            UI.println(player.getPosition());
            // connect them in circle, to ensure that there is a path
            for (int i=0; i<numPods; i++){
                Pod pod1 = allPods.get(i);
                Pod pod2 = allPods.get((i+1)%numPods);
                // ASSUMES one-way connections
                pod1.addPortalTo(pod2);               //**MAY NEED TO CHANGE***
            }
            // connect each pod to two random other pods.
            for (Pod pod : allPods){
                Pod podB = allPods.get((int)(Math.random()*allPods.size()));
                Pod podC = allPods.get((int)(Math.random()*allPods.size()));
                pod.addPortalTo(podB);               //**MAY NEED TO CHANGE***
                pod.addPortalTo(podC);               //**MAY NEED TO CHANGE***
            }
            UI.printf("Created %d pods\n", allPods.size());

            // Read trap name and items to disable trap, to make Traps and Item
            ArrayList<Trap> traps = new ArrayList<Trap>();
            ArrayList<Item> items = new ArrayList<Item>();
            while (data.hasNext()){
                //trap name, followed by number of items to disable trap,
                //followed by items (weight, name)
                String trapName = data.nextLine().trim();
                ArrayList<Item> itemsForTrap = new ArrayList<Item>();
                int numItems = data.nextInt(); data.nextLine();
                for (int i=0; i<numItems; i++){
                    double weight = data.nextDouble();
                    String itemName = data.nextLine().trim();
                    // ASSUMES Item contructor:
                    Item it = new Item(itemName, weight);             //**MAY NEED TO CHANGE***
                    itemsForTrap.add(it);
                }
                // ASSUMES Trap contructor:
                Trap trap = new Trap(trapName, itemsForTrap);         //**MAY NEED TO CHANGE***
                traps.add(trap);
                items.addAll(itemsForTrap);
            }
            data.close();

            // ASSUMES Item contructor:
            items.add(new Item("Torch", 0.4));
            //put the traps in random rooms (other than the dockingPod)
            //but not in rooms that already have a trap
            UI.printf("Created %d traps and %d items\n", traps.size(), items.size());
            while (!traps.isEmpty()){
                Pod pod = allPods.get(1+(int) (Math.random()*numPods-1));
                // ASSUMES methods on Pod
                if (pod.getTrap() == null){               //**MAY NEED TO CHANGE***
                    pod.setTrap(traps.remove(0));         //**MAY NEED TO CHANGE***
                    UI.println(pod + ": " + pod.getTrap());
                }
            }
            //put the Items in random rooms.
            while (!items.isEmpty()){
                Pod pod = allPods.get((int) (Math.random()*numPods));
                // ASSUMES method on Pod
                pod.addItem(items.remove(0));            //**MAY NEED TO CHANGE***
                //UI.println(pod);
                //UI.println(pod.getItems().size());
            }
            //
            UI.printf("added traps and items to Pods\n");
            currentPod = dockingPod;
            //player = new Player();                       //**MAY NEED TO CHANGE***
            UI.println("You are at the Docking pod:");
            // ASSUMES method on Pod
            currentPod.describe();     //**MAY NEED TO CHANGE***
        }
        catch(InputMismatchException e){UI.println("Wrong type of data at: " + data.nextLine());}
        catch(IOException e){UI.println("Failed to read data correctly:\n" + e);}
    }

    public static void main(String[] args){
        new AdventureGame();

    }

}
