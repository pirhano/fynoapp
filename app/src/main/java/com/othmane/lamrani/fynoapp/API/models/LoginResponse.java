package com.othmane.lamrani.fynoapp.API.models;

        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

/**
 * Created by Lamrani on 03/10/2017.
 */

public class LoginResponse {

    @SerializedName("error")
    @Expose
    private boolean error;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("user")
    @Expose
    private User user;

    public boolean getError() {
        return error;
    }

    public String getStatus() {
        return status;
    }

    public User getUser() {
        return user;
    }

    public String getToken(){ return token; }



}

