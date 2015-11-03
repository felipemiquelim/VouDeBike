package br.com.puc.facebookproject.ciclista;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.Profile;

import br.com.puc.facebookproject.BackgroundTask;
import br.com.puc.facebookproject.R;
import br.com.puc.facebookproject.dataBase;
import br.com.puc.facebookproject.menu;

/**
 * Created by Felipe on 31/10/2015.
 */
public class gerenciar_ciclista extends Activity {
    private Profile profile;
    String nome, email, celular, status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gerenciar_ciclista);

        addNome();
    }

    private void addNome() {
        profile = Profile.getCurrentProfile();
        TextView edtnome = (TextView) findViewById(R.id.edtnome);

        edtnome.setText(profile.getName());
    }


    public void cadastrar(View view) {
        TextView edtnome = (TextView) findViewById(R.id.edtnome);
        EditText edtemail = (EditText) findViewById(R.id.edtemail);
        EditText edtcelular = (EditText) findViewById(R.id.edtcelular);

        nome = edtnome.getText().toString();
        email = edtemail.getText().toString();
        celular = edtcelular.getText().toString();
        status = "1";

        String method = "update";
        BackgroundTask backgroundTask = new BackgroundTask(this);
        backgroundTask.execute(method,nome, email, celular, status);
        finish();
    }

    public void desativar(View view) {
        TextView edtnome = (TextView) findViewById(R.id.edtnome);
        EditText edtemail = (EditText) findViewById(R.id.edtemail);
        EditText edtcelular = (EditText) findViewById(R.id.edtcelular);

        nome = edtnome.getText().toString();
        email = edtemail.getText().toString();
        celular = edtcelular.getText().toString();
        status = "0";

        String method = "desativar";
        BackgroundTask backgroundTask = new BackgroundTask(this);
        backgroundTask.execute(method,nome, email, celular, status);
        finish();
    }

    public void cancelar(View view){
        Intent i;
        i = new Intent(gerenciar_ciclista.this, menu.class);
        startActivity(i);
        finish();
    }




}
