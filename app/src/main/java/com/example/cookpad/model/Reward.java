package com.example.cookpad.model;

public class Reward {
    private int id;
    private String urlImage;
    private String title;

    public Reward() {
    }

    public Reward(int id, String urlImage, String title) {
        this.id = id;
        this.urlImage = urlImage;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
