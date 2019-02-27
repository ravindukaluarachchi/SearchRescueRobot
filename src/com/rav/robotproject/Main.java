package com.rav.robotproject;

import com.rav.agents.*;
import com.rav.util.*;

public class Main{
    public static void main(String args[]){
        DriveAgent da = new DriveAgent();
        ProximitySenseAgent psa = new ProximitySenseAgent();
        MessageSpace.register(da);
        MessageSpace.register(psa);
        psa.start();

    }
}
