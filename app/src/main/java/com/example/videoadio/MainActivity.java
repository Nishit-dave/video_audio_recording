package com.example.videoadio;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static int MICROPHONE_PERMISSION_CODE = 200;
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;

    private static int CAMERA_PERMISSION_CODE = 100;
    private static int VIDEO_RECORD_CODE = 101;

    private Uri videoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (isCameraPresentInPhone()){
            Log.i("VIDEO_REOCRD_TAG", "Camara is detected");
            getCameraPremission();
        }
        else {
            Log.i("VIDEO_REOCRD_TAG", "No camara is not detected");
        }

        if (isMicrophonePresent()){
            getMicrophonePresent();
        }
    }

    public void btnRecodePressed(View v){

        try {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setOutputFile(getRecordingFilePayh());
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.prepare();
            mediaRecorder.start();

            Toast.makeText(this, "Reciding is started", Toast.LENGTH_LONG).show();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public void btnStopPressed(View v){

        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;

        Toast.makeText(this, "Reciding is stop", Toast.LENGTH_LONG).show();
    }

    public void btnPlayPressed(View v){

        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(getRecordingFilePayh());
            mediaPlayer.prepare();
            mediaPlayer.start();

            Toast.makeText(this, "Reciding is play", Toast.LENGTH_LONG).show();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    private boolean isMicrophonePresent(){
        if (this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE)){
            return true;
        }
        else {
            return false;
        }
    }

    private void getMicrophonePresent (){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new  String[] {Manifest.permission.RECORD_AUDIO}, MICROPHONE_PERMISSION_CODE );
        }
    }

    private String getRecordingFilePayh(){
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File musicDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File file = new File(musicDirectory, "testRcordingFile" + ".mp3");
        return file.getPath();
    }

    public void recordVideoButtonPressed(View v){

        recordVideo();

    }

    private boolean isCameraPresentInPhone(){
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)){
            return true;
        }
        else {
            return false;
        }
    }

    private void getCameraPremission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
    }

    private void recordVideo(){
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(intent, VIDEO_RECORD_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VIDEO_RECORD_CODE){

            if (resultCode == RESULT_OK){


                videoPath = data.getData();
                Log.i("VIDEO_REOCRD_TAG", "Video is record and avaialbe at path" + videoPath);
            }
            else if (resultCode == RESULT_CANCELED){
                Log.i("VIDEO_REOCRD_TAG", "Recoring vide Video is canceled");
            }
            else {
                Log.i("VIDEO_REOCRD_TAG", "Recoring vide Video has got some error");
            }
        }
    }
}