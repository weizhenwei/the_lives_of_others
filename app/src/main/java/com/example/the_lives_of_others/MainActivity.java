package com.example.the_lives_of_others;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button buttonStartRecord;
    private Button buttonStopRecord;
    private Button buttonDisplay;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonStartRecord = (Button) findViewById(R.id.buttonStartRecord);
        buttonStopRecord  = (Button) findViewById(R.id.buttonStopRecord);
        buttonDisplay     = (Button) findViewById(R.id.buttonDisplay);
        buttonStopRecord.setOnClickListener(this);
        buttonStopRecord.setOnClickListener(this);
        buttonDisplay.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonStartRecord:
                break;
            case R.id.buttonStopRecord:
                break;
            case R.id.buttonDisplay:
                break;
            case R.id.buttonTranslate:
                break;
            default:
                break;
        }
    }
}
