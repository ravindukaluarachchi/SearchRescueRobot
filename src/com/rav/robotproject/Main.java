package com.rav.robotproject;

import com.rav.agents.*;
import com.rav.util.*;

public class Main{
    public static void main(String args[]){
        LedAgent da = new LedAgent();
        ProximitySenseAgent psa = new ProximitySenseAgent();
        CameraAgent ca = new CameraAgent();
        MessageSpace.register(da);
        MessageSpace.register(ca);
        MessageSpace.register(psa);
        psa.start();

    }
}
