package com.example.goodmarksman.objects;

import javafx.application.Platform;
import javafx.scene.shape.Polygon;

public class Arrow {
    private final Polygon arrow;
    private int moveSpeed = 10;
    private boolean isShooting = false;

    public Arrow(Polygon arrow) {
        this.arrow = arrow;
    }
    public Arrow() {
        this.arrow = new Polygon();
    }

    public double getY() { return arrow.getLayoutY(); }
    public void setY(double y) { this.arrow.setLayoutY(y); }
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
