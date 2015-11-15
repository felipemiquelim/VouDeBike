package br.com.puc.facebookproject.localizacao;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.com.puc.facebookproject.GPSTracker;
import br.com.puc.facebookproject.R;
import br.com.puc.facebookproject.dataBase;

/**
 * Created by Felipe on 14/11/2015.
 */
public class localizacao extends FragmentActivity {
    // AccessToken accessToken;
   String[] listaMarkers, friendList;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.localizacao);
        setupScreen();
    }

    public void setFriendList(String[] friendList) {
        this.friendList = friendList;
        getMarkers();
    }

    private void setupScreen() {
        confirmar();
        new GetFriendList(this).execute();
        setUpMapIfNeeded();
    }

    private void confirmar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Gravar");
        builder.setMessage("Deseja salvar sua localização para que seus amigos possam ver?");

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                cadastrarLoc();

                Toast.makeText(localizacao.this, "Localização salva", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }

        });

        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(localizacao.this, "Localização não salva", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();

    }

    private void getMarkers() {
        String method = "selectFriends";
        localizacaoDB backgroundTask = new localizacaoDB(this, this);
        backgroundTask.setFriendList(friendList);
        backgroundTask.execute(method);

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
        GPSTracker gps = new GPSTracker(this);
        putMarkers();

        if (gps.canGetLocation()) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            LatLng coordinate = new LatLng(latitude, longitude);
            CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 14);
            mMap.animateCamera(yourLocation);

        }
        mMap.setMyLocationEnabled(true);

    }

    private Date ConvertToDate(String dateString){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyy-MM-dd HH:mm:ss");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return convertedDate;
    }

    private void putMarkers() {
        mMap.clear();
        if (listaMarkers != null) {
            for (int i = 0; i < listaMarkers.length; i++) {
                String[] marcador = listaMarkers[i].split(";");

                String opc = getFiltro();
                Date data = ConvertToDate(marcador[3]);
                Date dataAtual = ConvertToDate(getData());
                long diff = dataAtual.getTime() - data.getTime();
                long horas = diff / (60 * 60 * 1000);


                if(opc.equals("3") && horas<=1) {
                    MarkerOptions mo = new MarkerOptions();
                    mo.position(new LatLng(Float.valueOf(marcador[1]), Float.valueOf(marcador[2])));
                    mo.title(marcador[0]);// + " Tel:" + marcador[5]);
                    mo.snippet(marcador[3]);

                    mMap.addMarker(mo);
                }
                else if (opc.equals("2") && horas <=24) {
                        MarkerOptions mo = new MarkerOptions();
                        mo.position(new LatLng(Float.valueOf(marcador[1]), Float.valueOf(marcador[2])));
                        mo.title(marcador[0]);// + " Tel:" + marcador[5]);
                        mo.snippet(marcador[3]);

                        mMap.addMarker(mo);
                    }
                else if (opc.equals("1") && horas <= 48 ){
                        MarkerOptions mo = new MarkerOptions();
                        mo.position(new LatLng(Float.valueOf(marcador[1]), Float.valueOf(marcador[2])));
                        mo.title(marcador[0]);// + " Tel:" + marcador[5]);
                        mo.snippet(marcador[3]);

                        mMap.addMarker(mo);
                    }
                else if (opc.equals("0") && horas <= 168) {
                        MarkerOptions mo = new MarkerOptions();
                        mo.position(new LatLng(Float.valueOf(marcador[1]), Float.valueOf(marcador[2])));
                        mo.title(marcador[0]);// + " Tel:" + marcador[5]);
                        mo.snippet(marcador[3]);

                        mMap.addMarker(mo);
                    }
            }

            //listaMarkers = null;
        }
    }

    private String getData() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    public void setListaMarkers(String[] listaMarkers) {
        this.listaMarkers = listaMarkers;
    }

    private void cadastrarLoc() {
        Profile profile = Profile.getCurrentProfile();
        String id = profile.getId();

        LocationManager lm = (LocationManager) getSystemService(getApplicationContext().LOCATION_SERVICE);

        /*Location location;
        location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();*/
        GPSTracker gps = new GPSTracker(this);

        if (gps.canGetLocation()) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();


            String method = "register";
            localizacaoDB loc = new localizacaoDB(this);
            loc.execute(method, id, String.valueOf(latitude), String.valueOf(longitude));
        }
    }

    private String getFiltro() {

        Spinner spnFiltro = (Spinner) findViewById(R.id.spnData);

        String opc =  String.valueOf(spnFiltro.getSelectedItemPosition());

        /*
        0 - Última Semana
        1 - Ontem
        2 - Hoje
        3 - Última Hora
         */
        if (opc.isEmpty())
            opc = "0";

        return opc;
    }

    public void onButtonClick(View view) {
        if(view.getId()==R.id.btnFiltrar) {
            //getMarkers();
            putMarkers();
        }
        else  if(view.getId()==R.id.btnUpdate){
            cadastrarLoc();
            Toast.makeText(localizacao.this, "Localização salva", Toast.LENGTH_SHORT).show();
        }

    }


    class GetFriendList extends AsyncTask<String, String, String> {
        JSONArray jsonFriends;
        ProgressDialog pdig = new ProgressDialog(localizacao.this);
        localizacao parent;

        public GetFriendList(localizacao loc) {
            this.parent = loc;
        }


        @Override
        protected String doInBackground(String... params) {
            AccessToken accessToken = AccessToken.getCurrentAccessToken();
            Log.i("friends", "Working in background...");
            //LoginManager.getInstance().logInWithReadPermissions(FriendList.this, Arrays.asList("user_friends"));
            //Log.i(TAG,"Having token for: "+String.valueOf(AccessToken.getCurrentAccessToken().getPermissions()));
            if(accessToken!=null) {
                GraphRequestBatch batch = new GraphRequestBatch(
                        GraphRequest.newMyFriendsRequest(accessToken,
                                new GraphRequest.GraphJSONArrayCallback() {
                                    @Override
                                    public void onCompleted(JSONArray jarray,
                                                            GraphResponse response) {
                                        jsonFriends = jarray;
                                        Log.i("friends", "onCompleted: jsonArray "
                                                + jarray);
                                        Log.i("friends", "onCompleted: response "
                                                + response);
                                        //Toast.makeText(MainActivity.this, "result:" + jarray.toString(), Toast.LENGTH_LONG).show();
                                    }
                                }),
                        GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {

                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                System.out.println("meJSONObject: " + object);
                                System.out.println("meGraphResponse: " + response);

                            }
                        })
                );
                batch.addCallback(new GraphRequestBatch.Callback() {

                    @Override
                    public void onBatchCompleted(GraphRequestBatch batch) {
                        Log.i("friends", "onbatchCompleted: jsonArray "
                                + batch);
                    }
                });
                batch.executeAndWait();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                carregaFriends();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (pdig.isShowing())
                pdig.dismiss();
            super.onPostExecute(result);
        }

        private void carregaFriends() throws JSONException {
            String[] allNames = new String[jsonFriends.length()];

            for (int i=0; i<jsonFriends.length(); i++) {
                JSONObject actor = jsonFriends.getJSONObject(i);
                String id = actor.getString("id");
                allNames[i] =  id;
            }

            parent.setFriendList(allNames);
        }

        @Override
        protected void onPreExecute() {
            pdig.setTitle("Fetching");
            pdig.setMessage("Fetching facebook friends...");
            //pdig.show();
            Log.i("friends", "Starting...");
            super.onPreExecute();
        }


    }

}
