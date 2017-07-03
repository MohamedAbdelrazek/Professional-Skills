package com.oneteam.graduationproject.models;

/**
 * Created by Karim Gamal -PC on 7/2/2017.
 */

public class SkillModel {

    private int skillId;
    private String skillName;
    private String userId;

    public int getSkillId() {
        return skillId;
    }

    public void setSkillId(int skillId) {
        this.skillId = skillId;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public String  getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
