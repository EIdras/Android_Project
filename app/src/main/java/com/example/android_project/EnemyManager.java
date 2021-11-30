package com.example.android_project;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class EnemyManager {
    private GameActivity appCompatActivity;
    private List<EnemyShip> enemyShipList = new ArrayList<EnemyShip>();
    private EnemyShip enemyShip;
    private Bitmap shipBitmap;

    public EnemyManager(GameActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;

        Matrix matrix = new Matrix();
        matrix.postRotate(180);
        this.shipBitmap = BitmapFactory.decodeResource(appCompatActivity.getResources(), R.drawable.ennemy_ship);
        this.shipBitmap = Bitmap.createBitmap(shipBitmap, 0, 0, shipBitmap.getWidth(), shipBitmap.getHeight(), matrix, false);
        this.shipBitmap = Bitmap.createScaledBitmap(shipBitmap, (int) (shipBitmap.getWidth() * 0.4), (int) (shipBitmap.getHeight() * 0.4), false);

    }

    private Timer timer;

    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            if(isEnemyCreationRequired()){
                float posX = (float) (Math.random() * (MainActivity.SCREEN_WIDTH - shipBitmap.getWidth()));          // Génère la position X de l'ennemi entre 0 et la taille max de l'écran
                enemyShip = new EnemyShip(posX, - shipBitmap.getHeight(), shipBitmap ,15 ,15);
                synchronized (enemyShipList){
                    enemyShipList.add(enemyShip);
                }
            }
        }
    };

    private boolean isEnemyCreationRequired() {
        //TODO: Calculer si un ennemi doit être créé, en fonction
        // - Du nombre d'ennemis déjà présents à l'écran
        // - Du score actuel du joueur
        // - D'autres choses potentiellement


        return true;
    }


    public void start() {
        if (timer != null) {  return; }
        timer = new Timer();
        timer.scheduleAtFixedRate(timerTask, 0, 400);

    }

    public void stop() {
        timer.cancel();
        timer = null;
    }

    public void pause(){
        // Stopper son ici
        stop();
    }

    public List<EnemyShip> getEnemyShipList() {
        return enemyShipList;
    }

    public void setEnemyShipList(List<EnemyShip> enemyShipList) {
        this.enemyShipList = enemyShipList;
    }
}
