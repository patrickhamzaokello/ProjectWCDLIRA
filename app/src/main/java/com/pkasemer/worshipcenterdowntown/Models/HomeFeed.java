
package com.pkasemer.worshipcenterdowntown.Models;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class HomeFeed {

    @SerializedName("sliderBanners")
    @Expose
    private List<SliderBanner> sliderBanners = null;
    @SerializedName("daily_highlights")
    @Expose
    private List<DailyHighlight> dailyHighlights = null;
    @SerializedName("heading")
    @Expose
    private String heading;
    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("home_events")
    @Expose
    private List<HomeEvent> homeEvents = null;
    @SerializedName("home_sermons")
    @Expose
    private List<HomeSermon> homeSermons = null;

    public List<SliderBanner> getSliderBanners() {
        return sliderBanners;
    }

    public void setSliderBanners(List<SliderBanner> sliderBanners) {
        this.sliderBanners = sliderBanners;
    }

    public List<DailyHighlight> getDailyHighlights() {
        return dailyHighlights;
    }

    public void setDailyHighlights(List<DailyHighlight> dailyHighlights) {
        this.dailyHighlights = dailyHighlights;
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

    public List<HomeEvent> getHomeEvents() {
        return homeEvents;
    }

    public void setHomeEvents(List<HomeEvent> homeEvents) {
        this.homeEvents = homeEvents;
    }

    public List<HomeSermon> getHomeSermons() {
        return homeSermons;
    }

    public void setHomeSermons(List<HomeSermon> homeSermons) {
        this.homeSermons = homeSermons;
    }

}
