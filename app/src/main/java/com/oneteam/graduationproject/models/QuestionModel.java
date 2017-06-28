package com.oneteam.graduationproject.models;

/**
 * Created by Mohamed AbdelraZek on 6/28/2017.
 */

public class QuestionModel {

    private int Id;
    private String Title;
    private String Content;
    private int UserId;
    private String AuthorName;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getAuthorName() {
        return AuthorName;
    }

    public void setAuthorName(String authorName) {
        AuthorName = authorName;
    }
}
