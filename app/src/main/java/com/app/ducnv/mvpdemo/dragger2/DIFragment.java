package com.app.ducnv.mvpdemo.dragger2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.ducnv.mvpdemo.R;

public class DIFragment extends Fragment {

    Vehicle vehicle;

    public DIFragment() {

    }

    public static DIFragment newInstance() {
        DIFragment fragment = new DIFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_di, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        VehicleComponent component = DaggerVehicleComponent.builder().vehicleModule(new VehicleModule()).build();
        vehicle = component.provideVehicle();
        vehicle.increaseSpeed(3);
        Toast.makeText(getActivity(), String.valueOf(vehicle.getSpeed()), Toast.LENGTH_SHORT).show();
    }
}
