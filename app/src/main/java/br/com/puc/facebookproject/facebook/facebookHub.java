package br.com.puc.facebookproject.facebook;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.widget.ProfilePictureView;

import br.com.puc.facebookproject.R;

/**
 * Created by Felipe on 21/11/2015.
 */
public class facebookHub extends Activity {

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

    class GetPic extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            new GraphRequest(
                    AccessToken.getCurrentAccessToken(),
                    "...?fields={fieldname_of_type_ProfilePictureSource}",
                    null,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {
                        public void onCompleted(GraphResponse response) {
            /* handle the result */
                        }
                    }
            ).executeAsync();


            return null;
        }
    }

}
