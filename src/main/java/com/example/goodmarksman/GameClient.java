package com.example.goodmarksman;

import com.example.goodmarksman.models.GameModel;
import com.example.goodmarksman.objects.*;
import com.example.goodmarksman.objects.Action;

import java.util.ArrayList;

public class GameClient implements IObserver {
    Action gameState = Action.GAME_STOPPED;
    ClientState clientState = ClientState.NOT_READY;
    public Thread messageListener;

    private final Client server;
    private ArrayList<Client> players = new ArrayList<>();

    // Подключение к серверу
    public GameClient(Client server) throws Exception {
        this.server = server;
        System.out.println("Client connected to " + server.getSocket().getLocalPort());

        messageListener = new Thread(this::messageListener);
        messageListener.setDaemon(true);
//        messageListener.start();
//        new Thread(this::run).start();
    }

    void messageListener() {
//        boolean listening = true;

        while (true) {
//            System.out.println("Message received");
            try {
                Msg msg = server.readMsg();
                synchronized (this) {
                    switch (msg.getAction()) {
                        case CONNECTION_ERROR:
                            System.err.println(msg.message);
                            server.getSocket().close();
                            System.err.println("Socket was closed!");
                            break;
                        case UPDATE_GAME_STATE:
                            System.out.println(msg.clientsData);
                            MainClient.m.getDao().setClientsData(msg.clientsData);
                            MainClient.m.updateState();
                            break;
//                        case CLIENT_CONNECTED:

                    }
                }
//                System.out.println("Message Listener out: " + MainClient.m.getDao().clientsData.getArray());
            } catch (Exception e) {
                System.err.println("Message Listener error: " + e.getMessage());
                return;
            }
        }
//        System.out.println("Server disconnected");
    }

    public int getServerPort() {
        return server.getSocket().getLocalPort();
    }

    //TODO: Запуск цикла клиентской части
    void run() {
//        while (true) {
//            Resp r = readResp();
//            m.set(r.getPoints());
//        }
    }

    @Override
    public void event(GameModel m) {}
}
