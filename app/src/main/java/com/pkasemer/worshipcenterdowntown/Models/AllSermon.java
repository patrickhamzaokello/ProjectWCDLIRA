
package com.pkasemer.worshipcenterdowntown.Models;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class AllSermon {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("heading")
    @Expose
    private String heading;
    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("home_sermons")
    @Expose
    private List<HomeSermon> homeSermons = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public List<HomeSermon> getHomeSermons() {
        return homeSermons;
    }

    public void setHomeSermons(List<HomeSermon> homeSermons) {
        this.homeSermons = homeSermons;
    }

}
