package com.example.android_project;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class Projectile {

    private ImageView imageView;// ImageView of the projectile
    private float[] position;   // Position of the ImageView, contains the X and Y position
    private float[] size;       // Size of the ImageView, contains the weight and the height
    private Drawable icon;      // Drawable ressource of the ImageView

    public Projectile(ImageView imageView, float[] position, float[] size, Drawable icon) {
        this.imageView = imageView;
        this.position = position;
        this.size = size;
        this.icon = icon;

        imageView.setImageDrawable(icon);
    }


    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public float[] getPosition() {
        return position;
    }

    public void setPosition(float[] position) {
        this.position = position;
    }

    public float[] getSize() {
        return size;
    }

    public void setSize(float[] size) {
        this.size = size;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
}
