package com.example.cookpad.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private String id;
    private String name;
    private String imageUrl;
    private String description;
    private String birthday;
    private ArrayList<String> listFriend;

    public User() {
    }

    public User(String id, String name, String imageUrl, String description, String birthday, ArrayList<String> listFriend) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.description = description;
        this.birthday = birthday;
        this.listFriend = listFriend;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public ArrayList<String> getListFriend() {
        return listFriend;
    }

    public void setListFriend(ArrayList<String> listFriend) {
        this.listFriend = listFriend;
    }
}
