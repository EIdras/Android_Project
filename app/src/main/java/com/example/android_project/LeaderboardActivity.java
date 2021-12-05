package com.example.android_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LeaderboardActivity extends AppCompatActivity {

    private RecyclerView scoresRecyclerView;
    private LinearLayoutManager scoresLinearLayoutManager;

    // Firebase
    public DatabaseReference databaseReference;
    public static class ScoreViewHolder extends RecyclerView.ViewHolder{
        TextView dataPseudoTextView;
        TextView dataScoreTextView;

        public ScoreViewHolder(@NonNull View itemView) {
            super(itemView);
            dataPseudoTextView = itemView.findViewById(R.id.dataPseudoTextView);
            dataScoreTextView = itemView.findViewById(R.id.dataScoreTextView);
        }
    }

    public FirebaseRecyclerAdapter<Score, ScoreViewHolder> firebaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        scoresRecyclerView = findViewById(R.id.scoresRecyclerView);
        scoresLinearLayoutManager = new LinearLayoutManager(this);
        scoresLinearLayoutManager.setStackFromEnd(true);
        scoresRecyclerView.setLayoutManager(scoresLinearLayoutManager);


        // Instantiation de la Firebase reference
        ScoreDataBase scoreDataBase = new ScoreDataBase("Simon");
        databaseReference = scoreDataBase.getDatabaseReferenceOfScores();
        SnapshotParser<Score> parser = scoreDataBase.getSnapShotParser();


        // TODO : Ne fonctionne pas
        // Connecte l'interface avec Firebase en faisant une requete pour toutes les entrées dans Leaderboard
        FirebaseRecyclerOptions<Score> options = new FirebaseRecyclerOptions.Builder<Score>().
                setQuery(this.databaseReference.child(ScoreDataBase.PATH_LEADERBOARD), parser).build();
        Log.e("tamer","options");
        firebaseAdapter = new FirebaseRecyclerAdapter<Score, ScoreViewHolder>(options) {
            // Utilise les informations dans le message récupéré sur Firebase pour l'afficher
            @Override
            protected void onBindViewHolder(@NonNull ScoreViewHolder messageViewHolder, int i, @NonNull Score score) {
                Log.e("tamer","onBindViewHolder");
                messageViewHolder.dataScoreTextView.setText(String.valueOf(score.getScore()));
                messageViewHolder.dataPseudoTextView.setText(score.getName());
            }

            // Ajoute des item_message dans le linearLayout
            @NonNull
            @Override
            public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                Log.e("tamer","onCreateViewHolder");
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                return new ScoreViewHolder(inflater.inflate(R.layout.item_score, parent, false));
            }
        };
        // Tous les éléments visuels ci-dessus s'ajoutent dans le messagesRecyclerView
       scoresRecyclerView.setAdapter(firebaseAdapter);

       firebaseAdapter.startListening();
    }

    @Override
    protected void onPause() {
        super.onPause();
        firebaseAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseAdapter.startListening();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onPause();
    }

}