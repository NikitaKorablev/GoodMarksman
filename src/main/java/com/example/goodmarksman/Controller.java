package com.example.goodmarksman;

import com.example.goodmarksman.objects.Game;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;

public class Controller {
    @FXML
    private Pane gameWindow;
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

    private View gameView;
    private Game game;

    @FXML
    protected void onStartButtonClick() {
        if (game == null) {
            gameView = new View(gameWindow, score, shots);
            Model model = new Model();
            game = model.getGame();
            game.init(bigTarget, smallTarget, arrow);
        }

        game.startGame(gameView);
    }
    @FXML
    protected void onStopButtonClick() {
        if (gameView == null) return;
        if (game.state == Game.GameState.STOPPED) return;

        game.stopGame(gameView);
    }
    @FXML
    protected void onShotButtonClick() {
        if (gameView == null) return;
        game.shot(gameView);
    }
    @FXML
    protected void onPauseButtonClick() {
        if (gameView == null) return;
        try {
            game.setPaused();
        } catch (InterruptedException e) {
            System.err.println(e);
        }
    }
}
