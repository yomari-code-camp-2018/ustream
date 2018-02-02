package com.sankalpa.mustream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.sankalpa.mustream.events.PauseEvent;
import com.sankalpa.mustream.events.PlayEvent;
import com.sankalpa.mustream.events.PlayNextEvent;
import com.sankalpa.mustream.events.PrepareEvent;
import com.sankalpa.mustream.events.StopEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MediaPlayerActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {
    private static final String TAG = "MediaPlayer";

    MediaPlayer mp;
    Thread server ;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);

        TextView textView = findViewById(R.id.ip_address);

        String ipAddress = Network.getWifiIpAddress(this);


        textView.setText(ipAddress);

        server = new Thread(new SyncServer(this));
        server.start();
        mp = new MediaPlayer();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            try {
                if(mp.isPlaying()) {
                    mp.stop();
                }
                mp.setOnErrorListener(this);
                mp.setOnPreparedListener(this);
                mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mp.pause();
                Uri datum = data.getData();
                mp.setDataSource(this, datum);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mp.prepareAsync();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop(){
        super.onStop();
    }
    public void play(View view){
        if(mp.isPlaying()){
            EventBus.getDefault().post(new PauseEvent());
            mp.pause();
            ((Button) findViewById(R.id.play_pause)).setText("Play");
        } else{
            EventBus.getDefault().post(new PlayEvent());
            mp.start();
            ((Button) findViewById(R.id.play_pause)).setText("Pause");
        }
    }


    public void select(View view) {
        intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/*");
        startActivityForResult(intent,1);
    }

    public void stop(View view) {
        mp.stop();
        EventBus.getDefault().post(new StopEvent());
    }

    public void playNext(View view) {
        EventBus.getDefault().post(new PlayNextEvent());
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.e(TAG, "Error playing file");
        return false;
    }
}
