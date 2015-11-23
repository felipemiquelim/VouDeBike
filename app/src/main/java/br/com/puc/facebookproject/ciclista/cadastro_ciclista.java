package br.com.puc.facebookproject.ciclista;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.Profile;

import br.com.puc.facebookproject.R;

/**
 * Created by Felipe on 25/10/2015.
 */
public class cadastro_ciclista extends Activity {
    private Profile profile;
    String nome, email, celular, status, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ciclista_cadastro);

        addNome();
    }

    private void addNome() {
        profile = Profile.getCurrentProfile();
        TextView edtnome = (TextView) findViewById(R.id.edtnome);

        edtnome.setText(profile.getName());
        id = profile.getId();
    }

    public void cadastrar(View view) {
        TextView edtnome = (TextView) findViewById(R.id.edtnome);
        EditText edtemail = (EditText) findViewById(R.id.edtemail);
        EditText edtcelular = (EditText) findViewById(R.id.edtcelular);

        nome = edtnome.getText().toString();
        email = edtemail.getText().toString();
        celular = edtcelular.getText().toString();
        status = "1";


        String method = "register";
        controler_ciclista backgroundTask = new controler_ciclista(this);
        backgroundTask.execute(method,nome, email, celular, status, id);
        finish();
    }

}
