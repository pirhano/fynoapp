package com.othmane.lamrani.fynoapp.helper.osmdroid;

import android.content.Context;
import android.util.Log;

import com.othmane.lamrani.fynoapp.helper.Constants;

import org.osmdroid.api.IMapController;
import org.osmdroid.views.MapView;

/**
 * Created by Lamrani on 09/10/2017.
 */

public class MyWMSLayer {

    public void getMapFromGeoserver(Context context, MapView mapView){

        // Get the url from Utils class
        // the result of baseUrl should look like this => http://192.168.1.80:8080/geoserver/wms

        String[] baseUrl = new String[1];
        baseUrl[0] = Constants.GEOSERVER.BASE_URL;
        String layerName = Constants.GEOSERVER.LAYER_NAME;

        Log.d("MapView", baseUrl[0] + ", layer: " + layerName);

        // Instantiate WMSTileProvider class with base url and layer name to get the tiles
        WMSTileProvider tileSource = new WMSTileProvider("MyLayer", baseUrl , layerName);

        // Use google maps


        // Setting the result as tile source of the map
        //mapView.setTileSource(tileSource);
        mapView.setTileSource(GogleMapsProvider.GoogleRoads);

        // map controllers options
        //mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

        //Place la position de la carte
        IMapController mapController = mapView.getController();
        mapView.setMinZoomLevel(6);

        // Add the scale Bar
        //ScaleBarOverlay myScaleBarOverlay = new ScaleBarOverlay(mapView);
        //mapView.getOverlays().add(myScaleBarOverlay);

    }



}