package com.example.goodmarksman.objects;

import javafx.application.Platform;
import javafx.scene.shape.Polygon;

public class Arrow {
//    Polygon arrow = null;
    private int ownerPort;
    private final int moveSpeed = 10;
    private boolean isShooting = false;
    private int layoutX = 38, layoutY = 122;

    public Arrow(Polygon arrow) {
        layoutX = (int) arrow.getLayoutX();
        layoutY = (int) arrow.getLayoutY();
//        this.arrow = arrow;
    }
    public Arrow() {
//        this.arrow = new Polygon();
//        this.layoutX = 38;
//        this.arrow.setLayoutY(y);
//        this.arrow.getPoints().addAll(-30.0, 5.0, 0.0, 0.0, -30.0, -5.0);
    }
    public Arrow(Arrow a) {
//        this.arrow = new Polygon();
        this.layoutX = a.layoutX;
        this.layoutY = a.layoutY;
//        this.arrow.setLayoutX(a.layoutX);
//        this.arrow.setLayoutY(a.y);
//        this.arrow.getPoints().addAll(-30.0, 5.0, 0.0, 0.0, -30.0, -5.0);
    }

    public Polygon getPolygon() {
        Polygon polygon = new Polygon();
        polygon.getPoints().addAll(-30.0, 5.0, 0.0, 0.0, -30.0, -5.0);
        polygon.setLayoutX(this.layoutX);
        polygon.setLayoutY(this.layoutY);

        return polygon;
    }

//    public double getY() { return arrow.getLayoutY(); }
    public double getY() { return this.layoutY; }
//    public void setY(double y) { Platform.runLater(() -> this.arrow.setLayoutY(y));  }
    public void setY(double y) { this.layoutY = (int)y; }
//    public double getX() { return arrow.getLayoutX(); }
    public double getX() { return this.layoutX; }
//    public void setX(double x) { Platform.runLater(() -> arrow.setLayoutX(x)); }
    public void setX(double x) { this.layoutX = (int)x; }

    public int getSpeed() { return this.moveSpeed; }

    public void setIsShooting(boolean bool) {
        this.isShooting = bool;
    }
    public boolean getIsShooting() { return this.isShooting; }

    public void setOwnerPort(int port) { this.ownerPort = port; }
    public int getOwnerPort() { return this.ownerPort; }

    @Override
    public String toString() {
        return "Arrow{" +
                "ownerPort=" + ownerPort +
                ", moveSpeed=" + moveSpeed +
                ", isShooting=" + isShooting +
                ", layoutX=" + layoutX +
                ", layoutY=" + layoutY +
                '}';
    }
}
