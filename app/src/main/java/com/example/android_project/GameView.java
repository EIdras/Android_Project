package com.example.android_project;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
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

    public SpaceShip getSpaceShip() {
        return spaceShip;
    }

    SpaceShip spaceShip;
    ProjectileManager projectileManager;
    float shipIconWidth, shipIconHeight, piouIconWidth, piouIconHeight;

    Bitmap bg, ship, pPiou;




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
        //pPiou = BitmapFactory.decodeResource(appCompatActivity.getResources(), R.drawable.piou);

        ship = Bitmap.createScaledBitmap(ship, (int) (ship.getWidth() * 0.3), (int) (ship.getHeight() * 0.3), true);
        //piou = Bitmap.createScaledBitmap(piou, (int) (piou.getWidth() * 0.55), (int) (piou.getHeight() * 0.55), true);

        shipIconWidth  = ship.getWidth();
        shipIconHeight = ship.getHeight();

        //piouIconWidth  = pPiou.getWidth();
        //piouIconHeight = pPiou.getHeight();

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

        long timeMillis = System.currentTimeMillis();
        projectileManager.start();

        score = 0;
        while(!gameOver && isRunning){
            if (surfaceHolder.getSurface().isValid()){
                canvas = surfaceHolder.lockCanvas();
                canvas.save();

                canvas.drawBitmap(bg, 0, 0, paint);                                        // Affiche l'image de fond d'écran (espace) sur le Canvas
                canvas.drawBitmap(ship, spaceShip.getShipPosX(), spaceShip.getShipPosY(), paint);   // Affiche le vaisseau à sa position définie dans la classe SpaceShip

                List<Projectile> piouList = projectileManager.getPiouList();
                for(Projectile p: piouList){
                    if(p.getPiouPosY() < -1000){
                        piouList.remove(p);
                        Log.e("piou", "Piou sorti de l'écran, supprimé");
                        break;
                    }
                    canvas.drawBitmap(p.getBitmap(), p.getPiouPosX(), p.getPiouPosY(), paint);  // Affiche chaque projectile sur le Canvas
                    p.setPiouPosY(p.getPiouPosY() - p.getVelocity());                         // Permet d'actualiser la position de chaque projectile (en Y) suivant leur vitesse

                }

                //canvas.drawBitmap(pPiou, spaceShip.getShipPosX() + shipIconWidth / 2 - piouIconWidth / 2, spaceShip.getShipPosY() - piouIconHeight, paint);

                //canvas.clipPath(path, Region.Op.DIFFERENCE);
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

}