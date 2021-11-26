package com.example.android_project;

import java.util.Timer;
import java.util.TimerTask;

public class ScoreManager {
    int score = 0;
    private Timer timer;

    private TimerTask timerTask = new TimerTask() {

        @Override
        public void run() {
            score ++;
        }
    };


    public void start() {
        if (timer != null) {
            return;
        }
        timer = new Timer();

        timer.scheduleAtFixedRate(timerTask, 0, 500);

    }

    public void stop() {
        timer.cancel();
        timer = null;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
