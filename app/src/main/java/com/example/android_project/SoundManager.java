package com.example.android_project;


import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class SoundManager {

    MediaPlayer piou;

    public SoundManager(GameActivity pContext) {

        piou = MediaPlayer.create(pContext.getApplicationContext(), R.raw.laserfire01);
        piou.setVolume(0.2f, 0.2f);
    }

    public void start(){
        piou.start();
    }

    public void stop(){
        piou.stop();
    }
}
