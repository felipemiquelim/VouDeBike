package br.com.puc.facebookproject.maps;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.facebook.Profile;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import br.com.puc.facebookproject.GPSTracker;
import br.com.puc.facebookproject.R;
import br.com.puc.facebookproject.ciclista.controler_ciclista;

public class TelaEstabelecimento extends FragmentActivity {
    String[] listaMarkers;
    private Spinner spinner;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Double newEstabelecimentoLat, newEstabelecimentoLong;
    Boolean admin = false;
    String IDAprovar;

    public void setAdmin(Boolean admin) {
        this.admin = admin;

        Button btnAprovar = (Button) findViewById(R.id.btnAprovar);
        Button btnRemover = (Button) findViewById(R.id.btnRemover);
        if (admin) {
            btnAprovar.setVisibility(View.VISIBLE);
            btnRemover.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        administrador();
        getMarkers();
        setUpMapIfNeeded();
    }

    private void getMarkers() {
        String method = "select";
        controler_estabelecimento backgroundTask = new controler_estabelecimento(this, this);
        backgroundTask.execute(method);
    }

    private void administrador() {
        String method = "verificaAdmin";
        Profile profile = Profile.getCurrentProfile();

        controler_ciclista backgroundTask = new controler_ciclista(getApplicationContext(), this);
        backgroundTask.execute(method, profile.getName());
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

                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                    @Override
                    public void onMapClick(LatLng point) {
                        mMap.clear();
                        getMarkers();
                        putMarkers();


                        mMap.addMarker(new MarkerOptions().position(point)
                                .draggable(true)
                                .title("New Place")
                                .snippet("Click on the button below to add")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.cycling)));

                        newEstabelecimentoLat = point.latitude;
                        newEstabelecimentoLong = point.longitude;

                        Button btnIncluir = (Button) findViewById(R.id.btnIncluir);
                        btnIncluir.setEnabled(true);
                    }
                });

                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker arg0) {
                        if (arg0.isInfoWindowShown()) {
                            arg0.hideInfoWindow();
                        } else {
                            arg0.showInfoWindow();
                        }
                        String[] aprovar = arg0.getTitle().split(";");

                        IDAprovar = aprovar[0];

                        //tv.setText(myMarker.getTitle());    //Change TextView text here like this

                        return true;
                    }
                });


                setUpMap();
            }
        }
    }


    private void setUpMap() {
        GPSTracker gps = new GPSTracker(this);

        if (gps.canGetLocation()) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            LatLng coordinate = new LatLng(latitude, longitude);
            CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 14);
            mMap.animateCamera(yourLocation);

        }

        mMap.setMyLocationEnabled(true);
        addListenerOnSpinnerItemSelection();
        putMarkers();
        Button btnIncluir = (Button) findViewById(R.id.btnIncluir);
        btnIncluir.setEnabled(false);


    }

    private void addListenerOnSpinnerItemSelection() {
        spinner = (Spinner) findViewById(R.id.spRota);
    }


    private void putMarkers() {

        if (listaMarkers != null) {
            for (int i = 0; i < listaMarkers.length; i++) {
                String[] marcador = listaMarkers[i].split(";");
                if(marcador[4].equals(spinner.getSelectedItem().toString().toUpperCase()) || (spinner.getSelectedItem().toString().equals("Todos"))
                        || (spinner.getSelectedItemId() == 4)) {
                    MarkerOptions mo = new MarkerOptions();
                    mo.position(new LatLng(Float.valueOf(marcador[2]), Float.valueOf(marcador[3])));
                    mo.title(marcador[6] + ";" + marcador[0] + " Tel:" + marcador[5]);
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

           // listaMarkers = null;
        }
    }

    public void setListaMarkers(String[] listaMarkers) {
        this.listaMarkers = listaMarkers;
    }


    public void filtrar(View view) {
        mMap.clear();

        NaoAprovados();
        if (spinner.getSelectedItemPosition() != 4) {
            listaMarkers = null;
            getMarkers();
        }
        putMarkers();
    }

    private void NaoAprovados() {
        if (spinner.getSelectedItemPosition() == 4) {
            listaMarkers = null;
            String method="selectAllPend2";

            controler_estabelecimento db = new controler_estabelecimento(getApplicationContext(), this);
            db.execute(method);
        }
        else
            getMarkers();
    }

    public void onButtonClick(View v){
        if(v.getId()==R.id.btnIncluir){
            Intent i = new Intent(TelaEstabelecimento.this,cadastro_estabelecimento.class);
            Bundle b = new Bundle();
            b.putDouble("lat", newEstabelecimentoLat);
            b.putDouble("long", newEstabelecimentoLong);
            i.putExtras(b); //Put your id to your next Intent
            startActivity(i);
        }
        if(v.getId()==R.id.btnAprovar) {
            if (IDAprovar != null) {
                String method = "aprovar";

                controler_estabelecimento db = new controler_estabelecimento(getApplicationContext(), this);
                db.execute(method, IDAprovar);
                IDAprovar = null;
                mMap.clear();

                NaoAprovados();
            } else
                Toast.makeText(getApplicationContext(), "Clique em um marcador", Toast.LENGTH_SHORT).show();
        }
        if(v.getId()==R.id.btnRemover) {
            if (IDAprovar != null) {
                String method = "remover";

                controler_estabelecimento db = new controler_estabelecimento(getApplicationContext(), this);
                db.execute(method, IDAprovar);
                IDAprovar = null;
                mMap.clear();

                NaoAprovados();
            } else
                Toast.makeText(getApplicationContext(), "Clique em um marcador", Toast.LENGTH_SHORT).show();
        }

    }

}
