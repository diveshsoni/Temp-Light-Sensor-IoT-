package edu.rit.csci759.rspi;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import edu.rit.csci759.rspi.utils.MCP3008ADCReader;

public class Implementation implements RpiIndicatorInterface {
	public static GpioController gpio = GpioFactory.getInstance();
	public static GpioPinDigitalOutput pin1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_27, "MyLED", PinState.LOW);
    public static GpioPinDigitalOutput pin2 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_28, "MyLED", PinState.LOW);
    public static GpioPinDigitalOutput pin3 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_29, "MyLED", PinState.LOW);
    
	
    @Override
	
	public void led_all_off() {
		// TODO Auto-generated method stub
		pin1.low();
		pin2.low();
		pin3.low();
	}

	@Override
	public void led_all_on() {
		// TOThread.sleep(1000);
		pin1.high();
		pin2.high();
		pin3.high();
        
	}

	@Override
	public void led_error(int blink_count) throws InterruptedException {
		// TODO Auto-generated method stub
		
		int count=0;
		
		while(count<blink_count){
	        // provision gpio pin #01 as an output pin and turn on by default
			pin1.high();
			pin2.high();
			pin3.high();
			Thread.sleep(1000);
			pin1.low();
			pin2.low();
			pin3.low();
			Thread.sleep(1000);
	        count++;
	        }
		
	}

	@Override
	public void led_when_low() {
		// TODO Auto-generated method stubfinal GpioController gpio = GpioFactory.getInstance();
		pin1.low();
		pin2.low();
		pin3.high();
	}

	@Override
	public void led_when_mid() {
		// TODO Auto-generated method stub
		pin1.low();
		pin2.high();
		pin3.low();
	}

	@Override
	public void led_when_high() {
		// TODO Auto-generated method stub
		pin1.high();
		pin2.low();
		pin3.low();
        
	}

	@Override
	public int read_ambient_light_intensity() {
		// TODO Auto-generated method stub	
		
		int ambient;
		int adc_ambient = MCP3008ADCReader.readAdc(MCP3008ADCReader.MCP3008_input_channels.CH1.ch());
		// [0, 1023] ~ [0x0000, 0x03FF] ~ [0&0, 0&1111111111]
		// convert in the range of 1-100
		ambient = (int)(adc_ambient / 10.24); 
		
		
			System.out.println("readAdc:" + Integer.toString(adc_ambient) + 
					" (0x" + MCP3008ADCReader.lpad(Integer.toString(adc_ambient, 16).toUpperCase(), "0", 2) + 
					", 0&" + MCP3008ADCReader.lpad(Integer.toString(adc_ambient, 2), "0", 8) + ")");        
			System.out.println("Ambient:" + ambient + "/100 (" + adc_ambient + "/1024)");
			return ambient;
	}

	@Override
	public  float read_temperature() {
		// TODO Auto-generated method stub
		
		int adc_temperature = MCP3008ADCReader.readAdc(MCP3008ADCReader.MCP3008_input_channels.CH0.ch());
		// [0, 1023] ~ [0x0000, 0x03FF] ~ [0&MCP3008ADCReader.initSPI(gpio);0, 0&1111111111]
		// convert in the range of 1-100
		int temperature = (int)(adc_temperature / 10.24);
		float tmp36_mVolts =(float) (adc_temperature * (3300.0/1024.0));
		float temp_C= (float) (((tmp36_mVolts - 100.0) / 10.0) - 40.0);
		
		return (int)temp_C;
	}
	public static void main(String args[]){
		Implementation test= new Implementation();
		System.out.println(test.read_temperature());
		
		
	}

}
