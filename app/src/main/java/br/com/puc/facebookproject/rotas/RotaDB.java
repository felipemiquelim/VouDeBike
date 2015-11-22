package br.com.puc.facebookproject.rotas;

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
 * Created by Felipe on 21/11/2015.
 */
public class RotaDB extends AsyncTask<String, Void, String>  {
    Context cx;
    String ip = "http://ec2-54-207-26-150.sa-east-1.compute.amazonaws.com/voudebike/";

    public RotaDB(Context ctx) {
        this.cx = ctx;
    }

    @Override
    protected String doInBackground(String... params) {
        String reg_url = ip + "rota/register.php";

        String method = params[0];
        if(method.equals("register"))
        {
            String id = params[1];
            String alias = params[2];
            String or = params[3];
            String lator = params[4];
            String longor = params[5];
            String dest = params[6];
            String latdest = params[7];
            String longdest = params[8];

            try {
                URL url = new URL(reg_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("ncicli", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8") + "&" +
                        URLEncoder.encode("ralias", "UTF-8") + "="  + URLEncoder.encode(alias, "UTF-8") + "&" +
                        URLEncoder.encode("rorigem", "UTF-8") + "=" + URLEncoder.encode(or, "UTF-8") + "&" +
                        URLEncoder.encode("rlator", "UTF-8") + "="  + URLEncoder.encode(lator, "UTF-8") + "&" +
                        URLEncoder.encode("rlongor", "UTF-8") + "=" + URLEncoder.encode(longor, "UTF-8") + "&" +
                        URLEncoder.encode("rdest", "UTF-8") + "="  + URLEncoder.encode(dest, "UTF-8") + "&" +
                        URLEncoder.encode("rlatdest", "UTF-8") + "=" + URLEncoder.encode(latdest, "UTF-8") + "&" +
                        URLEncoder.encode("rlongdest", "UTF-8") + "="  + URLEncoder.encode(longdest, "UTF-8") + "&";
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS = httpURLConnection.getInputStream();
                IS.close();
                return "Route Registration Success";
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
        if (result != null) {
            if (result.equals("Route Registration Success")) {
                Toast.makeText(cx, result, Toast.LENGTH_LONG).show();
            }
        }
    }
}
