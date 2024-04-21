package com.example.goodmarksman;

//import com.example.goodmarksman.models.Game;
import com.example.goodmarksman.objects.*;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

//TODO: Будет отвечать за ведение счета всех игроков
// и хранение игровых объектов
public class DAO implements Iterable<Score> {
    private Pane gameView;
    private Circle smallTarget = null;
    private Circle bigTarget = null;
    private Polygon arrow;

    private final ArrayList<Polygon> arrows = new ArrayList<>();

    private ArrayList<Text> scoreList = new ArrayList<>();
    private ArrayList<Text> shotsList  = new ArrayList<>();
    private ArrayList<HBox> statisticBoxes = new ArrayList<>();

    private final ClientsDataArray clientsData = new ClientsDataArray();
    private final ArrayList<Client> players = new ArrayList<>();

    public int playersSize() {
        return players.size();
    }

    public void addClient(Client cl, Arrow arrow, Score score) throws Exception {
        if (players.contains(cl) || clientsData.getIndex(cl.getSocket().getPort()) != -1) {
            throw new Exception("Player is already added.");
        }

        players.add(cl);
        clientsData.add("", cl.getSocket().getPort(),
                arrow, score).setPlayerName(cl.getName());
    }

//    public ClientsDataArray getArray() { return this.clientsData; }

    public void sendMsg(Msg msg) throws IOException {
        if (msg.portOwner == -1) throw new IOException("Port owner not set.");
        Client client = players.get(playerIndex(msg.portOwner));
        client.sendMsg(msg);
    }

    public Client getClient(int ind) {
        if (ind == -1 && !players.isEmpty()) { return players.get(players.size() - 1); }
        return players.get(ind);
    }

    public void setClientName(Socket s, String name) {
        clientsData.setClientName(s.getPort(), name);
    }

    public void removeClient(Client cl) {
        clientsData.remove(cl.getSocket().getPort());
        players.remove(cl);
    }

    public int playerIndex(int port) {
        return clientsData.getIndex(port);
    }

    public ClientData getClientData(int port) {
        return clientsData.getData(port);
    }

    public ClientsDataArray getClientsData() {
        return clientsData;
    }

    public void setClientsData(ClientsDataArray clientsData) {
        this.clientsData.setClientsData(clientsData);
    }

    public Pane getGameView() { return gameView; }
    public void setGameView(Pane gameView) { this.gameView = gameView; }

    public Circle getSmallTarget() { return smallTarget; }
    public void setSmallTarget(Circle smallTarget) { this.smallTarget = smallTarget; }

    public Circle getBigTarget() { return bigTarget; }
    public void setBigTarget(Circle bigTarget) { this.bigTarget = bigTarget; }

    public Polygon getArrow() { return arrow; }
    public void setArrow(Polygon arrow) { this.arrow = arrow; }

    public ArrayList<Polygon> getArrows() {
        ArrayList<Polygon> a = new ArrayList<>();
        if (arrow != null) a.add(this.arrow);
        if (!arrows.isEmpty()) a.addAll(getArrows());

        return a;
    }

    public ArrayList<Text> getScoreList() { return scoreList; }
    public void setScoreList(ArrayList<Text> scoreList) { this.scoreList = scoreList; }

    public ArrayList<Text> getShotsList() { return shotsList; }
    public void setShotsList(ArrayList<Text> shotsList) { this.shotsList = shotsList; }

    public ArrayList<HBox> getStatisticBoxes() { return statisticBoxes; }
    public void setStatisticBoxes(ArrayList<HBox> statisticBoxes) {
        System.err.println("DAO: " + statisticBoxes);
        this.statisticBoxes = statisticBoxes;
        System.err.println("DAO: " + this.statisticBoxes);
    }

    public Polygon getEnemyArrow(COLORS color) {
        if (arrows.isEmpty()) return null;

        for (Polygon arrow : arrows) {
            if (arrow.getFill().equals(color.getValue())) return arrow;
        }

        return null;
    }

//    public void moveArrow(Arrow arrow) {
//        double newX = arrow.getX() + arrow.getSpeed();
//        arrow.setX(newX);
//
//    }

    public void updateTargets(ArrayList<Target> targets) {
        Platform.runLater(() -> {
            Target target1 = targets.get(0);
            Target target2 = targets.get(1);
            if (target1.getRadius() > target2.getRadius()) {
                Target tmp = target1;
                target1 = target2;
                target2 = tmp;
            }

            if (smallTarget == null || bigTarget == null) {
                smallTarget = target1.getCircle();
                bigTarget = target2.getCircle();

                gameView.getChildren().add(smallTarget);
                gameView.getChildren().add(bigTarget);
            } else {
                smallTarget.setLayoutY(target1.getY());
                smallTarget.setFill(target1.getCurrentColor().getValue());

                bigTarget.setLayoutY(target2.getY());
                bigTarget.setFill(target2.getCurrentColor().getValue());
            }
        });
    }

