package com.manage.pojo;

public class Student {
    private Integer id;
    private String name;
    private String gender;
    private Integer age;
    private String address;
    public String phone;
    private String username;    // 新增：用户名
    private String password;    // 新增：密码
    private String avatar;      // 新增：头像URL

    // 无参构造器
    public Student() {
    }

    // 全参构造器
    public Student(Integer id, String name, String gender, Integer age, String address, String phone, String username, String password, String avatar) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.address = address;
        this.phone = phone;
        this.username = username;
        this.password = password;
        this.avatar = avatar;
    }

    // getter和setter方法
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getAvatar() {
        return avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

}