package com.oneteam.graduationproject;

import android.util.Log;

import com.oneteam.graduationproject.models.CommentModel;
import com.oneteam.graduationproject.models.QuestionModel;
import com.oneteam.graduationproject.models.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Mohamed AbdelraZek on 6/29/2017.
 */

public class Parser {

    public static ArrayList<QuestionModel> getAllPost(String jsonStr) {
        ArrayList<QuestionModel> list = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            JSONArray jsonArray = jsonObject.getJSONArray("questionModel");
            Log.i("ZOKA", "Array Length" + jsonArray.length());

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jobject = jsonArray.getJSONObject(i);
                QuestionModel questionModel = new QuestionModel();
                questionModel.setAuthorName(jobject.getString("author_name"));
                questionModel.setContent(jobject.getString("content"));
                questionModel.setTitle(jobject.getString("title"));
                questionModel.setId(jobject.getInt("qid"));
                questionModel.setUserId(jobject.getInt("user_id"));

                list.add(questionModel);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return list;
    }

    public static UserModel getUserInfo(String JsonUserResponse) {

        UserModel userModel = new UserModel();


        try {
            JSONObject jsonObject = new JSONObject(JsonUserResponse);
            userModel.setFirstName(jsonObject.getString("firstName"));
            userModel.setLastName(jsonObject.getString("lastName"));
            userModel.setMobileNumber(jsonObject.getString("phone"));
            userModel.setId(jsonObject.getString("userId"));
            userModel.setEmailAddress(jsonObject.getString("userName"));
            userModel.setPassword(jsonObject.getString("userPassword"));


        } catch (JSONException e) {
            Log.i("ZOKA", "" + e.getMessage());
        }


        return userModel;
    }

    public static ArrayList<CommentModel> getComments(String jsonStr) {
        Log.i("ZOKA", "Array Length" + jsonStr);

        ArrayList<CommentModel> list = new ArrayList<>();
        try {
            JSONObject jsonOb = new JSONObject(jsonStr);
            JSONArray jsonArray = jsonOb.getJSONArray("commentModel");
            Log.i("ZOKA", "Array Length" + jsonArray.length());

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                CommentModel commentModel = new CommentModel();

                commentModel.setAuthorName(jsonObject.getString("author_name"));
                commentModel.setCommentId(jsonObject.getInt("comment_id"));
                commentModel.setContent(jsonObject.getString("content"));
                commentModel.setQuestionId(jsonObject.getInt("question_id"));
                commentModel.setUserId(jsonObject.getInt("user_id"));

                list.add(commentModel);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return list;


    }
}
