package com.mecitdeniz.petto;

import com.mecitdeniz.petto.Objects.Basvuru;
import com.mecitdeniz.petto.Objects.Hayvan;
import com.mecitdeniz.petto.Objects.Il;
import com.mecitdeniz.petto.Objects.Ilan;
import com.mecitdeniz.petto.Objects.Kullanici;
import com.mecitdeniz.petto.Objects.Mesaj;
import com.mecitdeniz.petto.Objects.Sikayet;
import com.mecitdeniz.petto.Objects.Tur;
import com.mecitdeniz.petto.Objects.Yorum;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface PetsApi {

    @GET("hayvanlar")
    Call<List<Hayvan>> getHayvanlar();

    @GET("ilanlar")
    Call<List<Ilan>> ilanlariGetir();

    @POST("/ilanlar")
    Call<Integer> ilanOlustur(@Body Ilan ilan);

    @GET("/ilanlar/bekleyen")
    Call<List<Ilan>> onayBekleyenIlanlariGetir();

    @DELETE("/{kullaniciId}/ilanlar/{ilanId}")
    Call<Integer> ilanSil(@Path("kullaniciId") int kullaniciAdi,@Path("ilanId") int ilanId);

    @GET("ilanlar/{ilanID}")
    Call<Ilan> idIleIlanGetir(@Path("ilanID") int ilanID);

    @GET("/ilanlar/{ilanId}/onayla/{editorId}")
    Call<Integer> ilanOnayla(@Path("ilanId") int ilanId,@Path("editorId") int editorId);

    @GET("/ilanlar/{ilanId}/reddet/{editorId}")
    Call<Integer> ilanReddet(@Path("ilanId") int ilanId,@Path("editorId") int editorId);

    @GET("/ilanlar/{ilanId}/yayindanKaldir/{editorId}")
    Call<Integer> ilanYayindanKaldir(@Path("ilanId") int ilanId,@Path("editorId") int editorId);

    @GET("/{kullaniciId}/ilanlar")
    Call<List<Ilan>> kullaniciIdIleIlanlariGetir(@Path("kullaniciId") int kullaniciId);


    @GET("/kullanicilar/{kullaniciID}")
    Call<Kullanici> idIlekullaniciGetir(@Path("kullaniciID") int kullaniciID);

    @GET("/kullanicilar/{kullaniciID}/hayvanlar")
    Call<List<Hayvan>> kullaniciIdileHayvanlariGetir(@Path("kullaniciID") int kullaniID);


    @POST("/giris")
    @FormUrlEncoded
    Call<Kullanici> girisYap(@Field("email") String email, @Field("sifre") String sifre);

    @POST("/kaydol")
    Call<Integer> kaydol(@Body Kullanici kullanici);

    @POST("/basvurular")
    Call<Integer> bavuruYap(@Body Basvuru basvuru);

    @GET("/kullanicilar/{kullaniciId}/basvurular")
    Call<List<Basvuru>> kullaniciIdIleBasvurulariGetir(@Path("kullaniciId") int kullaniciId);

    @GET("/kullanicilar")
    Call<List<Kullanici>> kullanicilariGetir();

    @POST("/sikayetler")
    Call<Integer> sikayetOlustur(@Body Sikayet sikayet);

    @GET("/sikayetler")
    Call<List<Sikayet>> sikayetleriGetir();

    @GET("/ilanlar/{ilanId}/sikayetler")
    Call<List<Sikayet>> ilanIdIleSikayetleriGetir(@Path("ilanId") int ilanId);

    @GET("/iller")
    Call<List<Il>> illeriGetir();


    @GET("/ilanlar/{ilanId}/basvurular")
    Call<List<Basvuru>> ilanIdIleBasvurulariGetir(@Path("ilanId") int ilanId);

    @GET("/ilanlar/{ilanId}/basvuru")
    Call<Basvuru> ilanIdIleBasvuruGetir(@Path("ilanId") int ilanId);

    @GET("/mesajlar/{basvuruId}")
    Call<List<Mesaj>> basvuruIdIleMesajlariGetir(@Path("basvuruId") int basvuruId);

    @POST("/mesajlar")
    Call<Integer> mesajGonder(@Body Mesaj mesaj);

    @PUT("/admin/{adminId}/kullaniciRolGuncelle/{kullaniciId}")
    Call<Integer> kullaniciRolGuncelle(@Path("adminId") int adminId, @Path("kullaniciId") int kullaniciId);

    @Multipart
    @POST("/upload")
    Call<Integer> resimYukle(@Part MultipartBody.Part part);

    @POST("/hayvanlar")
    Call<Integer> hayvanEkle(@Body Hayvan hayvan);

    @DELETE("/{kullaniciId}/hayvanlar/{hayvanId}")
    Call<Integer> hayvanSil(@Path("kullaniciId") int kullaniciId ,@Path("hayvanId") int hayvanId);

    @DELETE("/kullanicilar/{kullaniciId}/basvurular/{basvuruId}")
    Call<Integer> basvuruSil(@Path("kullaniciId") int kullaniciId ,@Path("basvuruId") int basvuruId);

    @GET("/basvurular/{basvuruId}/reddet/{kullaniciId}")
    Call<Integer> basvuruReddet(@Path("basvuruId") int basvuruId ,@Path("kullaniciId") int kullaniciId);

    @GET("/basvurular/{basvuruId}/onayla/{kullaniciId}")
    Call<Integer> basvuruOnayla(@Path("basvuruId") int basvuruId ,@Path("kullaniciId") int kullaniciId);

    @PUT("/kullanicilar/{kullaniciId}/ilanlar/{ilanId}")
    Call<Integer> ilanDuzenle(@Body Ilan ilan, @Path("kullaniciId") int kullaniciId, @Path("ilanId") int ilanId);

    @GET("/turler")
    Call<List<Tur>> turleriGetir();

    @POST("/yorumlar")
    Call<Integer> yorumEkle(@Body Yorum yorum);

    @GET("/yorumlar/{kullaniciId}")
    Call<List<Yorum>> kullaniciIdIleYorumlariGetir(@Path("kullaniciId") int kullaniciId);

    @GET("/yorumlar/{kullaniciId}/ortalama")
    Call<Float> ortalamaOyGetir(@Path("kullaniciId") int kullaniciId);

    @PUT("/kullanicilar/{kullaniciId}")
    Call<Integer> kullaniciGuncelle(@Body Kullanici kullanici,@Path("kullaniciId") int kullaniciId);

    @PUT("/{kullaniciId}/hayvanlar/{hayvanId}")
    Call<Integer> hayvanGuncelle(@Path("kullaniciId") int kullaniciId, @Path("hayvanId") int hayvanId,@Body Hayvan hayvan);

    @GET("/kullanicilar/{email}/denetle")
    Call<Kullanici> emailKayitliMi(@Path("email") String email);

    @PUT("/kullanicilar/{kullaniciId}/sifreYenile")
    Call<Integer> sifreYenile(@Path("kullaniciId") int kullaniciId, @Body Kullanici kullanici);
}
