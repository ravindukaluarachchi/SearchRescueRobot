package com.rav.agents;

import com.rav.util.MessageSpace;

public class ProximitySenseAgent implements Agent {
    private boolean run = true;

    public void start(){
        new Thread(new Runnable(){
            @Override
            public void run(){
                int i = 0;
                while(run){
                    try{
                        i++;
                        System.out.println("i: " + i);
                        if(i % 10 == 0){
                            MessageSpace.broadcast(DriveAgent.class, "stop");
                            MessageSpace.broadcast(LedAgent.class, "red");
                        }
                        Thread.sleep(500);
                    }catch(InterruptedException e){
                      e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void stop(){
        run = false;
    }
}
