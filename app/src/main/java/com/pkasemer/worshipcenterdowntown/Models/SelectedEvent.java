
package com.pkasemer.worshipcenterdowntown.Models;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class SelectedEvent {

    @SerializedName("eventid")
    @Expose
    private Integer eventid;
    @SerializedName("eventimage")
    @Expose
    private String eventimage;
    @SerializedName("eventtitle")
    @Expose
    private String eventtitle;
    @SerializedName("eventdate")
    @Expose
    private String eventdate;
    @SerializedName("eventtime")
    @Expose
    private String eventtime;
    @SerializedName("eventlocation")
    @Expose
    private String eventlocation;
    @SerializedName("eventdescription")
    @Expose
    private String eventdescription;
    @SerializedName("eventstartdate")
    @Expose
    private String eventstartdate;
    @SerializedName("eventenddate")
    @Expose
    private String eventenddate;
    @SerializedName("cdate")
    @Expose
    private String cdate;
    @SerializedName("home_events")
    @Expose
    private List<HomeEvent> homeEvents = null;

    public Integer getEventid() {
        return eventid;
    }

    public void setEventid(Integer eventid) {
        this.eventid = eventid;
    }

    public String getEventimage() {
        return eventimage;
    }

    public void setEventimage(String eventimage) {
        this.eventimage = eventimage;
    }

    public String getEventtitle() {
        return eventtitle;
    }

    public void setEventtitle(String eventtitle) {
        this.eventtitle = eventtitle;
    }

    public String getEventdate() {
        return eventdate;
    }

    public void setEventdate(String eventdate) {
        this.eventdate = eventdate;
    }

    public String getEventtime() {
        return eventtime;
    }

    public void setEventtime(String eventtime) {
        this.eventtime = eventtime;
    }

    public String getEventlocation() {
        return eventlocation;
    }

    public void setEventlocation(String eventlocation) {
        this.eventlocation = eventlocation;
    }

    public String getEventdescription() {
        return eventdescription;
    }

    public void setEventdescription(String eventdescription) {
        this.eventdescription = eventdescription;
    }

    public String getEventstartdate() {
        return eventstartdate;
    }

    public void setEventstartdate(String eventstartdate) {
        this.eventstartdate = eventstartdate;
    }

    public String getEventenddate() {
        return eventenddate;
    }

    public void setEventenddate(String eventenddate) {
        this.eventenddate = eventenddate;
    }

    public String getCdate() {
        return cdate;
    }

    public void setCdate(String cdate) {
        this.cdate = cdate;
    }

    public List<HomeEvent> getHomeEvents() {
        return homeEvents;
    }

    public void setHomeEvents(List<HomeEvent> homeEvents) {
        this.homeEvents = homeEvents;
    }

}
