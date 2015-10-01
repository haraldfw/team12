package com.team12.rally;

import lejos.hardware.Button;
import lejos.hardware.motor.*;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.BaseSensor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.sensor.NXTLightSensor;
import lejos.robotics.SampleProvider;

import java.util.ArrayList;

/* FolgLinje.java  GS - 2012-01-20

 * Program som gj�r at en enkel robot f�lger en sort linje
 * Du trenger en enkel robot som kan svinge
 * en lyssensor koblet til sensor 1 - pekende nedover
 * en trykksensor koblet til sensor 2 - pekende rett fram i g� retningen
 */

public class Rally {

    SampleProvider sampleProviderRight;
    SampleProvider sampleProviderleft;

    float[] samples;

    float colorThresholdRight;
    float colorThresholdLeft;

    NXTRegulatedMotor motorRight = Motor.B;
    NXTRegulatedMotor motorLeft = Motor.C;

    int state = 0;

    public Rally(ArrayList<BaseSensor> closables) {
        {
            Brick brick = BrickFinder.getDefault();
            Port sLeft = brick.getPort("S3");
            Port sRight = brick.getPort("S4");
            {
                NXTLightSensor left = new NXTLightSensor(sRight);
                sampleProviderleft = left.getRedMode();
                closables.add(left);
            }
            {
                EV3ColorSensor right = new EV3ColorSensor(sLeft);
                sampleProviderRight = right.getMode("RGB");
                closables.add(right);
            }
        }

        samples = new float[sampleProviderRight.sampleSize()];

        calibrateThresholds();

        motorLeft.forward();
        motorRight.forward();
    }

    public void update() throws InterruptedException {
        motorRight.setSpeed(400);
        motorLeft.setSpeed(400);

        boolean right = isOnLine(sampleProviderRight, colorThresholdRight);
        boolean left = isOnLine(sampleProviderleft, colorThresholdLeft);

        if (right && left) {    // begge detekterer sort (override)
            switch (state % 3 + 1) {
                case 1:
                    motorLeft.setSpeed(200);        // venstre
                    break;
                case 2:
                    motorRight.setSpeed(200);        // høyre
                    break;
                case 3:
                    motorLeft.setSpeed(200);        // venstre
                    break;
            }
//			System.out.println("begge");
            state++;
        } else if (right) {    // Drive right
            motorRight.setSpeed(200);        // snu i  200 millisekund
//			System.out.println("venstre svart");
        } else if (left) {        // Drive left
            motorLeft.setSpeed(200);        // snu i  200 millisekund
//			System.out.println("hoyre svart");
        } else {                    // Kjør framover
//			System.out.println("hvit");
            return;
        }
        Thread.sleep(200);
    }

    private boolean isOnLine(SampleProvider s, float colorThreshold) {
        s.fetchSample(samples, 0);
        return samples[0] * 100 > colorThreshold;
    }

    private float getColor(SampleProvider sampleProvider) {
        sampleProvider.fetchSample(samples, 0);
        return samples[0];
    }

    private void calibrateThresholds() {
        System.out.println("Hold color-sensor over light color and press LEFT");
        while (!Button.LEFT.isDown()) {
        }

        float lightLeft = getColor(sampleProviderRight);
        float lightRight = getColor(sampleProviderleft);

        System.out.println("Hold color-sensor over dark color and press RIGHT");
        while (!Button.RIGHT.isDown()) {
        }

        float darkLeft = getColor(sampleProviderRight);
        float darkRight = getColor(sampleProviderleft);

        colorThresholdRight = (lightLeft - darkLeft) / 2f + darkLeft;
        colorThresholdLeft = (lightRight - darkRight) / 2f + darkRight;
    }
}
