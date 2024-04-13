package com.example.goodmarksman.objects;

public class Data {
    private String playerName;
    private int playerPort;

    private Arrow arrow;
    private Score score;

    public Data(String playerName, int playerPort, Arrow arrow, Score score) {
        if (playerName == null || playerName == "") this.playerName = "undefined";
        else this.playerName = playerName;

        this.playerPort = playerPort;
        this.arrow = arrow;
        this.score = score;
    }

    public String getPlayerName() { return playerName; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }

    public int getPlayerPort() { return playerPort; }
    public void setPlayerPort(int playerPort) { this.playerPort = playerPort; }

    public Arrow getArrow() { return arrow; }
    public void setArrow(Arrow arrow) { this.arrow = arrow; }

    public Score getScore() { return score; }
    public void setScore(Score score) { this.score = score; }

    @Override
    public String toString() {
        return "Data{" +
                "playerName='" + playerName + '\'' +
                ", playerPort=" + playerPort +
                ", arrow=" + arrow +
                ", score=" + score +
                '}';
    }
}
