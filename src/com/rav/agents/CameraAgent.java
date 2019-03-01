package com.rav.agents;

import com.rav.util.MessageSpace;

public class CameraAgent implements Agent {
    public void scan(){
        boolean goal = false;
        
        try{
			Thread.sleep(500);
			MessageSpace.broadcast(LedAgent.class, "off");
		}catch(Exception e){
			e.printStackTrace();
		}
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
