package com.example.cookpad.model;

import java.util.List;

public class Food {
    private int id;
    private String imageUrl;
    private String title;
    private User user;
    private List<StepCook> stepCookList;
    private String description;
    private List<Comment> commentList;
    private List<String> resources;

    public Food() {
    }

    public Food(int id, String imageUrl, String title, User user, List<StepCook> stepCookList, String description, List<Comment> commentList, List<String> resources) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.title = title;
        this.user = user;
        this.stepCookList = stepCookList;
        this.description = description;
        this.commentList = commentList;
        this.resources = resources;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<StepCook> getStepCookList() {
        return stepCookList;
    }

    public void setStepCookList(List<StepCook> stepCookList) {
        this.stepCookList = stepCookList;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public List<String> getResources() {
        return resources;
    }

    public void setResources(List<String> resources) {
        this.resources = resources;
    }
}
