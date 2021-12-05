package com.example.android_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

public class GameEnd extends AppCompatActivity {

    TextView scoreView;
    Button menuBtn;
    ScoreDataBase scoreDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        int score = bundle.getInt("score");

        setContentView(R.layout.activity_game_end);

        scoreView = findViewById(R.id.scoreView);
        scoreView.setText(""+score);
        scoreDataBase = new ScoreDataBase("Simon");
        addScoreToDB(score);

        menuBtn = findViewById(R.id.menuBtn);
        menuBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {

    }

    public void addScoreToDB(int score){
        // Créée une nouvelle instance de Score
        String pseudo = "Simon"; // A remplacer
        Score newScore = new Score(pseudo, score);
        scoreDataBase.addScore(score);
    }
}