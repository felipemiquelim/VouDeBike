package br.com.puc.facebookproject.facebook;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.widget.ProfilePictureView;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import br.com.puc.facebookproject.R;

/**
 * Created by Felipe on 21/11/2015.
 */
public class facebookHub extends Activity {
    private Profile profile;
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.facebook_hub);

        setImage();

    }

    private void setImage() {
        Profile profile = Profile.getCurrentProfile();
        ImageView user_picture;

        String id = profile.getId();

        ProfilePictureView profilePictureView;
        profilePictureView = (ProfilePictureView) findViewById(R.id.friendProfilePicture);
        profilePictureView.setProfileId(id);
    }

    public void onButtonClick(View v) {
        if (v.getId() == R.id.btnPostar) {
            postar();

        }
        else if (v.getId() == R.id.btnLocalizacao) {
            Intent i = new Intent(facebookHub.this, sharelocationgps.class);
            startActivity(i);
        }
    }

    private void postar() {
        getCurrProfile();

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        // this part is optional
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                //Toast.makeText(facebookHub.this, "Postado", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                //Toast.makeText(facebookHub.this, "Erro", Toast.LENGTH_LONG).show();
            }
        });

        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle("Vou de Bike")
                    .setContentDescription("App Vou de Bike")
                    .setContentUrl(Uri.parse("http://www.pucsp.br"))
                    .build();

            shareDialog.show(linkContent);
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void getCurrProfile() {
        profile = Profile.getCurrentProfile();
    }

}
