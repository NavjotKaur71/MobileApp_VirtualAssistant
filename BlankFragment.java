package com.example.mycomputer.voicemobileapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


public class BlankFragment extends Fragment {
    public BlankFragment()
    {

    }
    ImageButton ib1,ib2,ib3;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_blank, container, false);

    }
    public void onResume(){
        ib1=(ImageButton)getActivity().findViewById(R.id.ib2);
        ib2=(ImageButton)getActivity().findViewById(R.id.ib3);
        ib3=(ImageButton)getActivity().findViewById(R.id.ib4);
        ib3.setOnClickListener(new View.OnClickListener()
        {
                                   @Override
                                   public void onClick(View v) {

                                   }
                               }
        );




}}
