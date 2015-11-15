package br.com.puc.facebookproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import br.com.puc.facebookproject.ciclista.cadastro_ciclista;
import br.com.puc.facebookproject.ciclista.gerenciar_ciclista;
import br.com.puc.facebookproject.maps.MapsActivity;

/**
 * Created by Felipe on 25/10/2015.
 */
public class dataBase extends AsyncTask <String, Void, String> {
    Context ctx;
    AlertDialog alertDialog;
    MapsActivity mParent;
    admin mParentAdm;
    private String[] listaElementos;
    String ip = "http://192.168.0.12/voudebike/";

    public dataBase(Context ctx, MapsActivity ma) {
        this.mParent = ma;
        this.ctx = ctx;
    }
    public dataBase(Context ctx, admin adm) {
        this.mParentAdm = adm;
        this.ctx = ctx;
    }

    public dataBase(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    protected void onPostExecute(String result) {
        if (result!=null) {
            if (result.equals("Registration Success")) {
                Toast.makeText(ctx, "Cadastro Efetuado", Toast.LENGTH_LONG).show();
            } else if (result.equals("Pending")) {
                Toast.makeText(ctx, "Estabelecimentos Carregados", Toast.LENGTH_LONG).show();
                mParentAdm.setEstabelecimentos(listaElementos);
            }else if (result.equals("Pending2")) {
                Toast.makeText(ctx, "Estabelecimentos Pendentes Carregados", Toast.LENGTH_LONG).show();
                mParent.setListaMarkers(listaElementos);
            } else if (result.equals("Updated Status")) {
                Toast.makeText(ctx, "Estabelecimento Aprovado", Toast.LENGTH_LONG).show();
            } else if (result.equals("Removed")) {
                Toast.makeText(ctx, "Estabelecimento Removido", Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(ctx, "Markers carregados", Toast.LENGTH_LONG).show();
                mParent.setListaMarkers(listaElementos);
            }
        }
    }

    @Override
    protected String doInBackground(String... params) {
        String sel_url = ip + "maps/selectAll.php";
        String spen_url = ip + "maps/selectAllPending.php";
        String ins_url = ip + "maps/insert.php";
        String upd_url = ip + "maps/update.php";
        String del_url = ip + "maps/delete.php";

        String method = params[0];
        String status = "1";

        if(method.equals("select")) {
            try {
                URL url = new URL(sel_url);
                HttpURLConnection httpURLConnection2 = (HttpURLConnection) url.openConnection();
                httpURLConnection2.disconnect();
                httpURLConnection2 = (HttpURLConnection) url.openConnection();
                httpURLConnection2.setDoOutput(true);
                httpURLConnection2.setRequestMethod("POST");
                httpURLConnection2.setDoInput(true);
                OutputStream OS = httpURLConnection2.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("nome","UTF-8") + "=" + URLEncoder.encode("name", "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();

                Document doc = parseXML(httpURLConnection2.getInputStream());
                NodeList descNodes = doc.getElementsByTagName("marker");
                listaElementos = new String[descNodes.getLength()];

                for(int i=0; i<descNodes.getLength();i++)
                {
                    Node checkNode=descNodes.item(i);

                    Element element = (Element) checkNode;
                    String name = element.getAttribute("name");
                    String address = element.getAttribute("address");
                    String lat = element.getAttribute("lat");
                    String lon = element.getAttribute("lng");
                    String type = element.getAttribute("type");
                    String tel = element.getAttribute("telefone");
                    String nestbl = element.getAttribute("nestbl");
                    listaElementos[i] = name + ";" + address + ";" + lat + ";" + lon + ";" + type + ";" + tel + ";" + nestbl;
                }
                httpURLConnection2.disconnect();

                return "OK";

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
        if(method.equals("insert"))
        {
            String razao = params[1];
            String tipo = params[2];
            String endereco = params[3];
            String tel = params[4];
            String lat = params[5];
            String lon = params[6];
            try {
                URL url = new URL(ins_url);
                HttpURLConnection httpURLConnection3 = (HttpURLConnection) url.openConnection();
                httpURLConnection3.setRequestMethod("POST");
                httpURLConnection3.setDoOutput(true);
                OutputStream OS = httpURLConnection3.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("razao", "UTF-8") + "=" + URLEncoder.encode(razao, "UTF-8") + "&" +
                        URLEncoder.encode("tipo", "UTF-8") + "="  + URLEncoder.encode(tipo, "UTF-8") + "&" +
                        URLEncoder.encode("endereco", "UTF-8") + "=" + URLEncoder.encode(endereco, "UTF-8") + "&" +
                        URLEncoder.encode("lat", "UTF-8") + "=" + URLEncoder.encode(lat, "UTF-8") + "&" +
                        URLEncoder.encode("long", "UTF-8") + "=" + URLEncoder.encode(lon, "UTF-8") + "&" +
                        URLEncoder.encode("tel", "UTF-8") + "=" + URLEncoder.encode(tel, "UTF-8") + "&";
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS = httpURLConnection3.getInputStream();
                IS.close();

                return "Registration Success";
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else if(method.equals("selectAllPend")) {
            try {
                URL url = new URL(spen_url);
                HttpURLConnection httpURLConnection2 = (HttpURLConnection) url.openConnection();
                httpURLConnection2.disconnect();
                httpURLConnection2 = (HttpURLConnection) url.openConnection();
                httpURLConnection2.setDoOutput(true);
                httpURLConnection2.setRequestMethod("POST");
                httpURLConnection2.setDoInput(true);
                OutputStream OS = httpURLConnection2.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("nome","UTF-8") + "=" + URLEncoder.encode("name", "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();

                Document doc = parseXML(httpURLConnection2.getInputStream());
                NodeList descNodes = doc.getElementsByTagName("marker");
                listaElementos = new String[descNodes.getLength()];

                for(int i=0; i<descNodes.getLength();i++)
                {
                    Node checkNode=descNodes.item(i);

                    Element element = (Element) checkNode;
                    String name = element.getAttribute("name");
                    String address = element.getAttribute("address");
                    String id = element.getAttribute("id");
                    //String lon = element.getAttribute("lng");
                    //String type = element.getAttribute("type");
                    //String tel = element.getAttribute("telefone");
                    //listaElementos[i] = name + ";" + address + ";" + lat + ";" + lon + ";" + type + ";" + tel;
                    listaElementos[i] = id + "; " + name + "; END: " + address;
                }
                httpURLConnection2.disconnect();

                return "Pending";

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
        else if(method.equals("selectAllPend2")) {
            try {
                URL url = new URL(spen_url);
                HttpURLConnection httpURLConnection2 = (HttpURLConnection) url.openConnection();
                httpURLConnection2.disconnect();
                httpURLConnection2 = (HttpURLConnection) url.openConnection();
                httpURLConnection2.setDoOutput(true);
                httpURLConnection2.setRequestMethod("POST");
                httpURLConnection2.setDoInput(true);
                OutputStream OS = httpURLConnection2.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("nome","UTF-8") + "=" + URLEncoder.encode("name", "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();

                Document doc = parseXML(httpURLConnection2.getInputStream());
                NodeList descNodes = doc.getElementsByTagName("marker");
                listaElementos = new String[descNodes.getLength()];

                for(int i=0; i<descNodes.getLength();i++)
                {
                    Node checkNode=descNodes.item(i);

                    Element element = (Element) checkNode;
                    String name = element.getAttribute("name");
                    String address = element.getAttribute("address");
                    String id = element.getAttribute("id");
                    String lat = element.getAttribute("lat");
                    String lon = element.getAttribute("lng");
                    String type = element.getAttribute("type");
                    String tel = element.getAttribute("telefone");
                    listaElementos[i] = name + ";" + address + ";" + lat + ";" + lon + ";" + type + ";" + tel + ";" + id ;
                    //listaElementos[i] = id + "; " + name + "; END: " + address;
                }
                httpURLConnection2.disconnect();

                return "Pending2";

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
        else if(method.equals("aprovar")) {
            String id = params[1];
            try {
                URL url = new URL(upd_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8") + "&";
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS = httpURLConnection.getInputStream();
                IS.close();
                return "Updated Status";
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(method.equals("remover")) {
            String id = params[1];
            try {
                URL url = new URL(del_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8") + "&";
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS = httpURLConnection.getInputStream();
                IS.close();
                return "Removed";
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



        return null;
    }

    @Override
    protected void onPreExecute() {
        alertDialog = new AlertDialog.Builder(ctx).create();
        alertDialog.setTitle("Maps");
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    private Document parseXML(InputStream stream)
            throws Exception
    {
        DocumentBuilderFactory objDocumentBuilderFactory = null;
        DocumentBuilder objDocumentBuilder = null;
        Document doc = null;
        try
        {
            objDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
            objDocumentBuilder = objDocumentBuilderFactory.newDocumentBuilder();

            doc = objDocumentBuilder.parse(stream);
        }
        catch(Exception ex)
        {
            throw ex;
        }

        return doc;
    }


    public String[] getListaElementos() {
        return listaElementos;
    }
}
