package com.mecitdeniz.petto.Objects;

import java.io.Serializable;

public class Ilce implements Serializable {

    private int id;
    private String ad;

    public Ilce() {

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
        return ad;
    }
}
