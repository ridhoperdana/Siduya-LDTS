package net.ridhoperdana.siduya;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by RIDHO on 4/23/2016.
 */
public class PlaceAPI {
    net.ridhoperdana.siduya.Address request;
    public List<Predictions> tampung_result = new ArrayList<>();
    public ArrayList<String> resultList;
    private StringBuilder url;

    public ArrayList<String> autocomplete(String input) {

        // Making HTTP request
        try {
            url = new StringBuilder("https://maps.googleapis.com/maps/api/place/autocomplete/json?");
            url.append("input=" + URLEncoder.encode(input, "utf8"));
            url.append("&types=" + "address");
            url.append("&location=" + "-7.25747,112.75209");
            url.append("&radius=" + 50000);
            url.append("&language=" + "id");
            url.append("&key=" + "AIzaSyBVuRYeAWRZhzeF9c51pOUfAC93iP7FgBE");
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url.toString());

            HttpResponse httpResponse = httpClient.execute(httpGet);
            Reader reader = new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8");
            Gson baru = new Gson();

            request = baru.fromJson(reader, net.ridhoperdana.siduya.Address.class);

            resultList = new ArrayList<String>(request.getPredictions().size());
            for(int i=0; i<request.getPredictions().size(); i++)
            {
                resultList.add(request.getPredictions().get(i).getDescription());
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resultList;
    }

    private static String getStringFromInputStream(Reader is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(is);
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();
    }

    private class Async extends AsyncTask<String, Integer, Double>
    {

        @Override
        protected Double doInBackground(String... params) {
            return null;
        }
    }
}