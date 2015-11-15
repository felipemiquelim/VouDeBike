package br.com.puc.facebookproject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginClient;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.appdatasearch.GetRecentContextCall;

import br.com.puc.facebookproject.ciclista.gerenciar_ciclista;
import br.com.puc.facebookproject.localizacao.localizacao;
import br.com.puc.facebookproject.localizacao.localizacaoDB;
import br.com.puc.facebookproject.maps.Direcion;
import br.com.puc.facebookproject.maps.MapsActivity;

/**
 * Created by Felipe on 12/10/2015.
 */
public class menu extends AppCompatActivity {
    ListView listView;
    ArrayAdapter<String> listAdapter;
    String fragmentArray[] = {"GERENCIAR CONTA", "ADMIN"};
    DrawerLayout drawerLayout;
    Boolean admin = false;

    private CallbackManager mCallBackManager;
    private FacebookCallback<LoginResult> mCallback = new FacebookCallback<LoginResult>() {
        private ProfileTracker mProfileTracker;

        @Override
        public void onSuccess(LoginResult loginResult) {
            mProfileTracker = new ProfileTracker() {
                @Override
                protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                    Log.v("facebook - profile", profile2.getFirstName());
                    Log.v("facebook - profile", profile2.getName());
                    mProfileTracker.stopTracking();
                }
            };
            mProfileTracker.startTracking();

            //VOLTA AO LOGIN
            Intent i = new Intent(menu.this, MainActivity.class);
            startActivity(i);
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException error) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        mCallBackManager = CallbackManager.Factory.create();
        LoginButton logoutButton = (LoginButton) findViewById(R.id.logout_button);
        //loginButton.setReadPermissions("user_friends");

        logoutButton.registerCallback(mCallBackManager, mCallback);

        //Configura o menu Lateral
        setDrawers();

        //Verifica se o ciclista já está cadastrado no BD
        clienteExiste();

        //Verifica se é Admin
        administrador();

    }

    private void setDrawers() {
        listView = (ListView) findViewById(R.id.listView);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fragmentArray);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment fragment;
                switch (position) {
                    case 0:
                        //fragment = new FragmentOne();
                        Intent i = new Intent(menu.this, gerenciar_ciclista.class);
                        startActivity(i);
                        break;
                    case 1:
                        //fragment = new FragmentTwo();
                        if (admin) {
                            Intent i4 = new Intent(menu.this, admin.class);
                            startActivity(i4);
                        } else
                            Toast.makeText(getApplicationContext(), "Permissões insuficientes", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        break;
                }
                //FragmentManager fragmentManager = getSupportFragmentManager();
                //fragmentManager .beginTransaction().replace(R.id.linearLayout, fragment).commit();
                drawerLayout.closeDrawers();
            }
        });
    }

    private void administrador() {
        String method = "verificaAdmin";
        Profile profile = Profile.getCurrentProfile();

        BackgroundTask backgroundTask = new BackgroundTask(getApplicationContext(), this);
        backgroundTask.execute(method, profile.getName());
    }

    private void clienteExiste() {
        String method = "verificaCliente";
        Profile profile = Profile.getCurrentProfile();

        BackgroundTask backgroundTask = new BackgroundTask(getApplicationContext(), this);
        backgroundTask.execute(method, profile.getId());
    }

    public void onButtonClick(View v) {
        if (v.getId() == R.id.btnFacebook) {
            /*Intent i = new Intent(menu.this, shareonfacebook.class);
            startActivity(i);
            Intent i = new Intent(menu.this, sharelocationgps.class);
            startActivity(i);
            Intent i = new Intent(menu.this, shareactivity.class);
            startActivity(i);*/
        }
        if (v.getId() == R.id.btnEstabelecimento) {
            Intent i = new Intent(menu.this, MapsActivity.class);
            startActivity(i);
        }
        if (v.getId() == R.id.btnRota) {
            Intent i = new Intent(menu.this, Direcion.class);
            startActivity(i);

        }
        if (v.getId() == R.id.btnLocalizacao) {
            Intent i = new Intent(menu.this, localizacao.class);
            startActivity(i);
        }

    }




        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (mCallBackManager.onActivityResult(requestCode, resultCode, data)) {
                return;
            }
        }

    public void faceLogout(View view) {
        LoginManager.getInstance().logOut();
        Intent i = new Intent(menu.this, MainActivity.class);
        startActivity(i);
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }


   /*private void teste(){
        GraphRequestBatch batch = new GraphRequestBatch(
                GraphRequest.newMyFriendsRequest(
                        accessToken,
                        new GraphRequest.GraphJSONArrayCallback() {
                            @Override
                            public void onCompleted(
                                    JSONArray jsonArray,
                                    GraphResponse response) {
                                // Application code for users friends
                                System.out.println("getFriendsData onCompleted : jsonArray " + jsonArray);
                                System.out.println("getFriendsData onCompleted : response " + response);
                                try {
                                    JSONObject jsonObject = response.getJSONObject();
                                    System.out.println("getFriendsData onCompleted : jsonObject " + jsonObject);
                                    JSONObject summary = jsonObject.getJSONObject("summary");
                                    System.out.println("getFriendsData onCompleted : summary total_count - " + summary.getString("total_count"));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        })

        );
        batch.addCallback(new GraphRequestBatch.Callback() {
            @Override
            public void onBatchCompleted(GraphRequestBatch graphRequests) {
                // Application code for when the batch finishes
            }
        });
        batch.executeAsync();

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,picture");


    }*/

}
