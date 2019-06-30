package com.example.cookpad.model;

import java.util.List;

public class Challenge {
    private int id;
    private String title;
    private String date;
    private String urlImage;
    private String contentTitle;
    private String ingredient;
    private String tips;
    private List<Reward> rewardList;

    public Challenge() {
    }

    public Challenge(int id, String title, String date, String urlImage, String contentTitle, String ingredient, String tips, List<Reward> rewardList) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.urlImage = urlImage;
        this.contentTitle = contentTitle;
        this.ingredient = ingredient;
        this.tips = tips;
        this.rewardList = rewardList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getContentTitle() {
        return contentTitle;
    }

    public void setContentTitle(String contentTitle) {
        this.contentTitle = contentTitle;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public List<Reward> getRewardList() {
        return rewardList;
    }

    public void setRewardList(List<Reward> rewardList) {
        this.rewardList = rewardList;
    }
}
