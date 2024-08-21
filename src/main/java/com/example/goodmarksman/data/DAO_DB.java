package com.example.goodmarksman.data;

import com.example.goodmarksman.HibernateSessionFactoryUtil;
import com.example.goodmarksman.objects.Score;
import org.hibernate.Session;

import java.sql.*;
import java.util.ArrayList;

public class DAO_DB extends DAO {

    Connection c;

    public DAO_DB() {
        connect();
    }

    public ArrayList<Score> getScoreBord(ArrayList<String> playerNames) {
        return getSqlQuery(playerNames);
    }
    public ArrayList<Score> getScoreBord(String name) {
        if (name.isEmpty()) return null;
        if (name.equals("*")) return getSqlQuery(null);

        ArrayList<String> names = new ArrayList<>();
        names.add(name);

        return getSqlQuery(names);
    }

    public void insertScore(Score score) throws Exception {
        try {
            Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();

            ArrayList<Score> scores = getScoreBord(score.getPlayerName());
            if (scores.size() > 1) {
                session.close();
                throw new Exception("Find to many players with name " + score.getPlayerName());
            }

            session.beginTransaction();
            if (!scores.isEmpty() && scores.get(0).getShotCountValue() > score.getShotCountValue()) {
                score.setId(scores.get(0).getId());
                session.update(score);
            } else if (scores.isEmpty()) session.persist(score);

            session.getTransaction().commit();
            session.close();

        } catch (Exception e) {
            System.err.println("Insert score err: " + e.getMessage());
            throw e;
        }
    }

    private void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(
                    "jdbc:sqlite:players_score.db");
            System.out.println("Opened database successfully");
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC not found: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("DB connection err: " + e.getMessage());
        }
    }

    private ArrayList<Score> getSqlQuery(ArrayList<String> playerNames) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();

        ArrayList<Score> scores;
        if (playerNames == null) {
            scores = (ArrayList<Score>) session
                    .createQuery("FROM Score", Score.class).list();
        } else {
            scores = (ArrayList<Score>) session
                    .createQuery("FROM Score WHERE playerName IN :playerNames", Score.class)
                    .setParameterList("playerNames", playerNames).list();
        }

        System.out.println(scores);

        session.close();
        return scores;
    }
}
