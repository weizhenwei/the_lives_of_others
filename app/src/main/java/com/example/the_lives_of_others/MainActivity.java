package com.example.the_lives_of_others;

import java.io.File;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioRecord;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.the_lives_of_others.AudioConfig;
import com.example.the_lives_of_others.AudioUtils;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int MY_PERMISSIONS_REQUEST = 1001;
    private static final String TAG = "LoO";

    /*
     * UI
     */
    private Button buttonControlRecord;
    private Button buttonDisplay;

    /*
     * audio
     */
    private AudioUtils audioUtils;


    /**
     * permission required;
     */
    private String[] permissions = new String[]{
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    /**
     * permission refused by user;
     */
    private List<String> mPermissionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermissions();

        audioUtils = new AudioUtils();
        audioUtils.init();

        buttonControlRecord = (Button) findViewById(R.id.buttonControlRecord);
        buttonDisplay       = (Button) findViewById(R.id.buttonDisplay);
        buttonControlRecord.setOnClickListener(this);
        buttonDisplay.setOnClickListener(this);



    }

    private void checkPermissions() {
        // after Marshmallow version, support runtime permission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (int i = 0; i < permissions.length; i++) {
                if (ContextCompat.checkSelfPermission(this, permissions[i]) !=
                        PackageManager.PERMISSION_GRANTED) {
                    mPermissionList.add(permissions[i]);
                }
            }
            if (!mPermissionList.isEmpty()) {
                String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);
                ActivityCompat.requestPermissions(this, permissions, MY_PERMISSIONS_REQUEST);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, permissions[i] + " forbidden by user!");
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonControlRecord: {
                Button button = (Button) view;
                if (button.getText().toString().equals("Start_Record")) {
                    button.setText("Stop_Record");
                    File fileDirectory = getExternalFilesDir(Environment.DIRECTORY_MUSIC);
                    Log.i(TAG, "file dirctory " + fileDirectory);
                    audioUtils.startRecord(fileDirectory);
                } else {
                    button.setText("Start_Record");
                    audioUtils.stopRecord();
                }
                break;
            }
            case R.id.buttonDisplay: {
                Button button = (Button) view;
                String string = button.getText().toString();
                if (string.equals("Start_Display")) {
                    button.setText("Stop_Display");
                    audioUtils.startPlay();
                } else {
                    button.setText("Start_Display");
                    audioUtils.stopPlay();
                }
                break;
            }
            case R.id.buttonTranslate:
                break;
            default:
                break;
        }
    }
}
