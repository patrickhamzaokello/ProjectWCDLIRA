
package com.pkasemer.worshipcenterdowntown.Models;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class HomeSermon {

    @SerializedName("sermonid")
    @Expose
    private Integer sermonid;
    @SerializedName("sermonbanner")
    @Expose
    private String sermonbanner;
    @SerializedName("sermontitle")
    @Expose
    private String sermontitle;
    @SerializedName("sermondate")
    @Expose
    private String sermondate;
    @SerializedName("sermontime")
    @Expose
    private String sermontime;
    @SerializedName("sermonlocation")
    @Expose
    private String sermonlocation;
    @SerializedName("sermonauthor")
    @Expose
    private String sermonauthor;
    @SerializedName("sermonyoutube")
    @Expose
    private String sermonyoutube;
    @SerializedName("sermonsoundcloud")
    @Expose
    private String sermonsoundcloud;
    @SerializedName("sermondescription")
    @Expose
    private String sermondescription;
    @SerializedName("video")
    @Expose
    private Object video;
    @SerializedName("audio")
    @Expose
    private Object audio;
    @SerializedName("file")
    @Expose
    private Object file;
    @SerializedName("cdate")
    @Expose
    private String cdate;

    public Integer getSermonid() {
        return sermonid;
    }

    public void setSermonid(Integer sermonid) {
        this.sermonid = sermonid;
    }

    public String getSermonbanner() {
        return sermonbanner;
    }

    public void setSermonbanner(String sermonbanner) {
        this.sermonbanner = sermonbanner;
    }

    public String getSermontitle() {
        return sermontitle;
    }

    public void setSermontitle(String sermontitle) {
        this.sermontitle = sermontitle;
    }

    public String getSermondate() {
        return sermondate;
    }

    public void setSermondate(String sermondate) {
        this.sermondate = sermondate;
    }

    public String getSermontime() {
        return sermontime;
    }

    public void setSermontime(String sermontime) {
        this.sermontime = sermontime;
    }

    public String getSermonlocation() {
        return sermonlocation;
    }

    public void setSermonlocation(String sermonlocation) {
        this.sermonlocation = sermonlocation;
    }

    public String getSermonauthor() {
        return sermonauthor;
    }

    public void setSermonauthor(String sermonauthor) {
        this.sermonauthor = sermonauthor;
    }

    public String getSermonyoutube() {
        return sermonyoutube;
    }

    public void setSermonyoutube(String sermonyoutube) {
        this.sermonyoutube = sermonyoutube;
    }

    public String getSermonsoundcloud() {
        return sermonsoundcloud;
    }

    public void setSermonsoundcloud(String sermonsoundcloud) {
        this.sermonsoundcloud = sermonsoundcloud;
    }

    public String getSermondescription() {
        return sermondescription;
    }

    public void setSermondescription(String sermondescription) {
        this.sermondescription = sermondescription;
    }

    public Object getVideo() {
        return video;
    }

    public void setVideo(Object video) {
        this.video = video;
    }

    public Object getAudio() {
        return audio;
    }

    public void setAudio(Object audio) {
        this.audio = audio;
    }

    public Object getFile() {
        return file;
    }

    public void setFile(Object file) {
        this.file = file;
    }

    public String getCdate() {
        return cdate;
    }

    public void setCdate(String cdate) {
        this.cdate = cdate;
    }

}
