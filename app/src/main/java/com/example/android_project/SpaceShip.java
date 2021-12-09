package com.example.android_project;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class SpaceShip {

    public static final int MAX_HEALTH = 100;
    private float shipPosX;
    private float shipPosY;
    private Bitmap bitmap;
    private int health;

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

    public void looseHealth(int loosedHP){
        if (health - loosedHP < 0) health = 0;
        else health -= loosedHP;
    }

    public void winHealth(int wonHP){
        if (health + wonHP > MAX_HEALTH) health = MAX_HEALTH;
        else health += wonHP;
    }

    public Rect getHitBox(){
        return new Rect((int)shipPosX,(int)shipPosY,(int)shipPosX + bitmap.getWidth(), (int)shipPosY + bitmap.getHeight());
    }
}
