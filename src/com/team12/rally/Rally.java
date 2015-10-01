package com.team12.rally;

import lejos.hardware.Button;
import lejos.hardware.motor.*;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.NXTColorSensor;
import lejos.hardware.port.Port;
import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.robotics.SampleProvider;

/* FolgLinje.java  GS - 2012-01-20

 * Program som gj�r at en enkel robot f�lger en sort linje
 * Du trenger en enkel robot som kan svinge
 * en lyssensor koblet til sensor 1 - pekende nedover
 * en trykksensor koblet til sensor 2 - pekende rett fram i g� retningen
 */

public class Rally {

	SampleProvider sampleProviderLeft;
	SampleProvider sampleProviderRight;

	float[] samples;

	float colorThresholdLeft;
	float colorThresholdRight;

	NXTRegulatedMotor motorRight = Motor.B;
	NXTRegulatedMotor motorLeft = Motor.C;

	int state = 0;

	public Rally() {

		// Definerer sensorer:
		Brick brick = BrickFinder.getDefault();
		Port s1 = brick.getPort("S1"); // fargesensor 1 (h�yre)
		Port s2 = brick.getPort("S2"); // fargesensor 2 (venstre)

		EV3ColorSensor fargesensor1 = new EV3ColorSensor(s1); // EV3-fargesensor
		NXTColorSensor fargesensor2 = new NXTColorSensor(s2); // NXT-fargesensor

		sampleProviderLeft = fargesensor1.getMode("RGB");  // svart = 0.01..
		sampleProviderRight = fargesensor2.getMode("RGB");  // svart = 0.01..

		samples = new float[sampleProviderLeft.sampleSize()];

		System.out.println("Hold color-sensor over light color and press LEFT");
		while(!Button.LEFT.isDown()) {
		}

		float lightLeft = getColor(sampleProviderLeft);
		float lightRight = getColor(sampleProviderRight);

		System.out.println("Hold color-sensor over dark color and press RIGHT");
		while(!Button.RIGHT.isDown()) {
		}

		float darkLeft = getColor(sampleProviderLeft);
		float darkRight = getColor(sampleProviderRight);
		colorThresholdLeft = (lightLeft - darkLeft) / 2f + darkLeft;
		colorThresholdRight = (lightRight - darkRight) / 2f + darkRight;

		motorLeft.forward();
		motorRight.forward();
	}

	public void update() throws InterruptedException{

		// (Re)setter hastighet p� roboten
		motorRight.setSpeed(400);		// h�yre
		motorLeft.setSpeed(400);		// venstre

		boolean venstre = isOnLine(sampleProviderLeft, colorThresholdLeft);
		boolean hoyre	= isOnLine(sampleProviderRight, colorThresholdRight);

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
		} else {					// Kjør framover
			System.out.println("hvit");
		}
		Thread.sleep(200);
	}

	private boolean isOnLine(SampleProvider s,float colorThreshold){
		s.fetchSample(samples, 0);
		return samples[0]*100 > colorThreshold;
	}

	public float getColor(SampleProvider sampleProvider) {
		sampleProvider.fetchSample(samples, 0);
		return samples[0];
	}

	public static void main (String[] arg) throws Exception  {
		boolean fortsett = true;

		while (fortsett){ 	// Fortsett s� lenge roboten ikke treffer noe

		}
	}
}
