package com.team12.segway;

import lejos.hardware.Button;

/**
 * Created by haraldfw on 10/16/15.
 */
public class Main {

    /**
     * Initializes a segway-object and calls it's update-method as often as possible until the segway has fallen.
     *
     * @param args Not used
     */
    public static void main(String[] args) {
        Segway segway = new Segway();
        while (!Button.ENTER.isDown() && !segway.hasFallen) {
            segway.update();
        }
    }
}
