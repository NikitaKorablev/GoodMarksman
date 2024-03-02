package com.example.goodmarksman.objects;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Target {
    private final Circle target;
    private final COLORS color;
    private int moveSpeed;
    private final int startOrientation;
    private int orientation;
    private final int weight;
    private final int colorCoolDown = 20;
    public int currentCoolDown = 0;

    public Target(Circle circle, COLORS color, int orientation, int weight, int speed) {
        this.target = circle;
        this.color = color;
        this.moveSpeed = speed;
        this.startOrientation = orientation;
        this.orientation = orientation;
        this.weight = weight;
    }

    public COLORS getColor() { return color; }
    public void setCoolDown() { this.currentCoolDown = this.colorCoolDown; }
    public boolean updateCoolDown() throws Exception {
        if (this.currentCoolDown < 0) throw new Exception("CurrentCoolDown < 0");

        boolean res = (this.currentCoolDown - 1 == 0);
        if (this.currentCoolDown > 0) this.currentCoolDown--;
        return res;
    }

    public double getRadius() { return this.target.getRadius(); }
    public double getX() { return this.target.getLayoutX(); }

    public void setY(double Y) {
        Platform.runLater(() -> this.target.setLayoutY(Y));
    }
    public double getY() { return this.target.getLayoutY(); }

    public void setOrientation(int orientation) { this.orientation = orientation; }
    public int getOrientation() { return orientation; }

    public int getStartOrientation() { return startOrientation; }

    public void setMoveSpeed(int speed) { this.moveSpeed = speed; }
    public double getMoveSpeed() { return this.moveSpeed; }

    public int getWeight() { return weight; }

    public void setColor(Color color) {
        Platform.runLater(() -> target.setFill(color));
    }

    public boolean isHitted(double X, double Y) {
        double distance = Math.sqrt(Math.pow(this.getX() - this.getRadius() - X, 2) +
                Math.pow(Y - this.getY(), 2));
        return distance <= target.getRadius();
    }
}
