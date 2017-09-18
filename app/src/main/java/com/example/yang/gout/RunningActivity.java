package com.example.yang.gout;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.yang.gout.run.DB.RunRecordManager;
public class RunningActivity extends Activity {

    private Button goBtn;
    private TextView distanceLabel;
    private TextView countLabel;
    private TextView speedLabel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running);
        goBtn = (Button)findViewById(R.id.gobtn);
        distanceLabel = (TextView)findViewById(R.id.distanceLabel);
        speedLabel = (TextView)findViewById(R.id.speedLabel);
        countLabel = (TextView)findViewById(R.id.countLabel);
        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RunningActivity.this,
                        RunActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        RunRecordManager manager = new RunRecordManager(this);
        distanceLabel.setText(manager.totalDistance()*0.001+"");
        countLabel.setText(manager.runCount()+"");
        speedLabel.setText(manager.advSpeed()+"m/s");

    }
}


