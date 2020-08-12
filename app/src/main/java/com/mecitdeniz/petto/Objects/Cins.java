package com.mecitdeniz.petto.Objects;

import java.io.Serializable;

public class Cins implements Serializable {

    private int id;
    private String ad;
    private int turId;

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

    public int getTurId() {
        return turId;
    }

    public void setTurId(int turId) {
        this.turId = turId;
    }

    @Override
    public String toString() {
        return ad;
    }
}
