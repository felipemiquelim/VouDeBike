package br.com.puc.facebookproject.ciclista;

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

import br.com.puc.facebookproject.TelaPrincipal;
import br.com.puc.facebookproject.maps.TelaEstabelecimento;

/**
 * Created by Felipe on 25/10/2015.
 */
public class controler_ciclista extends AsyncTask<String, Void, String>{
    Context ctx;
    AlertDialog alertDialog;
    TelaPrincipal mParent;
    TelaEstabelecimento mParent2;
    String ip = "http://ec2-54-207-26-150.sa-east-1.compute.amazonaws.com/voudebike/";

    public controler_ciclista(Context ctx, TelaPrincipal m) {
        this.mParent = m;
        this.ctx = ctx;
    }

    public controler_ciclista(Context ctx, TelaEstabelecimento m) {
        this.mParent2 = m;
        this.ctx = ctx;
    }

    public controler_ciclista(Context ctx) {
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
        if(result != null) {
            if (result.equals("Registro Efetuado")) {
                Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
            } else if (result.equals("Alteração Realizada")) {
                Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
            } else if (result.equals("Desativação Efetuada")) {
                Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
            } else if (result.equals("Admin") || result.equals("User")) {
                if (result.equals("Admin")) {
                    if(mParent!=null)
                        mParent.setAdmin(true);
                    else if(mParent2!=null)
                        mParent2.setAdmin(true);
                }
            } else if (result.equals("Autorizado")) {
                Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
            } else {
                //alertDialog.setMessage(result);
                //alertDialog.show();
                if (result.trim().equals("0")) {
                    Intent i = new Intent(ctx, cadastro_ciclista.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ctx.startActivity(i);
                } else
                    Toast.makeText(ctx, "Usuário já registrado", Toast.LENGTH_LONG).show();
            }
        }


    }

    @Override
    protected String doInBackground(String... params) {
        String reg_url = ip + "ciclista/register.php";
        String sel_url = ip + "ciclista/select.php";
        String upd_url = ip + "ciclista/update.php";
        String des_url = ip + "ciclista/desativar.php";
        String adm_url = ip + "ciclista/selectAdmin.php";
        String aut_url = ip + "ciclista/autorizar.php";

        String method = params[0];
        if(method.equals("register"))
        {
            String nome = params[1];
            String email = params[2];
            String celular = params[3];
            String status = params[4];
            String id = params[5];
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
                        URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8") + "&" +
                        URLEncoder.encode("status", "UTF-8") + "=" + URLEncoder.encode(status, "UTF-8") + "&";
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS = httpURLConnection.getInputStream();
                IS.close();
                return "Registro Efetuado";
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(method.equals("verificaCliente")) {
            String id = params[1];
            try {
                URL url = new URL(sel_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
            String data = URLEncoder.encode("id","UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
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
                return "Alteração Realizada";
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
                return "Desativação Efetuada";
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(method.equals("verificaAdmin")) {
            try {
                String nome = params[1];
                URL url = new URL(adm_url);
                HttpURLConnection httpURLConnection2 = (HttpURLConnection) url.openConnection();
                httpURLConnection2.setDoOutput(true);
                httpURLConnection2.setRequestMethod("POST");
                httpURLConnection2.setDoInput(true);
                OutputStream OS = httpURLConnection2.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("nome","UTF-8") + "=" + URLEncoder.encode(nome, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();

                InputStream IS = httpURLConnection2.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(IS, "iso-8859-1"));
                String response = "";
                String line = "";
                while((line = bufferedReader.readLine())!=null) {
                    response += line;
                }
                bufferedReader.close();
                IS.close();
                httpURLConnection2.disconnect();

                if (response.trim().equals("1"))
                    return "Admin";
                else
                    return "User";


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(method.equals("autorizar")) {
            String email = params[1];
            try {
                URL url = new URL(aut_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&";
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS = httpURLConnection.getInputStream();
                IS.close();
                return "Autorizado";
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
