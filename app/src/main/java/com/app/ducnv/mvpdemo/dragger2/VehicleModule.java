package com.app.ducnv.mvpdemo.dragger2;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ducng on 10/5/2017.
 */

@Module
public class VehicleModule {

    @Provides
    @Singleton
    Motor provideMotor() {
        return new Motor();
    }

    @Provides
    @Singleton
    Vehicle provideVehicle() {
        return new Vehicle(new Motor());
    }


}
