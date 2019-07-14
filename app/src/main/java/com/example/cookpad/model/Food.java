package com.example.cookpad.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Food implements Serializable {
    private String id;
    private String imageUrl;
    private String title;
    private User user;
    private ArrayList<StepCook> stepCookList;
    private String description;
    private List<Comment> commentList;
    private List<String> resources;


    public Food() {
    }

    public Food(String id, String imageUrl, String title, User user, ArrayList<StepCook> stepCookList,
                String description, List<Comment> commentList, List<String> resources) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.title = title;
        this.user = user;
        this.stepCookList = stepCookList;
        this.description = description;
        this.commentList = commentList;
        this.resources = resources;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public ArrayList<StepCook> getStepCookList() {
        return stepCookList;
    }

    public void setStepCookList(ArrayList<StepCook> stepCookList) {
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
