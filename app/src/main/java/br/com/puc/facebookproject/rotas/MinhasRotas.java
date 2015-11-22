package br.com.puc.facebookproject.rotas;

/**
 * Created by Felipe on 21/11/2015.
 */

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.http.AndroidHttpClient;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.facebook.Profile;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import br.com.puc.facebookproject.GPSTracker;
import br.com.puc.facebookproject.R;


public class MinhasRotas extends FragmentActivity {
    private SupportMapFragment mapFrag;
    private GoogleMap map;
    private Marker marker;
    private Polyline polyline;
    private List<LatLng> list;
    private long distance;
    private String[] rotas;
    private String[] rotasFull;
    //Felipe
    LatLng latlongOrigem;
    LatLng latlongDestino;
    Double latUltimo, longUltimo;
    String alias;

    public void setRotas(String[] ro) {
        this.rotas = ro;

        rotasFull = new String[ro.length + 1];
        for(int i=0; i<ro.length;i++) {
            rotasFull[i] = ro[i];
        }

        incluirRotas();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.minhasrotas);

        GoogleMapOptions options = new GoogleMapOptions();
        options.zOrderOnTop(true);
        //mapFrag = SupportMapFragment.newInstance(options);
        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.replace(R.id.llContainer, mapFrag);
//        ft.commit();
        buscarRotas();
        configSpin();
    }



    @Override
    public void onResume(){
        super.onResume();

        new Thread(){
            public void run(){
                while(mapFrag.getMap() == null){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                runOnUiThread(new Runnable(){
                    public void run(){
                        configMap();
                    }
                });
            }
        }.start();
    }

    public void configMap(){
        map = mapFrag.getMap();
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        list = new ArrayList<LatLng>();

        //Felipe
        map.setMyLocationEnabled(true);
        GPSTracker gps = new GPSTracker(this);
        if (gps.canGetLocation()) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            LatLng coordinate = new LatLng(latitude, longitude);
            CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 14);
            map.animateCamera(yourLocation);

        }

        //LatLng latLng = new LatLng(-23.5645445, -46.6856809);
        //CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(13).build();
        //CameraUpdate update = CameraUpdateFactory.newCameraPosition(cameraPosition);

        //map.moveCamera(update);
        //map.animateCamera(update);
