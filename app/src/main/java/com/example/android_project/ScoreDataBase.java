package com.example.android_project;

import android.util.Log;

import androidx.annotation.NonNull;

import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
        Log.e("clé", getSnapShotParser().toString());
        databaseReference.child(PATH_LEADERBOARD).child(pseudo).child("score").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                try {
                    if (snapshot.getValue() != null) {
                        try {
                            Log.e("TAG", "if ("+score+ " > " + snapshot.getValue() + ")"); // Récupère la valeur du score
                            if (score > Math.toIntExact((long)snapshot.getValue())){
                                databaseReference.child(PATH_LEADERBOARD).child(pseudo).setValue(new Score(pseudo, score));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.e("TAG", " it's null.");
                        databaseReference.child(PATH_LEADERBOARD).child(pseudo).setValue(new Score(pseudo, score));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("onCancelled", " cancelled");
            }
        });


    }

}