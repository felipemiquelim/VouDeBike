package br.com.puc.facebookproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.ShareOpenGraphAction;
import com.facebook.share.model.ShareOpenGraphContent;
import com.facebook.share.model.ShareOpenGraphObject;
import com.facebook.share.widget.ShareDialog;

import org.kobjects.util.Util;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Felipe on 17/10/2015.
 */
public class sharelocationgps extends Activity {
    TextView _addressText, _locationText;
    LocationManager _locationManager;
    String _locationProvider;
    Location _currentLocation;

    //SHARE OBJECTS
    private Button bShare;
    private Profile profile;
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sharelocationgps);

        _addressText = (TextView) findViewById(R.id.address_text);
        _locationText = (TextView) findViewById(R.id.location_text);

        InitializeLocationManager();

    }


    //SHARE METHOD
    private void ShareOnFacebook() {

        profile = Profile.getCurrentProfile();

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        // this part is optional
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Toast.makeText(sharelocationgps.this, "Success", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(sharelocationgps.this, "Fail", Toast.LENGTH_LONG).show();
            }
        });

        String mapsURL = "https://www.google.com.br/maps/@";
        mapsURL += String.valueOf(_currentLocation.getLatitude());
        mapsURL += ",";
        mapsURL += String.valueOf(_currentLocation.getLongitude());
        mapsURL += ",20z?hl=pt";

        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle("Vou de Bike")
                    .setContentDescription( _addressText.getText().toString())
                    .setContentUrl(Uri.parse(mapsURL))
                    .build();


            shareDialog.show(linkContent);
        }

        bShare = (Button) findViewById(R.id.bShare);
    }

    //SHARE AUX
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void InitializeLocationManager() {
        Criteria criteriaForLocationService = new Criteria();
        //criteriaForLocationService.setAccuracy(Accuracy.Fine);
        _locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List<String> acceptableLocationProviders = _locationManager.getProviders(criteriaForLocationService, true);

        if (!acceptableLocationProviders.isEmpty()) {
            _locationProvider = acceptableLocationProviders.get(0);
        } else {
            _locationProvider = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            _locationManager.requestLocationUpdates(_locationProvider, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    _currentLocation = location;
                    if (_currentLocation == null) {
                        _locationText.setText("Unable to determine your location.");
                    } else {
                        _locationText.setText(String.format("{0},{1}", _currentLocation.getLatitude(), _currentLocation.getLongitude()));
                    }
                }

                @Override
                public void onProviderDisabled(String provider) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onProviderEnabled(String provider) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onStatusChanged(String provider, int status,
                                            Bundle extras) {
                    // TODO Auto-generated method stub
                }
            });
        }
        catch (Exception e) { }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
        _locationManager.removeUpdates((LocationListener) this);
        }
        catch (Exception e) { }
    }

    public void AddressButton_OnClick(View view) throws IOException, InterruptedException {
        if (_currentLocation == null)
        {
            _addressText.setText("Can't determine the current address.");
            return;
        }

        Geocoder geocoder = new Geocoder(this);
        List<Address> addressList = geocoder.getFromLocation(_currentLocation.getLatitude(), _currentLocation.getLongitude(), 10);


        if (!addressList.isEmpty())
        {
            Address address = addressList.get(0);
            StringBuilder deviceAddress = new StringBuilder();
            for (int i = 0; i < address.getMaxAddressLineIndex(); i++)
            {
                deviceAddress.append(address.getAddressLine(i))
                        .append(",");
            }
            _addressText.setText(deviceAddress.toString());
            //Log.d("Lat", String.valueOf(_currentLocation.getLatitude()));
            //Log.d("Lon", String.valueOf(_currentLocation.getLongitude()));
        }
        else
        {
            _addressText.setText("Unable to determine the address.");
        }

        
    }

    public void shareContent(View view) {
        /*try {
            _locationManager.removeUpdates((LocationListener) this);

        }
        catch (Exception e) {
            Log.d("Felipe", "Ërro na Loc");
        }*/
        _locationManager = null;
        ShareOnFacebook();
    }
}
