package com.team12.segway;

import lejos.hardware.Button;

/**
 * Created by haraldfw on 10/16/15.
 */
public class Main {

    public static void main(String[] args) {
        Segway segway = new Segway();
        while(!Button.ENTER.isDown()) {
            segway.update();
        }
    }
}
