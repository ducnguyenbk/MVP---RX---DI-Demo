package com.app.ducnv.mvpdemo.dragger2;

import javax.inject.Inject;

/**
 * Created by ducng on 10/5/2017.
 */

public class Vehicle {
    private Motor motor;

    @Inject
    public Vehicle(Motor motor) {
        this.motor = motor;
    }

    public void increaseSpeed(int value) {
        motor.calculated(value);
    }

    public void stop() {
        motor.brake();
    }

    public int getSpeed() {
        return motor.getValue();
    }

}


