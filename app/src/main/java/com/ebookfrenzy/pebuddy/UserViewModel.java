package com.ebookfrenzy.pebuddy;

import androidx.lifecycle.ViewModel;

import com.ebookfrenzy.pebuddy.ui.main.login.User;

public class UserViewModel extends ViewModel {

    private String name;
    private String username;
    private String first_name;
    private String last_name;
    private String email;
    private String level = "Beginner";
    private String password;

    public void setPassword(String password) { this.password = password; }

    public String getPassword() { return password; }

    public String getLevel() { return level; }

    public void setLevel(String level) { this.level = level; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
