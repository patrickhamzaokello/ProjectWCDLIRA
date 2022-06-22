
package com.pkasemer.worshipcenterdowntown.Models;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class SliderBanner {

    @SerializedName("sliderid")
    @Expose
    private Integer sliderid;
    @SerializedName("serialid")
    @Expose
    private Integer serialid;
    @SerializedName("filename")
    @Expose
    private String filename;
    @SerializedName("cdate")
    @Expose
    private String cdate;

    public Integer getSliderid() {
        return sliderid;
    }

    public void setSliderid(Integer sliderid) {
        this.sliderid = sliderid;
    }

    public Integer getSerialid() {
        return serialid;
    }

    public void setSerialid(Integer serialid) {
        this.serialid = serialid;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getCdate() {
        return cdate;
    }

    public void setCdate(String cdate) {
        this.cdate = cdate;
    }

}
