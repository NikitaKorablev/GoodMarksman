package com.example.goodmarksman;

import com.example.goodmarksman.objects.Game;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

public class Model {
    public Game getGame(Circle target1, Circle target2, Polygon arrow, View view) {
        return new Game(target1, target2, arrow, view);
    }

    public String getData() { return ""; } // for example
}
