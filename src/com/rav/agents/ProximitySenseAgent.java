package com.rav.agents;

import com.rav.util.MessageSpace;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class ProximitySenseAgent implements Agent {
    private boolean run = true;
    private final static float SOUND_SPEED = 340.29f;  // speed of sound in m/s
    
    private final static int TRIG_DURATION_IN_MICROS = 10; // trigger duration of 10 micro s
    private final static int WAIT_DURATION_IN_MILLIS = 60; // wait 60 milli s

    private final static int TIMEOUT = 2100;
    private  GpioPinDigitalInput echoPin;
    private  GpioPinDigitalOutput trigPin;
    
    public void start(){		
        new Thread(new Runnable(){
            @Override
            public void run(){
				GpioController gpio = GpioFactory.getInstance();
				trigPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04);
				trigPin.low();
				echoPin = gpio.provisionDigitalInputPin(RaspiPin.GPIO_05);
				
				/*
        
				Thread.sleep(100);

				System.out.println("starting measurement");
				pinTrig.high();
				Thread.sleep(0.01)
				pinTrig.low();*/

				float distance = 0;
				while( run ) {
					try {
						distance = measureDistance();
						
						//System.out.printf( "%1$d,%2$.3f%n", System.currentTimeMillis(), measureDistance() );
					} catch( TimeoutException e ) {
						//System.err.println( e );
					}
					System.out.print(distance );
					if(distance < 15) {
						System.out.print(" sending red");
						MessageSpace.broadcast(LedAgent.class, "red");
						MessageSpace.broadcast(CameraAgent.class, "scan");
						run = false;
						System.out.println(" sent");
					}else{
						System.out.print(" sending off");
						MessageSpace.broadcast(LedAgent.class, "off");
						System.out.println(" sent");
					}
					
					try {
						Thread.sleep( WAIT_DURATION_IN_MILLIS );
					} catch (InterruptedException ex) {
						System.err.println( "Interrupt during trigger" );
					}
				}
				gpio.shutdown();
				gpio.unprovisionPin(trigPin);
				gpio.unprovisionPin(echoPin);
        /*
                int i = 0;
                while(run){
                    try{
                        i++;
                        System.out.println("i: " + i);
                        if(i % 20 == 0){
							MessageSpace.broadcast(LedAgent.class, "off");
							System.out.println("off");
						}else if(i % 30 == 0){
							MessageSpace.broadcast(LedAgent.class, "red");
							System.out.println("red");
                        }else if(i % 10 == 0){
							System.out.println("multicolor");
                            MessageSpace.broadcast(DriveAgent.class, "stop");
                            MessageSpace.broadcast(LedAgent.class, "multicolor");
                        }
                        Thread.sleep(500);
                    }catch(InterruptedException e){
                      e.printStackTrace();
                    }
                }*/
            }
        }).start();
    }
	
	public float measureDistance() throws TimeoutException {
        this.triggerSensor();
        this.waitForSignal();
        long duration = this.measureSignal();
        
        return duration *  SOUND_SPEED / ( 2 * 10000 );
    }
    
    private void triggerSensor() {
        try {
            this.trigPin.high();
            Thread.sleep( 0, TRIG_DURATION_IN_MICROS * 1000 );
            this.trigPin.low();
        } catch (InterruptedException ex) {
            System.err.println( "Interrupt during trigger" );
        }
    }
    
    private void waitForSignal() throws TimeoutException {
        int countdown = TIMEOUT;
        
        while( this.echoPin.isLow() && countdown > 0 ) {
            countdown--;
        }
        
        if( countdown <= 0 ) {
            throw new TimeoutException( "Timeout waiting for signal start" );
        }
    }
    
    
    private long measureSignal() throws TimeoutException {
        int countdown = TIMEOUT;
        long start = System.nanoTime();
        while( this.echoPin.isHigh() && countdown > 0 ) {
            countdown--;
        }
        long end = System.nanoTime();
        
        if( countdown <= 0 ) {
            throw new TimeoutException( "Timeout waiting for signal end" );
        }
        
        return (long)Math.ceil( ( end - start ) / 1000.0 );  // Return micro seconds
    }
    
    

    public void stop(){
        run = false;
    }
    
	private static class TimeoutException extends Exception {

        private final String reason;
        
        public TimeoutException( String reason ) {
            this.reason = reason;
        }
        
        @Override
        public String toString() {
            return this.reason;
        }
    }

}
