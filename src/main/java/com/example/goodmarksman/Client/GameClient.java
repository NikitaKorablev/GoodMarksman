package com.example.goodmarksman.Client;

import com.example.goodmarksman.MainClient;
import com.example.goodmarksman.enams.ClientState;
import com.example.goodmarksman.models.GameModel;
import com.example.goodmarksman.objects.*;
import com.example.goodmarksman.enams.Action;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.util.ArrayList;

public class GameClient implements IObserver {
    Action gameState = Action.GAME_STOPPED;
    ClientState clientState = ClientState.NOT_READY;
    public Thread messageListener;

    private final Client server;

    // Подключение к серверу
    public GameClient(Client server) throws Exception {
        this.server = server;
        System.out.println("Client connected to " + server.getSocket().getLocalPort());

        messageListener = new Thread(this::messageListener);
        messageListener.setDaemon(true);
    }

    private void showScoreBoard(ArrayList<Score> scoreBoard) {
        SB_Controller controller = new SB_Controller();

        Platform.runLater(() -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("score-bord.fxml"));
                fxmlLoader.setController(controller);
                Stage stage = new Stage();
                stage.setTitle("Score Board");
                stage.setScene(new Scene(fxmlLoader.load()));
                stage.show();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        });

        controller.setTable(scoreBoard);
    }

    void messageListener() {
        while (true) {
            try {
                Msg msg = server.readMsg();
                synchronized (Thread.currentThread()) {
                    switch (msg.getAction()) {
                        case CONNECTION_ERROR:
                            server.getSocket().close();
                            break;
                        case UPDATE_GAME_STATE:
                            MainClient.m.getDao().setClientsData(msg.clientsData);
                            MainClient.m.updateState();
                            break;
                        case CLIENT_DISCONNECTED:
                            Platform.runLater(() -> {
                                try {
                                    MainClient.m.getDao().deleteClient(msg.clientData);
                                } catch (Exception e) {
                                    System.err.println("Delete Client Error in GameClient: " + e.getMessage());
                                    throw new RuntimeException(e);
                                }
                            });
                            break;
                        case CLIENT_STATE:
                            System.out.println("\nWin event: " + msg + "\n");
                            if (msg.clientState.equals(ClientState.WIN)) {
                                showScoreBoard(msg.scoreBoard);
                                Platform.runLater(() ->
                                    new Alert(Alert.AlertType.INFORMATION, msg.message).show()
                                );
                            }
                            break;
                        case GET_DB:
                            showScoreBoard(msg.scoreBoard);
                            break;
                        default:
                            break;
                    }
                }
            } catch (Exception e) {
                System.err.println("Message Listener error: " + e.getMessage());
                return;
            }
        }
    }

    public int getServerPort() {
        return server.getSocket().getLocalPort();
    }

    @Override
    public void event(GameModel m) {}
}
