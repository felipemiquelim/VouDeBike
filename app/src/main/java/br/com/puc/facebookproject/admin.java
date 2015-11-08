package br.com.puc.facebookproject;

import android.app.Activity;
import android.app.Notification;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.facebook.Profile;

/**
 * Created by Felipe on 08/11/2015.
 */
public class admin extends Activity {
    private String[] estabelecimentos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin);
        buscarEst();
    }

    private void buscarEst() {
        String method="selectAllPend";

        br.com.puc.facebookproject.dataBase db = new br.com.puc.facebookproject.dataBase(getApplicationContext(), this);
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
    }

    private void aprovar() {
        String method="aprovar";
        Spinner spinner = (Spinner) findViewById(R.id.spinEst);
        int index = spinner.getSelectedItemPosition();
        String param[] = estabelecimentos[index].split(";");

        br.com.puc.facebookproject.dataBase db = new br.com.puc.facebookproject.dataBase(getApplicationContext(), this);
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
