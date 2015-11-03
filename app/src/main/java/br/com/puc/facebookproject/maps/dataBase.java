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
    private String[] listaElementos;
    String ip = "http://192.168.0.12/voudebike/";

    public dataBase(Context ctx, MapsActivity ma) {
        this.mParent = ma;
        this.ctx = ctx;
    }

    public dataBase(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    protected void onPostExecute(String result) {
            Toast.makeText(ctx,"Markers carregados",Toast.LENGTH_LONG).show();
            mParent.setListaMarkers(listaElementos);
    }

    @Override
    protected String doInBackground(String... params) {
        String sel_url = ip + "maps/select.php";

        String method = params[0];

        if(method.equals("select")) {
            try {
                URL url = new URL(sel_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("nome","UTF-8") + "=" + URLEncoder.encode("name", "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();

                Document doc = parseXML(httpURLConnection.getInputStream());
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
                    listaElementos[i] = name + ";" + address + ";" + lat + ";" + lon + ";" + type;
                }
                httpURLConnection.disconnect();

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
