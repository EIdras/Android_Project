package com.example.android_project;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ProjectileManager {

    private GameActivity appCompatActivity;
    private List<Projectile> piouList = new ArrayList<Projectile>();
    private Projectile piou;
    private Bitmap pPiou;
    private SpaceShip spaceShip;

    public ProjectileManager(GameActivity appCompatActivity, SpaceShip spaceShip) {
        this.appCompatActivity = appCompatActivity;
        this.spaceShip = spaceShip;

        this.pPiou = BitmapFactory.decodeResource(appCompatActivity.getResources(), R.drawable.piou);
        this.pPiou = Bitmap.createScaledBitmap(pPiou, (int) (pPiou.getWidth() * 0.55), (int) (pPiou.getHeight() * 0.55), true);
    }

    private Timer timer;

    private TimerTask timerTask = new TimerTask() {

        @Override
        public void run() {
            // Les instructions suivantes sont exécutées à intervalle régulier

            piou = new Projectile(spaceShip.getShipPosX() + spaceShip.getBitmap().getWidth() / 2 - piou.getBitmap().getWidth() / 2,
                    spaceShip.getShipPosY() - piou.getBitmap().getHeight(),
                    pPiou);

            piouList.add(piou);

        }
    };

    public void start() {
        if(timer != null) {
            return;
        }
        timer = new Timer();
        timer.scheduleAtFixedRate(timerTask, 0, 2000);
    }

    public void stop() {
        timer.cancel();
        timer = null;
    }

    public List<Projectile> getPiouList() {
        return piouList;
    }

    public void setPiouList(List<Projectile> piouList) {
        this.piouList = piouList;
    }

    public Bitmap getpPiou() {
        return pPiou;
    }

    public void setpPiou(Bitmap pPiou) {
        this.pPiou = pPiou;
    }
}
