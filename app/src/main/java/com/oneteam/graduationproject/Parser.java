package com.oneteam.graduationproject;

import android.util.Log;

import com.oneteam.graduationproject.models.QuestionModel;

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
}
