package com.example.android_project;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class Heart {
    public static final int DEFAULT_HEAL_VALUE = 5;
    private float posX;
    private float posY;
    private Bitmap bitmap;
    private int healthValue;

    public Heart(float posX, float posY, Bitmap bitmap) {
        this.posX = posX;
        this.posY = posY;
        this.bitmap = bitmap;
        this.healthValue = DEFAULT_HEAL_VALUE;
    }


    public float getPosX() {
        return posX;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public float getPosY() {
        return posY;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getHealthValue() {
        return healthValue;
    }
    public Rect getHitBox(){
        return new Rect((int)posX,(int)posY,(int)posX + bitmap.getWidth(), (int)posY + bitmap.getHeight());
    }


}
