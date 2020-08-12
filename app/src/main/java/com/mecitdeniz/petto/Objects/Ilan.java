package com.mecitdeniz.petto.Objects;

import com.mecitdeniz.petto.Objects.Hayvan;
import com.mecitdeniz.petto.Objects.Il;
import com.mecitdeniz.petto.Objects.Ilce;
import com.mecitdeniz.petto.Objects.Kullanici;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Ilan implements  Serializable {



    private int id;
    private String baslik;
    private String aciklama;
    private int  durumId;

    private int yayinlayanId;

    private int ilId;

    private int ilceId;

    private Il il;

    private Ilce ilce;

    private Date yayinTarihi;

    private String baslangicTarihi;

    private String bitisTarihi;

    private int tipId;
    public List<Hayvan> hayvanlar = new ArrayList<Hayvan>();

    private Kullanici yayinlayan = new Kullanici();

    public Ilan() {

    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBaslik() {
        return baslik;
    }

    public void setBaslik(String baslik) {
        this.baslik = baslik;
    }

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public List<Hayvan> getHayvanlar() {
        return hayvanlar;
    }

    public void setHayvanlar(List<Hayvan> hayvanlar) {
        this.hayvanlar = hayvanlar;
    }


    public int getDurumId() {
        return durumId;
    }

    public void setDurumId(int durumId) {
        this.durumId = durumId;
    }


    public Kullanici getYayinlayan() {
        return yayinlayan;
    }

    public void setYayinlayan(Kullanici yayinlayan) {
        this.yayinlayan = yayinlayan;
    }

    public int getTipId() {
        return tipId;
    }

    public void setTipId(int tipId) {
        this.tipId = tipId;
    }

    public int getYayinlayanId() {
        return yayinlayanId;
    }

    public void setYayinlayanId(int yayinlayanId) {
        this.yayinlayanId = yayinlayanId;
    }

    public Il getIl() {
        return il;
    }

    public void setIl(Il il) {
        this.il = il;
    }

    public void setIlce(Ilce ilce) {
        this.ilce = ilce;
    }

    public Ilce getIlce() {
        return ilce;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIlId() {
        return ilId;
    }

    public void setIlId(int ilId) {
        this.ilId = ilId;
    }

    public int getIlceId() {
        return ilceId;
    }

    public void setIlceId(int ilceId) {
        this.ilceId = ilceId;
    }

    public Date getYayinTarihi() {
        return yayinTarihi;
    }

    public void setYayinTarihi(Date yayinTarihi) {
        this.yayinTarihi = yayinTarihi;
    }

    public String getBaslangicTarihi() {
        return baslangicTarihi;
    }

    public void setBaslangicTarihi(String baslangicTarihi) {
        this.baslangicTarihi = baslangicTarihi;
    }

    public String getBitisTarihi() {
        return bitisTarihi;
    }

    public void setBitisTarihi(String bitisTarihi) {
        this.bitisTarihi = bitisTarihi;
    }

    @Override
    public String toString() {
        return "Ilan{" +
                "id=" + id +
                ", baslik='" + baslik + '\'' +
                ", aciklama='" + aciklama + '\'' +
                ", durumId=" + durumId +
                ", yayinlayanId=" + yayinlayanId +
                ", ilId=" + ilId +
                ", ilceId=" + ilceId +
                ", il=" + il +
                ", ilce=" + ilce +
                ", yayinTarihi=" + yayinTarihi +
                ", baslangicTarihi=" + baslangicTarihi +
                ", bitisTarihi=" + bitisTarihi +
                ", tipId=" + tipId +
                ", hayvanlar=" + hayvanlar +
                ", yayinlayan=" + yayinlayan +
                '}';
    }
}
