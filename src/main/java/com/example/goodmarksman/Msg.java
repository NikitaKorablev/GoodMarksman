package com.example.goodmarksman;

import com.example.goodmarksman.objects.*;
import com.example.goodmarksman.objects.Action;

import java.util.ArrayList;

public class Msg {
    Action action = null;
    ClientState clientState = null;
    ArrayList<Score> scoreBoard = null;
    Arrow arrow = null;
//    ArrayList<Client> players = null;
//    ArrayList<Arrow> arrows = null;
//    ArrayList<Score> score = null;
//    ClientsData clientsData = null;
    ClientsDataArray clientsData = null;
    ClientData clientData = null;
    String message = null;
    int view_width;
    int view_height;
    int portOwner = -1;

//    public Msg(ArrayList<Score> scoreBoard, MsgAction action) {
//        this.scoreBoard = scoreBoard;
//        this.action = action;
//    }

    public Msg(ArrayList<Score> scoreBoard) {
        this.scoreBoard = scoreBoard;
    }

    public Msg(String message, Action action) {
        this.action = action;
        this.message = message;
    }

    public Msg(int[] wh, Action action) {
        this.action = action;
        this.view_width = wh[0];
        this.view_height = wh[1];
    }

    public Msg(ClientState state, Action action) {
        this.clientState = state;
        this.action = action;
    }

    public Msg(Arrow arrow, Action action) {
        this.arrow = arrow;
        this.action = action;
    }

//    public Msg(ArrayList<Arrow> arrows, ArrayList<Score> score, MsgAction action) {
////        this.players = players;
//        this.arrows = arrows;
//        this.score = score;
//        this.action = action;
//    }

    public Msg(ClientsDataArray dataArray, Action action) {
        this.clientsData = dataArray;
        this.action = action;
    }

    public Msg(ClientData data, Action action) {
        this.clientData = data;
        this.action = action;
    }

    public Msg(String message, ClientState state, Action action) {
        this.message = message;
        this.clientState = state;
        this.action = action;
    }

    public Msg(String message) {
        this.message = message;
    }

    public Action getAction() {
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
                ", arrow=" + arrow +
                ", clientsData=" + clientsData +
                ", clientData=" + clientData +
                ", message='" + message + '\'' +
                ", view_width=" + view_width +
                ", view_height=" + view_height +
                ", portOwner=" + portOwner +
                '}';
    }
}