package net.ridhoperdana.siduya;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by RIDHO on 4/23/2016.
 */
public class Address implements Serializable{

    @SerializedName("predictions")
    private List<Predictions> predictions;

    public List<Predictions> getPredictions() {
        return predictions;
    }

    public void setPredictions(List<Predictions> predictions) {
        this.predictions = predictions;
    }
}
