package com.example.android_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    public static int SCREEN_WIDTH = 0;
    public static int SCREEN_HEIGHT = 0;
    private Button playBtn, settingsBtn, leaderboardBtn;


    // TODO List :
    //  + Gérer les onPause / onResume / onStop avec le son
    //  + Ajouter une page de fin de jeu
    //  + Ajouter une page de classement
    //  ~ Gérer les scores avec Firebase
    //  Ajouter des options (?) Dark mode / Sons et musique / Skins débloquables avec des points calculés sur le score
    //  Ajouter des features de gameplay (différents ennemis, bonus de tir, tir spéciaux...)
    //  Ajouter une musique de fond et des bruitages pour les vaisseaux
    //  Redimensionner le fond d'écran pour qu'il s'adapte à l'échelle de chaque écran
    //  Gérer la génération d'ennemis
    //  Ajouter une proba que l'ennemi se transforme en coeur (vie) quand il est tué
    //  Faire défiler l'image de fond lentement à l'infini
    //  Ajouter un multiplicateur de points quand pas de dégâts subis pendant un certain temps



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playBtn             = findViewById(R.id.playBtn);
        settingsBtn         = findViewById(R.id.settingsBtn);
        leaderboardBtn      = findViewById(R.id.leaderboardBtn);

        playBtn.setOnClickListener(v -> openActivity(GameActivity.class));
        settingsBtn.setOnClickListener(v -> openActivity(GameActivity.class));
        leaderboardBtn.setOnClickListener(v -> openActivity(LeaderboardActivity.class));


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