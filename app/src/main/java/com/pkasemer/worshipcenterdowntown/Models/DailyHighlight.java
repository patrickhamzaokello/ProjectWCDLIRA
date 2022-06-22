
package com.pkasemer.worshipcenterdowntown.Models;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class DailyHighlight {

    @SerializedName("news_id")
    @Expose
    private Integer newsId;
    @SerializedName("news_title")
    @Expose
    private String newsTitle;
    @SerializedName("news_description")
    @Expose
    private String newsDescription;
    @SerializedName("verse")
    @Expose
    private String verse;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("cdate")
    @Expose
    private String cdate;
    @SerializedName("status")
    @Expose
    private Integer status;

    public Integer getNewsId() {
        return newsId;
    }

    public void setNewsId(Integer newsId) {
        this.newsId = newsId;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsDescription() {
        return newsDescription;
    }

    public void setNewsDescription(String newsDescription) {
        this.newsDescription = newsDescription;
    }

    public String getVerse() {
        return verse;
    }

    public void setVerse(String verse) {
        this.verse = verse;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCdate() {
        return cdate;
    }

    public void setCdate(String cdate) {
        this.cdate = cdate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
