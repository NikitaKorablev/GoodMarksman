package com.example.goodmarksman;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

public class Game {
    @FXML
    private Pane gameWindow;
    @FXML
    private Circle bigTarget;
    @FXML
    private Circle smallTarget;
    @FXML
    private Polygon arrow;

    private double upperThreshold;
    private double lowerThreshold;
    private boolean isPaused = true;
    private boolean isShooting = false;
    private int bigTargetOrientation = 1;
    private int smallTargetOrientation = -1;
    private void startView() {
        Thread thread = new Thread(() -> {
            try {
                while(!isPaused) {
                    if (bigTargetOrientation == 1 & bigTarget.getLayoutY() == lowerThreshold - bigTarget.getRadius())
                        bigTargetOrientation = -1;
                    else if (bigTargetOrientation == -1 & bigTarget.getLayoutY() == upperThreshold + bigTarget.getRadius())
                        bigTargetOrientation = 1;

                    if (smallTargetOrientation == 1 & smallTarget.getLayoutY() == lowerThreshold - smallTarget.getRadius())
                        smallTargetOrientation = -1;
                    else if (smallTargetOrientation == -1 & smallTarget.getLayoutY() == upperThreshold + smallTarget.getRadius())
                        smallTargetOrientation = 1;

                    bigTarget.setLayoutY(bigTarget.getLayoutY() + bigTargetOrientation);
                    smallTarget.setLayoutY(smallTarget.getLayoutY() + smallTargetOrientation);

                    if (isShooting) {
                        if (arrow.getLayoutX() >= 410) {
                            arrow.setLayoutX(60);
                            isShooting = false;
                            System.out.println(arrow.getPoints());
                        }
                        arrow.setLayoutX(arrow.getLayoutX() + 10);
                    }
                    Thread.sleep(10);
                }
            } catch(InterruptedException err) {
                System.err.println(err);
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
    protected void onShootButtonClick() { isShooting = true; }
}
