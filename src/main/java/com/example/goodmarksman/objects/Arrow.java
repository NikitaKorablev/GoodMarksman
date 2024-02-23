package com.example.goodmarksman.objects;

import javafx.application.Platform;
import javafx.scene.shape.Polygon;

public class Arrow {
    private final Polygon arrow;
    private int moveSpeed = 10;
    private boolean isShooting = false;

    Arrow(Polygon arrow) {
        this.arrow = arrow;
    }

    public double getY() { return arrow.getLayoutY(); }
    public double getX() { return arrow.getLayoutX(); }
    public void setX(double x) {
        Platform.runLater(() -> arrow.setLayoutX(x));
    }

    public int getSpeed() { return this.moveSpeed; }

    public void setIsShooting(boolean bool) {
        this.isShooting = bool;
    }
    public boolean getIsShooting() { return this.isShooting; }
}
