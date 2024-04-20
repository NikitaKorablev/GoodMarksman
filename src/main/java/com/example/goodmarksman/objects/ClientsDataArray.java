package com.example.goodmarksman.objects;

import javafx.scene.shape.Polygon;

import java.util.ArrayList;

public class ClientsDataArray {
    private ArrayList<ClientData> clientsData = new ArrayList<>();
    private final ArrayList<COLORS> freeColors = new ArrayList<>();
    private final ArrayList<COLORS> busyColors = new ArrayList<>();

    public ClientsDataArray() {
        freeColors.add(COLORS.DARK_BLUE);
        freeColors.add(COLORS.ORANGE);
        freeColors.add(COLORS.BLACK);
        freeColors.add(COLORS.PURPLE);
    }
    public ClientsDataArray(ClientData data) {
        clientsData.add(data);
    }
    public ClientsDataArray(String playerName, int socketPort, Arrow arrow, Score score) {
        clientsData.add(new ClientData(playerName, socketPort, arrow, score));
    }
    public ClientsDataArray(ArrayList<ClientData> data) {
        clientsData.addAll(data);
    }

    public void clearAllData() {
        clientsData.clear();
    }

    public void updateArrow(Arrow arrow) {
        try {
            getData(arrow.getOwnerPort()).updateArrow(arrow);
        } catch (Exception e) {
            System.err.println("Update arrow failed: " + e.getMessage());
        }
    }

//    public void add(Data data) { clientsData.add(data); }
    public ClientData add(String playerName, int socketPort, Arrow arrow, Score score) {
        arrow.setColor(freeColors.get(0));
        busyColors.add(freeColors.get(0));
        freeColors.remove(0);

        ClientData data = new ClientData(playerName, socketPort, arrow, score);
        clientsData.add(data);

        return data;
    }

    public ClientData getData(int socketPort) {
        System.err.println(socketPort);
        System.err.println(clientsData);
        for (ClientData data : clientsData) {
            if (data.getPlayerPort() == socketPort) {
                return data;
            }
        }
        return null;
    }

    public int getIndex(int socketPort) {
//        System.out.println(clientsData);
        //TODO: Приходит неверный сокет
        int i = 0;
        for (ClientData data : clientsData) {
            if (data.getPlayerPort() == socketPort) { return i; }
            i++;
        }
        return -1;
    }

    public void remove(int socketPort) {
        try {
            ClientData data = getData(socketPort);
            int colorInd = busyColors.indexOf(data.getArrow().getColorName());
            freeColors.add(busyColors.get(colorInd));
            busyColors.remove(colorInd);

            clientsData.remove(data);
        } catch (Exception e) {
            System.err.println("Remove client Error: " + e.getMessage());
        }
    }

    public ArrayList<ClientData> getClientsData() { return clientsData; }
    public void setClientsData(ArrayList<ClientData> clientsData) {
        System.out.println("ClientsData: " + clientsData);
        this.clientsData.clear();
        this.clientsData = clientsData;
    }

    public void setClientName(int port, String name) {
        getData(port).setPlayerName(name);
    }

    @Override
    public String toString() {
        return "ClientsData{" +
                "clientsData=" + clientsData +
                ", freeColors=" + freeColors +
                ", busyColors=" + busyColors +
                '}';
    }
}