    public void updateArrow(Arrow arrow) {
        if (arrow.getOwnerPort() == -1) { throw new NullPointerException(); }
//        System.err.println("Server port: " + MainClient.game.getServerPort());
        if (arrow.getOwnerPort() == MainClient.game.getServerPort()) {
//            System.err.println("My color is: " + arrow.getColorName());
            Platform.runLater(() -> {
                this.arrow.setLayoutX(arrow.getX());
//                this.arrow.setLayoutY(arrow.getY());
//                System.err.println(this.arrow);

                try {
                    this.arrow.setFill(arrow.getColorName().getValue());
                } catch (Error e) {
                    System.err.println(e.getMessage());
                }
            });
        } else {
//            System.err.println("Enemy color is: " + arrow.getColorName());
            Platform.runLater(() -> {
                Polygon poly = getEnemyArrow(arrow.getColorName());
                if (poly == null) {
                    poly = arrow.getPolygon();
                    this.gameView.getChildren().add(poly);
                }
                arrows.add(poly);
//                System.err.println(poly);

                poly.setLayoutX(arrow.getX());
                poly.setLayoutY(arrow.getY());
                try {
                    poly.setFill(arrow.getColorName().getValue());
                } catch (Error e) {
                    System.err.println(e.getMessage());
                }
            });
        }
    }

//    public HBox getEnemyArrow(int ) {
//        if (arrows.isEmpty()) return null;
//
//        for (Polygon arrow : arrows) {
//            if (arrow.getFill().equals(color.getValue())) return arrow;
//        }
//
//        return null;
//    }

    public void updateScore(Score score, COLORS arrowColor) {
//        System.err.println("score: " + score);
//        System.err.println("score: " + statisticBoxes);

        if (score.getOwnerPort() == -1) { throw new NullPointerException(); }
//        System.err.println("Server port: " + MainClient.game.getServerPort());
        if (score.getOwnerPort() == MainClient.game.getServerPort()) {
            System.err.println("children: " + statisticBoxes.get(0).getChildren());
            Platform.runLater(() -> {
                for (Node node: statisticBoxes.get(0).getChildren()) {
                    if (node instanceof Circle circle) {
                        circle.setFill(arrowColor.getValue());
                    } else if (node instanceof Label label) {
                        label.setText(score.getPlayerName());
                    }
                }

                this.scoreList.get(0).setText(String.valueOf(score.getScore()));
                this.shotsList.get(0).setText(String.valueOf(score.getShotCount()));
            });
        } else {
            System.err.println("score: " + score);
            Platform.runLater(() -> {
                int i = 0;
                for (HBox box: statisticBoxes) {
                    if (i == 0)  {
                        ++i;
                        continue;
                    }

                    Circle circle = null;
                    Label label = null;
                    for (Node node: box.getChildren()) {
                        if (node instanceof Circle)
                            circle = (Circle) node;
                        else if (node instanceof Label)
                            label = (Label) node;
                    }

                    if (circle != null && label != null) {
                        if (label.getText().equals(score.getPlayerName())) {
                            this.scoreList.get(i).setText(String.valueOf(score.getScore()));
                            this.shotsList.get(i).setText(String.valueOf(score.getShotCount()));

                            return;
                        } else if (label.getText().isEmpty()) {
                            circle.setFill(arrowColor.getValue());
                            label.setText(score.getPlayerName());

                            this.scoreList.get(i).setText(String.valueOf(score.getScore()));
                            this.shotsList.get(i).setText(String.valueOf(score.getShotCount()));

                            return;
                        }
                    }

                    ++i;
                }

            });
        }

    }

    //    ArrayList<Score> scoreBoard = new ArrayList<>();

//    public Game getGame(Circle target1, Circle target2, Polygon arrow) {
////        return new Game(target1, target2, arrow);
//    }

//    public ArrayList<Score> getScoreBoard() {
//        return scoreBoard;
//    }

//    public void set(ArrayList<Score> scoreBoard) {
//        this.scoreBoard = scoreBoard;
//    }

//    public void add(Score score) {
//        this.scoreBoard.add(score);
//    }

//    public void remove(Socket cs) {
//        this.scoreBoard.removeIf(s -> s.getSocket() == cs);
//    }

    @Override
    public Iterator<Score> iterator() {
//        return scoreBoard.iterator();
        return null;
    }

    @Override
    public void forEach(Consumer<? super Score> action) {
        Iterable.super.forEach(action);
    }

    @Override
    public Spliterator<Score> spliterator() {
        return Iterable.super.spliterator();
    }

    //    ArrayList<Point> allPoint = new ArrayList<>();
//
//    void set(ArrayList<Point> allPoint) {
//        this.allPoint = allPoint;
//    }
//
//    public ArrayList<Point> getPoints() {
//        return allPoint;
//    }
//
//    void add(Point p) {
//        allPoint.add(p);
//    }
//
//    void remove(Point p) {
//        allPoint.remove(p);
//    }
//
//    @Override
//    public Iterator<Point> iterator() {
//        return allPoint.iterator();
//    }
//
//    @Override
//    public void forEach(Consumer<? super Point> action) {
//        Iterable.super.forEach(action);
//    }
//
//    @Override
//    public Spliterator<Point> spliterator() {
//        return Iterable.super.spliterator();
//    }
}
