package br.com.puc.facebookproject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import br.com.puc.facebookproject.ciclista.controler_ciclista;
import br.com.puc.facebookproject.maps.controler_estabelecimento;

/**
 * Created by Felipe on 08/11/2015.
 */
public class TelaAdmin extends Activity {
    private String[] estabelecimentos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin);
        buscarEst();
    }

    private void buscarEst() {
        String method="selectAllPend";

        controler_estabelecimento db = new controler_estabelecimento(getApplicationContext(), this);
        db.execute(method);
    }

    public void onButtonClick(View view) {
        if(view.getId()==R.id.btnAprovar) {
            aprovar();
            buscarEst();
        }
        else if(view.getId()==R.id.btnVoltar) {
            this.finish();
        }
        else if(view.getId()==R.id.btnAutorizar) {
            autorizar();
        }
    }

    private void autorizar() {
        String method="autorizar";
        EditText edtemail = (EditText) findViewById(R.id.edtemail);
        String email = edtemail.getText().toString();

        controler_ciclista bt = new controler_ciclista(getApplicationContext());
        bt.execute(method,email);
        edtemail.setText("Email");
    }

    private void aprovar() {
        String method="aprovar";
        Spinner spinner = (Spinner) findViewById(R.id.spinEst);
        int index = spinner.getSelectedItemPosition();
        String param[] = estabelecimentos[index].split(";");

        controler_estabelecimento db = new controler_estabelecimento(getApplicationContext(), this);
        db.execute(method,param[0]);
    }

    public void setEstabelecimentos(String[] estabelecimentos) {
        this.estabelecimentos = estabelecimentos;
        incluirEst();
    }

    private void incluirEst() {
        if (estabelecimentos != null) {
            if (estabelecimentos.length > 0) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        this, android.R.layout.simple_spinner_item, estabelecimentos);

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Spinner sItems = (Spinner) findViewById(R.id.spinEst);
                sItems.setAdapter(adapter);
            }
        }
    }

}
