package com.ensi.pierre.rsshandler;

public class RssItem {

    private String imageURL = null ;
    private StringBuffer title = new StringBuffer();
    private StringBuffer description = new StringBuffer();
    private StringBuffer date = new StringBuffer();

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public StringBuffer getTitle() {
        return title;
    }

    public void setTitle(StringBuffer title) {
        this.title = title;
    }

    public StringBuffer getDescription() {
        return description;
    }

    public void setDescription(StringBuffer description) {
        this.description = description;
    }

    public StringBuffer getDate() {
        return date;
    }

    public void setDate(StringBuffer date) {
        this.date = date;
    }
}
