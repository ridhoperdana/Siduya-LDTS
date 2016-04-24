package net.ridhoperdana.siduya;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by RIDHO on 4/24/2016.
 */
public class PlaceDetail implements Serializable {

    @SerializedName("html_attributions_detail")
    private List html_attributions;

    @SerializedName("results_detail")
    private List<ResultsDetail> results;

    @SerializedName("status_detail")
    private String status;

    public List getHtml_attributions() {
        return html_attributions;
    }

    public List<ResultsDetail> getResults() {
        return results;
    }

    public String getStatus() {
        return status;
    }
}
