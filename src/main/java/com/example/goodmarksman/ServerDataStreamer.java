package com.example.goodmarksman;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class ServerDataStreamer {
    private DataInputStream dis;
    private DataOutputStream dos;

    ServerDataStreamer(DataInputStream in, DataOutputStream out) {
        this.dis = in;
        this.dos = out;
    }
}
