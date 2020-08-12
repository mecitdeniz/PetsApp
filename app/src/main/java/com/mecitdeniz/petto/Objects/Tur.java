package com.mecitdeniz.petto.Objects;

import java.io.Serializable;
import java.util.List;

public class Tur implements Serializable {

    private int id;
    private String ad;

    private List<Cins> cinsler;

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

    public List<Cins> getCinsler() {
        return cinsler;
    }

    public void setCinsler(List<Cins> cinsler) {
        this.cinsler = cinsler;
    }

    @Override
    public String toString() {
        return ad;
    }
}

