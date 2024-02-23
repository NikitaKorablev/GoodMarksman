package com.example.goodmarksman.objects;

import com.example.goodmarksman.View;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

public class Game {
    private Target target1;
    private Target target2;
    private Arrow arrow;
    private Thread gameThread;
    private final int timeSleep = 500;
    public GameState state = GameState.NOT_INITIALIZED;

    public void init(Circle target1, Circle target2, Polygon arrow) {
        this.target1 = new Target(target1, -1, 1);
        this.target2 = new Target(target2, 1, 2);
        this.arrow = new Arrow(arrow);

        this.state = GameState.STOPPED;
    }

    private void eventCheck(View view) {
        if (arrow.getX() >= target1.getX() - target1.getRadius() && arrow.getX() <= target1.getX()) {
            if (target1.isHitted(arrow.getX(), arrow.getY())) {
                view.paint(target1, View.COLOR.GREEN);
                view.hit(target1, arrow);
                this.gameFreeze(this.timeSleep);
                view.paint(target1, View.COLOR.BLUE);
            }
        } else if (arrow.getX() >= target2.getX() - target2.getRadius() && arrow.getX() <= target2.getX()) {
            if (target2.isHitted(arrow.getX(), arrow.getY())) {
                view.paint(target2, View.COLOR.GREEN);
                view.hit(target2, arrow);
                this.gameFreeze(this.timeSleep);
                view.paint(target2, View.COLOR.RED);
            }
        } else if (arrow.getX() >= view.getMaxX()) {
            arrow.setIsShooting(false);
            arrow.setX(view.getArrowStartPosition());
        }
    }

    private void gameFreeze(int time) {
        try {
            this.setPaused();
            Thread.sleep(time);
            this.gameResume();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    private boolean gameResume() {
        if (state == GameState.NOT_INITIALIZED) return false;

        if (gameThread != null) {
            if (this.state == GameState.PAUSED) { this.state = GameState.STARTED; }
            else return false;
        }

        return true;
    }

    public void startGame(View view) {
        if (!gameResume()) return;

        gameThread = new Thread(() -> {
            while(this.state == GameState.STARTED) {
                view.move(target1);
                view.move(target2);

                if (arrow.getIsShooting()) {
                    view.move(arrow);
                    this.eventCheck(view);
                }

                try {
                    Thread.sleep(10);
                } catch(InterruptedException err) {
                    System.err.println(err);
                }
            }
        });

        this.state = GameState.STARTED;
        gameThread.setDaemon(true);
        gameThread.start();
    }

    public void setPaused() throws InterruptedException {
        if (state == GameState.STOPPED || state == GameState.NOT_INITIALIZED) return;

        this.state = GameState.PAUSED;
    }
    public void stopGame(View view) {
        this.state = GameState.STOPPED;
        gameThread = null;

        view.setStartPositions(target1, target2, arrow);
    }

    public void shot(View view) {
        if (this.state != GameState.STARTED) return;

        arrow.setIsShooting(true);
        view.shotInc();
    }

    public enum GameState {
        STARTED, STOPPED, PAUSED, NOT_INITIALIZED
    }
}
