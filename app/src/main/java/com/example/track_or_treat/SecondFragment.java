package com.example.track_or_treat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.sql.SQLOutput;

public class SecondFragment extends Fragment {

    public static String userLoc, friendLoc, dest; //change these to what you want the variable names to be

    EditText userInput;
    EditText friendInput;
    EditText destInput;

    Button buttonBar, buttonRes, buttonThe;

    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userInput = (EditText) ((MainActivity) getActivity()).findViewById(R.id.userAdd);
        friendInput = (EditText) ((MainActivity) getActivity()).findViewById(R.id.friendAdd);

        buttonBar = (Button) ((MainActivity) getActivity()).findViewById(R.id.button);
        buttonRes = (Button) ((MainActivity) getActivity()).findViewById(R.id.button2);
        buttonThe = (Button) ((MainActivity) getActivity()).findViewById(R.id.button3);
        buttonBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //also change variable names here
                userLoc = userInput.getText().toString().replace(" ", "+");
                friendLoc = friendInput.getText().toString().replace(" ", "+");
                dest = "bar";
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_mapsActivity);

            }
        });
        buttonRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //also change variable names here
                userLoc = userInput.getText().toString().replace(" ", "+");
                friendLoc = friendInput.getText().toString().replace(" ", "+");
                dest = "restaurant";
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_mapsActivity);

            }
        });
        buttonThe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //also change variable names here
                userLoc = userInput.getText().toString().replace(" ", "+");
                friendLoc = friendInput.getText().toString().replace(" ", "+");
                dest = "movie_theater";
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_mapsActivity);

            }
        });
    }

}





