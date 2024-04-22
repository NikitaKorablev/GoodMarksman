package com.example.goodmarksman.objects;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Target {
//    private final Circle target;
    private COLORS curColor;
    private COLORS baseColor;
    private int moveSpeed;
    private final int startOrientation;
    private int orientation;
    private final int weight;
    private final int colorCoolDown = 20;
    private int radius = 0;
    private int startY;
    private int X = 0;
    private int Y = 0;

    private int upperThreshold;
//    private final int min_x = 38;
//    private final int max_x = 380;

    public int currentCoolDown = 0;

    public Target(COLORS color, int orientation, int weight, int speed) {
//        this.target = circle;
        this.curColor = color;
        this.baseColor = color;
        this.moveSpeed = speed;
        this.startOrientation = orientation;
        this.orientation = orientation;
        this.weight = weight;
    }

    public Target(COLORS color, int x, int y, int radius, int orientation, int weight, int speed) {
//        this.target = new Circle(radius);
        this.radius = radius;

        this.X = x;
        this.Y = y;
        this.startY = y;

        this.curColor = color;
        this.baseColor = color;
        this.moveSpeed = speed;
        this.startOrientation = orientation;
        this.orientation = orientation;
        this.weight = weight;
    }

    public void setUpperThreshold(int paneHeight) {
        this.upperThreshold = paneHeight;
    }

    public int getStartY() {
        return startY;
    }

    public Circle getCircle() {
        Circle circle = new Circle();
        circle.setRadius(radius);
        circle.setLayoutX(X);
        circle.setLayoutY(Y);
        circle.setFill(curColor.getValue());
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(1);
        return circle;
    }

    public COLORS getCurrentColor() { return curColor; }
    public COLORS getBaseColor() { return baseColor; }

    public void setCurrentColor(COLORS color) { this.curColor = color; }
    public void setBaseColor(COLORS color) { this.baseColor = color; }

    public void setCoolDown() { this.currentCoolDown = this.colorCoolDown; }
    public boolean updateCoolDown() throws Exception {
        if (this.currentCoolDown < 0) throw new Exception("CurrentCoolDown < 0");

        boolean res = (this.currentCoolDown - 1 == 0);
        if (this.currentCoolDown > 0) this.currentCoolDown--;
        return res;
    }

    public void move() throws Exception {
        System.err.println("Upper threshold: " + upperThreshold);
        if (upperThreshold == 0) { throw new Exception(""); }

        int newY = Y + moveSpeed * orientation;
        if (newY < radius ||
                newY > upperThreshold - radius) {
            orientation = orientation * (-1);
            newY = Y + moveSpeed * orientation;
        }
        System.err.println("newY: " + newY);
        Y = newY;

        try {
            if (updateCoolDown()) { curColor = baseColor; }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void hit() {
        this.curColor = COLORS.GREEN;
        setCoolDown();
    }

    public void setRadius(int radius) { this.radius = radius; }
    public int getRadius() { return this.radius; }

    public void setX(int x) { this.X = x; }
    public int getX() { return this.X; }

    public void setY(int Y) { this.Y = Y; }
    public int getY() { return this.Y; }

    public void setOrientation(int orientation) { this.orientation = orientation; }
    public int getOrientation() { return orientation; }

    public int getStartOrientation() { return startOrientation; }

    public void setMoveSpeed(int speed) { this.moveSpeed = speed; }
    public double getMoveSpeed() { return this.moveSpeed; }

    public int getWeight() { return weight; }

    public boolean isHitted(double X, double Y) {
        double distance = Math.sqrt(Math.pow(this.getX() - this.getRadius() - X, 2) +
                Math.pow(Y - this.getY(), 2));
        return distance <= radius;
    }

    @Override
    public String toString() {
        return "Target{" +
                "curColor=" + curColor +
                ", baseColor=" + baseColor +
                ", moveSpeed=" + moveSpeed +
                ", startOrientation=" + startOrientation +
                ", orientation=" + orientation +
                ", weight=" + weight +
                ", colorCoolDown=" + colorCoolDown +
                ", radius=" + radius +
                ", X=" + X +
                ", Y=" + Y +
                ", upperThreshold=" + upperThreshold +
//                ", min_x=" + min_x +
//                ", max_x=" + max_x +
                ", currentCoolDown=" + currentCoolDown +
                '}';
    }
}
