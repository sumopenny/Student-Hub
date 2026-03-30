package com.manage.pojo;

import lombok.Data;

/**
 * 登录成功结果封装类
 */
@Data
public class LoginInfo {
    private Integer id;
    private String username;
    private String name;
    private String avatar;
    private String token;
    private String gender;
    private Integer age;
    private String address;
    private String phone;

    public LoginInfo() {
    }

    public LoginInfo(Integer id, String username, String name, String avatar, String token) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.avatar = avatar;
        this.token = token;
    }

    public LoginInfo(Integer id, String username, String name, String avatar, String token, 
                    String gender, Integer age, String address, String phone) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.avatar = avatar;
        this.token = token;
        this.gender = gender;
        this.age = age;
        this.address = address;
        this.phone = phone;
    }
}
