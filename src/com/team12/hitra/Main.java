package com.team12.hitra;

import lejos.hardware.Button;

/**
 * Created by haraldfw on 9/7/15.
 */
public class Main {

    public static void main(String[] args) {
        TunnelWasher washer = new TunnelWasher();

        washer.wash();

        while (!Button.ENTER.isDown()) {
            System.out.println("Driving");
            washer.drive(500);
            washer.listenForHonk();
            if(washer.getColor() < washer.colorThreshold) {
                System.out.println("Turning...");
                washer.turn();
            }
        }

        System.out.println("Washing finished");
    }
}