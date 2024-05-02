package com.example.goodmarksman.objects;

import javafx.scene.text.Text;

public class Score {
//    Text score = null;
//    Text shotCount = null;
    private int portOwner;
    private String playerName = "";
    private int scoreValue = 0;
    private int shotCountValue = 0;

    public Score(Text score, Text shot) {
//        this.score = score;
//        this.shotCount = shot;
        this.scoreValue = Integer.parseInt(score.getText());
        this.shotCountValue = Integer.parseInt(shot.getText());
    }
    public Score(int port) {
        this.portOwner = port;
    }
    public Score() {
//        this.score = new Text();
//        this.shotCount = new Text();
    }
    public void nullify() {
        this.scoreValue = 0;
        this.shotCountValue = 0;
    }

    public int getScore() { return this.scoreValue; }
    public void setScore(int i) {
        this.scoreValue = i;
//        Platform.runLater(() -> this.score.setText(Integer.toString(i)));
    }
    public int scoreInc(int weight) {
        this.scoreValue += weight;
        return this.scoreValue;
//        setScore(this.scoreValue);
    }
    public void setStartScore() {
//        this.setScore(0);
        this.scoreValue = 0;
    }

    public String getPlayerName() { return this.playerName; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }

    public int getOwnerPort() { return this.portOwner; }
    public void setOwnerPort(int port) { this.portOwner = port; }

    public int getShotCount() { return this.shotCountValue; }
    public void setShotCount(int i) {
        this.shotCountValue = i;
//        Platform.runLater(() -> this.shotCount.setText(Integer.toString(i)));
    }
    public void shotCountInc() {
        this.shotCountValue++;
//        setShotCount(this.shotCountValue);
    }
    public void setStartShotCount() {
//        this.setShotCount(0);
        this.shotCountValue = 0;
    }

    @Override
    public String toString() {
        return "Score{" +
                "portOwner=" + portOwner +
                ", playerName='" + playerName + '\'' +
                ", scoreValue=" + scoreValue +
                ", shotCountValue=" + shotCountValue +
                '}';
    }
}