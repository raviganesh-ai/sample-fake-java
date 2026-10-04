package com.example.library.model;

import java.io.Serializable;

public class Member implements Serializable {
    private int id;
    private String fullName;
    private String email;
    private String membershipLevel;

    public Member() {
    }

    public Member(int id, String fullName, String email, String membershipLevel) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.membershipLevel = membershipLevel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMembershipLevel() {
        return membershipLevel;
    }

    public void setMembershipLevel(String membershipLevel) {
        this.membershipLevel = membershipLevel;
    }
}
