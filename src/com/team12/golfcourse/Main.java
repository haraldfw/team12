package com.team12.golfcourse;

import lejos.hardware.Button;

/**
 * Created by haraldfw on 9/18/15.
 */
public class Main {
    public static void main(String[] args) {
        PressureSensitiveCar car = new PressureSensitiveCar();
        while(!Button.ENTER.isDown()) {
            try {
                car.update();
            } catch(Exception e) {

            }
        }
    }

}
