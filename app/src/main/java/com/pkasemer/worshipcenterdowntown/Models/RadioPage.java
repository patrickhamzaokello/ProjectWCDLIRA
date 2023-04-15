
package com.pkasemer.worshipcenterdowntown.Models;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class RadioPage {

    @SerializedName("Radio")
    @Expose
    private List<Radio> radio;
    @SerializedName("heading")
    @Expose
    private String heading;
    @SerializedName("label")
    @Expose
    private String label;

    public List<Radio> getRadio() {
        return radio;
    }

    public void setRadio(List<Radio> radio) {
        this.radio = radio;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

}
