package com.othmane.lamrani.fynoapp.helper.osmdroid;

import org.osmdroid.tileprovider.MapTile;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.tilesource.XYTileSource;

/**
 * Created by Lamrani on 06/07/2018.
 */

public class GogleMapsProvider {

        public static final OnlineTileSourceBase GoogleHybrid = new XYTileSource("Google-Hybrid",
                0, 19, 256, ".png", new String[] {
                "http://mts0.google.com"
        }) {
            @Override
            public String getTileURLString(MapTile aTile) {
                //return getBaseUrl() + "/vt/lyrs=y&x=" + aTile.getX() + "&y=" +aTile.getY() + "&z=" + aTile.getZoomLevel();
                return getBaseUrl() + "/vt/lyrs=y@113&hl=en&src=app&x=" + aTile.getX() +"&y="+aTile.getY()+"&z="+aTile.getZoomLevel()+"&s=Gal&apistyle=s.t:17|s.e:Al|p.v:off";
            }
        };

        // Google roads
        public static final OnlineTileSourceBase GoogleRoads = new XYTileSource("Google-Hybrid",
                0, 19, 256, ".png", new String[] {
                "http://mts0.google.com"
        }) {
            @Override
            public String getTileURLString(MapTile aTile) {
                //return getBaseUrl() + "/vt/lyrs=y&x=" + aTile.getX() + "&y=" +aTile.getY() + "&z=" + aTile.getZoomLevel();
                return getBaseUrl() + "/vt/lyrs=m@113&hl=en&src=app&x=" + aTile.getX() +"&y="+aTile.getY()+"&z="+aTile.getZoomLevel()+"&s=Gal&apistyle=s.t:17|s.e:Al|p.v:off";
            }
        };



}
