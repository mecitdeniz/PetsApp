package com.mecitdeniz.petto.Objects;

import java.io.Serializable;

public class Rol implements Serializable {

    private int id;

    private String ad;

    public Rol() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    @Override
    public String toString() {
        return "Rol{" +
                "id=" + id +
                ", ad='" + ad + '\'' +
                '}';
    }
}
