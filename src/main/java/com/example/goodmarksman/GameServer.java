package com.example.goodmarksman;

import com.example.goodmarksman.models.GameModel;
import com.example.goodmarksman.objects.*;
import com.example.goodmarksman.objects.Action;

import java.io.*;
import java.util.ArrayList;

public class GameServer implements IObserver {
    private final ArrayList<Thread> messageListeners = new ArrayList<>();
    private Thread gameThread = null;

    Action gameState = Action.NULL;
    boolean isPaused = false;
    int countReadyPlayers = 0;

    // Подключение к серверу
    public GameServer(Client cl) {
        addListener(cl).start();
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
        System.out.println("new players count " + MainServer.model.playersSize());
    }

    private void stopGame() {
        System.out.println("Stop Game was called.");

        if (gameState == Action.NULL ||
                gameThread == null ||
                gameThread.isInterrupted()) return;

        synchronized (this.gameThread) {
            if (!isPaused) countReadyPlayers--;
            gameThread.interrupt();
            gameState = Action.GAME_STOPPED;
            isPaused = false;
            gameThread.notify();
        }

        gameThread = null;

        MainServer.model.getDao().getClientsData().nullify();
        MainServer.model.event();

        synchronized (this) {
            gameState = Action.NULL;
        }
    }

    void messageListener(Client cl) {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Msg msg = cl.readMsg();

                synchronized (Thread.currentThread()) {
                    switch (msg.getAction()) {
                        case CONNECTION_ERROR:
                            throw new Exception(msg.message);
                        case CLIENT_STATE:
                            System.out.println(msg.clientState);

                            if (gameState == Action.NULL &&
                                gameThread == null) {
                                gameThread = new Thread(this::run);
                                gameThread.setDaemon(true);
                                gameState = Action.GAME_STOPPED;
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
//                                if (!isPaused) countReadyPlayers--;
//                                isPaused = false;
//                                gameState = Action.GAME_STOPPED;
//                                gameThread = null;
//                                MainServer.model.getDao().getClientsData().nullify();
//                                MainServer.model.event();
                                stopGame();
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
            } catch (Exception e) {
                System.err.println("Remove is called");
                Thread.currentThread().interrupt();
                try {
                    System.out.println(gameState);

                    System.err.println("Client " + cl.getSocket().getPort() + " closed.");
                    System.err.println("GameServer: " + e.getMessage());

                    stopGame();

                    for (Client client : MainServer.model.getDao().getPlayers()) {
                        if (client == cl) continue;
                        client.sendMsg(new Msg(
                                MainServer.model.getClientData(cl.getSocket()),
                                Action.CLIENT_DISCONNECTED
                        ));
                    }

                    messageListeners.remove(MainServer.model.getPlayerIndex(cl.getSocket()));
                    MainServer.model.getDao().removeClient(cl);
                    MainServer.model.removeObserver(cl.getIObserver());
//                    MainServer.model.event();
                } catch (Exception err) {
                    System.err.println("In Err on GameServer: " + err.getMessage());
                    throw new RuntimeException(err);
                }
            }
        }
    }

    void run() {
        System.out.println("Game started!!!");

        while (!Thread.currentThread().isInterrupted() && gameState == Action.GAME_STARTED) {
            if (isPaused) {
                try {
                    synchronized (Thread.currentThread()) {
                        gameThread.wait();
                    }
                } catch (Exception e) {
                    System.err.println("Game error: " + e.getMessage());
                    stopGame();
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
                System.err.println("Sleep error in game thread: " + err.getMessage());
            }
        }
    }

//    public void stopGame() {
//        gameState = Action.GAME_STOPPED;
//        gameThread.interrupt();
//        gameThread = null;
//        gameState = Action.NULL;
////        this.view.setStartPositions(target1, target2, arrow);
//    }

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
                int score = MainServer.model.getDao().getClientsData().updateScore(arrow.getOwnerPort(), t.getWeight());
                if (score >= 6) {
                    ArrayList<Client> players = MainServer.model.getDao().getPlayers();
                    try {
                        Client winner_cl = null;
                        for (Client cl: players) {
                            if (cl.getSocket().getPort() == arrow.getOwnerPort()) {
                                winner_cl = cl;
                                break;
                            }
                        }

                        assert winner_cl != null;
                        for (Client cl: players) {
                            if (cl == winner_cl) {
                                cl.sendMsg(new Msg("!!!YOU WIN!!!", ClientState.WIN, Action.CLIENT_STATE));
                            } else {
                                cl.sendMsg(new Msg("!!!" + winner_cl.getName() + " is WINNER!!", ClientState.WIN, Action.CLIENT_STATE));
                            }
                        }

                        stopGame();
                    } catch (IOException e) {
                        System.err.println("Win error: " + e.getMessage());
                        throw new RuntimeException(e);
                    }
                }
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
