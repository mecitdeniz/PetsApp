package com.mecitdeniz.petto.Objects;

import java.io.Serializable;

public class Sikayet implements Serializable {


    private int id;

    private int sikayetEdenId;

    private int ilanId;

    private  String aciklama;

    private Ilan ilan;

    private Kullanici sikayetEden;

    public Sikayet() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSikayetEdenId() {
        return sikayetEdenId;
    }

    public void setSikayetEdenId(int sikayetEdenId) {
        this.sikayetEdenId = sikayetEdenId;
    }

    public int getIlanId() {
        return ilanId;
    }

    public void setIlanId(int ilanId) {
        this.ilanId = ilanId;
    }

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public Ilan getIlan() {
        return ilan;
    }

    public void setIlan(Ilan ilan) {
        this.ilan = ilan;
    }

    public Kullanici getSikayetEden() {
        return sikayetEden;
    }

    public void setSikayetEden(Kullanici sikayetEden) {
        this.sikayetEden = sikayetEden;
    }

    @Override
    public String toString() {
        return "Sikayet{" +
                "id=" + id +
                ", sikayetEdenId=" + sikayetEdenId +
                ", ilanId=" + ilanId +
                ", aciklama='" + aciklama + '\'' +
                ", ilan=" + ilan +
                ", sikayetEden=" + sikayetEden +
                '}';
    }
}
