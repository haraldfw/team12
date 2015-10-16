package com.team12.rally;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.*;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.BaseSensor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.sensor.NXTLightSensor;
import lejos.robotics.SampleProvider;

import java.util.ArrayList;

public class Rally {

    SampleProvider sampleProviderRight;
    SampleProvider sampleProviderleft;

    float[] samples;

    float colorThresholdRight;
    float colorThresholdLeft;

    NXTRegulatedMotor motorRight = Motor.B;
    NXTRegulatedMotor motorLeft = Motor.C;

    private enum State {
        LEFT,
        RIGHT
    }

    private State state = State.LEFT;

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

        System.out.println("Press DOWN to start");
        while (!Button.DOWN.isDown()) ;
        LCD.clear();

        motorRight.setSpeed(400);
        motorLeft.setSpeed(400);
        motorLeft.forward();
        motorRight.forward();
    }

    public void update() throws InterruptedException {
        boolean right = isOnLine(sampleProviderRight, colorThresholdRight);
        boolean left = isOnLine(sampleProviderleft, colorThresholdLeft);

        if (right && left) { // Both sensors are over black
            // continue to thread sleep
            motorRight.setSpeed(400);
            motorLeft.setSpeed(400);
//			System.out.println("begge");
        } else if (right) {    // Drive right
            turnRight();
//			System.out.println("venstre svart");
        } else if (left) {        // Drive left
            turnLeft();
//			System.out.println("hoyre svart");
        } else {
            switch (state) {
                case LEFT:
                    turnLeft();
                    break;
                case RIGHT:
                    turnRight();
                    break;
            }
            return;
        }
        Thread.sleep(200);
    }

    private void turnLeft() {
        state = State.LEFT;
        motorRight.setSpeed(400);
        motorLeft.setSpeed(200);
    }

    private void turnRight() {
        state = State.RIGHT;
        motorRight.setSpeed(200);
        motorLeft.setSpeed(400);
    }

    private boolean isOnLine(SampleProvider s, float colorThreshold) {
        return getColor(s) < colorThreshold;
    }

    private float getColor(SampleProvider sampleProvider) {
        sampleProvider.fetchSample(samples, 0);
        return samples[0];
    }

    private void calibrateThresholds() {
        System.out.println("Hold color-sensor over light color and press LEFT");
        while (!Button.LEFT.isDown()) ; // Wait for left-press

        float lightLeft = getColor(sampleProviderRight);
        float lightRight = getColor(sampleProviderleft);

        System.out.println("Hold color-sensor over dark color and press RIGHT");
        while (!Button.RIGHT.isDown()) ; // Wait for right-press

        float darkLeft = getColor(sampleProviderRight);
        float darkRight = getColor(sampleProviderleft);

        colorThresholdRight = (lightLeft - darkLeft) / 2f + darkLeft;
        colorThresholdLeft = (lightRight - darkRight) / 2f + darkRight;
        LCD.clear();
    }
}
