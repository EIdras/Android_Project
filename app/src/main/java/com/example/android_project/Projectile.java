package com.example.android_project;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class Projectile {

    private static final int DEFAULT_VELOCITY = 60;
    private float piouPosX;
    private float piouPosY;
    Bitmap bitmap;

    private float velocity;

    public Projectile(float piouPosX, float piouPosY, Bitmap bitmap) {
        this.piouPosX = piouPosX;
        this.piouPosY = piouPosY;
        this.bitmap = bitmap;
        this.velocity = DEFAULT_VELOCITY;
    }

    public float getPiouPosX() {
        return piouPosX;
    }

    public void setPiouPosX(float piouPosX) {
        this.piouPosX = piouPosX;
    }

    public float getPiouPosY() {
        return piouPosY;
    }

    public void setPiouPosY(float piouPosY) {
        this.piouPosY = piouPosY;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public float getVelocity() {
        return velocity;
    }

    public void setVelocity(float velocity) {
        this.velocity = velocity;
    }

    @Override
    public String toString() {
        return "Projectile{" +
                "piouPosX=" + piouPosX +
                ", piouPosY=" + piouPosY +
                '}';
    }
}



