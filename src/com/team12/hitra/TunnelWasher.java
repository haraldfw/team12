package com.team12.hitra;

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.motor.Motor;
import lejos.hardware.motor.NXTRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.NXTSoundSensor;
import lejos.robotics.SampleProvider;

import java.util.ArrayList;

/**
 * Created by haraldfw on 9/7/15.
 */
public class TunnelWasher {

    private NXTRegulatedMotor washMotor;
    private NXTRegulatedMotor rightMotor;
    private NXTRegulatedMotor leftMotor;

    private NXTSoundSensor soundSensor;

    private float[] colorSamples;
    SampleProvider colorSampleProvider;

    float colorThreshold;

    public TunnelWasher() {
        washMotor = Motor.A;
        leftMotor = Motor.C;
        rightMotor = Motor.B;

        Brick brick = BrickFinder.getDefault();
        Port s1 = brick.getPort("S1"); // fargesensor
        Port s2 = brick.getPort("S2"); // trykksensor

        soundSensor = new NXTSoundSensor(s1);

        EV3ColorSensor colorSensor = new EV3ColorSensor(s2);
        colorSensor.setFloodlight(true);

        colorSampleProvider = colorSensor.getRGBMode();
        colorSamples = new float[colorSampleProvider.sampleSize()];

        System.out.println("Hold color-sensor over light color and press LEFT");
        while(!Button.LEFT.isDown()) {
        }

        float light = getColor();

        System.out.println("Hold color-sensor over dark color and press RIGHT");
        while(!Button.RIGHT.isDown()) {
        }

        float dark = getColor();

        colorThreshold = (light - dark) / 2f + dark;

        for(int i = 400; i >= 0; i--) {
            System.out.println("Starting in: " + i);
        }
    }

    public void drive(int speed) {
        leftMotor.setSpeed(speed);
        rightMotor.setSpeed(speed);
        leftMotor.forward();
        rightMotor.forward();
    }

    public void stop() {
        leftMotor.stop();
        rightMotor.stop();
    }

    public void wash() {
        washMotor.setSpeed(500);
        washMotor.forward();
    }

    public void stopWash() {
        washMotor.stop();
    }

    public void listenForHonk() {
        if(getSoundLevel() > 0.5f) {
            stop();
            System.out.println("Stop");
            try {
                Thread.sleep(2000);
            } catch (Exception e) {}
            drive(500);
        }
    }

    double getSoundLevel() {
        SampleProvider s = soundSensor.getDBAMode();
        float[] floats = new float[s.sampleSize()];
        s.fetchSample(floats, 0);
        return floats[0];
    }

    public float getColor() {
        colorSampleProvider.fetchSample(colorSamples, 0);
        return colorSamples[0];
    }

    public void turn() {
        stop();
        try {
            Thread.sleep(500);
        } catch (Exception e) {}
        rightMotor.setSpeed(500);
        rightMotor.forward();
        leftMotor.setSpeed(20);
        leftMotor.forward();

        try {
            Thread.sleep(1300);
        } catch (Exception e) {}
        stop();
    }
}
/*

import lejos.hardware.motor.*;
        import lejos.hardware.sensor.EV3TouchSensor;
        import lejos.hardware.sensor.EV3ColorSensor;
        import lejos.hardware.port.Port;
        import lejos.hardware.Brick;
        import lejos.hardware.BrickFinder;
        import lejos.robotics.SampleProvider;

public class FolgLinje_enkel{

    public static void main (String[] arg) throws Exception  {

        // Definerer sensorer:
        Brick brick = BrickFinder.getDefault();
        Port s1 = brick.getPort("S1"); // fargesensor
        Port s2 = brick.getPort("S2"); // trykksensor

        EV3ColorSensor fargesensor = new EV3ColorSensor(s1); // ev3-fargesensor
        SampleProvider fargeLeser = fargesensor.getMode("RGB");  // svart = 0.01..
        float[] fargeSample = new float[fargeLeser.sampleSize()];  // tabell som innholder avlest verdi

        SampleProvider trykksensor = new EV3TouchSensor(s2);
        float[] trykkSample = new float[trykksensor.sampleSize()]; // tabell som inneholder avlest verdi

        // Setter hastighet på roboten
        Motor.A.setSpeed(400);
        Motor.C.setSpeed(400);

        Motor.B.setSpeed(900);  // vifte arm

        // Beregn verdi for svart
        int svart = 0;
        for (int i = 0; i<100; i++){
            fargeLeser.fetchSample(fargeSample, 0);
            svart += fargeSample[0]* 100;
        }
        svart = svart / 100 + 5;
        System.out.println("Svart: " + svart);

        boolean fortsett = true;

        while (fortsett){ 	// Fortsett så lenge roboten ikke treffer noe
            fargeLeser.fetchSample(fargeSample, 0);
            if (fargeSample[0]*100 > svart){   // sjekk sort linje
                Motor.A.forward();
                Motor.B.forward();        // viftearm
                Motor.C.stop();  		// snu i  200 millisekund
                Thread.sleep(100);
                System.out.println("hvit");
            } else {
                // Kjør framover
                Motor.A.forward();
                Motor.C.forward();
                System.out.println("svart");
            }

            trykksensor.fetchSample(trykkSample, 0);
            if (trykkSample[0] > 0){
                System.out.println("Avslutter");
                fortsett = false;
            }

            Motor.A.stop();
            Motor.C.stop();
            Motor.B.stop();
        }
    }
}
*/