package net.ridhoperdana.siduya;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by RIDHO on 4/17/2016.
 */
public class DatabaseTelpon implements Serializable{

    @SerializedName("notelpon")
    private String notelpon;

//    public List<String> getNotelpon() {
//        return notelpon;
//    }
//
//    public void setNotelpon(List<String> notelpon) {
//        this.notelpon = notelpon;
//    }


    public String getNotelpon() {
        return notelpon;
    }

    public void setNotelpon(String notelpon) {
        this.notelpon = notelpon;
    }
}
