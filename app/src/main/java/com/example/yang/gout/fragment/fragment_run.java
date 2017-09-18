package com.example.yang.gout.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yang.gout.R;
import com.example.yang.gout.RunningActivity;
import com.example.yang.gout.run.DB.RunRecordManager;

public class fragment_run extends Fragment {
    private TextView go;
    private TextView distanceLabel;
    public fragment_run() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_run,null);
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        go= (TextView)getActivity().findViewById(R.id.run);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "已经点击", Toast.LENGTH_LONG).show();
                Intent intent=new Intent(getActivity(),RunningActivity.class);
                startActivity(intent);
            }
        });

        distanceLabel = (TextView)getActivity().findViewById(R.id.distanceLabel);
    }

    @Override
    public void onResume() {
        super.onResume();
        RunRecordManager manager = new RunRecordManager(getContext());
        distanceLabel.setText(manager.totalDistance()*0.001+"");
    }
}
