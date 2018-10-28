package com.othmane.lamrani.fynoapp.API.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Lamrani on 15/11/2017.
 */

public class User implements Serializable{

    @SerializedName("first_name")
    @Expose
    private String name;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("service")
    @Expose
    private String service;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("last_access")
    @Expose
    private String last_access;

    @SerializedName("created_at")
    @Expose
    private String created_at;

    @SerializedName("updated_at")
    @Expose
    private String updated_at;


    // ********* Constructor *******

    public User(String name, String email, String service, String phone, String last_access, String created_at, String updated_at) {
        this.name = name;
        this.email = email;
        this.service = service;
        this.phone = phone;
        this.last_access = last_access;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }


    // ********* Getters *********

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getService() {
        return service;
    }

    public String getPhone() {
        return phone;
    }

    public String getLast_access() {
        return last_access;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }
}
