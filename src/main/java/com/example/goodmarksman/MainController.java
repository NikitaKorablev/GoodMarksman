package com.example.goodmarksman;

//import com.example.goodmarksman.models.Game;
import com.example.goodmarksman.models.GameModel;
import com.example.goodmarksman.objects.*;
import com.example.goodmarksman.objects.Action;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class MainController implements IObserver {
    private final int port = 3000;
    private InetAddress ip = null;

    @FXML
    private Button connectButton;
    @FXML
    private TextField inputNameField;

    @FXML
    private Pane gameView;
//    @FXML
//    VBox scoreBord;
//    @FXML
//    private Circle bigTarget;
//    @FXML
//    private Circle smallTarget;
    @FXML
    private Polygon arrow;
    @FXML
    private Text score_1;
    @FXML
    private Text shots_1;
    @FXML
    private Text score_2;
    @FXML
    private Text shots_2;
    @FXML
    private Text score_3;
    @FXML
    private Text shots_3;
    @FXML
    private Text score_4;
    @FXML
    private Text shots_4;

    @FXML
    private HBox name_block_1;
    @FXML
    private HBox name_block_2;
    @FXML
    private HBox name_block_3;
    @FXML
    private HBox name_block_4;

//    public void setPrimaryStage(Stage primaryStage) { this.primaryStage = primaryStage; }
//    public void setGame(GameClient game) { this.game = game; }
//    public void setInetAddress(InetAddress ip) { this.ip = ip; }
//    public void setPlayerName(String playerName) { this.playerName = playerName; }
//    public void setServer(Client server) { this.server = server; }

    @FXML
    public void initialize() {
        if (gameView != null && MainClient.game != null) {
            ArrayList<Text> scoreList = new ArrayList<>();
            ArrayList<Text> shotsList = new ArrayList<>();

            ArrayList<HBox> statisticBoxes = new ArrayList<>();

            System.err.println(statisticBoxes);

            synchronized (MainClient.m) {
                scoreList.add(score_1);
                scoreList.add(score_2);
                scoreList.add(score_3);
                scoreList.add(score_4);

                shotsList.add(shots_1);
                shotsList.add(shots_2);
                shotsList.add(shots_3);
                shotsList.add(shots_4);

                statisticBoxes.add(name_block_1);
                statisticBoxes.add(name_block_2);
                statisticBoxes.add(name_block_3);
                statisticBoxes.add(name_block_4);

                MainClient.m.getDao().setGameView(gameView);
                MainClient.m.getDao().setScoreList(scoreList);
                MainClient.m.getDao().setShotsList(shotsList);
                MainClient.m.getDao().setStatisticBoxes(statisticBoxes);
//                MainClient.model.getDao().setSmallTarget(smallTarget);
//                MainClient.model.getDao().setBigTarget(bigTarget);
                MainClient.m.getDao().setArrow(arrow);
            }



            MainClient.m.addObserver((model) -> {
//                System.out.println("test");
                System.out.println("Event out: " + MainClient.m.getDao().getClientsData().getArray());
                ClientsDataArray dataObj = MainClient.m.getDao().getClientsData();

                synchronized (Thread.currentThread()) {
                    MainClient.m.getDao().updateTargets(dataObj.getTargets());

                    for (ClientData data: dataObj.getArray()) {
                        System.err.println("Player port: " + data.getPlayerPort());
                        MainClient.m.getDao().updateArrow(data.getArrow());
                        MainClient.m.getDao().updateScore(data.getScore(), data.getArrow().getColorName());
                    }

                    MainClient.m.getDao().getClientsData().clearAllData();
                }
            });
        }
    }

    @FXML
    void connect() {
        System.out.println("Connect called");

        if (MainClient.game != null) return;

        try {
            MainClient.playerName = inputNameField.getText();
            if (MainClient.playerName.isEmpty()) throw new Exception("Player name is empty");

            ip = InetAddress.getLocalHost();
            Socket ss = new Socket(ip, port);
            System.out.println("ClientStart \n");

            MainClient.server = new Client(ss);
            MainClient.game = new GameClient(MainClient.server);

            // TODO: отправка имени игрока на сервер
            MainClient.server.sendMsg(new Msg(MainClient.playerName, Action.SET_NAME));
            // TODO: проверка имени на идентичнсть

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("game-view.fxml"));

            MainClient.primaryStage.setScene(new Scene(fxmlLoader.load()));
            MainClient.primaryStage.show();

            MainClient.game.messageListener.start();

        } catch (Exception e) {
            // При отключении сервера, клиент уходит в переподключение
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            System.err.println(e.getMessage());
        }
    }

    @FXML
    protected void onStartButtonClick() {
        if (MainClient.game == null) {
            new Alert(Alert.AlertType.ERROR, "Game is undefined!").show();
            System.err.println("Game is undefined!");
            return;
        }

        if (MainClient.game.clientState == ClientState.NOT_READY &&
            MainClient.game.gameState == Action.GAME_STARTED) {
            MainClient.game.clientState = ClientState.READY;

            try {
                MainClient.server.sendMsg(new Msg(MainClient.game.clientState, Action.CLIENT_STATE));
            } catch (IOException e) {
                System.err.println(e.getMessage());
                throw new RuntimeException(e);
            }
        }

        if (MainClient.game.clientState == ClientState.NOT_READY &&
            MainClient.game.gameState == Action.GAME_STOPPED) {
            MainClient.game.clientState = ClientState.READY;
            MainClient.game.gameState = Action.GAME_STARTED;

            try {
                MainClient.server.sendMsg(new Msg(
                        new int[]{(int) gameView.getWidth(), (int) gameView.getHeight()},
                        Action.WIDTH_INIT));

                MainClient.server.sendMsg(new Msg(MainClient.game.clientState, Action.CLIENT_STATE));
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }
    }
    @FXML
    protected void onStopButtonClick() {
        if (gameView == null || MainClient.game.gameState == Action.GAME_STOPPED) return;

        try {
            MainClient.game.gameState = Action.GAME_STOPPED;
            MainClient.game.clientState = ClientState.NOT_READY;
            MainClient.server.sendMsg(new Msg("", Action.GAME_STOPPED));
        } catch (IOException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
    @FXML
    protected void onShotButtonClick() {
        if (gameView == null ||
                MainClient.game.gameState == Action.GAME_STOPPED ||
                MainClient.game.clientState == ClientState.NOT_READY) return;

        try {
            MainClient.server.sendMsg(new Msg("", Action.SHOT));
        } catch (IOException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onPauseButtonClick() {
        if (gameView == null ||
                MainClient.game.clientState == ClientState.NOT_READY ||
                MainClient.game.gameState == Action.GAME_STOPPED) return;

        try {
            MainClient.game.clientState = ClientState.NOT_READY;
            MainClient.server.sendMsg(new Msg(ClientState.NOT_READY, Action.CLIENT_STATE));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // Перемещение стрелы в место нажатия
    @FXML
    protected void mouseOnClicked(MouseEvent event) {
        arrow.setLayoutY(event.getY());

        try {
            MainClient.server.sendMsg(new Msg(
                    new Arrow(arrow, MainClient.server.getSocket().getLocalPort()),
                    Action.UPDATE_GAME_STATE));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void event(GameModel m) {}
}
