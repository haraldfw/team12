package com.team12.segway;

import lejos.hardware.BrickFinder;
import lejos.hardware.motor.Motor;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.sensor.EV3GyroSensor;

/**
 * Created by haraldfw on 10/16/15.
 */
public class Segway {

    NXTRegulatedMotor motorRight = Motor.A;
    NXTRegulatedMotor motorLeft = Motor.B;
    /**
     * The gyro-sensor to be used by the segway.
     */
    EV3GyroSensor gyroSensor;
    /**
     * Needed to read values from the sensor.
     */
    float[] samples;
    /**
     * A boolean that is true once the segway has fallen.
     */
    boolean hasFallen = false;

    /**
     * returns a new Segway-object
     */
    public Segway() {
        gyroSensor = new EV3GyroSensor(BrickFinder.getDefault().getPort("S1"));
        samples = new float[gyroSensor.getAngleMode().sampleSize()];
    }

    /**
     * This method is called as often as possible. This is where the segway reacts to gyro-input.
     */
    public void update() {
        float angle = readAngle();
        float rate = readRate();
        motorLeft.setSpeed(50 * rate);
        motorRight.setSpeed(50 * rate);

        if (angle < 50 || angle > 130) {
            hasFallen = true;
        }

        if (angle < 90) {
            motorLeft.backward();
            motorRight.backward();
        } else if (angle > 90) {
            motorLeft.forward();
            motorRight.forward();
        }
    }

    /**
     * Returns the value for the angle of the gyrosensor.
     *
     * @return The angle value of the sensor in degrees.
     */
    private float readAngle() {
        gyroSensor.getAngleMode().fetchSample(samples, 0);
        return samples[0];
    }

    /**
     * Returns the rotation-velocity of the sensor in degrees per second.
     *
     * @return The rotation-velocity in degrees per second.
     */
    private float readRate() {
        gyroSensor.getRateMode().fetchSample(samples, 0);
        return samples[0];
    }
}
