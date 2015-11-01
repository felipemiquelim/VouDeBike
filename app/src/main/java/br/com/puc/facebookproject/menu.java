package br.com.puc.facebookproject;

import android.app.Activity;
import android.content.Intent;
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
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import br.com.puc.br.com.puc.fragment.FragmentOne;
import br.com.puc.br.com.puc.fragment.FragmentTwo;
import br.com.puc.facebookproject.ciclista.gerenciar_ciclista;

/**
 * Created by Felipe on 12/10/2015.
 */
public class menu extends AppCompatActivity {
    ListView listView;
    ArrayAdapter<String> listAdapter;
    String fragmentArray[] = {"GERENCIAR CONTA", "FRAGMENT 2"};
    DrawerLayout drawerLayout;

    private CallbackManager mCallBackManager;
    private FacebookCallback<LoginResult> mCallback= new FacebookCallback<LoginResult>() {
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
        LoginButton logoutButton =  (LoginButton) findViewById(R.id.logout_button);
        //loginButton.setReadPermissions("user_friends");

        logoutButton.registerCallback(mCallBackManager, mCallback);

        //Configura o menu Lateral
        setDrawers();

        //Verifica se o ciclista já está cadastrado no BD
        clienteExiste();

    }

    private void setDrawers() {
        listView = (ListView) findViewById(R.id.listView);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,fragmentArray);
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
                        fragment = new FragmentTwo();
                        break;
                    default:
                        fragment = new FragmentOne();
                        break;
                }
                //FragmentManager fragmentManager = getSupportFragmentManager();
                //fragmentManager .beginTransaction().replace(R.id.linearLayout, fragment).commit();
                drawerLayout.closeDrawers();
            }
        });
    }

    private void clienteExiste() {
        String method="verificaCliente";
        Profile profile = Profile.getCurrentProfile();

        BackgroundTask backgroundTask = new BackgroundTask(getApplicationContext(),this);
        backgroundTask.execute(method, profile.getName());
    }

    public void onButtonClick(View v) {
        if (v.getId() == R.id.bShareOnFacebook) {
            Intent i = new Intent(menu.this, shareonfacebook.class);
            startActivity(i);
        }
        if (v.getId() == R.id.bShareLocation) {
            Intent i = new Intent(menu.this, sharelocationgps.class);
            startActivity(i);
        }
        if (v.getId() == R.id.bShareActivity) {
            Intent i = new Intent(menu.this, shareactivity.class);
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

}
