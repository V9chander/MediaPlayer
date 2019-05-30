package com.example.animopark1119.servicetest;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class AudioService extends Service implements
        MediaPlayer.OnCompletionListener {
    //media player
     MediaPlayer player;
    //song list

    //current position
    private int songPosn;
    int[] songs={R.raw.a,R.raw.b};
    int mCompleted = 0;


     @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {

       Toast.makeText(getApplicationContext(),"Service Created",Toast.LENGTH_LONG).show();
          player=MediaPlayer.create(getApplicationContext(),songs[0]);
            player.setLooping(true);


     }






    @Override
    public void onDestroy() {
        if (player.isPlaying()) {
            player.stop();
        }
        player.release();
        }


    @Override
    public void onStart(Intent intent, int startId) {

        return;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!player.isPlaying()) {
            player.start();
        }
        return START_STICKY;
    }
    public void onStop() {

    }
    public void onPause() {
        if(player != null && player.isPlaying()){
            player.pause();
        } else {
            player.start();
        }
    }



    @Override
    public void onCompletion(MediaPlayer mp) {



    }


    int songindex;
    public void Next()
    {
        MediaPlayer mPlayer2;
        if(songindex==0)
        {
            player=MediaPlayer.create(this, R.raw.a);
            player.start();
        }
        else if(songindex==1)
        {
            player= MediaPlayer.create(this, R.raw.b);
            player.start();
        }
    }

}//class



