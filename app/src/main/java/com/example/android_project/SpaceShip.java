package com.example.android_project;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class SpaceShip {

    public static final int MAX_HEALTH = 100;
    private float shipPosX;
    private float shipPosY;
    Bitmap bitmap;
    int health;

    public SpaceShip(float shipPosX, float shipPosY, Bitmap bitmap) {
        this.shipPosX = shipPosX;
        this.shipPosY = shipPosY;
        this.bitmap = bitmap;
        this.health = MAX_HEALTH;
    }

    public float getShipPosX() {
        return shipPosX;
    }

    public void setShipPosX(float shipPosX) {
        this.shipPosX = shipPosX;
    }

    public float getShipPosY() {
        return shipPosY;
    }

    public void setShipPosY(float shipPosY) {
        this.shipPosY = shipPosY;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public Rect getHitBox(){
        return new Rect((int)shipPosX,(int)shipPosY,(int)shipPosX + bitmap.getWidth(), (int)shipPosY + bitmap.getHeight());
    }
}
