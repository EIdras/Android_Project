package com.example.android_project;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public static int SCREEN_WIDTH = 0;
    public static int SCREEN_HEIGHT = 0;
    private Button playBtn;

    // TODO List :
    //  Gérer les onPause / onResume / onStop avec le son
    //  Ajouter une page de fin de jeu
    //  Ajouter une page de classement
    //  Gérer les scores avec Firebase
    //  Ajouter des options (?) Dark mode / Sons et musique / Skins débloquables avec des points calculés sur le score
    //  Ajouter des features de gameplay (différents ennemis, bonus de tir, tir spéciaux...)
    //  Ajouter une musique de fond et des bruitages pour les vaisseaux
    //  Redimensionner le fond d'écran pour qu'il s'adapte à l'échelle de chaque écran
    //  Gérer la génération d'ennemis
    //  Ajouter une proba que l'ennemi se transforme en coeur (vie) quand il est tué



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playBtn = findViewById(R.id.playBtn);

        playBtn.setOnClickListener(v -> openActivity(GameActivity.class));

        Point metrics = new Point();
        getWindowManager().getDefaultDisplay().getRealSize(metrics);
        SCREEN_WIDTH = metrics.x;
        SCREEN_HEIGHT = metrics.y;

    }

    public void openActivity(Class classToOpen){
        Intent intent = new Intent(this, classToOpen);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public void onBackPressed() {

    }
}