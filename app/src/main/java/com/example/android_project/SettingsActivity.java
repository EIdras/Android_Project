package com.example.android_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class SettingsActivity extends AppCompatActivity {

    private ImageButton ship_red, ship_blue, ship_green, ship_orange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Drawable btn_icon = ContextCompat.getDrawable(this, R.drawable.custom_button);

        ship_red = findViewById(R.id.ship_red);
        ship_blue = findViewById(R.id.ship_blue);
        ship_green = findViewById(R.id.ship_green);
        ship_orange = findViewById(R.id.ship_orange);

        MainActivity.SKIN = R.drawable.spaceship_red;
        ship_red.setBackground(btn_icon);

        ship_red.setOnClickListener(view -> {
            MainActivity.SKIN = R.drawable.spaceship_red;
            ship_red.setBackground(btn_icon);
            ship_blue.setBackground(null);
            ship_green.setBackground(null);
            ship_orange.setBackground(null);
        });
        ship_blue.setOnClickListener(view -> {
            MainActivity.SKIN = R.drawable.spaceship_blue;
            ship_red.setBackground(null);
            ship_blue.setBackground(btn_icon);
            ship_green.setBackground(null);
            ship_orange.setBackground(null);
        });
        ship_green.setOnClickListener(view -> {
            MainActivity.SKIN = R.drawable.spaceship_green;
            ship_red.setBackground(null);
            ship_blue.setBackground(null);
            ship_green.setBackground(btn_icon);
            ship_orange.setBackground(null);
        });
        ship_orange.setOnClickListener(view -> {
            MainActivity.SKIN = R.drawable.spaceship_orange;
            ship_red.setBackground(null);
            ship_blue.setBackground(null);
            ship_green.setBackground(null);
            ship_orange.setBackground(btn_icon);
        });

    }

}