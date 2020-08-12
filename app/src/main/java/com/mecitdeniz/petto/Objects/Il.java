package com.mecitdeniz.petto.Objects;

import java.io.Serializable;
import java.util.List;

public class Il implements Serializable {

    private int id;

    private String ad;

    private List<Ilce> ilceler;

    public Il() {
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

    public List<Ilce> getIlceler() {
        return ilceler;
    }

    public void setIlceler(List<Ilce> ilceler) {
        this.ilceler = ilceler;
    }

    @Override
    public String toString() {
        return ad;
    }
}
