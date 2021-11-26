package com.example.android_project;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ProjectileManager {

    private static int shootFrequency = 200;

    private GameActivity appCompatActivity;
    private SoundManager soundManager;
    private List<Projectile> playerPiouList = new ArrayList<Projectile>();
    private Projectile piou;
    private Bitmap piouBitmap;
    private SpaceShip spaceShip;

    public ProjectileManager(GameActivity appCompatActivity, SpaceShip spaceShip) {
        this.appCompatActivity = appCompatActivity;
        this.spaceShip = spaceShip;

        this.piouBitmap = BitmapFactory.decodeResource(appCompatActivity.getResources(), R.drawable.piou);
        this.piouBitmap = Bitmap.createScaledBitmap(piouBitmap, (int) (piouBitmap.getWidth() * 0.6), (int) (piouBitmap.getHeight() * 0.6), true);
        this.soundManager = new SoundManager(appCompatActivity);
    }

    private Timer timer;

        private TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {
                // Les instructions suivantes sont exécutées à intervalle régulier
                // L'intervalle en question est spécifié par la variable 'shootFrequency'

                piou = new Projectile(
                        spaceShip.getShipPosX() + spaceShip.getBitmap().getWidth() / 2 - piouBitmap.getWidth() / 2,
                        spaceShip.getShipPosY() - piouBitmap.getHeight(),
                        piouBitmap);

                // A régler
                //soundManager.stop();
                //soundManager.start();

                synchronized (playerPiouList) {
                    playerPiouList.add(piou);
                }
            }
        };



    public void start() {
        if (timer != null) {
            return;
        }
        timer = new Timer();

        timer.scheduleAtFixedRate(timerTask, 0, shootFrequency);

    }

    public void stop() {
        timer.cancel();
        timer = null;
    }

    public List<Projectile> getPiouList() {
        return playerPiouList;
    }

    public void setPiouList(List<Projectile> piouList) {
        this.playerPiouList = piouList;
    }

    public Bitmap getPiouBitmap() {
        return piouBitmap;
    }

    public void setPiouBitmap(Bitmap piouBitmap) {
        this.piouBitmap = piouBitmap;
    }


}
