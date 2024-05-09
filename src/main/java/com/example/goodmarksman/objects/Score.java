package com.example.goodmarksman.objects;

import javax.persistence.*;
import javafx.scene.text.Text;

@Entity
@Table (name = "Score")
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id = 0;

    @Column(name = "PlayerName")
    private String playerName = "";

    @Column(name = "Hits")
    private int scoreValue = 0;

    @Column(name = "ShotsCount")
    private int shotCountValue = 0;

    private int portOwner;

    public Score() {}

    public Score(String playerName, int scoreValue, int shotCountValue) {
        this.playerName = playerName;
        this.scoreValue = scoreValue;
        this.shotCountValue = shotCountValue;
    }

//    public Score(Text score, Text shot) {
////        this.score = score;
////        this.shotCount = shot;
//        this.scoreValue = Integer.parseInt(score.getText());
//        this.shotCountValue = Integer.parseInt(shot.getText());
//    }
    public Score(int port) {
        this.portOwner = port;
    }

    public void nullify() {
        this.scoreValue = 0;
        this.shotCountValue = 0;
    }

    public void setId(int id) { this.id = id; }
    public int getId() { return id; }

    public int getScoreValue() { return this.scoreValue; }
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

    public int getShotCountValue() { return this.shotCountValue; }
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
