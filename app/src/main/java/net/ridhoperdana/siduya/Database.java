package net.ridhoperdana.siduya;

import com.google.gson.annotations.SerializedName;

/**
 * Created by RIDHO on 4/17/2016.
 */
public class Database {

    @SerializedName("id")
    private String id;

    @SerializedName("kategori")
    private String kategori;

    @SerializedName("namalayanan")
    private String namalayanan;

    @SerializedName("nomortelpon")
    private String notelpon;

    @SerializedName("latitude")
    private String lat;

    @SerializedName("longitude")
    private String longitude;

    @SerializedName("distance")
    private String distance;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getNamalayanan() {
        return namalayanan;
    }

    public void setNamalayanan(String namalayanan) {
        this.namalayanan = namalayanan;
    }

    public String getNotelpon() {
        return notelpon;
    }

    public void setNotelpon(String notelpon) {
        this.notelpon = notelpon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
