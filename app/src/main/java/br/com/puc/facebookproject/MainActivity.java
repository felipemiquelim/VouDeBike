package br.com.puc.facebookproject;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import android.content.pm.Signature;
import android.view.View;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import br.com.puc.sqlite.MySQLiteHelper;
import br.com.puc.sqlite.br.com.puc.sqlite.crud.TB_CICLISTA;
import br.com.puc.sqlite.ciclista;

public class MainActivity extends AppCompatActivity {
    private Button bNovaTela;
    private Profile profile;
    private AccessToken accessToken;
    private CallbackManager mCallBackManager;
    private FacebookCallback<LoginResult> mCallback= new FacebookCallback<LoginResult>() {
        private ProfileTracker mProfileTracker;

        @Override
        public void onSuccess(LoginResult loginResult) {
            mProfileTracker = new ProfileTracker() {
                @Override
                protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                    mProfileTracker.stopTracking();
                }
            };
            mProfileTracker.startTracking();
            accessToken = loginResult.getAccessToken();

            getProfile();

            if (profile != null) {
               /*Log.d("Name: ", profile.getFirstName());
                Log.d("Lastname: ", profile.getLastName());*/
                Intent i = new Intent(MainActivity.this, menu.class);
                startActivity(i);
            } else {
                Intent i = new Intent(MainActivity.this, MainActivity.class);
                startActivity(i);
            }

        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException error) {

        }
    };

    private void getProfile() {
        try {
            profile = Profile.getCurrentProfile();
        }
        catch (Exception e){
            Log.wtf("Erro", "FAIL");
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        //getUserInfo();

        mCallBackManager = CallbackManager.Factory.create();
        LoginButton loginButton =  (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");

        loginButton.registerCallback(mCallBackManager, mCallback);

        //Operacao de DB Antiga
        // CreateDataBaseStructure();

        //VERIFICA SE JÁ ESTA LOGADO, SE SIM VAI PARA O MENU
        getProfile();
        if (profile != null) {
            Intent i = new Intent(MainActivity.this, menu.class);
            startActivity(i);
        }

    }

    private void CreateDataBaseStructure() {

        MySQLiteHelper dbhelper = new MySQLiteHelper(this);
        TB_CICLISTA tb_ciclista = new TB_CICLISTA();

        dbhelper.teste(tb_ciclista);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getUserInfo(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("Key Hash:- ", Base64.encodeToString(md.digest(), Base64.DEFAULT));

            }
        }
        catch (PackageManager.NameNotFoundException e){}
        catch (NoSuchAlgorithmException e){}
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mCallBackManager.onActivityResult(requestCode, resultCode, data)) {
            return;
        }
    }




}
