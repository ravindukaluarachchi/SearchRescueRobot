package com.rav.util;

import java.util.List;
import java.util.ArrayList;
import com.rav.agents.*;
import java.lang.reflect.Method;

public class MessageSpace {
    private static List<Agent> agents = new ArrayList<>();

    public static void register(Agent agent){
        agents.add(agent);
    }

    public static void broadcast(Class c,String message){
        for(Agent agent : agents){

            if(c.equals(agent.getClass())){
                try {
                    Method method = c.getMethod(message);
                    method.invoke(agent);
                } catch(Exception e){
                    e.printStackTrace();
                }                
            }
        }
    }
}
