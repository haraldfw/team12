/* FolgLinje.java  GS - 2012-01-20

 * Program som gjør at en enkel robot følger en sort linje
 * Du trenger en enkel robot som kan svinge
 * en lyssensor koblet til sensor 1 - pekende nedover
 * en trykksensor koblet til sensor 2 - pekende rett fram i gå retningen
 */
import lejos.hardware.motor.*;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.NXTColorSensor;
import lejos.hardware.port.Port;
import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.robotics.SampleProvider;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.lcd.LCD;

public class FolgLinje_enkel{

	public static void main (String[] arg) throws Exception  {

		// Definerer sensorer:
		Brick brick = BrickFinder.getDefault();
		Port s1 = brick.getPort("S1"); // fargesensor 1 (høyre)
		Port s2 = brick.getPort("S2"); // fargesensor 2 (venstre)

		EV3ColorSensor fargesensor1 = new EV3ColorSensor(s1); // EV3-fargesensor
		NXTColorSensor fargesensor2 = new NXTColorSensor(s2); // NXT-fargesensor
		
		SampleProvider fargeLeser1 = fargesensor1.getMode("RGB");  // svart = 0.01..
		SampleProvider fargeLeser2 = fargesensor2.getMode("RGB");  // svart = 0.01..
		
		// Tabeller som innholder avlest verdi
		float[] fargeSample = new float[fargeLeser1.sampleSize()]; 
		
		System.out.println("Hold color-sensor over light color and press LEFT");
		while(!Button.LEFT.isDown()) {
		}

		float light = getColor();

		System.out.println("Hold color-sensor over dark color and press RIGHT");
		while(!Button.RIGHT.isDown()) {
		}

		float dark = getColor();
		float colorThreshold = (light - dark) / 2f + dark;
		
		//Definerer motorer
		NXTRegulatedMotor motorRight = Motor.B;
		NXTRegulatedMotor motorLeft = Motor.C;
		motorRight.forward();
		motorLeft.forward();
		
		
		boolean fortsett = true;
		int state = 0;
		
		while (fortsett){ 	// Fortsett så lenge roboten ikke treffer noe

			// (Re)setter hastighet på roboten
			motorRight.setSpeed(400);		// høyre
			motorLeft.setSpeed(400);		// venstre
		
			boolean venstre = isOnLine(fargeLeser1, colorThreshold);
			boolean hoyre	= isOnLine(fargeLeser2, colorThreshold);
			
			if (venstre && hoyre){	// begge detekterer sort (override)
				switch(state%3+1){
					case 1:	
						motorLeft.setSpeed(200);		// venstre		
						break;
					case 2:
						motorRight.setSpeed(200);		// høyre
						break;
					case 3:
						motorLeft.setSpeed(200);		// venstre
						break;
				}
				System.out.println("begge");
				state++;
			} else if (venstre){	// Kjør venstre
				motorLeft.setSpeed(200);  		// snu i  200 millisekund
				System.out.println("venstre svart");
			}
			 else if (hoyre){		// Kjør hoyre
				motorRight.setSpeed(200);		// snu i  200 millisekund  		
				System.out.println("hoyre svart");
			}
			else{					// Kjør framover
				System.out.println("hvit");
			}
			Thread.sleep(200);
		}
	}
	
	private static boolean isOnLine(SampleProvider s,float colorThreshold){
		s.fetchSample(fargeSample, 0);
		return fargeSample1[0]*100 > colorThreshold;
	}
}
