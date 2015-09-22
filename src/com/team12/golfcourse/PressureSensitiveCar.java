package com.team12.golfcourse;

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.motor.Motor;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.sensor.EV3TouchSensor;

/**
 * Created by haraldfw on 9/18/15.
 */
public class PressureSensitiveCar {

    private float[] samples;

    private EV3TouchSensor touchLeft;
    private EV3TouchSensor touchRight;
    private NXTRegulatedMotor motorLeft = Motor.C;
    private NXTRegulatedMotor motorRight = Motor.B;

    public PressureSensitiveCar() {
        Brick brick = BrickFinder.getDefault();
        touchLeft = new EV3TouchSensor(brick.getPort("S2"));
        touchRight = new EV3TouchSensor(brick.getPort("S1"));
        motorLeft.setSpeed(200);
        motorRight.setSpeed(200);
        samples = new float[touchLeft.sampleSize()];
        forward();
    }

    public void update() throws InterruptedException {
        boolean leftPressed = isPressed(touchLeft);
        boolean rightPressed = isPressed(touchRight);

        if (leftPressed && rightPressed) {
            stop();
            backward();
            Thread.sleep(1500);
            if (Math.random() < 0.5) {
                turnLeft();
            } else {
                turnRight();
            }
        } else if (leftPressed) {
            stop();
            backward();
            Thread.sleep(500);
            turnRight();
        } else if (rightPressed) {
            stop();
            backward();
            Thread.sleep(500);
            turnLeft();
        }
    }

    private void backward() {
        motorLeft.backward();
        motorRight.backward();
    }

    private void forward() {
        motorLeft.forward();
        motorRight.forward();
    }

    private void turnLeft() throws InterruptedException {
        motorLeft.backward();
        motorRight.forward();
        Thread.sleep(500);
        forward();
    }

    private void turnRight() throws InterruptedException {
        motorLeft.forward();
        motorRight.backward();
        Thread.sleep(500);
        forward();
    }

    private void stop() throws InterruptedException {
        Motor.A.stop();
        Motor.B.stop();
    }

    private boolean isPressed(EV3TouchSensor sensor) {
        sensor.fetchSample(samples, 0);
        if (samples[0] == 0) {
            return false;
        }
        return true;
    }
}
