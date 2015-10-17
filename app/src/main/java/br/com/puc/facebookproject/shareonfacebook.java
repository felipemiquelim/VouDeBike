package br.com.puc.facebookproject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

/**
 * Created by Felipe on 14/10/2015.
 */
public class shareonfacebook extends Activity {
    private Button bShare;
    private Profile profile;
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shareonfacebook);
        getCurrProfile();

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        // this part is optional
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Toast.makeText(shareonfacebook.this, "Success", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(shareonfacebook.this, "Fail", Toast.LENGTH_LONG).show();
            }
        });

        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle("Vou de Bike")
                    .setContentDescription("Teste do App Vou de Bike")
                    .setContentUrl(Uri.parse("http://www.pucsp.br"))
                    .build();

            shareDialog.show(linkContent);
        }

        bShare = (Button) findViewById(R.id.bShare);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void getCurrProfile() {
        profile = Profile.getCurrentProfile();
    }


    //Share
    /*private boolean bReauth = false;
    public void shareContent() {
        LoginManager lg = LoginManager.getInstance();
        if (profile != null) {
            List<String> permissions = (List) AccessToken.getCurrentAccessToken().getPermissions();
            List<String> newPermissions = Arrays.asList("publish_actions");

            if (verifyPermissions(permissions, newPermissions)) {
                bReauth = true;
                lg.logInWithReadPermissions(shareonfacebook.this, newPermissions);
                return;
            }

            Bundle params = new Bundle();
            params.putString("name", "VouDeBike");
            params.putString("caption", "Ficou mais fácil andar de Bike");
            params.putString("description", "D: Ficou mais fácil andar de Bike");
            params.putString("link", "http://www.google.com.br");
            params.putString("picture", "http://industriacriativa.espm.br/ic/wp-content/uploads/2012/09/va-de-bicicleta-para-o-trabalho-38-594-588x405.jpg");


        }
    }
    public boolean verifyPermissions(List<String> permissions, List<String> newPermissions) {
        for (String p : permissions) {
            if (newPermissions.contains(p)) {
                return (true);
            }
        }
        return (false);
    }*/
}
