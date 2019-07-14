package com.example.cookpad.model;

import java.io.Serializable;

public class Comment implements Serializable {
    private User user;
    private String comment ;

    public Comment(){}

    public Comment(User user, String comment) {
        this.user = user;
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
