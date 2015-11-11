package br.com.puc.facebookproject.localizacao;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Felipe on 09/11/2015.
 */
public class localizacaoDB extends AsyncTask<String, Void, String> {
    Context ctx;
    String ip = "http://192.168.0.12/voudebike/";

    public localizacaoDB(Context ctx) {
        this.ctx = ctx;
    }


    @Override
    protected String doInBackground(String... params) {
        String reg_url = ip + "localizacao/register.php";

        String method = params[0];
        if(method.equals("register"))
        {
            String id = params[1];
            String lat = params[2];
            String lon = params[3];
            try {
                URL url = new URL(reg_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("ncicli", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8") + "&" +
                        URLEncoder.encode("latdloclza", "UTF-8") + "="  + URLEncoder.encode(lat, "UTF-8") + "&" +
                        URLEncoder.encode("longloclza", "UTF-8") + "=" + URLEncoder.encode(lon, "UTF-8") + "&";
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS = httpURLConnection.getInputStream();
                IS.close();
                return "Loc Registration Success";
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        if (result.equals("Loc Registration Success")) {
            Toast.makeText(ctx,result,Toast.LENGTH_LONG).show();
        }
    }
}
