package com.example.android_project;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

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
    private TimerTask timerTask;


    private int enemyCount, actualScore;
    private int maxEnemyNb;

    private boolean isEnemyCreationRequired() {
        //TODO: Calculer si un ennemi doit être créé, en fonction
        // - Du nombre d'ennemis déjà présents à l'écran
        // - Du score actuel du joueur
        // - D'autres choses potentiellement

        enemyCount = enemyShipList.size();
        actualScore = appCompatActivity.getGameView().getScore();

        // Plus actualscore est proche de 1000, plus il y a de chances (random) de spawn un ennemi (true)


        // Au départ et jusqu'a 10 points, on aura 1 seul ennemi à l'écran max
        // Tant que le score augmente, le nombre d'ennemis max s'adapte selon le score actuel
        // Si on atteint 1000 points, on aura 20 ennemis à l'écran max et c'est le maximum du jeu

        if (actualScore <= 10){
            maxEnemyNb = 1;
        }
        else if (actualScore >= 1000){
            maxEnemyNb = 20;
        }
        else {
            maxEnemyNb = (int) Math.round(0.02 * actualScore - 0.8); // Fonction mathématique qui définit le nombre d'ennemis max en fonction du score actuel
            if (maxEnemyNb == 0) maxEnemyNb = 1; // Correction lorsque l'arondi d'une valeur inférieure à 0.5 renvoie 0
        }
        Log.e("Nb Ennemis", "Score : "+actualScore+ " --- Nb Ennemi max : " +maxEnemyNb);

        if (enemyCount >= maxEnemyNb){
            return false;
        }

        return true;
    }


    public void start() {
        if (timer != null) {  return; }
        timer = new Timer();
        timerTask = new TimerTask() {
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
