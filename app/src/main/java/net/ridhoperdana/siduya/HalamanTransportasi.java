package net.ridhoperdana.siduya;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class HalamanTransportasi extends AppCompatActivity {

    public List<Results> tampung_result = new ArrayList<>();
    Tempat request;
    public Double lat_depan;
    public Double longt_depan;
    public StringBuilder url;
    private String url_baru, latutf, longtutf, radiusutf, distance, key;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_halaman_transportasi);

        dialog = new ProgressDialog(this);

        lat_depan = getIntent().getDoubleExtra("Lat", 1);
        longt_depan = getIntent().getDoubleExtra("Lng", 1);

        try {
            url_baru = URLEncoder.encode("taxi_stand|train_station", "UTF-8");
            latutf = URLEncoder.encode(lat_depan.toString(), "UTF-8");
            longtutf = URLEncoder.encode(longt_depan.toString(), "UTF-8");
            radiusutf = URLEncoder.encode("5000", "UTF-8");
            distance = URLEncoder.encode("distance", "UTF-8");
            key = URLEncoder.encode("AIzaSyBVuRYeAWRZhzeF9c51pOUfAC93iP7FgBE", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        url = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        url.append("location=" + latutf + "," + longtutf);
        url.append("&radius=" + radiusutf);
        url.append("&types=" + url_baru);
        url.append("&rankBy=" + distance);
        url.append("&key=" + key);

        new Async().execute(url.toString());
    }

    public void getJSONFromUrl(String url) {

        // Making HTTP request
        try {
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);

            HttpResponse httpResponse = httpClient.execute(httpGet);
            Reader reader = new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8");
            Gson baru = new Gson();
            request = baru.fromJson(reader, Tempat.class);
//            Log.d("list->", request.)

            for(int i=0; i<request.getResults().size(); i++)
            {
                tampung_result.add(request.getResults().get(i));
//                Log.d("List Nama->", tampung_result.get(i).getName());
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class Async extends AsyncTask<String, Integer, Double>
    {

        @Override
        protected Double doInBackground(String... params) {
            getJSONFromUrl(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Double aDouble) {
            super.onPostExecute(aDouble);
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_transportasi);
            Adapter adapter_kesehatan = new Adapter(tampung_result, getApplication());
            recyclerView.setAdapter(adapter_kesehatan);
            recyclerView.setLayoutManager(new LinearLayoutManager(HalamanTransportasi.this));
            dialog.hide();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Sedang mengunduh data...");
            dialog.setCancelable(false);
            dialog.show();
        }
    }
}
