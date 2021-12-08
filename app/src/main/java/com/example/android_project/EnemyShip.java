package com.example.android_project;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class EnemyShip extends SpaceShip{

    int velocity_Y;
    int velocity_X;

    int nbFramesAfterDeath;

    public EnemyShip(float shipPosX, float shipPosY, Bitmap bitmap, int velocity_X, int velocity_Y) {
        super(shipPosX, shipPosY, bitmap);
        this.velocity_X = velocity_X;
        this.velocity_Y = velocity_Y;
        this.nbFramesAfterDeath = 0;
    }

    public int getVelocity_Y() {
        return velocity_Y;
    }

    public void setVelocity_Y(int velocity_Y) {
        this.velocity_Y = velocity_Y;
    }

    public int getVelocity_X() {
        return velocity_X;
    }

    public void setVelocity_X(int velocity_X) {
        this.velocity_X = velocity_X;
    }

    public Rect getHitBox(){
       return super.getHitBox();
    }

    public void incrementNbFramesAfterDeath() {
        this.nbFramesAfterDeath ++;
    }

    public int getNbFramesAfterDeath() {
        return nbFramesAfterDeath;
    }
}
