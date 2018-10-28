package com.othmane.lamrani.fynoapp.API.models.map_subclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Lamrani on 09/10/2017.
 */

public class Features {

        @SerializedName("id")
        @Expose
        private String id;

        @SerializedName("properties")
        @Expose
        private Properties properties;

        @SerializedName("geometry")
        @Expose
        private Geometry geometry;

        public String getId() {
            return id;
        }

        public Properties getProperties() {
            return properties;
        }

        public Geometry getGeometry() {
            return geometry;
        }






}



