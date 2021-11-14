package com.example.android_project;

import android.graphics.Bitmap;

public class SpaceShip {

    private float shipPosX;
    private float shipPosY;
    Bitmap bitmap;

    public SpaceShip(float shipPosX, float shipPosY, Bitmap bitmap) {
        this.shipPosX = shipPosX;
        this.shipPosY = shipPosY;
        this.bitmap = bitmap;
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
}
