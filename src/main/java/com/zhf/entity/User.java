package com.zhf.entity;

import java.time.LocalDateTime;

public class User {
    
    private Long userId;
    private String userName;
    private Integer age;
    private Integer sex;
    private String placeOfOrigin;
    private String university;
    private String createBy;
    private LocalDateTime createTime;
    private String updateBy;
    private LocalDateTime updateTime;

    public User() {}

    public User(String userName, Integer age, Integer sex, String placeOfOrigin, String university) {
        this.userName = userName;
        this.age = age;
        this.sex = sex;
        this.placeOfOrigin = placeOfOrigin;
        this.university = university;
    }

    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public Integer getSex() { return sex; }
    public void setSex(Integer sex) { this.sex = sex; }

    public String getPlaceOfOrigin() { return placeOfOrigin; }
    public void setPlaceOfOrigin(String placeOfOrigin) { this.placeOfOrigin = placeOfOrigin; }

    public String getUniversity() { return university; }
    public void setUniversity(String university) { this.university = university; }

    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }

    public String getUpdateBy() { return updateBy; }
    public void setUpdateBy(String updateBy) { this.updateBy = updateBy; }

    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", age=" + age +
                ", sex=" + sex +
                ", placeOfOrigin='" + placeOfOrigin + '\'' +
                ", university='" + university + '\'' +
                '}';
    }
}