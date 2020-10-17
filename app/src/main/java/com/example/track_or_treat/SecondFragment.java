package com.example.track_or_treat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class SecondFragment extends Fragment {
    public String location;
    public int age, duration;
    public boolean driving;

    EditText ageInput;
    EditText locationsInput;
    EditText durationInput;
    EditText drivingInput;

    Button button;


    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ageInput = (EditText) ((MainActivity) getActivity()).findViewById(R.id.ageInput);
        locationsInput = (EditText) ((MainActivity) getActivity()).findViewById(R.id.locationInput);
        durationInput = (EditText) ((MainActivity) getActivity()).findViewById(R.id.durationInput);
        drivingInput = (EditText) ((MainActivity) getActivity()).findViewById(R.id.drivingInput);



        button = (Button) ((MainActivity) getActivity()).findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                age = Integer.valueOf(ageInput.getText().toString());
                location = locationsInput.getText().toString();
                duration = Integer.valueOf(durationInput.getText().toString());
                if (drivingInput.getText().toString().toLowerCase() == "yes") {
                    driving = true;
                } else {
                    driving = false;
                }

            }
        });

    }
}





