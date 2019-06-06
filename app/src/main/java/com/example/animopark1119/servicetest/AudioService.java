package com.example.animopark1119.servicetest;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class AudioService extends Service {

    private final IBinder iBinder=new MyBinder();

    int count=0;

     MediaPlayer mediaPlayer;
     MediaPlayer mp3;
     MediaPlayer mp2;
    int[] res={R.raw.a,R.raw.b,R.raw.c,R.raw.d};

    @Override
    public IBinder onBind(Intent intent) {

        return iBinder;
    }


    @Override
    public void onCreate() {

    mediaPlayer = MediaPlayer.create(getApplicationContext(), res[count]);
        mp2 = MediaPlayer.create(getApplicationContext(),res[1] );
        mp3 = MediaPlayer.create(getApplicationContext(),res[2] );
        mp2.setLooping(true);
        mp3.setLooping(true);
        mediaPlayer.setLooping(true);
        Toast.makeText(getApplicationContext(), "Service Created", Toast.LENGTH_LONG).show();
      }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(getApplicationContext(),"Service Started",Toast.LENGTH_LONG).show();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
    }

    public class MyBinder extends Binder {
       AudioService getService(){
       return AudioService.this;
          }

    }// MyBinder Class

        public int getMusicDuration() {
        return mediaPlayer.getDuration();
    }

    public int getMusicCurPos(){

        return mediaPlayer.getCurrentPosition();
}

public void seekToPos(int pos){
        mediaPlayer.seekTo(pos);
    }
public void playNextSong(){
        mp3.stop();
       mediaPlayer.stop();
      }
public void prevSong(){
    mp2.stop();
    mediaPlayer.stop();

}

}//class
