package com.example.android_project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.core.content.ContextCompat;

import java.util.Iterator;
import java.util.List;

public class GameView extends SurfaceView implements Runnable {

    private Thread              gameThread;
    private GameActivity        appCompatActivity;
    private SurfaceHolder       surfaceHolder;
    private Path                path;
    private int                 score;
    private boolean             gameOver;
    private boolean             isRunning;
    private float               shipIconWidth, shipIconHeight, bgIconHeight;

    private Paint               paint;
    private SpaceShip           playerShip;
    private ProjectileManager   projectileManager;
    private EnemyManager        enemyManager;
    private ScoreManager        scoreManager;
    private Bitmap              bg, ship;


    public GameView(GameActivity appCompatActivity) {
        super(appCompatActivity.getApplicationContext(), null);
        this.appCompatActivity = appCompatActivity;
        surfaceHolder = getHolder();
        path = new Path();

    }

    public void start(){

        hideSystemUI();

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);

        bg = BitmapFactory.decodeResource(appCompatActivity.getResources(), R.drawable.space);
        ship = BitmapFactory.decodeResource(appCompatActivity.getResources(), R.drawable.spaceship);

        ship = Bitmap.createScaledBitmap(ship, (int) (ship.getWidth() * 0.3), (int) (ship.getHeight() * 0.3), true);

        bgIconHeight = bg.getHeight();

        shipIconWidth  = ship.getWidth();
        shipIconHeight = ship.getHeight();

        playerShip = new SpaceShip(MainActivity.SCREEN_WIDTH /2 - shipIconWidth / 2, MainActivity.SCREEN_HEIGHT /2 + shipIconHeight, ship);
        projectileManager = new ProjectileManager(appCompatActivity, playerShip);
        enemyManager = new EnemyManager(appCompatActivity);
        scoreManager = new ScoreManager();


        Bitmap bitmap = Bitmap.createBitmap(24, 24, Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(Color.MAGENTA);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
        appCompatActivity.getWindow().setBackgroundDrawable(bitmapDrawable);

    }
    public void pause(){
        try {
            isRunning = false;
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume(){
        isRunning = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        Canvas canvas;

        projectileManager.start();
        enemyManager.start();
        scoreManager.start();

        score = 0;
        while(!gameOver && isRunning){
            if (surfaceHolder.getSurface().isValid()) {

                canvas = surfaceHolder.lockCanvas();
                canvas.drawColor(Color.BLACK); // Pour 'clear' le canvas
                canvas.save();

                canvas.drawBitmap(bg, 0, 0, paint);                                         // Affiche l'image de fond d'écran (espace) sur le Canvas
                canvas.drawBitmap(ship, playerShip.getShipPosX(), playerShip.getShipPosY(), paint); // Affiche le vaisseau à sa position définie dans la classe SpaceShip

                // Gestion des projectiles et des ennemis (Calculs + affichage)
                manageProjectiles(canvas);
                manageEnemies(canvas);

                // Dessin de l'UI
                drawLifeBar(canvas);
                drawPlayerScore(canvas);

                path.rewind();
                canvas.restore();
                surfaceHolder.unlockCanvasAndPost(canvas);
                score = scoreManager.getScore();

                isGameOver();
            }
        }

        if (gameOver){
            // Permet d'afficher le toast sur le thread UI
            ContextCompat.getMainExecutor(getContext()).execute(()  -> {
                Toast.makeText(getContext(), "PERDU GROS NUL", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void manageEnemies(Canvas canvas) {
        synchronized (enemyManager.getEnemyShipList()) {
            Iterator<EnemyShip> enemyIterator = enemyManager.getEnemyShipList().iterator();
            while (enemyIterator.hasNext()) {
                EnemyShip enemy = enemyIterator.next();
                    if (enemy.getShipPosY() > MainActivity.SCREEN_HEIGHT){
                        enemyIterator.remove();
                    }
                    else if (enemy.getHitBox().intersect(playerShip.getHitBox())){
                        enemyIterator.remove();
                        playerShip.setHealth(playerShip.getHealth() - 10);
                    }
                    else {
                        canvas.drawBitmap(enemy.getBitmap(), enemy.getShipPosX(), enemy.getShipPosY(), paint);
                        enemy.setShipPosY(enemy.getShipPosY() + enemy.getVelocity_Y());
                    }
                }

        }
    }

    private void manageProjectiles(Canvas canvas) {
        synchronized (projectileManager.getPiouList()) {
            Iterator<Projectile> projectileIterator = projectileManager.getPiouList().iterator();
            while (projectileIterator.hasNext()){
                Projectile p = projectileIterator.next();
                if (p.getPiouPosY() < -50) {
                    projectileIterator.remove();                                                   // Retire le projectile de la liste lorsequ'il est sorti de l'écran
                } else {
                    canvas.drawBitmap(p.getBitmap(), p.getPiouPosX(), p.getPiouPosY(), paint);      // Affiche chaque projectile sur le Canvas
                    p.setPiouPosY(p.getPiouPosY() - p.getVelocity());                               // Permet d'actualiser la position de chaque projectile (en Y) suivant leur vitesse
                }
            }
        }
    }

    private void drawPlayerScore(Canvas canvas) {

        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(4);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(64);

        float xPos = (canvas.getWidth() / 2);
        float yPos = (float) (MainActivity.SCREEN_HEIGHT * 0.9);

        canvas.drawText(String.valueOf(score),xPos,yPos, paint);
    }

    private void isGameOver() {
        if (playerShip.getHealth() <= 0){
            gameOver = true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

                float pointX = event.getX();
                float pointY = event.getY();

                playerShip.setShipPosX(pointX - shipIconWidth/2);
                playerShip.setShipPosY(pointY - shipIconWidth/2);
                return true;

    }

    private void hideSystemUI() {
        ActionBar actionBar = appCompatActivity.getSupportActionBar();
        if (actionBar != null) actionBar.hide();
        appCompatActivity.getWindow().getDecorView().setSystemUiVisibility(
                  View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        appCompatActivity.getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
    }


    // Variables pour la barre de vie
    float padding = 100;                                // Marge entre la barre de vie et les bords de l'écran
    float vSize = 80;                                   // Hauteur de la barre de vie
    float barPadd = 10;                                 // Marge entre la barre blanche et la verte
    float greenBarSize = MainActivity.SCREEN_WIDTH      // Taille totale de la barre verte
            - 2 * padding - 2 * barPadd;

    float x1 = padding;
    float y1 = MainActivity.SCREEN_HEIGHT - padding - vSize;
    float x2 = MainActivity.SCREEN_WIDTH - padding;
    float y2 = MainActivity.SCREEN_HEIGHT - padding;

    private void drawLifeBar(Canvas canvas){


        int maxHP       = SpaceShip.MAX_HEALTH  ;
        int actualHP    = playerShip.getHealth();
        float ratioHP     = (float) actualHP / maxHP   ;

        paint.setStrokeWidth(1);
        // La barre blanche est simplement un cadre
        paint.setColor(Color.WHITE);
        canvas.drawRect(x1, y1, x2, y2, paint);

        // La barre verte représente la vie du joueur
        paint.setColor(Color.GREEN);

        canvas.drawRect(x1+barPadd, y1+barPadd, padding+barPadd+(greenBarSize*ratioHP), y2-barPadd, paint);

    }


}