//		map.animateCamera(update, 3000, new CancelableCallback(){
//			@Override
//			public void onCancel() {
//			}
//
//			@Override
//			public void onFinish() {
//			}
//		});

        //EVENTS
        map.setOnCameraChangeListener(new OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
					/*if(marker != null){
						marker.remove();
					}

					customAddMarker(new LatLng(cameraPosition.target.latitude, cameraPosition.target.longitude), "1: Marcador Alterado", "O Marcador foi reposicionado");
										*/
            }
        });

        map.setOnMapClickListener(new OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if(marker != null){
                    marker.remove();
                }

               // customAddMarker(new LatLng(latLng.latitude, latLng.longitude), "2: Marcador Alterado", "O Marcador foi reposicionado");
               // list.add(latLng);
               // drawRoute();
            }
        });
    }

    public void customAddMarker(LatLng latLng, String title, String snippet){
        MarkerOptions options = new MarkerOptions();
        options.position(latLng).title(title).snippet(snippet).draggable(true);

        marker = map.addMarker(options);

    }

    public void drawRoute(){
        PolylineOptions po;

        if(polyline == null){
            po = new PolylineOptions();
            for(int i = 0, tam = list.size(); i < tam; i++){
                po.add(list.get(i));
            }

            po.color(Color.RED);
            polyline = map.addPolyline(po);
        }else{
            polyline.setPoints(list);
        }
    }

    public void getDistance(View view){
		/*double distance = 0;*/

        for(int i = 0, tam = list.size(); i < tam; i++){
            if (i < tam-1){
                distance += distance(list.get(i), list.get(i+1));
            }
        }

        Toast.makeText(MinhasRotas.this, "Distancia: " + distance + " metros", Toast.LENGTH_LONG).show();
    }

    public static double distance(LatLng startP, LatLng endP){
        double lat1 = startP.latitude;
        double lat2 = endP.latitude;
        double lon1 = startP.longitude;
        double lon2 = endP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return 6366000 * c;
    }

    public void getLocation(View view){
        Geocoder gc = new Geocoder(MinhasRotas.this);

        List<Address> AdressList;
        try {
            AdressList = gc.getFromLocation(list.get(list.size()-1).latitude, list.get(list.size()-1).longitude, 1);
            //AdressList = gc.getFromLocationName("Av paulista, São Paulo, São Paulo, Brasil", 1);

            String adress = AdressList.get(0).getThoroughfare()+"\n";
            adress += "Bairro: " + AdressList.get(0).getSubLocality()+"\n";
            adress += "Cidade: " + AdressList.get(0).getLocality()+"\n";
            adress += "Estado: " + AdressList.get(0).getAdminArea()+"\n";
            adress += "Pais: " + AdressList.get(0).getCountryName();

            LatLng ll = new LatLng(AdressList.get(0).getLatitude(), AdressList.get(0).getLongitude());

            Toast.makeText(MinhasRotas.this, "Local: " + adress + "\n" + ll, Toast.LENGTH_LONG).show();
            //Toast.makeText(MainActivity.this, "Local: " + ll, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

	/* ***************************************** ROTA ***************************************** */

    public void getRouteByGMAV2() throws UnsupportedEncodingException{
        EditText etO = (EditText) findViewById(R.id.origin);
        EditText etD = (EditText) findViewById(R.id.destination);
        String origin = URLEncoder.encode(etO.getText().toString(), "UTF-8");
        String destination = URLEncoder.encode(etD.getText().toString(), "UTF-8");

        getRoute(origin, destination);
        //getRoute(new LatLng(-20.195403, -40.234478), new LatLng(-20.304596, -40.291813));
    }

    // WEB CONNECTION
    public void getRoute(final String origin, final String destination){
        //public void getRoute(final LatLng origin, final LatLng destination){
        new Thread(){
            public void run(){
                String url= "http://maps.googleapis.com/maps/api/directions/json?mode=bicycling&origin="
                        + origin+"&destination="
                        + destination+"&sensor=false";
				/*String url= "http://maps.googleapis.com/maps/api/directions/json?origin="
						+ origin.latitude+","+origin.longitude+"&destination="
						+ destination.latitude+","+destination.longitude+"&sensor=false";*/


                HttpResponse response;
                HttpGet request;
                AndroidHttpClient client = AndroidHttpClient.newInstance("route");

                request = new HttpGet(url);
                try {
                    response = client.execute(request);
                    final String answer = EntityUtils.toString(response.getEntity());

                    runOnUiThread(new Runnable(){
                        public void run(){
                            try {
                                //Log.i("Script", answer);
                                list = buildJSONRoute(answer);
                                drawRoute();
                            }
                            catch(JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }
                catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    // PARSER JSON
    public List<LatLng> buildJSONRoute(String json) throws JSONException{
        JSONObject result = new JSONObject(json);
        JSONArray routes = result.getJSONArray("routes");

        distance = routes.getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONObject("distance").getInt("value");

        JSONArray steps = routes.getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONArray("steps");
        List<LatLng> lines = new ArrayList<LatLng>();

        for(int i=0; i < steps.length(); i++) {
            Log.i("Script", "STEP: LAT: "+steps.getJSONObject(i).getJSONObject("start_location").getDouble("lat")+" | LNG: "+steps.getJSONObject(i).getJSONObject("start_location").getDouble("lng"));


            String polyline = steps.getJSONObject(i).getJSONObject("polyline").getString("points");

            for(LatLng p : decodePolyline(polyline)) {
                lines.add(p);
            }

            Log.i("Script", "STEP: LAT: "+steps.getJSONObject(i).getJSONObject("end_location").getDouble("lat")+" | LNG: "+steps.getJSONObject(i).getJSONObject("end_location").getDouble("lng"));
        }

        return(lines);
    }

    // DECODE POLYLINE
    private List<LatLng> decodePolyline(String encoded) {

        List<LatLng> listPoints = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        //Felipe
        boolean gravaInicio = true;

        while (index < len) {

            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)), (((double) lng / 1E5)));
            Log.i("Script", "POL: LAT: " + p.latitude + " | LNG: " + p.longitude);

            //Felipe
            if(gravaInicio) {
                latlongOrigem = new LatLng(p.latitude, p.longitude);
                gravaInicio = false;
            }
            latUltimo = p.latitude;
            longUltimo = p.longitude;


            listPoints.add(p);
        }

        //Felipe
        latlongDestino = new LatLng(latUltimo, longUltimo);
        configuraZoom();
        return listPoints;
        //Felipe

    }

    private void configuraZoom() {
        //double latitude =
        //double longitude = gps.getLongitude();
        LatLng coordinate = latlongOrigem;
        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 14);
        map.animateCamera(yourLocation);
    }


    private void incluirRotas() {

        if (rotas != null) {
            String[] linha;
            for (int i =0; i<rotas.length; i++){
                linha = rotas[i].split(";");
                rotas[i] = linha[0];
            }

            if (rotas.length > 0) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        this, android.R.layout.simple_spinner_item, rotas);

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Spinner sRotas = (Spinner) findViewById(R.id.spRota);
                sRotas.setAdapter(adapter);
            }
        }
    }

    private void buscarRotas() {
        String method="selectAll";
        Profile profile = Profile.getCurrentProfile();

        RotaDB db = new RotaDB(getApplicationContext(), this);
        db.execute(method, profile.getId());
    }

    private void configSpin() {
        Spinner sRotas = (Spinner) findViewById(R.id.spRota);
        sRotas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                EditText edtO = (EditText) findViewById(R.id.origin);
                EditText edtD = (EditText) findViewById(R.id.destination);
                String[] linha = rotasFull[position].split(";");

                edtO.setText(linha[1]);
                edtD.setText(linha[2]);
                try {
                    getRouteByGMAV2();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }




}
