package com.example.android_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    public static int SCREEN_WIDTH = 0;
    public static int SCREEN_HEIGHT = 0;
    private Button playBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playBtn = findViewById(R.id.playBtn);

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(GameActivity.class);
            }
        });

        Point metrics = new Point();
        getWindowManager().getDefaultDisplay().getRealSize(metrics);
        SCREEN_WIDTH = metrics.x;
        SCREEN_HEIGHT = metrics.y;
    }

    public void openActivity(Class classToOpen){
        Intent intent = new Intent(this, classToOpen);
        startActivity(intent);
    }
}