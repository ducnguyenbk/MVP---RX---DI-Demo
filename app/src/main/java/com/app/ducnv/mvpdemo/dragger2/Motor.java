package com.app.ducnv.mvpdemo.dragger2;

import android.util.Log;

/**
 * Created by ducng on 10/5/2017.
 */

public class Motor {
    private int value;

    public Motor() {
        value = 0;
    }

    public int getValue() {
        return value;
    }

    public void calculated(int x) {
        value = value + x;
    }

    public void brake() {
        value = 0;
    }

}
