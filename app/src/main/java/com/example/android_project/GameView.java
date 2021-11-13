package com.example.android_project;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.appcompat.app.AppCompatActivity;

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

        piouIconWidth  = pPiou.getWidth();
        piouIconHeight = pPiou.getHeight();

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

        score = 0;
        while(!gameOver && isRunning){
            if (surfaceHolder.getSurface().isValid()){
                canvas = surfaceHolder.lockCanvas();
                canvas.save();

                canvas.drawBitmap(bg, 0, 0, paint);
                canvas.drawBitmap(ship, spaceShip.getShipPosX(), spaceShip.getShipPosY(), paint);

                for(Projectile p: projectileManager.getPiouList()){
                    canvas.drawBitmap(p.getBitmap(), p.getPiouPosX(), p.getPiouPosY(), paint);
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

    private AppCompatActivity getAppCompatActivity() {
        return appCompatActivity;
    }

    private void dpToPixel(int dps){
        final float scale = getContext().getResources().getDisplayMetrics().density;
        int pixels = (int) (dps * scale + 0.5f);
    }

}