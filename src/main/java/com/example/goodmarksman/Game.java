package com.example.goodmarksman;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;

import java.lang.Math;

public class Game {
    @FXML
    private Pane gameWindow;
    @FXML
    private Circle bigTarget;
    @FXML
    private Circle smallTarget;
    @FXML
    private Polygon arrow;
    @FXML
    private Text shots;
    @FXML
    private Text score;

    private double upperThreshold;
    private double lowerThreshold;
    private boolean isPaused = true;
    private boolean isShooting = false;
    private int bigTargetOrientation = 1;
    private int smallTargetOrientation = -1;

    private final int arrowSpeed = 10;
    private final int targetSpeed = 1;

    private void moveArrowToStart() { arrow.setLayoutX(38); }
    private void colorizeBigTarget(Color color) { bigTarget.setFill(color); }
    private void colorizeSmallTarget(Color color) { smallTarget.setFill(color); }

    private void addScore(int add) { score.setText(Integer.toString(Integer.parseInt(score.getText())+add)); }
    private void addShots() { shots.setText(Integer.toString(Integer.parseInt(shots.getText())+1)); }
    private void startView() {
        double[] sep = {bigTarget.getLayoutX() - bigTarget.getRadius(),
                        bigTarget.getLayoutX(),
                        smallTarget.getLayoutX() - smallTarget.getRadius(),
                        smallTarget.getLayoutX()};

        Thread thread = new Thread(() -> {
            while(!isPaused) {
                Platform.runLater(() -> {
                    colorizeBigTarget(Color.rgb(33, 212, 255));
                    colorizeSmallTarget(Color.rgb(255, 33, 33));
                    if (bigTargetOrientation == 1 & bigTarget.getLayoutY() == lowerThreshold - bigTarget.getRadius())
                        bigTargetOrientation = -1;
                    else if (bigTargetOrientation == -1 & bigTarget.getLayoutY() == upperThreshold + bigTarget.getRadius())
                        bigTargetOrientation = 1;

                    if (smallTargetOrientation == 1 & smallTarget.getLayoutY() == lowerThreshold - smallTarget.getRadius())
                        smallTargetOrientation = -1;
                    else if (smallTargetOrientation == -1 & smallTarget.getLayoutY() == upperThreshold + smallTarget.getRadius())
                        smallTargetOrientation = 1;

                    bigTarget.setLayoutY(bigTarget.getLayoutY() + bigTargetOrientation * targetSpeed);
                    smallTarget.setLayoutY(smallTarget.getLayoutY() + smallTargetOrientation * targetSpeed);

                    if (isShooting) {
                        if (arrow.getLayoutX() >= sep[0] & arrow.getLayoutX() <= sep[1]) {
                            double distance = Math.sqrt(Math.pow(sep[1] - arrow.getLayoutX(), 2) +
                                    Math.pow(arrow.getLayoutY() - bigTarget.getLayoutY(), 2));
                            if (distance <= bigTarget.getRadius()) {
                                colorizeBigTarget(Color.rgb(0, 128, 0));
                                isShooting = false;
                                addScore(1);
                                moveArrowToStart();
                            }
                        } else if (arrow.getLayoutX() >= sep[2] & arrow.getLayoutX() <= sep[3]) {
                            double distance = Math.sqrt(Math.pow(sep[3] - arrow.getLayoutX(), 2) +
                                    Math.pow(arrow.getLayoutY() - smallTarget.getLayoutY(), 2));
                            if (distance <= smallTarget.getRadius()) {
                                isShooting = false;
                                addScore(2);
                                colorizeSmallTarget(Color.rgb(0, 128, 0));
                                moveArrowToStart();
                            }
                        }

                        else if (arrow.getLayoutX() >= 380) {
                            moveArrowToStart();
                            isShooting = false;
                        }
                        arrow.setLayoutX(arrow.getLayoutX() + arrowSpeed);
                    }
                });
                try {
                    Thread.sleep(10);
                } catch(InterruptedException err) {
                    System.err.println(err);
                }
            }

        });

        thread.setDaemon(true);
        thread.start();
    }
    @FXML
    protected void onStartButtonClick() {
        if (!isPaused) return;

        upperThreshold = 0;
        lowerThreshold = gameWindow.getHeight();

        isPaused = false;
        startView();
    }
    @FXML
    protected void onStopButtonClick() {
        isPaused = true;
    }
    @FXML
    protected void onShotButtonClick() {
        addShots();
        isShooting = true;
    }
}
