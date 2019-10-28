package com.example.queueskip.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.queueskip.R;
import com.example.queueskip.ui.dashboard.DashboardViewModel;

public class profile_fragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

      //  ViewModelProviders.of(this).get(DashboardViewModel.class);
        ViewModelProviders.of(this);
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        return view;
    }

}
