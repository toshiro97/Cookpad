package com.example.cookpad.model;

import java.io.Serializable;

public class StepCook implements Serializable {
    private int id;
    private String content;
    private String urlImage;

    public StepCook() {
    }

    public StepCook(int id, String content, String urlImage) {
        this.id = id;
        this.content = content;
        this.urlImage = urlImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }
}
