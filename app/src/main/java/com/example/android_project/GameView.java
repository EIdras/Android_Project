package com.example.android_project;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class GameView extends SurfaceView implements Runnable {

    private Thread gameThread;
    private GameActivity appCompatActivity;
    private SurfaceHolder surfaceHolder;
    private Path path;
    private int score;
    private boolean gameOver;
    private boolean isRunning;

    Paint paint;

    SpaceShip spaceShip;
    ProjectileManager projectileManager;
    float shipIconWidth, shipIconHeight,
            bgIconHeight;

    Bitmap bg, ship;


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

        spaceShip = new SpaceShip(MainActivity.SCREEN_WIDTH /2 - shipIconWidth / 2, MainActivity.SCREEN_HEIGHT /2 + shipIconHeight, ship);
        projectileManager = new ProjectileManager(appCompatActivity, spaceShip);

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


        score = 0;
        while(!gameOver && isRunning){
            if (surfaceHolder.getSurface().isValid()) {
                canvas = surfaceHolder.lockCanvas();
                canvas.drawColor(Color.BLACK); // Pour 'clear' le canvas
                canvas.save();

                canvas.drawBitmap(bg, 0, 0, paint);                                         // Affiche l'image de fond d'écran (espace) sur le Canvas
                canvas.drawBitmap(ship, spaceShip.getShipPosX(), spaceShip.getShipPosY(), paint);   // Affiche le vaisseau à sa position définie dans la classe SpaceShip
                drawLifeBar(canvas);


                List<Projectile> piouList = projectileManager.getPiouList();
                synchronized (piouList) {
                    for (Projectile p : piouList) {
                        if (p.getPiouPosY() < -50) {
                            piouList.remove(p);                                                         // Retire le projectile de la liste lorsequ'il est sorti de l'écran
                            break;
                        } else {
                            canvas.drawBitmap(p.getBitmap(), p.getPiouPosX(), p.getPiouPosY(), paint);  // Affiche chaque projectile sur le Canvas
                            p.setPiouPosY(p.getPiouPosY() - p.getVelocity());                           // Permet d'actualiser la position de chaque projectile (en Y) suivant leur vitesse
                        }
                    }
                }

                path.rewind();
                canvas.restore();
                surfaceHolder.unlockCanvasAndPost(canvas);
            }

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

                float pointX = event.getX();
                float pointY = event.getY();

                spaceShip.setShipPosX(pointX - shipIconWidth/2);
                spaceShip.setShipPosY(pointY - shipIconWidth/2);
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
    }


    private void drawLifeBar(Canvas canvas){
        float padding = 100;                // Marge entre la barre de vie et les bords de l'écran
        float vSize = 80;                   // Hauteur de la barre de vie
        float barPadd = 10;                 // Marge entre la barre blanche et la verte
        float x1 = padding;
        float y1 = MainActivity.SCREEN_HEIGHT - padding - vSize;
        float x2 = MainActivity.SCREEN_WIDTH - padding;
        float y2 = MainActivity.SCREEN_HEIGHT - padding;


        paint.setStrokeWidth(1);
        paint.setColor(Color.WHITE);
        canvas.drawRect(x1, y1, x2, y2, paint);
        paint.setColor(Color.GREEN);
        canvas.drawRect(x1+barPadd, y1+barPadd, x2-barPadd, y2-barPadd, paint);

    }


}