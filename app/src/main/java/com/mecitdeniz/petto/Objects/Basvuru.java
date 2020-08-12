package com.mecitdeniz.petto.Objects;

import java.io.Serializable;
import java.util.Date;

public class Basvuru implements Serializable {

    private int id;

    private  int ilanId;

    private  int basvuranId;

    private  String aciklama;

    private Kullanici basvuran;

    private Ilan ilan;

    private Date basvuruTarihi;

    private int durumId;
    public Basvuru() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIlanId() {
        return ilanId;
    }

    public void setIlanId(int ilanId) {
        this.ilanId = ilanId;
    }

    public int getBasvuranId() {
        return basvuranId;
    }

    public void setBasvuranId(int basvuranId) {
        this.basvuranId = basvuranId;
    }

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public Kullanici getBasvuran() {
        return basvuran;
    }

    public void setBasvuran(Kullanici basvuran) {
        this.basvuran = basvuran;
    }

    public Ilan getIlan() {
        return ilan;
    }

    public void setIlan(Ilan ilan) {
        this.ilan = ilan;
    }

    public Date getBasvuruTarihi() {
        return basvuruTarihi;
    }

    public void setBasvuruTarihi(Date yayinTarihi) {
        this.basvuruTarihi = basvuruTarihi;
    }

    public int getDurumId() {
        return durumId;
    }

    public void setDurumId(int durumId) {
        this.durumId = durumId;
    }

    @Override
    public String toString() {
        return "Basvuru{" +
                "id=" + id +
                ", ilanId=" + ilanId +
                ", basvuranId=" + basvuranId +
                ", aciklama='" + aciklama + '\'' +
                ", basvuran=" + basvuran +
                ", ilan=" + ilan +
                ", basvuruTarihi=" + basvuruTarihi +
                ", durumId=" + durumId +
                '}';
    }
}
