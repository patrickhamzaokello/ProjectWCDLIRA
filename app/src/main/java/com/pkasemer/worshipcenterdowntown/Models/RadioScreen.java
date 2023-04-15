
package com.pkasemer.worshipcenterdowntown.Models;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class RadioScreen {

    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("radioPage")
    @Expose
    private List<RadioPage> radioPage;
    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;
    @SerializedName("total_results")
    @Expose
    private Integer totalResults;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<RadioPage> getRadioPage() {
        return radioPage;
    }

    public void setRadioPage(List<RadioPage> radioPage) {
        this.radioPage = radioPage;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

}
