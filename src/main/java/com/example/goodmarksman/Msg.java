package com.example.goodmarksman;

import com.example.goodmarksman.objects.*;

import java.util.ArrayList;

public class Msg {
    MsgAction action = null;
    ClientState clientState = null;
    ArrayList<Score> scoreBoard = null;
//    ArrayList<Client> players = null;
//    ArrayList<Arrow> arrows = null;
//    ArrayList<Score> score = null;
//    ClientsData clientsData = null;
    ArrayList<Data> clientsData = null;
    String message = null;
    int portOwner = -1;

//    public Msg(ArrayList<Score> scoreBoard, MsgAction action) {
//        this.scoreBoard = scoreBoard;
//        this.action = action;
//    }

    public Msg(ArrayList<Score> scoreBoard) {
        this.scoreBoard = scoreBoard;
    }

    public Msg(String message, MsgAction action) {
        this.action = action;
        this.message = message;
    }

    public Msg(ClientState state, MsgAction action) {
        this.clientState = state;
        this.action = action;
    }

//    public Msg(ArrayList<Arrow> arrows, ArrayList<Score> score, MsgAction action) {
////        this.players = players;
//        this.arrows = arrows;
//        this.score = score;
//        this.action = action;
//    }

    public Msg(ArrayList<Data> data, MsgAction action) {
        this.clientsData = data;
        this.action = action;
    }

    public Msg(int port, ArrayList<Data> data, MsgAction action) {
        this.clientsData = data;
        this.action = action;
    }

    public Msg(String message) {
        this.message = message;
    }

    public MsgAction getAction() {
        return action;
    }

//    public ArrayList<Score> getPoints() {
//        return scoreBoard;
//    }

    public Msg() {}

    @Override
    public String toString() {
        return "Msg{" +
                "action=" + action +
                ", clientState=" + clientState +
                ", scoreBoard=" + scoreBoard +
                ", clientsData=" + clientsData +
                ", message='" + message + '\'' +
                '}';
    }
}
