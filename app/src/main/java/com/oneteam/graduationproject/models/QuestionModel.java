package com.oneteam.graduationproject.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mohamed AbdelraZek on 6/28/2017.
 */

public class QuestionModel implements Parcelable {

    private int Id;
    private String Title;
    private String Content;
    private int UserId;
    private String AuthorName;

    public QuestionModel() {

    }

    protected QuestionModel(Parcel in) {
        Id = in.readInt();
        Title = in.readString();
        Content = in.readString();
        UserId = in.readInt();
        AuthorName = in.readString();
    }

    public static final Creator<QuestionModel> CREATOR = new Creator<QuestionModel>() {
        @Override
        public QuestionModel createFromParcel(Parcel in) {
            return new QuestionModel(in);
        }

        @Override
        public QuestionModel[] newArray(int size) {
            return new QuestionModel[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeString(Title);
        dest.writeString(Content);
        dest.writeInt(UserId);
        dest.writeString(AuthorName);
    }
}
