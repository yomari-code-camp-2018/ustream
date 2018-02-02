package com.sankalpa.mustream;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void speaker(View view) {
        Config.getInstance().setMode(Mode.SPEAKER);
        Intent intent = new Intent(this, SpeakerActivity.class);
        startActivity(intent);
    }

    public void mediaPlayer(View view) {
        Config.getInstance().setMode(Mode.MUSIC_PLAYER);
        Intent intent = new Intent(this, MediaPlayerActivity.class);
        startActivity(intent);
    }
}