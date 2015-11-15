package br.com.puc.facebookproject.localizacao;

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
 * Created by Felipe on 09/11/2015.
 */
public class localizacaoDB extends AsyncTask<String, Void, String> {
    Context ctx;
    String ip = "http://192.168.0.12/voudebike/";


    public void setFriendList(String[] friendList) {
        this.friendList = friendList;
    }

    String[] friendList, frienListRetorno;
    localizacao parent;

    public localizacaoDB(Context ctx) {
        this.ctx = ctx;
    }

    public localizacaoDB(Context ctx, localizacao loc) {
        this.parent = loc;
        this.ctx = ctx;
    }


    @Override
    protected String doInBackground(String... params) {
        String reg_url = ip + "localizacao/register.php";
        String frn_url = ip + "localizacao/friends.php";

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
        else if(method.equals("selectFriends"))
        {
            String paramFriends = generateString();

            try {
                URL url = new URL(frn_url);
                HttpURLConnection httpURLConnection2 = (HttpURLConnection) url.openConnection();
                httpURLConnection2.disconnect();
                httpURLConnection2 = (HttpURLConnection) url.openConnection();
                httpURLConnection2.setDoOutput(true);
                httpURLConnection2.setRequestMethod("POST");
                httpURLConnection2.setDoInput(true);
                OutputStream OS = httpURLConnection2.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("friends","UTF-8") + "=" + URLEncoder.encode(paramFriends, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();

                Document doc = parseXML(httpURLConnection2.getInputStream());
                NodeList descNodes = doc.getElementsByTagName("item");
                frienListRetorno = new String[descNodes.getLength()];

                for(int i=0; i<descNodes.getLength();i++)
                {
                    Node checkNode=descNodes.item(i);

                    Element element = (Element) checkNode;
                    String name = element.getAttribute("name");
                    String lat = element.getAttribute("lat");
                    String lon = element.getAttribute("lng");
                    String date = element.getAttribute("data");
                    frienListRetorno[i] = name + ";" + lat + ";" + lon + ";" + date;
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
        return null;
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

    private String generateString() {
        String finalFriends = "";

        for(int i=0; i<friendList.length;i++){
            if (i!=0)
                finalFriends+= ",";
            finalFriends+= friendList[i];
        }

        return finalFriends;
    }

    @Override
    protected void onPostExecute(String result) {
        if (result.equals("Loc Registration Success")) {
            Toast.makeText(ctx,result,Toast.LENGTH_LONG).show();
        } else
            parent.setListaMarkers(frienListRetorno);
    }
}
