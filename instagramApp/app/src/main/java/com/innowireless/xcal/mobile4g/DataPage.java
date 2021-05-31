package com.innowireless.xcal.mobile4g;

public class DataPage {
    String media_url;
    String media_type;
    String thumbnail_url;

//    public DataPage(String media_url, String media_type, String thumbnail_url){
//        this.media_url = media_url;
//        this.media_type = media_type;
//        this.thumbnail_url = thumbnail_url;
//    }

    public DataPage(String media_url, String media_type){
        this.media_url = media_url;
        this.media_type = media_type;
    }

    public String getmedia_url() { return media_url; }

    public void setmedia_url(String media_url) {
        this.media_url = media_url;
    }

    public String getmedia_type() {
        return media_type;
    }

    public void setmedia_type(String media_type) {
        this.media_type = media_type;
    }

    public String getthumbnail_url() {
        return thumbnail_url;
    }

    public void setthumbnail_url(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }
}

