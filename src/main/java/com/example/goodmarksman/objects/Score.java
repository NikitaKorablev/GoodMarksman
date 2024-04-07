package com.example.goodmarksman.objects;

import javafx.application.Platform;
import javafx.scene.text.Text;

import java.net.Socket;

public class Score {
//    private Socket socket;
//    public final Text score;
//    public final Text shotCount;
    private int scoreValue = 0;
    private int shotCountValue = 0;

    public Score(Text score, Text shot) {
//        this.score = score;
//        this.shotCount = shot;
    }

    public int getScore() { return this.scoreValue; }
//    private void setScore(int i) {
//        Platform.runLater(() -> this.score.setText(Integer.toString(i)));
//    }
    public void scoreInc(int weight) {
        this.scoreValue += weight;
//        setScore(this.scoreValue);
    }
    public void setStartScore() {
//        this.setScore(0);
        this.scoreValue = 0;
    }

    public int getShotCount() { return this.shotCountValue; }
//    private void setShotCount(int i) {
//        Platform.runLater(() -> this.shotCount.setText(Integer.toString(i)));
//    }
    public void shotCountInc() {
        this.shotCountValue++;
//        setShotCount(this.shotCountValue);
    }
    public void setStartShotCount() {
//        this.setShotCount(0);
        this.shotCountValue = 0;
    }

//    @Override
//    public String toString() {
//        return "Score{" +
//                "score=" + score +
//                ", shotCount=" + shotCount +
//                ", scoreValue=" + scoreValue +
//                ", shotCountValue=" + shotCountValue +
//                '}';
//    }


    @Override
    public String toString() {
        return "Score{" +
//                "score=" + score +
//                ", shotCount=" + shotCount +
                ", scoreValue=" + scoreValue +
                ", shotCountValue=" + shotCountValue +
                '}';
    }
}
