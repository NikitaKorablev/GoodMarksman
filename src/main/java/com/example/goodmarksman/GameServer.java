package com.example.goodmarksman;

import com.example.goodmarksman.models.GameModel;
import com.example.goodmarksman.objects.*;
import com.example.goodmarksman.objects.Action;
import com.google.gson.Gson;

import java.io.*;
import java.util.ArrayList;

public class GameServer implements IObserver {
//    GameModel model = Models.buildGM();
    Gson gson = new Gson();

    private final ArrayList<Thread> messageListeners = new ArrayList<>();
    private Thread gameThread = null;

    Action gameState = Action.GAME_STOPPED;
    boolean isPaused = false;
    Thread sendState = null;

    int countReadyPlayers = 0;

    // Подключение к серверу
    public GameServer(Client cl) {
//        addPlayer(cl);
        addListener(cl).start();

//        gameThread = new Thread(this::run);
//        gameThread.setDaemon(true);
//        new Thread(this::run).start();
    }

    public Thread addListener(Client cl) {
        Thread thread = new Thread(() -> messageListener(cl));
        thread.setDaemon(true);
        messageListeners.add(thread);
        return thread;
    }

    public void addPlayer(Client cl) {
        System.out.println("players count " + MainServer.model.playersSize());
        if (MainServer.model.playersSize() >= 4) {
            try {
                cl.sendMsg(new Msg("Too many players connected to the server",
                        Action.CONNECTION_ERROR)
                );
            } catch (IOException e) {
                System.err.println("Add player Error: " + e.getMessage());
                throw new RuntimeException(e);
            }

            return;
        }

        try {
            MainServer.model.addClient(cl, new Arrow(cl.getSocket().getPort()), new Score(cl.getSocket().getPort()));
        } catch (Exception e) {
            System.err.println("Error in addPlayer() in GameServer: " + e.getMessage());
            throw new RuntimeException(e);
        }
//        Thread thread = new Thread(() -> messageListener(cl));
//        thread.setDaemon(true);
//        thread.start();
//        messageListeners.add(thread);
        System.out.println("new players count " + MainServer.model.playersSize());
    }

    void messageListener(Client cl) {
        try {
            while (true) {
                Msg msg = cl.readMsg();

                synchronized (Thread.currentThread()) {
                    switch (msg.getAction()) {
//                        case CONNECT:
//                            System.out.println("Client say: " + msg.message);
//                            break;
                        case CONNECTION_ERROR:
                            throw new Exception(msg.message);
                        case CLIENT_STATE:
                            System.out.println(msg.clientState);

                            if (gameState == Action.GAME_STOPPED &&
                                gameThread == null) {
                                gameThread = new Thread(this::run);
                                gameThread.setDaemon(true);
                            }

                            if (gameState == Action.GAME_STARTED &&
                                    isPaused
                                    && msg.clientState == ClientState.READY) {
                                this.countReadyPlayers++;
                                System.err.println(countReadyPlayers);
                                if (this.countReadyPlayers == MainServer.model.playersSize()) isStarted();
                                break;
                            } else if (msg.clientState == ClientState.NOT_READY) {
                                isPaused = true;
                                this.countReadyPlayers--;
                                System.err.println(countReadyPlayers);
                            } else if (gameState == Action.GAME_STOPPED &&
                                    msg.clientState == ClientState.READY) {
                                // Проверка на уже запущенную игру
                                if (isStarted()) break;

                                this.countReadyPlayers++;
                                System.err.println(countReadyPlayers);
                                //TODO: готово не больше 4 игроков
                                if (this.countReadyPlayers == MainServer.model.playersSize()) {
                                    isPaused = false;
                                    gameState = Action.GAME_STARTED;
                                    gameThread.start();
                                }
                            }

                            break;
                        case SET_NAME:
//                            MainServer.model.getDao().setClientName(cl.getSocket(), msg.message);
                            cl.setName(msg.message);
                            addPlayer(cl);
//                            cl.sendMsg(new Msg("", MsgAction.CLIENT_CONNECTED));
                            break;
                        case GAME_STOPPED:
                            if (gameThread != null && gameThread.isAlive()) {
                                gameState = Action.GAME_STOPPED;
                                gameThread = null;
                                MainServer.model.getDao().getClientsData().nullify();
                                MainServer.model.event();
                            }
                            break;
                        case UPDATE_GAME_STATE:
                            if (msg.arrow != null) {
                                MainServer.model.getDao().getClientsData().updateArrow(msg.arrow);
                                MainServer.model.event();
                            }
                            break;
                        case SHOT:
//                            System.err.println("SHOT " + msg.message);
                            MainServer.model.getDao().getClientsData().arrowShot(cl.getSocket().getPort());
                            break;
                        case WIDTH_INIT:
                            for (Arrow a: MainServer.model.getDao().getClientsData().getArrows()) {
                                a.setMaxX(msg.view_width);
                            }
                            for (Target t: MainServer.model.getDao().getClientsData().getTargets()) {
                                t.setUpperThreshold(msg.view_height);
                            }
                            break;
                        default:
                            System.out.println("Get message: " + msg.action);
                            break;
                    }
                }
            }

        } catch (Exception e) {
            try {
                cl.getSocket().close();
                System.err.println("Client " + cl.getSocket().getPort() + " closed.");
                System.err.println("GameServer: " + e.getMessage());
                gameState = Action.GAME_STOPPED;
                gameThread.interrupt();
                synchronized (this) {
                    messageListeners.remove(MainServer.model.getPlayerIndex(cl.getSocket()));
                    MainServer.model.removePlayer(cl);
                    MainServer.model.removeObserver(cl.getIObserver());
//                        model.
//                        System.out.println(model.getArray());
                }
            } catch (Exception err) {
                System.err.println(err.getMessage());
                throw new RuntimeException(err);
            }

            System.err.println("Remove is called");
        }
    }

