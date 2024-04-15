package com.example.goodmarksman;

import com.example.goodmarksman.models.GameModel;
import com.example.goodmarksman.objects.*;
import com.google.gson.Gson;
import javafx.scene.layout.Pane;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class GameClient implements IObserver {

    GameModel m = Models.buildGM();
    Gson gson = new Gson();

    MsgAction gameState = MsgAction.GAME_STOPPED;
    ClientState clientState = ClientState.NOT_READY;
    public Thread messageListener;

    private Client server;
    private ArrayList<Client> players = new ArrayList<>();

    Arrow arrow;
//    ArrayList<Score> scoreBord;
    Pane parentView;

    // Подключение к серверу
    public GameClient(Client server) throws Exception {
        this.server = server;
        System.out.println("Client connected to " + server.getSocket().getLocalPort());

//        new Thread(this::messageListener).start();
        messageListener = new Thread(this::messageListener);
        messageListener.setDaemon(true);
        messageListener.start();
//        new Thread(this::run).start();
    }

    void messageListener() {
//        boolean listening = true;

        while (true) {
            System.out.println("Message received");
            try {
                Msg msg = server.readMsg();
//                if (msg.getAction() == MsgAction.CONNECTED) {
////                    System.out.println(msg.message);
////                    server.setConnected(true);
////                    nameChecked = true;
//                }
                if (msg.getAction() == MsgAction.CONNECTION_ERROR) {
                    System.err.println(msg.message);
//                    server.setConnected(false);
//                    nameChecked = true;
//                    listening = false;
                    server.getSocket().close();
                    System.err.println("Socket was closed!");
                    continue;
                }
                if (msg.getAction() == MsgAction.UPDATE_GAME_STATE) {
                    System.out.println(msg.clientsData);
                    continue;
                }
            } catch (IOException e) {
                System.err.println("Message Listener error: " + e.getMessage());
                return;
            }
        }
//        System.out.println("Server disconnected");
    }

    //TODO: Запуск цикла клиентской части
    void run() {
//        while (true) {
//            Resp r = readResp();
//            m.set(r.getPoints());
//        }
    }

    public void updatePlayers(ArrayList<Client> players) {
        for (Client player : players) {
            if (player.getSocket().getPort() == server.getSocket().getLocalPort()) {
//                arrow.setY(player.getArrow().getY());
//                arrow.setX(player.getArrow().getX());

//                scoreBord.get(0).setShotCount(player.getScore().getShotCount());
//                scoreBord.get(0).setScore(player.getScore().getScore());
            }

            Client chosenPlayer = null;
            for (Client currPlayer : this.players) {
                if (currPlayer.getSocket().getPort() == player.getSocket().getPort()) {
                    chosenPlayer = currPlayer;
                    break;
                }
            }

//            scoreBord.get(players.indexOf(player)).setShotCount(player.getScore().getShotCount());
//            scoreBord.get(players.indexOf(player)).setScore(player.getScore().getScore());

            if (chosenPlayer != null) {
//                chosenPlayer.getArrow().setY(player.getArrow().getY());
//                chosenPlayer.getArrow().setX(player.getArrow().getX());

            } else {
//                Arrow a = new Arrow(player.getArrow());
//                parentView.getChildren().add(a.getPolygon());

//                Client newPlayer = new Client(player.getSocket(), a, scoreBord.get(players.indexOf(player)));
//                this.players.add(newPlayer);
            }
        }
    }

//    public Resp readResp(Client cl) throws IOException {
//        Resp r = null;
//        try {
//            String respStr = cl.getDis().readUTF();
//            r = gson.fromJson(respStr, Resp.class);
//        } catch(Exception e) {
//            System.out.println(e.getMessage());
//        }
//        return r;
//    }

//    public Msg readMsg() throws IOException {
//        Msg msg;
//        try {
//            String respStr = server.getDis().readUTF();
//            System.out.println(respStr);
//            msg = gson.fromJson(respStr, Msg.class);
//        } catch(IOException e) {
//            System.out.println(e.getMessage());
//            throw e;
//        }
//        return msg;
//    }

//    public void sendResp(Resp resp) {
//        String strResp = gson.toJson(resp);
//        try {
//            dos.writeUTF(strResp);
//        } catch (IOException e) {
//            System.out.println(e.getMessage());
//        }
//    }

//    public void sendMsg(Msg msg) {
//        try {
//            String strMsg = gson.toJson(msg);
//            System.out.println(strMsg);
//            server.getDos().writeUTF(strMsg);
//        } catch (IOException e) {
//            System.out.println(e.getMessage());
//        }
//    }

    @Override
    public void event(GameModel m) {}
}
