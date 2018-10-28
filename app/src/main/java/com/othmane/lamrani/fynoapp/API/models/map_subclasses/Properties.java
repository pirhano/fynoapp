package com.othmane.lamrani.fynoapp.API.models.map_subclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Lamrani on 09/10/2017.
 */

public class Properties{

    @SerializedName("Label")
    @Expose
    private String Label;

    @SerializedName("LabelA")
    @Expose
    private String LabelA;

    public String getLabel() {
        return Label;
    }

    public String getLabelA() {
        return LabelA;
    }
}