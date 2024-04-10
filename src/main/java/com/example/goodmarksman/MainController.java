package com.example.goodmarksman;

//import com.example.goodmarksman.models.Game;
import com.example.goodmarksman.objects.Client;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class MainController implements IObserver {
    GameModel m = Models.buildGM();
    // TODO: Прикрутить объекты с FXML (Arrow, Targets etc.)
    private GameClient game;
    private int port = 3000;
    private InetAddress ip = null;

    @FXML
    private Button connectButton;
    @FXML
    private TextField inputNameField;
    private String playerName = "";

    @FXML
    private Pane gameView;
//    @FXML
//    VBox scoreBord;
    @FXML
    private Circle bigTarget;
    @FXML
    private Circle smallTarget;
    @FXML
    private Polygon arrow;
    @FXML
    private Text shots;
    @FXML
    private Text score;


    @FXML
    public void initialize() {
        m.addObserver(this);
    }

    @FXML
    void connect() {
        if (game != null) return;

        try {
            Stage stage = (Stage) connectButton.getScene().getWindow();
            stage.setOnCloseRequest(event -> System.exit(0));

            playerName = inputNameField.getText();
            if (playerName.isEmpty()) throw new Exception("Player name is empty");

            Socket ss;
            ip = InetAddress.getLocalHost();
            ss = new Socket(ip, port);
            System.out.println("ClientStart \n");
            game = new GameClient(ss);

            // TODO: отправка имени игрока на сервер
            game.sendMsg(new Msg(playerName, MsgAction.CONNECTED));
            // TODO: проверка имени на идентичнсть

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("game-view.fxml"));
            stage.setScene(new Scene(fxmlLoader.load()));
        } catch (Exception e) {
            // TODO: При отключении сервера, клиент уходит в переподключение
//            if (e.getMessage().equals("Connection reset")) System.out.println("FUCK");
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
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
        if (gameView == null) return;
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
        arrow.setLayoutY(event.getY());

        // TODO: Отправить на сервер новые координаты стрелы
    }

//    @FXML
//    void mouseEvent(MouseEvent event) {
//        if (game == null) return;
//        ArrayList<Point> allP = new ArrayList<>();
//        allP.add(new Point((int)event.getX(), (int)event.getY()));
//        game.sendMsg(new Msg(allP, MsgAction.ADD));
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
