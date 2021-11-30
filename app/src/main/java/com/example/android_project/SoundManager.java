package com.example.android_project;


import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class SoundManager {

    private SoundPool soundPool;
    private HashMap<String, Integer> mapDeSon;
    private boolean isPoolLoaded;
    private static final float VOLUME = 0.9f;

    public SoundManager(GameActivity pContext) {

        mapDeSon = new LinkedHashMap();

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        SoundPool.Builder builder = new SoundPool.Builder();
        builder.setAudioAttributes(audioAttributes).setMaxStreams(5);

        isPoolLoaded = false;
        soundPool = builder.build();

        this.soundPool.setOnLoadCompleteListener((soundPool, sampleId, status) -> isPoolLoaded = true);

        mapDeSon.put("piou", this.soundPool.load(pContext, R.raw.laserfire01, 1));

    }

    public void start(){
    }

    public void stop(){

    }

    public void pause(){
        soundPool.stop(mapDeSon.get("piou"));
    }

    public void piouSound(){
        if(isPoolLoaded)
            soundPool.play(mapDeSon.get("piou"), VOLUME, VOLUME, 1, 0, 1f);
    }
}
