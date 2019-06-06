package com.example.animopark1119.servicetest;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Filter;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {


    AudioService audioService;
    SeekBar seekBar;
    public TextView startTimeField, endTimeField;
    Boolean bound = false;
    Button play;
    Button back;
    Button next;
    Runnable runnable;

    Handler handler=new Handler();

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            AudioService.MyBinder b = (AudioService.MyBinder) service;
            audioService = b.getService();
            bound=true;
            Toast.makeText(MainActivity.this, "Service Bound", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bound=false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seekBar = findViewById(R.id.seekbar);
        startTimeField = findViewById(R.id.tvStartTime);
        endTimeField = findViewById(R.id.tvEndtime);
         back = findViewById(R.id.bBackward);
         play = findViewById(R.id.bPlay);
        next = findViewById(R.id.bForward);

        runnable=new Runnable() {
            @Override
            public void run() {
             seekBarStatus();
            }
        };


           seekBar.setOnSeekBarChangeListener(this);

           play.setOnClickListener(new View.OnClickListener() {
               @Override
                   public void onClick(View v) {

                if (bound){
                      if (!audioService.mp2.isPlaying()&&!audioService.mp3.isPlaying()) {
                        playSong();
                        seekBarStatus();
                    }
                    if (!audioService.mediaPlayer.isPlaying()&&!audioService.mp3.isPlaying()) {

                    }
                    if (!audioService.mediaPlayer.isPlaying()&&!audioService.mp2.isPlaying()) {

                    }
                }
            }
           });
        next.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v) {

                    if (bound) {
                        audioService.playNextSong();
                        playNextSong();
                        seekBarNextStatus();
                    }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (bound){
                   audioService.prevSong();
                   playPrevSong();
                   seekBarPrevStatus();
                }
            }
        });

    }// oncreate

    @Override
    protected void onResume() {
        super.onResume();
            Intent intent = new Intent(MainActivity.this, AudioService.class);
            startService(intent);
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }
    @Override
    protected void onStart() {
        super.onStart();
    }
   @Override
    protected void onDestroy() {
       super.onDestroy();
       if (bound){
           unbindService(serviceConnection);
           audioService.mediaPlayer.stop();
       }

   }

         public void playSong(){
            if(bound){

                if (audioService.mediaPlayer.isPlaying()) {
                  audioService.mediaPlayer.pause();
                   play.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp);

               }
               else {
                   audioService.mediaPlayer.start();
                   play.setBackgroundResource(R.drawable.ic_pause_black_24dp);
               }
                  seekBar.setMax(audioService.getMusicDuration());
           }
    }

    public void playNextSong(){

        if(bound){
            if (audioService.mp2.isPlaying()) {
                audioService.mp2.pause();
                play.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp);
            }
            else {
                audioService.mp2.start();
                play.setBackgroundResource(R.drawable.ic_pause_black_24dp);
            }
            seekBar.setMax(audioService.mp2.getDuration());
        }
    }

    public void playPrevSong(){

        if(bound){
            if (audioService.mp3.isPlaying()) {
                audioService.mp3.pause();
                play.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp);
            }
            else {
                audioService.mp3.start();
                play.setBackgroundResource(R.drawable.ic_pause_black_24dp);
            }
            seekBar.setMax(audioService.mp3.getDuration());
        }
    }
    public void seekBarNextStatus() {

        Handler had=new Handler();
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
               seekBarNextStatus();
            }
        };
        if (bound) {
            //audioService.mediaPlayer.reset();
            if (audioService.mp2.isPlaying())
            seekBar.setProgress(audioService.mp2.getCurrentPosition());
            had.postDelayed(runnable, 3000);
        }
    }

    public void seekBarPrevStatus() {

        Handler had=new Handler();
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                seekBarPrevStatus();
            }
        };
        if (bound) {
            //audioService.mediaPlayer.reset();
            if (audioService.mp3.isPlaying())
                seekBar.setProgress(audioService.mp3.getCurrentPosition());
            had.postDelayed(runnable, 3000);
        }
    }
    public void seekBarStatus() {
        if (bound) {
            //audioService.mediaPlayer.reset();
            seekBar.setProgress(audioService.getMusicCurPos());
            handler.postDelayed(runnable, 3000);
        }
       }
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putBoolean("ServiceState",bound);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        bound=savedInstanceState.getBoolean("ServiceState");
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(bound)
            audioService.seekToPos(progress);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}





