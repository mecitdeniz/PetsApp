package com.mecitdeniz.petto.Objects;

import java.io.Serializable;

public class Kullanici implements Serializable {


    private int id;

    private String ad;

    private String soyAd;

    private  String profilResmi;

    private  String kullaniciAdi;

    private  String cinsiyetId;

    private  String email;

    private String sifre;

    private Rol rol;

    private int rolId;

    private String bio;

    public Kullanici() {

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

    public String getSoyAd() {
        return soyAd;
    }

    public void setSoyAd(String soyAd) {
        this.soyAd = soyAd;
    }

    public String getProfilResmi() {
        return profilResmi;
    }

    public void setProfilResmi(String profilResmi) {
        this.profilResmi = profilResmi;
    }

    public String getKullaniciAdi() {
        return kullaniciAdi;
    }

    public void setKullaniciAdi(String kullaniciAdi) {
        this.kullaniciAdi = kullaniciAdi;
    }

    public String getCinsiyetId() {
        return cinsiyetId;
    }

    public void setCinsiyetId(String cinsiyetId) {
        this.cinsiyetId = cinsiyetId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSifre() {
        return sifre;
    }

    public void setSifre(String sifre) {
        this.sifre = sifre;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public int getRolId() {
        return rolId;
    }

    public void setRolId(int rolId) {
        this.rolId = rolId;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    @Override
    public String toString() {
        return "Kullanici{" +
                "id=" + id +
                ", ad='" + ad + '\'' +
                ", soyAd='" + soyAd + '\'' +
                ", profilResmi='" + profilResmi + '\'' +
                ", kullaniciAdi='" + kullaniciAdi + '\'' +
                ", cinsiyetId='" + cinsiyetId + '\'' +
                ", email='" + email + '\'' +
                ", sifre='" + sifre + '\'' +
                ", rol=" + rol +
                ", rolId=" + rolId +
                ", bio='" + bio + '\'' +
                '}';
    }
}
