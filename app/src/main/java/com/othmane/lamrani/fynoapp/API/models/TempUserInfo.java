package com.othmane.lamrani.fynoapp.API.models;

/**
 * Created by Lamrani on 13/12/2017.
 */

public class TempUserInfo {

    private Integer id;
    private String name;
    private String job_function;
    private boolean remember_me = false;
    private String token;

    public TempUserInfo(int id, String name, String job_function, String token, boolean remember_me) {
        this.id = id;
        this.name = name;
        this.job_function = job_function;
        this.token = token;
        this.remember_me = remember_me;
    }

    public String getName() {
        return name;
    }

    public String getJob_function() {
        return job_function;
    }

    public boolean getRemember_me() {
        return remember_me;
    }

    public String getToken() {
        return token;
    }

    public Integer getId() {
        return id;
    }
}
