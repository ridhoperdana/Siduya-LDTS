package net.ridhoperdana.siduya;

import com.google.gson.annotations.SerializedName;

/**
 * Created by RIDHO on 4/17/2016.
 */
public class DatabaseTelpon {

    @SerializedName("nomortelepon")
    private String notelpon;

    public String getNotelpon() {
        return notelpon;
    }

    public void setNotelpon(String notelpon) {
        this.notelpon = notelpon;
    }
}
