package com.example.goodmarksman.objects;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class Arrow {
//    Polygon arrow = null;
    private int ownerPort;
    private final int moveSpeed = 10;
    private final Double[] points = new Double[]{-30.0, 5.0, 0.0, 0.0, -30.0, -5.0};
    private boolean isShooting = false;
    private int layoutX = 38, layoutY = 122;
    private COLORS colorName = COLORS.NULL;
//    private Color colorValue = null;

    public Arrow(Polygon arrow, int ownerPort) {
        layoutX = (int) arrow.getLayoutX();
        layoutY = (int) arrow.getLayoutY();
        this.ownerPort = ownerPort;
//        this.arrow = arrow;
    }
    public Arrow(int ownerPort) {
        this.ownerPort = ownerPort;
//        this.arrow = new Polygon();
//        this.layoutX = 38;
//        this.arrow.setLayoutY(y);
//        this.arrow.getPoints().addAll(-30.0, 5.0, 0.0, 0.0, -30.0, -5.0);
    }
    public Arrow() {}
    public Arrow(Arrow a) {
        this.ownerPort = a.getOwnerPort();
        this.colorName = a.getColorName();
        this.layoutX = a.layoutX;
        this.layoutY = a.layoutY;
    }

    public void setColor(COLORS color) {
        this.colorName = color;
    }
    public COLORS getColorName() { return colorName; }

    public Polygon getPolygon() {
        Polygon polygon = new Polygon();
        polygon.getPoints().addAll(this.points);
        polygon.setStroke(Color.BLACK);
        polygon.setStrokeWidth(1);
        polygon.setFill(this.colorName.getValue());
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
                ", colorName=" + colorName +
//                ", colorValue=" + colorValue +
                '}';
    }
//        private void paint(COLORS color) {
//        switch (color) {
//            case RED -> target.setColor(Color.rgb(255, 33, 33));
//            case BLUE -> target.setColor(Color.rgb(33, 212, 255));
//            case GREEN -> target.setColor(Color.rgb(0, 128, 0));
//        }
//    }
}
