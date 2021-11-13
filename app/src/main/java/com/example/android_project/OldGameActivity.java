package com.example.android_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class OldGameActivity extends AppCompatActivity {

    private ImageView spaceshipImageView;
    private ConstraintLayout bgConstraintLayout;
    private final int intervalPiou = 500;
    // Temp
    private Button shootBtn;


    private float shipPosX, shipPosY;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        spaceshipImageView = findViewById(R.id.spaceshipImageView);
        float shipIconWidth  = spaceshipImageView.getLayoutParams().width;
        float shipIconHeight = spaceshipImageView.getLayoutParams().height;

        shipPosX = spaceshipImageView.getX();
        shipPosY = spaceshipImageView.getY();

        float[] shipIconOrigin = {shipPosX , shipPosY};
        float[] shipIconCenter = {shipPosX + shipIconWidth / 2, shipPosY + shipIconHeight / 2};

        shootBtn = findViewById(R.id.shootBtn);

        shootBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Initialize a new ImageView widget
                ImageView iv = new ImageView(getApplicationContext());

                // Set an image for ImageView
                iv.setImageDrawable(getDrawable(R.drawable.piou));

                // Finally, add the ImageView to layout
                bgConstraintLayout.addView(iv);


                float piouIconWidth  = iv.getLayoutParams().width ;
                float piouIconHeight = iv.getLayoutParams().height;
                iv.requestLayout();

                Log.e("Piou", "" + piouIconWidth + piouIconHeight);
                Log.e("Piou", "" + iv.getWidth() + iv.getHeight());
                Log.e("Piou", "shipIconCenter[0]=" + shipIconCenter[0] + ", piouIconWidth / 2=" + piouIconWidth / 2);
                iv.setX(shipIconCenter[0] - 17);
                iv.setY(shipIconOrigin[1] - 130 );



            }
        });



        bgConstraintLayout = findViewById(R.id.bgConstraintLayout);

        bgConstraintLayout.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float pointX = event.getX();
                float pointY = event.getY();

                shipIconOrigin[0] = pointX - shipIconWidth/2;
                shipIconOrigin[1] = pointY - shipIconHeight/2;

                shipIconCenter[0] = pointX;
                shipIconCenter[1] = pointY;

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        shipPosX = shipIconOrigin[0];
                        shipPosY = shipIconOrigin[1];
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        shipPosX = shipIconOrigin[0];
                        shipPosY = shipIconOrigin[1];
                        break;
                    default:
                        return false;
                }
                spaceshipImageView.setX(shipPosX);
                spaceshipImageView.setY(shipPosY);
                return false;
            }
        });
    }
}