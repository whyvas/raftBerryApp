package com.whyvas.yammr.raftberry;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.util.AndroidUtil;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.datastore.MapDataStore;
import org.mapsforge.map.datastore.MultiMapDataStore;
import org.mapsforge.map.layer.Layers;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.overlay.Grid;
import org.mapsforge.map.layer.overlay.Marker;
import org.mapsforge.map.layer.overlay.Polygon;
import org.mapsforge.map.layer.overlay.Polyline;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.reader.MapFile;
import org.mapsforge.map.rendertheme.InternalRenderTheme;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class mapFragment extends Fragment {

        private static final int TOUCH_RADIUS = 16;
        private OnFragmentInteractionListener mListener;
        public MapView mapView;
        MultiMapDataStore multiMapDataStore;
        //private LocationManager mLocationManager;
        boolean followOwnship = false;
        Grid grid;
        Polygon sensorFootPrint = null;
        Marker ownShipMarker = null;
        Polyline leftLine = null;
        Polyline rightLine = null;
        List<LatLong> geoPointsForSensorFootprint = new ArrayList<>();
        List<LatLong> geoPointsForLeftLine = new ArrayList<>();
        List<LatLong> geoPointsForRightLine = new ArrayList<>();
        public boolean positionSlewed = false;
        public LatLong slewPosition = new LatLong(45.4284, -75.6863);



        public mapFragment() {
            super();
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            try {
                super.onCreate(savedInstanceState);
                AndroidGraphicFactory.createInstance(getActivity().getApplication());
                getActivity().setTitle(getClass().getSimpleName());

                //Check for a raftBerry/maps folder on the device. creates it if it doesn't exist.
                File folder = new File(Environment.getExternalStorageDirectory() + File.separator + "/raftBerry/maps");
                if (!folder.exists()) {
                    folder.mkdirs();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.raftberry, container, false);
            this.mapView = rootView.findViewById(R.id.MapsForge);
            this.mapView.setClickable(true);
            this.mapView.setZoomLevelMin((byte) 3);
            this.mapView.setZoomLevelMax((byte) 21);
            this.mapView.getMapScaleBar().setVisible(false);

            createLayers();

            return rootView;
        }

        @Override
        public void onDestroy() {
            if (this.mapView != null) {
                this.mapView.destroyAll();
                AndroidGraphicFactory.clearResourceMemoryCache();
            }
            //persistenceManager.close();
            super.onDestroy();
        }

        protected void createLayers() {
            try {

                TileCache tileCache = AndroidUtil.createTileCache(getContext(), "mapcache",
                        mapView.getModel().displayModel.getTileSize(), 1f,
                        mapView.getModel().frameBufferModel.getOverdrawFactor());

                //This loads all of the maps from raftBerry/maps into a MultiMapDataStore
                multiMapDataStore = new MultiMapDataStore(MultiMapDataStore.DataPolicy.RETURN_ALL);
                String path = Environment.getExternalStorageDirectory().toString() + "/raftBerry/maps";
                File directory = new File(path);
                File[] files = directory.listFiles();
                for (int i = 0; i < files.length; i++) {
                    Log.d("Files", "FileName:" + files[i].getName());
                    if (files[i].getName().endsWith("map")) {
                        File mapFile = new File(Environment.getExternalStorageDirectory(), "/raftBerry/maps/" + files[i].getName());
                        MapDataStore mapDataStore = new MapFile(mapFile);
                        multiMapDataStore.addMapDataStore(mapDataStore, true, true);
                    } else {
                        Log.e("File doesn't have .map extension, skipping: ", files[i].getName());
                    }
                }

                TileRendererLayer tileRendererLayer = new TileRendererLayer(tileCache, multiMapDataStore,
                        this.mapView.getModel().mapViewPosition, AndroidGraphicFactory.INSTANCE) {
                    @Override
                    public boolean onLongPress(LatLong geoPoint, Point viewPosition,
                                               Point tapPoint) {


                        return true;

                    }
                };


                tileRendererLayer.setXmlRenderTheme(InternalRenderTheme.DEFAULT);
                this.mapView.getLayerManager().getLayers().add(tileRendererLayer);
                Layers layers = this.mapView.getLayerManager().getLayers();


                this.mapView.setCenter(new LatLong(45.4284, -75.6843));
                this.mapView.setZoomLevel((byte) 14);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }


        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            if (context instanceof OnFragmentInteractionListener) {
                mListener = (OnFragmentInteractionListener) context;
            } else {
                throw new RuntimeException(context.toString()
                        + " must implement OnFragmentInteractionListener");
            }
            //   mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        }

        @Override
        public void onDetach() {
            super.onDetach();
            mListener = null;
        }

        @Override
        public void onResume() {
            super.onResume();
            //  mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            //  mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }

        @Override
        public void onPause() {
            super.onPause();

            //    mLocationManager.removeUpdates(this);
        }
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
