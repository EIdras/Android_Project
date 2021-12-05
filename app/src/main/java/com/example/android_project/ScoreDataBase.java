package com.example.android_project;

import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ScoreDataBase {

    public static final String PATH_LEADERBOARD = "Leaderboard";

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private String pseudo;


    // Base de données Direbase qui gère l'ajout de score et la récupération des scores

    public ScoreDataBase(String pseudo){
        this.pseudo = pseudo;
        databaseReference = FirebaseDatabase.getInstance("https://space-piou-default-rtdb.europe-west1.firebasedatabase.app").getReference();
    }


    public SnapshotParser<Score> getSnapShotParser(){
        SnapshotParser<Score> parser = snapshot -> {
            Score score = snapshot.getValue(Score.class);
            if (score != null){
                score.setId(snapshot.getKey());
            }
            return score;
        };
        return parser;
    }

    public DatabaseReference getDatabaseReferenceOfScores(){
        return databaseReference.child(PATH_LEADERBOARD);
    }


    public void addScore(int score){
        databaseReference.child(PATH_LEADERBOARD).push().setValue(new Score(pseudo, score));
    }

}