package br.com.puc.facebookproject.maps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import br.com.puc.facebookproject.R;
import br.com.puc.facebookproject.dataBase;

public class MapsActivity extends FragmentActivity {
    String[] listaMarkers;
    private Spinner spinner;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        getMarkers();
        setUpMapIfNeeded();
    }

    private void getMarkers() {
        String method = "select";
        dataBase backgroundTask = new dataBase(this, this);
        backgroundTask.execute(method);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

                    private float currentZoom = -1;

                    @Override
                    public void onCameraChange(CameraPosition pos) {
                        if (pos.zoom != currentZoom){
                            currentZoom = pos.zoom;
                            putMarkers();
                        }
                    }
                });
                setUpMap();

            }
        }
    }

    private void setUpMap() {
        mMap.setMyLocationEnabled(true);
        addListenerOnSpinnerItemSelection();
        putMarkers();
    }

    private void addListenerOnSpinnerItemSelection() {
        spinner = (Spinner) findViewById(R.id.spinner);
    }


    private void putMarkers() {

        if (listaMarkers != null) {
            for (int i = 0; i < listaMarkers.length; i++) {
                String[] marcador = listaMarkers[i].split(";");
                if(marcador[4].equals(spinner.getSelectedItem().toString().toUpperCase()) || spinner.getSelectedItem().toString().equals("Todos")) {

                    MarkerOptions mo = new MarkerOptions();
                    mo.position(new LatLng(Float.valueOf(marcador[2]), Float.valueOf(marcador[3])));
                    mo.title(marcador[0] + " Tel:" + marcador[5]);
                    mo.snippet(marcador[1]);

                    switch (marcador[4]) {
                        case "COMER E BEBER":
                            mo.icon(BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                            break;
                        case "LAZER":
                            mo.icon(BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                            break;
                        case "HOSPEDAGEM":
                            mo.icon(BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_RED));
                            break;
                        case "BICICLETARIA":
                            mo.icon(BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                            break;
                        default:
                            mo.icon(BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                            break;
                    }

                    mMap.addMarker(mo);
                }
            }

            listaMarkers = null;
        }
    }

    public void setListaMarkers(String[] listaMarkers) {
        this.listaMarkers = listaMarkers;
    }


    public void filtrar(View view) {
        mMap.clear();
        getMarkers();
        putMarkers();
    }
}
