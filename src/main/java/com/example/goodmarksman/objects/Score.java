package com.example.goodmarksman.objects;

import javafx.application.Platform;
import javafx.scene.text.Text;

import java.net.Socket;

public class Score {
//    Text score = null;
//    Text shotCount = null;
    int scoreValue = 0;
    int shotCountValue = 0;

    public Score(Text score, Text shot) {
//        this.score = score;
//        this.shotCount = shot;
        this.scoreValue = Integer.parseInt(score.getText());
        this.shotCountValue = Integer.parseInt(shot.getText());
    }
    public Score() {
//        this.score = new Text();
//        this.shotCount = new Text();
    }
    public Score(Score s) {
//        this.score = new Text();
//        this.score.setText(s.score.getText());
        this.scoreValue = s.scoreValue;
//        this.shotCount = new Text();
//        this.shotCount.setText(s.shotCount.getText());
        this.shotCountValue = s.shotCountValue;
    }

    public int getScore() { return this.scoreValue; }
    public void setScore(int i) {
        this.scoreValue = i;
//        Platform.runLater(() -> this.score.setText(Integer.toString(i)));
    }
    public void scoreInc(int weight) {
        this.scoreValue += weight;
//        setScore(this.scoreValue);
    }
    public void setStartScore() {
//        this.setScore(0);
        this.scoreValue = 0;
    }

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
//                "score=" + score +
//                ", shotCount=" + shotCount +
                "scoreValue=" + scoreValue +
                ", shotCountValue=" + shotCountValue +
                '}';
    }
}
