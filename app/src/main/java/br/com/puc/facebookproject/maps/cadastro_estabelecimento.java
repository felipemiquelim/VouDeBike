package br.com.puc.facebookproject.maps;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import br.com.puc.facebookproject.R;

/**
 * Created by Felipe on 08/11/2015.
 */
public class cadastro_estabelecimento extends Activity{
    Double lat, lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro_estabelecimento);

        Bundle b = getIntent().getExtras();
        lat = b.getDouble("lat");
        lon = b.getDouble("long");
    }


    public void onButtonClick(View view) {
        if(view.getId()==R.id.btncadastrar) {
            EditText edtrazao = (EditText) findViewById(R.id.edtnome);
            EditText edttel = (EditText) findViewById(R.id.edtTelefone);
            EditText edtend = (EditText) findViewById(R.id.edtEndereco);
            EditText edtnum = (EditText) findViewById(R.id.edtNum);
            EditText edtbairro = (EditText) findViewById(R.id.edtBairro);
            Spinner sptipo = (Spinner) findViewById(R.id.spinner2);

            String razao = edtrazao.getText().toString().toUpperCase();
            String telefone = edttel.getText().toString();
            String end = edtend.getText().toString().toUpperCase() + ", " +
                    edtnum.getText().toString() + "-" +
                    edtbairro.getText().toString().toUpperCase();
            String tipo = sptipo.getSelectedItem().toString().toUpperCase();
            limpar();
            String method = "insert";
            controler_estabelecimento backgroundTask = new controler_estabelecimento(this);
            backgroundTask.execute(method,razao, tipo, end, telefone, lat.toString(), lon.toString());
        }

        if(view.getId()==R.id.btnLimpar) {
            limpar();
        }

        if(view.getId()==R.id.btnCancelar) {
            this.finish();
        }

    }

    private void limpar() {
        EditText edtrazao = (EditText) findViewById(R.id.edtnome);
        EditText edttel = (EditText) findViewById(R.id.edtTelefone);
        EditText edtend = (EditText) findViewById(R.id.edtEndereco);
        EditText edtnum = (EditText) findViewById(R.id.edtNum);
        EditText edtbairro = (EditText) findViewById(R.id.edtBairro);
        Spinner sptipo = (Spinner) findViewById(R.id.spinner2);

        edtrazao.setText(null);
        edttel.setText(null);
        edtend.setText(null);
        edtnum.setText(null);
        edtbairro.setText(null);
        sptipo.setSelection(0);

    }
}
