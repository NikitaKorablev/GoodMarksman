package com.example.goodmarksman;

import com.example.goodmarksman.objects.Point;

import java.util.ArrayList;

public class Resp {
    //TODO: Поменять на Score Bord
    ArrayList<Point> points;

    public Resp(ArrayList<Point> points) {
        this.points = points;
    }

    public Resp() {}

    public ArrayList<Point> getPoints() {
        return points;
    }

}
