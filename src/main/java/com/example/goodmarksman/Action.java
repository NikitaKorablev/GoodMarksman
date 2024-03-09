package com.example.goodmarksman;

import java.io.Serializable;

public class Action implements Serializable {
    TypeAction ta;
    String msg = "";

    public Action(TypeAction ta, String msg) {
        this.ta = ta;
        this.msg = msg;
    }

    public TypeAction getTa() {
        return ta;
    }

    public void setTa(TypeAction ta) {
        this.ta = ta;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "Action{" +
                "ta=" + ta +
                ", msg='" + msg + '\'' +
                '}';
    }
}
