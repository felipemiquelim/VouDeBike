package br.com.puc.facebookproject.maps;

import android.app.Activity;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;

import br.com.puc.facebookproject.R;


/**
 * Created by Felipe on 02/11/2015.
 */
public class Direcion extends Activity {
    //static final HttpTransport HTTP_TRANSPORT = AndroidHttp.newCompatibleTransport();
    //static final JsonFactory JSON_FACTORY = new JacksonFactory();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.direcion);

        configuraCampos();
    }

    private void configuraCampos() {
        AutoCompleteTextView from = (AutoCompleteTextView) findViewById(R.id.from);
        AutoCompleteTextView to = (AutoCompleteTextView) findViewById(R.id.to);

        from.setText("Fisherman's Wharf, San Francisco, CA, United States");
        to.setText("The Moscone Center, Howard Street, San Francisco, CA, United States");

        from.setAdapter(new PlacesAutoCompleteAdapter(this, android.R.layout.simple_dropdown_item_1line));
        to.setAdapter(new PlacesAutoCompleteAdapter(this, android.R.layout.simple_dropdown_item_1line));
    }
}
