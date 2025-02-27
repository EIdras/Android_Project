package com.example.android_project;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Iterator;

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
    private Bitmap              bg, ship, heart;


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
        ship = BitmapFactory.decodeResource(appCompatActivity.getResources(), MainActivity.SKIN);
        ship = Bitmap.createScaledBitmap(ship, (int) (ship.getWidth() * 0.3), (int) (ship.getHeight() * 0.3), true);
        heart = BitmapFactory.decodeResource(appCompatActivity.getResources(), R.drawable.heart);

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

        projectileManager.pause();
        enemyManager.pause();
        scoreManager.stop();
    }

    public void resume(){
        isRunning = true;
        gameThread = new Thread(this);
        gameThread.start();

        scoreManager.start();

        projectileManager.start();
        enemyManager.start();
    }

    @Override
    public void run() {
        Canvas canvas;



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
                drawExplosions(canvas);
                drawHearts(canvas);

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
            ContextCompat.getMainExecutor(getContext()).execute(() -> Toast.makeText(getContext(), "Perdu !", Toast.LENGTH_SHORT).show());
            appCompatActivity.endGame(scoreManager.getScore());
        }
    }

    private void manageEnemies(Canvas canvas) {
        synchronized (enemyManager.getEnemyShipList()) {
            Iterator<EnemyShip> enemyIterator = enemyManager.getEnemyShipList().iterator();
            while (enemyIterator.hasNext()) {
                EnemyShip enemy = enemyIterator.next();
                    if (enemy.getShipPosY() > MainActivity.SCREEN_HEIGHT){
                        playerShip.looseHealth(4);
                        Log.e("Vie perdue", "Valeur 4");
                        enemyIterator.remove();
                    }
                    else if (enemy.getHitBox().intersect(playerShip.getHitBox())){
                        // TODO : faire du son
                        createExplosion(enemy);
                        enemyIterator.remove();
                        playerShip.looseHealth(10);
                        Log.e("Vie perdue", "Valeur 10");
                    }
                    else {
                        synchronized (projectileManager.getPiouList()) {
                            Iterator<Projectile> projectileIterator = projectileManager.getPiouList().iterator();
                            while (projectileIterator.hasNext()) {
                                Projectile p = projectileIterator.next();
                                if ((enemy.getHitBox().intersect(p.getHitBox())) && enemyManager.getEnemyShipList().contains(enemy)){
                                    projectileIterator.remove();
                                    // TODO : faire du son
                                    createExplosion(enemy);
                                    if(Math.random() < 0.05) createHeart(enemy);                    // Probabilité qu'un enemi se transforme en coeur a sa mort
                                    enemyIterator.remove();
                                    scoreManager.setScore(scoreManager.getScore() + 5);
                                }
                            }
                        }
                        canvas.drawBitmap(enemy.getBitmap(), enemy.getShipPosX(), enemy.getShipPosY(), paint);
                        enemy.setShipPosY(enemy.getShipPosY() + enemy.getVelocity_Y());
                    }
                }

        }
    }

    private ArrayList<Heart> hearts = new ArrayList<>();
    private void createHeart(EnemyShip enemy){
        Heart newHeart = new Heart(enemy.getShipPosX() + enemy.getBitmap().getWidth() / 2 - heart.getWidth() / 2,
                enemy.getShipPosY() + enemy.getBitmap().getHeight() / 2 - heart.getHeight() / 2,
                heart);
        hearts.add(newHeart);
    }

    private void drawHearts(Canvas canvas){
        Iterator<Heart> iterator = hearts.iterator();
        while(iterator.hasNext()){
            Heart h = iterator.next();
            if (h.getPosY() > MainActivity.SCREEN_HEIGHT) iterator.remove();
            if (h.getHitBox().intersect(playerShip.getHitBox())){
                playerShip.winHealth(h.getHealthValue());
                iterator.remove();
            }
            canvas.drawBitmap(h.getBitmap(), h.getPosX(), h.getPosY(), paint);
            h.setPosY(h.getPosY()+10);
        }
    }

    private ArrayList<EnemyShip> enemiesToExplode = new ArrayList<>();
    private void createExplosion(EnemyShip enemy){
        enemiesToExplode.add(enemy);
    }

    private void drawExplosions(Canvas canvas){
        Iterator<EnemyShip> iterator = enemiesToExplode.iterator();
        while(iterator.hasNext()){
            EnemyShip current = iterator.next();
            Bitmap explosion = null;
            if (current.getNbFramesAfterDeath() == 18){
                iterator.remove();
            }
            int drawable;
            int frame = current.getNbFramesAfterDeath();
            if (frame >= 15){
                drawable = R.drawable.explosion_frame6;
            }
            else if (frame >= 12){
                drawable = R.drawable.explosion_frame5;
            }
            else if (frame >= 9){
                drawable = R.drawable.explosion_frame4;
            }
            else if (frame >= 6){
                drawable = R.drawable.explosion_frame3;
            }
            else if (frame >= 3){
                drawable = R.drawable.explosion_frame2;
            }
            else
                drawable = R.drawable.explosion_frame1;

            explosion = BitmapFactory.decodeResource(appCompatActivity.getResources(), drawable);
            current.incrementNbFramesAfterDeath();
            explosion = Bitmap.createScaledBitmap(explosion, (int) (current.getBitmap().getWidth()), (int) (current.getBitmap().getHeight()), true);
            canvas.drawBitmap(explosion, current.getShipPosX(), current.getShipPosY(), paint);
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                appCompatActivity.getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            }
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

    public int getScore() {
        return score;
    }
}