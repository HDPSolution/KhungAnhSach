package com.hdpsolution.khunganhtrangsach.Model;

/**
 * Created by Administrator on 3/20/2018.
 */

public class Image {
    private Integer id;
    private String uri;
    private String urigalery;

    public Image(Integer id, String uri, String urigalery) {
        this.id = id;
        this.uri = uri;
        this.urigalery = urigalery;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUrigalery() {
        return urigalery;
    }

    public void setUrigalery(String urigalery) {
        this.urigalery = urigalery;
    }
}
