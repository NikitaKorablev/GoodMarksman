package com.example.goodmarksman;

//import com.example.goodmarksman.models.Game;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class MainController implements IObserver {
    GameModel m = Models.buildGM();
    // TODO: Прикрутить объекты с FXML (Arrow, Targets etc.)
    GameClient game;
    int port = 3000;
    InetAddress ip = null;

    @FXML
    Button connectButton;
    @FXML
    TextField inputNameField;
    String playerName = "";

    @FXML
    Pane gameView;
    @FXML
    VBox scoreBord;

    @FXML
    public void initialize() {
        m.addObserver(this);
    }

    @FXML
    void connect() {
        if (game != null) return;

        try {
            playerName = inputNameField.getText();
            if (playerName.isEmpty()) throw new Exception("Player name is empty");

            Socket cs;
            ip = InetAddress.getLocalHost();
            cs = new Socket(ip, port);
            System.out.println("ClientStart \n");

            game = new GameClient(cs);
            // TODO: отправка имени игрока на сервер
            game.sendMsg(new Msg(playerName, MsgAction.CONNECT));
            // TODO: проверка имени на идентичнсть

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("game-view.fxml"));
            Stage stage = (Stage) connectButton.getScene().getWindow();
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.setOnCloseRequest(event -> System.exit(0));
        } catch (Exception e) {
            // TODO: При отключении сервера, клиент уходит в переподключение
//            if (e.getMessage().equals("Connection reset")) System.out.println("FUCK");
            new Alert(Alert.AlertType.INFORMATION, e.getMessage()).show();
            System.out.println(e.getMessage());
        }
    }

    @FXML
    protected void onStartButtonClick() {
        if (game == null) {
            System.err.println("Game is undefined!");
            return;
        }

//        game.startGame();
    }
    @FXML
    protected void onStopButtonClick() {
        if (gameView == null) return;
//        if (game.state == Game.GameState.STOPPED) return;

//        game.stopGame();
    }
    @FXML
    protected void onShotButtonClick() {
//        if (gameView == null || game.state == Game.GameState.STOPPED) return;
//        game.shot();
    }
    @FXML
    protected void onPauseButtonClick() {
        if (gameView == null) return;
//        try {
//            game.setPaused();
//        } catch (InterruptedException e) {
//            System.err.println(e);
//        }
    }

    // Перемещение стрелы в место нажатия
    @FXML
    protected void mouseOnClicked(MouseEvent event) {
        if (game == null) return;

//        game.setArrowY(event.getY());
//        arrow.setLayoutY(event.getY());
    }



//    @FXML
//    void mouseEvent(MouseEvent event) {
//        if (game != null) {
//            ArrayList<Point> allP = new ArrayList<>();
//            allP.add(new Point((int)event.getX(), (int)event.getY()));
//            game.sendMsg(new Msg(allP, MsgAction.ADD));
//        }
//        else {
//            m.addPoint(new Point((int)event.getX(), (int)event.getY()));
//        }
//    }

    @Override
    public void event(GameModel m) {
        // TODO: Ивент выполняющийся в момент каких-либо изменений
        // TODO: (вызов event() происходит в GameModel)

        Platform.runLater(() -> {
//            viewPoints.getChildren().removeAll();
//
//            for (Point p: m) {
//                Circle circle = new Circle(p.getX(), p.getY(), 10);
//                circle.setFill(Color.RED);
//                viewPoints.getChildren().add(circle);
//            }
        });
    }
}
