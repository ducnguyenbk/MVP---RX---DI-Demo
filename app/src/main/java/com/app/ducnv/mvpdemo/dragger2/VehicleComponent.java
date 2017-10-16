package com.app.ducnv.mvpdemo.dragger2;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by ducng on 10/5/2017.
 */

@Singleton
@Component(modules = {VehicleModule.class})
public interface VehicleComponent {
    Vehicle provideVehicle();
}
