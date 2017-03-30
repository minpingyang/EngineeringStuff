/* Code for ENGR110 Assignment
 * Name:
 * Usercode:
 * ID:
 */

import ecs100.*;
import java.util.*;

/**
 * FSM Controller for a simulated Lift.
 * The core of the controller is the signal(String sensor) method
 * which is called by the lift every time a sensor
 * is signalled.
 *
 * Note, when your controller is started, the lift will be on floor 1 with the doors closed.
 * You should set the initial state of the controller to match this.

 */
public class LiftController {

    /**
     * The field that stores the current state of the FSM
     */
    private String state = "closed";     // initial state should be at floor 1 with the door closed.
    // Note, you probably want to "factor" the state by having additional variables
    // to represent aspects of the state
    /*# YOUR CODE HERE */
    private boolean atfloor1;
    private boolean atfloor2,atfloor3,req1,req2,req3,moveup,movedown;
    /**
     * The field containing the Lift.
     * The signal method will call methods on the lift to operate it
     */
    private Lift lift;  // the lift that is being contro    lled.

    // possible actions on the lift that you can perform:
    // lift.moveUp()             to start the lift moving up
    // lift.moveDown()           to start the lift moving down
    // lift.stop()               to stop the lift
    // lift.openDoor()           to start the doors opening
    // lift.closeDoor()          to start the doors closing
    // lift.restartTimer(1000)   to set the time for 1000 milliseconds
    // lift.turnWarningLightOn() to turn the warning light on
    // lift.turnWarningLightOff()to turn the warning light off

    /**
     * The Constructor is passed the lift that it is controlling.
     */
    public LiftController(Lift lift) {
        this.lift = lift;
        atfloor1=true;
    }

    /**
     * Receives a change in a sensor value that may trigger an action and state change.
     * If there is a transition out of the current state associated with this
     * sensor signal, 
     * - it will perform the appropriate action (if any)
     * - it will transition to the next state
     *   (by calling changeToState with the new state).
     *
     * Possible sensor values that you can respond to:
     * (note, you may not need to respond to all of them)
     *   "request1"   "request2"   "request3"
     *   "atF1"   "atF2"   "atF3"
     *   "startUp"   "startDown"
     *    "doorClosed"   "doorOpened"   "doorMoving"
     *    "timerExpired"
     *    "doorSensor"
     *    "withinCapacity"   "overCapacity"
     * 
     * You can either have one big method, or you can break it up into
     * a separate method for each state 
     */
    public void signal(String sensor){
        UI.printf("In state: %s, got sensor: %s%n", state, sensor);
        /*# YOUR CODE HERE */
        
        if (state.equals("closed")){
            if(sensor.equals("request1")) req1=true;
            if(sensor.equals("request2")) req2=true;
            if(sensor.equals("request3")) req3=true;
            
            if(sensor.equals("timerExpired")){
                if((req1&&atfloor1)) {
                    req1=false; 
                    lift.openDoor();              
                }
                if(req2&&atfloor2){ 

                    req2=false;
                    lift.openDoor();

                }
                if(req3&&atfloor3){
                    req3=false; 
                    lift.openDoor();

                }
                if(sensor.equals("doorOpened")){
                    state="opened";
                }

                if((req2&&atfloor1)||(req3&&atfloor1)) { atfloor1=false;   moveup=true;}
                if(req3&&atfloor2){ atfloor2=false; moveup=true;}
                if((req1&&atfloor3)||(req2&&atfloor3)){ atfloor3=false;  movedown=true;}
                if(req1&&atfloor2){ atfloor2=false;    movedown=true;}
                if(moveup) {lift.moveUp();  moveup=false; state="movingup"; }
                if(movedown) {lift.moveDown(); movedown=false;  state="movingdown";}
            }

        }
        else if (state.equals("movingup")){
            if(sensor.equals("request1")) req1=true;
            if(sensor.equals("request2")) req2=true;
            if(sensor.equals("request3")) req3=true;
            UI.println("req2"+req2);
            UI.println("sensor(atF2)"+sensor.equals("atF2"));

            if(req2&&sensor.equals("atF2")){  
                UI.println("stop"); 
                lift.stop(); 
                lift.openDoor();
                atfloor2=true; 
                req2=false;
                //UI.println("atf2true"); 
                state="opened";
            }
            if(req3&&sensor.equals("atF3")){  
                UI.println("stop3");
                lift.stop();
                lift.openDoor();
                atfloor3=true;
                req3=false;
                state="opened";
            }  

        }else if(state.equals("movingdown")){
            if(sensor.equals("request1")) req1=true;
            if(sensor.equals("request2")) req2=true;
            if(sensor.equals("request3")) req3=true;

            if(req2&&sensor.equals("atF2")){ 
                lift.stop();
                lift.openDoor();
                atfloor2=true;
                req2=false;
                state="opened";
            }
            if(req1&&sensor.equals("atF1")){ 
                lift.stop(); 
                lift.openDoor();
                atfloor1=true; 
                req1=false;
                state="opened";

            } 
        }

        else if (state.equals("opened")){
            if(sensor.equals("request1")) req1=true;
            if(sensor.equals("request2")) req2=true;
            if(sensor.equals("request3")) req3=true;

            if(sensor.equals("doorSensor")) lift.restartTimer(1000);

            if(sensor.equals("overCapacity")) {lift.turnWarningLightOn();  state="overweight";}

            if(sensor.equals("timerExpired")) { 
                lift.closeDoor();

            }
            if(sensor.equals("doorClosed")){
                lift.restartTimer(1000);
                state="closed";
            }
        }
        else if (state.equals("overweight")){
            if(sensor.equals("request1")) req1=true;
            if(sensor.equals("request2")) req2=true;
            if(sensor.equals("request3")) req3=true;

            if(sensor.equals("withinCapacity")) {lift.turnWarningLightOff();      state="opened";}

        }
    }

}
