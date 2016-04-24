package net.ridhoperdana.siduya;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by RIDHO on 4/24/2016.
 */
public class ResultsDetail implements Serializable {

    @SerializedName("address_components")
    private List address_components;

    @SerializedName("adr_address")
    private String adr_address;

    @SerializedName("formatted_address_detail")
    private String formatted_address;

    @SerializedName("geometry_detail")
    private Geometry geometry;

    @SerializedName("icon_detail")
    private String icon;

    @SerializedName("id_detail")
    private String id;

    @SerializedName("name_detail")
    private String name;

    @SerializedName("place_id_detail")
    private String place_id;

    @SerializedName("reference_detail")
    private String reference;

    @SerializedName("scope_detail")
    private String scope;

    @SerializedName("types_detail")
    private List<String> types;

    @SerializedName("url_detail")
    private String url;
}
