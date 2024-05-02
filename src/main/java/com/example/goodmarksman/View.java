package com.example.goodmarksman;

import com.example.goodmarksman.objects.Arrow;
import com.example.goodmarksman.objects.COLORS;
import com.example.goodmarksman.objects.Score;
import com.example.goodmarksman.objects.Target;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class View {
    private final Score scoreBoard;
    private final double lowerThreshold;
    private final double upperThreshold;
    private final double targetStartPos;
    private final double arrowStartPosition = 38;
    private final double max_x = 380;

    View(Pane parentPane, Text score, Text shotCount) {
        this.scoreBoard = new Score(score, shotCount);
        this.lowerThreshold = 0;
        this.upperThreshold = parentPane.getHeight();
        this.targetStartPos = this.upperThreshold / 2;
    }

    public void setArrowY(Arrow arrow, double y) {
        Platform.runLater(() -> arrow.setY(y));
    }

    public void scoreInc(int weight) { this.scoreBoard.scoreInc(weight); }
    public void shotInc() { this.scoreBoard.shotCountInc(); }
    private void setStartScoreBord() {
        this.scoreBoard.setStartScore();
        this.scoreBoard.setStartShotCount();
    }

    public double getMaxX() { return max_x; }
    public void paint(Target target, COLORS color) {
//        switch (color) {
//            case RED -> target.setColor(Color.rgb(255, 33, 33));
//            case BLUE -> target.setColor(Color.rgb(33, 212, 255));
//            case GREEN -> target.setColor(Color.rgb(0, 128, 0));
//        }
    }
    public void move(Target target) {
        double newY = target.getY() + target.getMoveSpeed() * target.getOrientation();
        if (newY < this.lowerThreshold + target.getRadius() ||
                newY > this.upperThreshold - target.getRadius()) {
            target.setOrientation(target.getOrientation() * (-1));
            newY = target.getY() + target.getMoveSpeed() * target.getOrientation();
        }
//        target.setY(newY);

//        try {
//            if (target.updateCoolDown()) { paint(target, target.getColor()); }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
    public void move(Arrow arrow) {
        double newX = arrow.getX() + arrow.getSpeed();
        arrow.setX(newX);
    }
    public void hit(Target target, Arrow arrow) {
        arrow.setIsShooting(false);
        arrow.setX(this.arrowStartPosition);
        this.scoreInc(target.getWeight());
        target.setCoolDown();
    }

    public void setStartPositions(Target target1, Target target2, Arrow arrow) {
//        target1.setY(this.targetStartPos);
        target1.setOrientation(target1.getStartOrientation());

//        target2.setY(this.targetStartPos);
        target2.setOrientation(target2.getStartOrientation());

        arrow.setX(this.arrowStartPosition);
        this.setStartScoreBord();
    }

    public double getArrowStartPosition() { return this.arrowStartPosition; }
}
