package com.example.goodmarksman;

import com.example.goodmarksman.objects.Point;
import com.example.goodmarksman.objects.Score;

import java.util.ArrayList;

public class Msg {
    MsgAction action = null;
    ArrayList<Score> scoreBoard = null;
    String message = null;

    public Msg(ArrayList<Score> scoreBoard, MsgAction action) {
        this.scoreBoard = scoreBoard;
        this.action = action;
    }

    public Msg(ArrayList<Score> scoreBoard) {
        this.scoreBoard = scoreBoard;
    }

    public Msg(String message, MsgAction action) {
        this.action = action;
        this.message = message;
    }

    public Msg(String message) {
        this.message = message;
    }

    public MsgAction getAction() {
        return action;
    }

    public ArrayList<Score> getPoints() {
        return scoreBoard;
    }

    public Msg() {}

    @Override
    public String toString() {
        return "Msg{" +
                "action=" + action +
                ", scoreBoard=" + scoreBoard +
                ", message='" + message + '\'' +
                '}';
    }
}
