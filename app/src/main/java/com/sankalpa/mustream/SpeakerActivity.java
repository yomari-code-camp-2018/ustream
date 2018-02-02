package com.sankalpa.mustream;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.sankalpa.mustream.events.PauseEvent;
import com.sankalpa.mustream.events.PlayEvent;
import com.sankalpa.mustream.events.PrepareEvent;
import com.sankalpa.mustream.events.ServerAddressEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

public class SpeakerActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {

    private static final String TAG = "Speaker";
    TextView ipAddress;
    Thread latencyThread;
    Thread websocketClient;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speaker);
        ipAddress = findViewById(R.id.ip_address);
        websocketClient = new Thread(new SyncClient());
        websocketClient.start();
        mp = new MediaPlayer();
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    public void scan(View view) {

    }

    public void play(View view) {
        if(mp.isPlaying()){
            mp.pause();
        } else {
            mp.start();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop(){
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void pauseMusic(PauseEvent e){
        if(mp.isPlaying()){
            mp.pause();
        }
    }
    @Subscribe
    public void playMusic(PlayEvent e){
       if(!mp.isPlaying()){
           mp.start();
       }
    }
    @Subscribe
    public void prepareMusic(PrepareEvent e){
        try {
            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + e.music);
            mp.setDataSource(this, uri);
            mp.setOnErrorListener(this);
            mp.setOnPreparedListener(this);
            mp.prepareAsync();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }
}
