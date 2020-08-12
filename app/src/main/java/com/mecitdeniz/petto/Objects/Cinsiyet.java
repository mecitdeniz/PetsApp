package com.mecitdeniz.petto.Objects;

import java.io.Serializable;

public class Cinsiyet implements Serializable {

    private int id;
    private String adi;

    public Cinsiyet() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdi() {
        return adi;
    }

    public void setAdi(String adi) {
        this.adi = adi;
    }

    @Override
    public String toString() {
        return "Cinsiyet{" +
                "id=" + id +
                ", adi='" + adi + '\'' +
                '}';
    }
}