    //TODO: Запуск цикла среверной части
    void run() {
        // TODO: Переезд в слушатель
        //  if (checkState()) return;
        System.out.println("Game started!!!");

        while (gameState == Action.GAME_STARTED) {
            if (isPaused) {
                try {
                    synchronized (Thread.currentThread()) {
                        try {
                            gameThread.wait();
                        } catch (InterruptedException e) {
                            System.err.println(e.getMessage());
                            this.stopGame();
                        }
                    }
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                    this.stopGame();
                }
            }

            ArrayList<Arrow> arrows = MainServer.model.getDao().getClientsData().getArrows();
            ArrayList<Target> targets = MainServer.model.getDao().getClientsData().getTargets();

            synchronized (Thread.currentThread()) {
                for (Target t: targets) {
                    System.out.println(t.getY());
                    try {
                        t.move();
                    } catch (Exception e) {
                        System.err.println(e.getMessage());
                    }
                    System.out.println(t.getY());
                }

                for (Arrow a: arrows) {
                    if (a.getIsShooting()) {
                        a.setX(a.getX() + a.getSpeed());
                        checkIsHit(a, targets);
                    }
                }
            }

            MainServer.model.event();

            try {
                Thread.sleep(10);
            } catch(InterruptedException err) {
                System.err.println(err.getMessage());
            }
        }
    }

    public void stopGame() {
        this.gameState = Action.GAME_STOPPED;
        gameThread.interrupt();
        gameThread = null;

//        this.view.setStartPositions(target1, target2, arrow);
    }

    private void checkIsHit(Arrow arrow, ArrayList<Target> targets) {
        System.out.println("checker: " + arrow);
        if (arrow.getX() >= arrow.getMaxX()) {
            arrow.setIsShooting(false);
            arrow.setX(arrow.getMinX());
            return;
        }

        for (Target t: targets) {
            if (arrow.getX() >= t.getX() - t.getRadius() &&
                    arrow.getX() <= t.getX() &&
                    t.isHitted(arrow.getX(), arrow.getY())) {
                arrow.hit();
                t.hit();
                MainServer.model.getDao().getClientsData().updateScore(arrow.getOwnerPort(), t.getWeight());
            }
        }
    }

    private boolean isStarted() throws Exception {
        if (gameThread != null) {
            if (this.isPaused) {
                try {
                    synchronized (this.gameThread) {
                        this.isPaused = false;
                        gameThread.notify();
                    }
                } catch (Exception e) {
                    System.err.println("Is started method call exception: " + e.getMessage());
                    throw new Exception(e);
                }
            }
            return false;
        } else System.err.println("gameThread is null");

        return true;
    }

    public Client getLastClient() {
        return MainServer.model.getClient(-1);
    }

    @Override
    public void event(GameModel m) {}
}
