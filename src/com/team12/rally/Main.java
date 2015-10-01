package com.team12.rally;

import lejos.hardware.Button;
import lejos.hardware.sensor.BaseSensor;

import java.util.ArrayList;

/**
 * Created by haraldfw on 10/1/15.
 */
public class Main {
    static ArrayList<BaseSensor> closables = new ArrayList<>();

    public static void main (String[] arg) throws Exception  {
        Rally car = new Rally(closables);

        while (!Button.ENTER.isDown()){ // Continue while the enter-button is not pressed
            car.update();
        }

        for(BaseSensor c : closables) {
            c.close();
        }
    }
}
