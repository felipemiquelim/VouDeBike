package br.com.puc.facebookproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Felipe on 12/10/2015.
 */
public class menu extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
    }

    public void onButtonClick(View v) {
        if (v.getId() == R.id.bShareLocation) {
            Intent i = new Intent(menu.this, sharelocation.class);
            startActivity(i);
        }
    }

}
