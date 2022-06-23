
package com.pkasemer.worshipcenterdowntown.Models;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class SelectedSermon {

    @SerializedName("sermonid")
    @Expose
    private String sermonid;
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
    private String video;
    @SerializedName("audio")
    @Expose
    private String audio;
    @SerializedName("file")
    @Expose
    private String file;
    @SerializedName("cdate")
    @Expose
    private String cdate;
    @SerializedName("home_sermons")
    @Expose
    private List<HomeSermon> homeSermons = null;

    public String getSermonid() {
        return sermonid;
    }

    public void setSermonid(String sermonid) {
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

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getCdate() {
        return cdate;
    }

    public void setCdate(String cdate) {
        this.cdate = cdate;
    }

    public List<HomeSermon> getHomeSermons() {
        return homeSermons;
    }

    public void setHomeSermons(List<HomeSermon> homeSermons) {
        this.homeSermons = homeSermons;
    }

}
