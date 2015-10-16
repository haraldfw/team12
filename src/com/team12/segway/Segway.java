package com.team12.segway;

import lejos.hardware.BrickFinder;
import lejos.hardware.motor.Motor;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.robotics.SampleProvider;

/**
 * Created by haraldfw on 10/16/15.
 */
public class Segway {

    NXTRegulatedMotor motorLeft = Motor.A;
    NXTRegulatedMotor motorRight = Motor.B;
    SampleProvider gyro;
    float[] samples;

    public Segway() {
        gyro = new EV3GyroSensor(BrickFinder.getDefault().getPort("1")).getAngleMode();
        samples = new float[gyro.sampleSize()];
    }

    // This method is called as often as possible. This is where the segway reacts to gyro-input
    public void update() {
        
    }

    private float readGyro() {
        gyro.fetchSample(samples, 0);
        return samples[0];
    }
}
