package com.rav.agents;

import com.rav.util.MessageSpace;

public class DriveAgent implements Agent {
    public void scan(){
        boolean goal = false;
        if(goal){
            MessageSpace.broadcast(ProximitySenseAgent.class, "stop");
            MessageSpace.broadcast(LedAgent.class, "green");
        }else{
            MessageSpace.broadcast(DriveAgent.class, "resume");
            MessageSpace.broadcast(LedAgent.class, "off");
        }
        System.out.println("drive start");
    }

}
