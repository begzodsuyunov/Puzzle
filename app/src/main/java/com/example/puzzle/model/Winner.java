package com.example.puzzle.model;

import java.util.Comparator;

public class Winner implements Comparator<Winner> {

    long time;
    int score;
    String text;

    public Winner(long time, int score, String text) {
        this.time = time;
        this.score = score;
        this.text = text;
    }

    public Winner() {
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int compare(Winner winner, Winner t1) {
        return (int) (winner.time - t1.time);
    }

}
