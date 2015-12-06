package br.com.puc.facebookproject.rotas;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Felipe on 21/11/2015.
 */
public class controler_rotas extends AsyncTask<String, Void, String>  {
    private String[] listaElementos;
    gerenciar_rotas parent;
    Context cx;
    String ip = "http://ec2-54-207-26-150.sa-east-1.compute.amazonaws.com/voudebike/";

    public controler_rotas(Context ctx) {
        this.cx = ctx;
    }

    public controler_rotas(Context ctx, gerenciar_rotas mr) {
        this.cx = ctx;
        this.parent = mr;
    }

    @Override
    protected String doInBackground(String... params) {
        String reg_url = ip + "rota/register.php";
        String all_url = ip + "rota/selectAll.php";
        String del_url = ip + "rota/delete.php";

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
                return "Rota Salva com Sucesso";
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(method.equals("selectAll")) {
            try {
                String id = params[1];

                URL url = new URL(all_url);
                HttpURLConnection httpURLConnection2 = (HttpURLConnection) url.openConnection();
                httpURLConnection2.disconnect();
                httpURLConnection2 = (HttpURLConnection) url.openConnection();
                httpURLConnection2.setDoOutput(true);
                httpURLConnection2.setRequestMethod("POST");
                httpURLConnection2.setDoInput(true);
                OutputStream OS = httpURLConnection2.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("id","UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();

                Document doc = parseXML(httpURLConnection2.getInputStream());
                NodeList descNodes = doc.getElementsByTagName("rota");
                listaElementos = new String[descNodes.getLength()];

                for(int i=0; i<descNodes.getLength();i++)
                {
                    Node checkNode=descNodes.item(i);

                    Element element = (Element) checkNode;
                    String origem = element.getAttribute("rorigem");
                    String dest = element.getAttribute("rdest");
                    String alias = element.getAttribute("ralias");
                    //String lon = element.getAttribute("lng");
                    //String type = element.getAttribute("type");
                    //String tel = element.getAttribute("telefone");
                    //listaElementos[i] = name + ";" + address + ";" + lat + ";" + lon + ";" + type + ";" + tel;
                    listaElementos[i] = alias + "; " + origem + "; " + dest;
                }
                httpURLConnection2.disconnect();

                return "All";

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
        else if(method.equals("remove"))
        {
            String id = params[1];
            String alias = params[2];

            try {
                URL url = new URL(del_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("ncicli", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8") + "&" +
                        URLEncoder.encode("ralias", "UTF-8") + "="  + URLEncoder.encode(alias, "UTF-8") + "&" ;
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS = httpURLConnection.getInputStream();
                IS.close();
                return "Rota Removida com Sucesso";
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
            if (result.equals("Rota Salva com Sucesso")) {
                Toast.makeText(cx, result, Toast.LENGTH_LONG).show();
            }
            else if (result.equals("All")) {
                Toast.makeText(cx, "Rotas Carregadas", Toast.LENGTH_LONG).show();
                parent.setRotas(listaElementos);
            }
            else if (result.equals("Rota Removida com Sucesso")) {
                Toast.makeText(cx, result, Toast.LENGTH_LONG).show();
            }
        }
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
}
