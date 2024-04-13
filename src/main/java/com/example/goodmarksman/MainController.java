package com.example.goodmarksman;

//import com.example.goodmarksman.models.Game;
import com.example.goodmarksman.models.GameModel;
import com.example.goodmarksman.objects.*;
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

import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class MainController implements IObserver {
    private final GameModel m = Models.buildGM();

    private final int port = 3000;
    private InetAddress ip = null;

    private GameClient game = null;
    private Client server = null;
    private String playerName = "";
    private Stage primaryStage;

    @FXML
    private Button connectButton;
    @FXML
    private TextField inputNameField;

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

    private final ArrayList<Text> scoreList = new ArrayList<>();
    private final ArrayList<Text> shotsList = new ArrayList<>();

    public void setPrimaryStage(Stage primaryStage) { this.primaryStage = primaryStage; }
    public void setGame(GameClient game) { this.game = game; }
    public void setInnetAddress(InetAddress ip) { this.ip = ip; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }
    public void setServer(Client server) { this.server = server; }

    @FXML
    public void initialize() {
        m.addObserver(this);
        if (gameView != null && game != null) {
            scoreList.add(score_1);
            scoreList.add(score_2);
            scoreList.add(score_3);
            scoreList.add(score_4);

            shotsList.add(shots_1);
            shotsList.add(shots_2);
            shotsList.add(shots_3);
            shotsList.add(shots_4);

            m.setGameView(gameView);
//            m.setScoreList(scoreList);
//            m.setShotsList(scoreList);
            m.setSmallTarget(smallTarget);
            m.setBigTarget(bigTarget);
            m.setArrow(arrow);
        }
    }

    @FXML
    void connect() {
        if (game != null) return;

        try {
//            System.out.println( == null);
            primaryStage.setOnCloseRequest(event -> System.exit(0));

            playerName = inputNameField.getText();
            if (playerName.isEmpty()) throw new Exception("Player name is empty");

            Socket ss;
            ip = InetAddress.getLocalHost();
            ss = new Socket(ip, port);
            System.out.println("ClientStart \n");


            server = new Client(ss);
            game = new GameClient(server);

            // TODO: отправка имени игрока на сервер
            server.sendMsg(new Msg(playerName, MsgAction.CONNECTED));
            // TODO: проверка имени на идентичнсть

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("game-view.fxml"));

            primaryStage.setScene(new Scene(fxmlLoader.load()));
            primaryStage.show();

            // передача созданный объектов новому контроллеру
            MainController controller = fxmlLoader.getController();
            controller.setPrimaryStage(primaryStage);
            controller.setGame(game);
            controller.setInnetAddress(ip);
            controller.setPlayerName(playerName);
            controller.setServer(server);

            System.out.println(score_1.getText() + " " + shots_1.getText());
        } catch (Exception e) {
            // При отключении сервера, клиент уходит в переподключение
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            System.out.println(e.getMessage());
        }
    }

    @FXML
    protected void onStartButtonClick() {
        if (game == null) {
            new Alert(Alert.AlertType.ERROR, "Game is undefined!").show();
            System.err.println("Game is undefined!");
            return;
        }

        if (game.clientState == ClientState.NOT_READY) {
            try {
                game.clientState = ClientState.READY;
                server.sendMsg(new Msg(game.clientState, MsgAction.CLIENT_STATE));
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
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
