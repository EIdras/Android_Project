package com.example.android_project;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ProjectileManager {

    private static int shootFrequency = 800;

    private GameActivity appCompatActivity;
    private SoundManager soundManager;
    private List<Projectile> playerPiouList = new ArrayList<Projectile>();
    private Projectile piou;
    private Bitmap pPiou;
    private SpaceShip spaceShip;

    public ProjectileManager(GameActivity appCompatActivity, SpaceShip spaceShip) {
        this.appCompatActivity = appCompatActivity;
        this.spaceShip = spaceShip;

        this.pPiou = BitmapFactory.decodeResource(appCompatActivity.getResources(), R.drawable.piou);
        this.pPiou = Bitmap.createScaledBitmap(pPiou, (int) (pPiou.getWidth() * 0.6), (int) (pPiou.getHeight() * 0.6), true);
        this.soundManager = new SoundManager(appCompatActivity);
    }

    private Timer timer;

        private TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {
                // Les instructions suivantes sont exécutées à intervalle régulier

                piou = new Projectile(
                        spaceShip.getShipPosX() + spaceShip.getBitmap().getWidth() / 2 - pPiou.getWidth() / 2,
                        spaceShip.getShipPosY() - pPiou.getHeight(),
                        pPiou);

                //soundManager.stop();
                soundManager.start();

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

    public Bitmap getpPiou() {
        return pPiou;
    }

    public void setpPiou(Bitmap pPiou) {
        this.pPiou = pPiou;
    }


}
