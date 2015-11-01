package br.com.puc.facebookproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

import br.com.puc.facebookproject.ciclista.cadastro_ciclista;

/**
 * Created by Felipe on 25/10/2015.
 */
public class BackgroundTask extends AsyncTask<String, Void, String>{
    Context ctx;
    AlertDialog alertDialog;
    menu mParent;


    BackgroundTask(Context ctx, menu m) {
        this.mParent = m;
        this.ctx = ctx;
    }

    public BackgroundTask(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    protected void onPreExecute() {
        alertDialog = new AlertDialog.Builder(ctx).create();
        alertDialog.setTitle("Usuário");
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        if(result.equals("Registration Success...")) {
            Toast.makeText(ctx,result,Toast.LENGTH_LONG).show();
        }
        else if (result.equals("Update Success...")) {
            Toast.makeText(ctx,result,Toast.LENGTH_LONG).show();
        }
        else if (result.equals("Deactivate Success...")) {
            Toast.makeText(ctx,result,Toast.LENGTH_LONG).show();
        }
        else {
            //alertDialog.setMessage(result);
            //alertDialog.show();
            if (result.trim().equals("0"))  {
                Intent i = new Intent(ctx, cadastro_ciclista.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(i);
            } else
                Toast.makeText(ctx,"Usuário já registrado",Toast.LENGTH_LONG).show();
        }


    }

    @Override
    protected String doInBackground(String... params) {
        String reg_url = "http://192.168.0.12/voudebike/ciclista/register.php";
        String sel_url = "http://192.168.0.12/voudebike/ciclista/select.php";
        String upd_url = "http://192.168.0.12/voudebike/ciclista/update.php";
        String des_url = "http://192.168.0.12/voudebike/ciclista/desativar.php";

        String method = params[0];
        if(method.equals("register"))
        {
            String nome = params[1];
            String email = params[2];
            String celular = params[3];
            String status = params[4];
            try {
                URL url = new URL(reg_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
            String data = URLEncoder.encode("nome", "UTF-8") + "=" + URLEncoder.encode(nome, "UTF-8") + "&" +
                        URLEncoder.encode("email", "UTF-8") + "="  + URLEncoder.encode(email, "UTF-8") + "&" +
                        URLEncoder.encode("celular", "UTF-8") + "=" + URLEncoder.encode(celular, "UTF-8") + "&" +
                        URLEncoder.encode("status", "UTF-8") + "=" + URLEncoder.encode(status, "UTF-8") + "&";
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS = httpURLConnection.getInputStream();
                IS.close();
                return "Registration Success...";
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(method.equals("verificaCliente")) {
            String name = params[1];
            try {
                URL url = new URL(sel_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
            String data = URLEncoder.encode("nome","UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(IS, "iso-8859-1"));
                String response = "";
                String line = "";
                while((line = bufferedReader.readLine())!=null) {
                    response += line;
                }
                bufferedReader.close();
                IS.close();
                httpURLConnection.disconnect();
                return response;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(method.equals("update")) {
            String nome = params[1];
            String email = params[2];
            String celular = params[3];
            String status = params[4];
            try {
                URL url = new URL(upd_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("nome", "UTF-8") + "=" + URLEncoder.encode(nome, "UTF-8") + "&" +
                        URLEncoder.encode("email", "UTF-8") + "="  + URLEncoder.encode(email, "UTF-8") + "&" +
                        URLEncoder.encode("celular", "UTF-8") + "=" + URLEncoder.encode(celular, "UTF-8") + "&" +
                        URLEncoder.encode("status", "UTF-8") + "=" + URLEncoder.encode(status, "UTF-8") + "&";
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS = httpURLConnection.getInputStream();
                IS.close();
                return "Update Success...";
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(method.equals("desativar")) {
            String nome = params[1];
            String email = params[2];
            String celular = params[3];
            String status = params[4];
            try {
                URL url = new URL(des_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("nome", "UTF-8") + "=" + URLEncoder.encode(nome, "UTF-8") + "&" +
                        URLEncoder.encode("email", "UTF-8") + "="  + URLEncoder.encode(email, "UTF-8") + "&" +
                        URLEncoder.encode("celular", "UTF-8") + "=" + URLEncoder.encode(celular, "UTF-8") + "&" +
                        URLEncoder.encode("status", "UTF-8") + "=" + URLEncoder.encode(status, "UTF-8") + "&";
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS = httpURLConnection.getInputStream();
                IS.close();
                return "Deactivate Success...";
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
