
package com.pkasemer.worshipcenterdowntown.Models;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class Notice {

    @SerializedName("noticeid")
    @Expose
    private Integer noticeid;
    @SerializedName("noticetitle")
    @Expose
    private String noticetitle;
    @SerializedName("noticedescription")
    @Expose
    private String noticedescription;
    @SerializedName("cdate")
    @Expose
    private String cdate;
    @SerializedName("created_date")
    @Expose
    private String createdDate;

    public Integer getNoticeid() {
        return noticeid;
    }

    public void setNoticeid(Integer noticeid) {
        this.noticeid = noticeid;
    }

    public String getNoticetitle() {
        return noticetitle;
    }

    public void setNoticetitle(String noticetitle) {
        this.noticetitle = noticetitle;
    }

    public String getNoticedescription() {
        return noticedescription;
    }

    public void setNoticedescription(String noticedescription) {
        this.noticedescription = noticedescription;
    }

    public String getCdate() {
        return cdate;
    }

    public void setCdate(String cdate) {
        this.cdate = cdate;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

}
