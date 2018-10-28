package com.othmane.lamrani.fynoapp.API.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Lamrani on 12/12/2017.
 */

public class UserDetails {

    @SerializedName("id")
    private Integer id;

    @SerializedName("corporate_structure_level_id")
    private Integer corporate_structure_level_id;

    @SerializedName("last_name")
    private String name;

    @SerializedName("first_name")
    private String first_name;

    @SerializedName("phone_number")
    private String phone_number;

    @SerializedName("mobile_number")
    private String mobile_number;

    @SerializedName("email")
    private String email;

    @SerializedName("birth_date")
    private String birth_date;

    @SerializedName("identity_card_number")
    private String identity_card_number;

    @SerializedName("city")
    private String city;

    @SerializedName("house_number")
    private String house_number;

    @SerializedName("street")
    private String street;

    @SerializedName("current_job_function")
    private String current_job_function;


    public Integer getId() {
        return id;
    }

    public Integer getCorporate_structure_level_id() {
        return corporate_structure_level_id;
    }

    public String getName() {
        return name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public String getEmail() {
        return email;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public String getIdentity_card_number() {
        return identity_card_number;
    }

    public String getCity() {
        return city;
    }

    public String getHouse_number() {
        return house_number;
    }

    public String getStreet() {
        return street;
    }

    public String getCurrentJob_function() {
        return current_job_function;
    }
}
