package com.example.goodmarksman.objects;

import java.util.ArrayList;

public class ClientsData {
    private final ArrayList<Data> clientsData = new ArrayList<>();

    public ClientsData() {}
    public ClientsData(Data data) {
        clientsData.add(data);
    }
    public ClientsData(String playerName, int socketPort, Arrow arrow, Score score) {
        clientsData.add(new Data(playerName, socketPort, arrow, score));
    }

    public void add(Data data) { clientsData.add(data); }
    public void add(String playerName, int socketPort, Arrow arrow, Score score) {
        clientsData.add(new Data(playerName, socketPort, arrow, score));
    }

    public Data getData(int socketPort) {
        for (Data data : clientsData) {
            if (data.getPlayerPort() == socketPort) { return data; }
        }
        return null;
    }

    public void remove(int socketPort) {
        this.clientsData.remove(this.getData(socketPort));
    }

    public ArrayList<Data> getClientsData() { return clientsData; }

    @Override
    public String toString() {
        return "ClientsData{" +
                "clientsData=" + clientsData +
                '}';
    }
}
