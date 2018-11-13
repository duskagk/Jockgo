package com.example.duskagk.jockgo;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;

public class Fragstudyline extends Fragment {
    View view;
    public Fragstudyline(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.studyline_f,container,false);
        return view;
    }
}
