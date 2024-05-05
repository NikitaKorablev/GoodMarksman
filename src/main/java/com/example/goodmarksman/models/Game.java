package com.example.goodmarksman.models;

//import com.example.goodmarksman.View;
import com.example.goodmarksman.objects.Arrow;
import com.example.goodmarksman.objects.Target;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

public class Game {
//    private final View view;
    private Target target1;
    private Target target2;
    private Arrow arrow;
    private Thread gameThread;
    private final int timeSleep = 500;
    public GameState state = GameState.STOPPED;

    public boolean isPaused = false;

    public Game(Circle target1, Circle target2, Polygon arrow) {
//        this.view = view;
//        this.target1 = new Target(target1, COLORS.BLUE, -1, 1, 1);
//        this.target2 = new Target(target2, COLORS.RED, 1, 2, 2);
//        this.arrow = new Arrow(arrow);
    }

//    private void eventCheck(View view) {
//        if (arrow.getX() >= target1.getX() - target1.getRadius() && arrow.getX() <= target1.getX()) {
//            if (target1.isHitted(arrow.getX(), arrow.getY())) {
//                view.paint(target1, COLORS.GREEN);
//                view.hit(target1, arrow);
//            }
//        } else if (arrow.getX() >= target2.getX() - target2.getRadius() && arrow.getX() <= target2.getX()) {
//            if (target2.isHitted(arrow.getX(), arrow.getY())) {
//                view.paint(target2, COLORS.GREEN);
//                view.hit(target2, arrow);
//            }
//        } else if (arrow.getX() >= view.getMaxX()) {
//            arrow.setIsShooting(false);
//            arrow.setX(view.getArrowStartPosition());
//        }
//    }

    private void gameFreeze(int time) {
        try {
            this.setPaused();
            Thread.sleep(time);
            this.checkState();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    private boolean checkState() {
        if (gameThread != null) {
            if (this.isPaused) {
                try {
                    synchronized (gameThread) {
                        this.isPaused = false;
                        gameThread.notify();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return true;
        }

        return false;
    }

//    public void setArrowY(double y) { this.view.setArrowY(arrow, y); }

    public void startGame() {
        if (checkState()) return;

        gameThread = new Thread(() -> {
            while(this.state == GameState.STARTED) {
                if (isPaused) {
                    try {
                        synchronized (gameThread) {
                            try {
                                gameThread.wait();
                            } catch (InterruptedException e) {
                                System.err.println(e);
                                this.stopGame();
                            }
                        }
                    } catch (Exception e) {
                        System.err.println(e);
                        this.stopGame();
                    }
                }

                if (arrow.getIsShooting()) {}

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
        if (state == GameState.STOPPED) return;

        this.isPaused = true;
    }
    public void stopGame() {
        this.state = GameState.STOPPED;
        gameThread = null;

//        this.view.setStartPositions(target1, target2, arrow);
    }

    public void shot() {
        if (!this.isPaused) {
            if (arrow.getIsShooting()) return;

            arrow.setIsShooting(true);
//            this.view.shotInc();
        }
    }

    public enum GameState {
        STARTED, STOPPED
    }
}
