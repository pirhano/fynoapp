package com.othmane.lamrani.fynoapp.helper.osmdroid;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Toast;

import com.othmane.lamrani.fynoapp.R;
import com.othmane.lamrani.fynoapp.helper.Constants;
import com.othmane.lamrani.fynoapp.helper.osmdroid.MyWMSLayer;
import com.othmane.lamrani.fynoapp.API.models.MapFeature;
import com.othmane.lamrani.fynoapp.API.callback.MapFeaturesAPI;
import com.othmane.lamrani.fynoapp.API.models.map_subclasses.Features;

import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Lamrani on 02/11/2017.
 */

public class MapHelper {

    private MapView map;
    private Marker poiMarker;
    private Context context;
    public static int MAX_POINTS = 300;

    public MapHelper(Context context, MapView map) {
        this.map = map;
        this.context = context;
        // Configure the poiClicked overlay
        poiMarker = new Marker(map);
    }


    /**
     * Configuring the map
     */
    public void configureMap(final MapFeaturesAPI service){

        // very important to make map stretch to large screens
        map.setTilesScaledToDpi(true);

        // Get the WMS tiles from Geoserver
        getMapFromGeoserver();


        // Get features informations onClick action
        MapEventsReceiver mReceiver = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                if(InfoWindow.getOpenedInfoWindowsOn(map).isEmpty()){
                    // close all the infowindows
                    poiMarker.setTitle(null);
                    poiMarker.setSnippet(null);
                    poiMarker.setSubDescription(null);

                    InfoWindow.closeAllInfoWindowsOn(map);
                    // Calling geoserver Getfeature info to return the result on an InfoWindow
                    getMapFeatureString(service, p);
                }
                else{
                    InfoWindow.closeAllInfoWindowsOn(map);
                }
                return false;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }

        };

        MapEventsOverlay overlayEvents = new MapEventsOverlay(mReceiver);

        map.getOverlays().add(overlayEvents);

    }

    // Create a WMS map from my geoserver

    /**
     *
     */
    public void getMapFromGeoserver() {
        MyWMSLayer myGeoserverMap = new MyWMSLayer();
        myGeoserverMap.getMapFromGeoserver(context, map);

        // limit scrollable area
        map.setScrollableAreaLimitDouble(new BoundingBox(36.049652, -0.764007, 20.342767, -19.397271));
        // mapView.setMinZoomLevel(6);
    }

    /**
     *
     * @param point: the clicked point on the map
     */

    public void getMapFeatureString(MapFeaturesAPI service, final GeoPoint point){

        // The map takes all the space. Therefore I computed the BBOX(bounding box) in this way:
        BoundingBox region =  map.getBoundingBox();
        double left = region.getLonWest();
        double top =  region.getLatNorth();
        double right = region.getLonEast();
        double bottom = region.getLatSouth();

        // and the width and height are taken as belows:
        int width = map.getWidth();
        int height = map.getHeight();

        // get pixels clicked on the map
        Point click =  map.getProjection().toPixels(point, null);
        int x = click.x;
        int y =  click.y;

        // request url to geoserver ( buffer param: tolerate some non clicked pixels)
        /*String url_getFeatureInfo = Constants.GEOSERVER.BASE_URL +
                "?SERVICE=WMS" +
                "&VERSION=1.1.1" +
                "&REQUEST=GetFeatureInfo" +
                "&FORMAT=image%2Fpng" +
                "&TRANSPARENT=true" +
                "&QUERY_LAYERS=" + Constants.GEOSERVER.LAYER_NAME +
                "&STYLES" +
                "&LAYERS=" + Constants.GEOSERVER.LAYER_NAME +
                "&INFO_FORMAT=application%2Fjson" +
                "&FEATURE_COUNT=50" +
                "&X=" + x +
                "&Y=" + y +
                "&SRS=EPSG%3A4326" +
                "&WIDTH="+ width +
                "&HEIGHT=" + height +
                "&BBOX=" + left + "," + bottom + "," + right + ","  + top +
                "&BUFFER=60";
        */

        // RETROFIT CALL

        Map<String, String> data = new HashMap<>();
        data.put("SERVICE", "WMS");
        data.put("VERSION", "1.1.1");
        data.put("REQUEST", "GetFeatureInfo");
        data.put("FORMAT", "image%2Fpng");
        data.put("TRANSPARENT", "true");
        data.put("QUERY_LAYERS", Constants.GEOSERVER.LAYER_NAME );
        data.put("STYLES", "");
        data.put("LAYERS", Constants.GEOSERVER.LAYER_NAME);
        data.put("INFO_FORMAT", "application%2Fjson");
        data.put("FEATURE_COUNT", "1");
        data.put("X", String.valueOf(x));
        data.put("Y", String.valueOf(y));
        data.put("SRS", "EPSG%3A4326");
        data.put("WIDTH", String.valueOf(width));
        data.put("HEIGHT", String.valueOf(height));
        data.put("BBOX", left + "," + bottom + "," + right + ","  + top);
        data.put("BUFFER", "60");

        // simplified call to request the news with already initialized service
        getConfigRetrofitCall();

        // Check if the credentials are matching
        final Call<MapFeature> mapFeatureResponseCall =  service.getFeaturesInformations(data);

        Log.i("Request retrofit", mapFeatureResponseCall.request().toString());

        mapFeatureResponseCall.enqueue(new Callback<MapFeature>() {
            @Override
            public void onResponse(Call<MapFeature> call, Response<MapFeature> response) {

                if(response != null){
                    if(response.isSuccessful()){

                        MapFeature mapFeatures = response.body();

                        if(mapFeatures != null){

                           /* for (Features feature : mapFeatures.getFeatures()) {
                                System.out.println("Id: " +  feature.getId() );
                                System.out.println("type: " +  feature.getGeometry().getType() );
                                System.out.println("label arabe: " +  feature.getProperties().getLabelA() );
                                System.out.println("label: " +  feature.getProperties().getLabel() );
                            }*/

                            // inflating a pop up overlay showing information about the point

                            if(!mapFeatures.getFeatures().isEmpty()){
                                String label = mapFeatures.getFeatures().get(0).getProperties().getLabel();
                                String labelA = mapFeatures.getFeatures().get(0).getProperties().getLabelA();

                                if(label != null){
                                    // Inflating a popup overlay

                                    poiMarker.setPosition(point);
                                    poiMarker.setTitle(label);

                                    if(!label.equalsIgnoreCase(labelA)){
                                        poiMarker.setSnippet(labelA);
                                    }

                                    poiMarker.showInfoWindow();
                                    Drawable icon = context.getResources().getDrawable(R.drawable.transparent);
                                    //poiMarker.setImage(ctx.getResources().getDrawable(R.drawable.ic_poi));
                                    poiMarker.setIcon(icon);
                                    // add the matker to the map
                                    map.getOverlays().add(poiMarker);
                                    //map.getController().setCenter(point);
                                    map.invalidate();
                                }
                            }
                        }
                    }
                    else{
                        Toast.makeText(context, "Failed request", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<MapFeature> call, Throwable t) {
                Log.i("Failure retrofit", "Map");
                if(t.getMessage() != null){
                    Log.i("Failure retrofit", t.getMessage());
                }
                //Toast.makeText(context, context.getString(R.string.intern_error), Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     *
     * @return the map service api
     */
    public MapFeaturesAPI getConfigRetrofitCall(){

        //Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.GEOSERVER.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

       return retrofit.create(MapFeaturesAPI.class);
    }


}
