package com.rav.agents;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class LedAgent implements Agent {
	private boolean stop;
	private boolean lock;
	
    public void red(){
		if (lock){
			return;
		}
		
		stop = false;
		new Thread(new Runnable(){
			public void run(){
				try{
					lock = true;
					GpioController gpio = GpioFactory.getInstance();
					GpioPinDigitalOutput pinBlue = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_23, "PinLED", PinState.LOW);
					GpioPinDigitalOutput pinRed = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_24, "PinLED", PinState.LOW);
					GpioPinDigitalOutput pinGreen = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_27, "PinLED", PinState.LOW);
					
					pinRed.low();
					pinBlue.low();	
					pinGreen.low();	
					
					pinRed.high();
					pinBlue.low();	
					pinGreen.low();		
					while(!stop){
						
					}
					pinBlue.low();	
					pinRed.low();
					pinGreen.low();	
					gpio.shutdown();
					gpio.unprovisionPin(pinBlue);
					gpio.unprovisionPin(pinRed);
					gpio.unprovisionPin(pinGreen);
					lock = false;
				}catch(com.pi4j.io.gpio.exception.GpioPinExistsException ex){
					System.err.println("pin already opened");
				}
			}
		}).start();	
    }	
    
    public void off(){
		stop = true;
    }
    
    public void multicolor(){	
		if (lock){
			return;
		}
		stop = false;
		try{	
			new Thread(new Runnable(){
				public void run(){
					lock = true;
					GpioController gpio = GpioFactory.getInstance();
					GpioPinDigitalOutput pinBlue  = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_23, "PinLED", PinState.LOW);
					GpioPinDigitalOutput pinRed   = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_24, "PinLED", PinState.LOW);
					GpioPinDigitalOutput pinGreen = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_27, "PinLED", PinState.LOW);			
					while(!stop){
						try{
							pinBlue.high();	
							pinRed.low();
							pinGreen.low();					
							Thread.sleep(500);
							pinBlue.low();	
							pinRed.high();
							pinGreen.low();	
							Thread.sleep(500);
							pinBlue.low();	
							pinRed.low();
							pinGreen.high();	
							Thread.sleep(500);
						}catch(InterruptedException ex){
							ex.printStackTrace();
						}
					}
					pinBlue.low();	
					pinRed.low();
					pinGreen.low();	
					gpio.shutdown();
					gpio.unprovisionPin(pinBlue);
					gpio.unprovisionPin(pinRed);
					gpio.unprovisionPin(pinGreen);
					lock = false;
				}
			}).start();		
		}catch(com.pi4j.io.gpio.exception.GpioPinExistsException ex){
			System.err.println("pin already opened");
		}
    }
}

