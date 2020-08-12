package com.mecitdeniz.petto.Objects;

import java.io.Serializable;
import java.util.Date;

public class Mesaj implements Serializable {

    private int id;
    private int basvuruId;
    private int gonderenId;
    private int alanId;
    private String mesaj;

    private Date gonderilmeTarihi;

    public Mesaj() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBasvuruId() {
        return basvuruId;
    }

    public void setBasvuruId(int basvuruId) {
        this.basvuruId = basvuruId;
    }

    public int getGonderenId() {
        return gonderenId;
    }

    public void setGonderenId(int gonderenId) {
        this.gonderenId = gonderenId;
    }

    public int getAlanId() {
        return alanId;
    }

    public void setAlanId(int alanId) {
        this.alanId = alanId;
    }

    public String getMesaj() {
        return mesaj;
    }

    public void setMesaj(String mesaj) {
        this.mesaj = mesaj;
    }

    public Date getGonderilmeTarihi() {
        return gonderilmeTarihi;
    }

    public void setGonderilmeTarihi(Date gonderilmeTarihi) {
        this.gonderilmeTarihi = gonderilmeTarihi;
    }

    @Override
    public String toString() {
        return "Mesaj{" +
                "id=" + id +
                ", basvuruId=" + basvuruId +
                ", gonderenId=" + gonderenId +
                ", alanId=" + alanId +
                ", mesaj='" + mesaj + '\'' +
                ", gonderilmeTarihi=" + gonderilmeTarihi +
                '}';
    }
}
