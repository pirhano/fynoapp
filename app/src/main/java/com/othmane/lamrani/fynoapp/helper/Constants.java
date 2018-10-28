package com.othmane.lamrani.fynoapp.helper;

/**
 * Created by Lamrani on 21/09/2017.
 */

public class Constants {



    public static final class HTTP{
        public static final String API_BASE_URL = "http://fynodev.mdinti.com/";//"http://192.168.43.158/"; //"http://fyno.mdinti.com/";
        public static final int STATUS_CODE_SUCCESS = 200;
        public static final int STATUS_CODE_NOT_AUTHORIZED = 401;
    }


    public static final class GEOSERVER{
        public static final String LAYER_NAME = "MdintimapsEntier";
        public static final String IP_HOST =  "83.169.23.140";
        public static final String PORT_HOST = "8080";
        public static final String url_host = IP_HOST + ":" + PORT_HOST;
        public static final String BASE_URL = "http://"+ url_host +"/geoserver/wms/";  // example => 192.168.1.80:8080/geoserver/wms
    }

    public static final class POSTGRES{
        public static final String BASE_URL = "http://"+ HTTP.API_BASE_URL ;
        public static String POSTGRESQL_PORT_HOST = "5432";
        public static String POSTGRESQL_USER = "mapuser";
        public static String POSTGRESQL_PASSWORD = "mapuser";
        public static String POSTGRESQL_DATABASE = "mapserver";

        public static String  StringConexionPrd = "jdbc:postgresql://" +
                "83.169.23.140:"+ POSTGRESQL_PORT_HOST +
                "/" + POSTGRESQL_DATABASE +
                "?" + "user=" + POSTGRESQL_USER + "&password=" + "mapuser14Malsehen";
    }

    public class REFERENCE {
        public static final String TODAY = "today";
        public static final String EQUIPMENT = CONFIG.PACKAGE_NAME + "equipment";
        public static final String EQUIPMENT_back_to_details = CONFIG.PACKAGE_NAME + "equipment_back_to_details";
        public static final String EQUIPMENT_licence_plate = CONFIG.PACKAGE_NAME + "equipment_licence_plate";
        public static final String STOP = CONFIG.PACKAGE_NAME + "stop";
        public static final String DRIVER = CONFIG.PACKAGE_NAME + "driver";

        // For shared preferences
        public static final String USER = "user";
        public static final String USER_ID = "user_id";
        public static final String USER_NAME = "user_name";
        public static final String USER_SERVICE = "user_service";
        public static final String USER_TOKEN = "user_token";
        public static final String USER_REMEMBER = "remember_me";

    }

    public class CONFIG {
        public static final String PACKAGE_NAME = "com.othmane.lamrani.fynoapp";
    }
}
