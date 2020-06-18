package com.example.student;

public class CardItem {
    String username;
    String level;
    String category;
    String subcategory;
    String body;
    String status;
    String reply;
    String userid;
    String permission;
    String childid;


    public CardItem(String username, String level, String category, String subcategory, String body, String status, String reply, String userid, String permission, String childid) {
        this.childid = childid;
        this.username = username;
        this.level = level;
        this.category = category;
        this.subcategory = subcategory;
        this.body = body;
        this.status = status;
        this.reply = reply;
        this.userid = userid;
        this.permission = permission;

    }

    public String getUsername() {
        return username;
    }

    public String getLevel() {
        return level;
    }

    public String getCategory() {
        return category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public String getBody() {
        return body;
    }

    public String getStatus() {
        return status;
    }

    public String getReply() {
        return reply;
    }

    public String getUserid() {
        return userid;
    }

    public String getPermission() {
        return permission;
    }

    public String getChildid() {
        return childid;
    }
}
