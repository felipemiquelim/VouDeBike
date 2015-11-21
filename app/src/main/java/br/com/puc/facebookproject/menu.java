package br.com.puc.facebookproject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import br.com.puc.facebookproject.ciclista.gerenciar_ciclista;
import br.com.puc.facebookproject.facebook.facebookHub;
import br.com.puc.facebookproject.localizacao.localizacao;
import br.com.puc.facebookproject.maps.Direcion;
import br.com.puc.facebookproject.maps.MapsActivity;

/**
 * Created by Felipe on 12/10/2015.
 */
public class menu extends AppCompatActivity {
    ProgressDialog progress;
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

        //Verifica o GPS
        verificarGPS();

        //Verifica se tem Internet
        if (verificaNET()) {
            //Verifica se o ciclista já está cadastrado no BD
            clienteExiste();
            //Verifica se é Admin
            administrador();
        }
        else
            //Toast.makeText(getApplicationContext(), "Ative a conexão Web", Toast.LENGTH_LONG).show();
            ativarNET();
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
    private boolean verificaNET() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void verificarGPS() {

        LocationManager manager = (LocationManager)getSystemService(getApplicationContext().LOCATION_SERVICE);
        if(!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //Ask the user to enable GPS
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Ativar GPS");
            builder.setMessage("É necessário ativar o GPS para continuar. Deseja ativar o GPS?");
            builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Launch settings, allowing user to make a change
                    Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(i);
                }
            });
            builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //No location service, no Activity
                    finish();
                }
            });
            builder.create().show();
        }
    }

    private void ativarNET() {

        //Ask the user to enable GPS
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ativar Internet");
        builder.setMessage("É necessário conectar à Internet. Como deseja se conectar?");
        builder.setPositiveButton("Wifi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Launch settings, allowing user to make a change
                Intent i = new Intent(Settings.ACTION_WIFI_SETTINGS);
                startActivity(i);
            }
        });
        builder.setNegativeButton("Dados", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(
                        "com.android.settings",
                        "com.android.settings.Settings$DataUsageSummaryActivity"));
                startActivity(intent);
            }
        });
        builder.create().show();

    }

    public void onButtonClick(View v) {
        if (v.getId() == R.id.btnFacebook) {
            Intent i = new Intent(menu.this, facebookHub.class);
            startActivity(i);
            /*
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
