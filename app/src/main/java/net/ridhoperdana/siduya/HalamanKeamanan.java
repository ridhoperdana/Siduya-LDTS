package net.ridhoperdana.siduya;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.xml.transform.Result;

public class HalamanKeamanan extends AppCompatActivity {

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
    public List<Results> tampung_result = new ArrayList<>();
    Tempat request;
    public Double lat_depan;
    public Double longt_depan;
    public StringBuilder url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_halaman_keamanan);

//        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.fragmentMap);
//        mapFragment.getMapAsync(this);
        lat_depan = getIntent().getDoubleExtra("Lat", 1);
        longt_depan = getIntent().getDoubleExtra("Lng", 1);

        url = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        url.append("location=" + lat_depan + "," + longt_depan);
        url.append("&radius=" + 5000);
        url.append("&types=" + "police");
        url.append("&rankBy=" + "distance");
        url.append("&key=" + "AIzaSyBVuRYeAWRZhzeF9c51pOUfAC93iP7FgBE");

//        new Async().execute("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-7.2799089%2C112.7972183&radius=5000&type=police&rankBy=distance&key=AIzaSyBVuRYeAWRZhzeF9c51pOUfAC93iP7FgBE");
//        List<Results> data = fill_with_data();

        new Async().execute(url.toString());

//        Log.d("data: ", tampung_result.get(1).getName());

//        recyclerView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final Dialog dialog = new Dialog(HalamanKeamanan.this);
//
//                //setting custom layout to dialog
//                dialog.setContentView(R.layout.custom_dialog);
////                dialog.setTitle("Custom Dialog");
//
//                //adding text dynamically
////                TextView txt = (TextView) dialog.findViewById(R.id.textView);
////                txt.setText("Put your dialog text here.");
//
////                ImageView image = (ImageView)dialog.findViewById(R.id.image);
////                image.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_dialog_info));
//
//                //adding button click event
////                Button dismissButton = (Button) dialog.findViewById(R.id.button);
//                ImageView image = (ImageView)dialog.findViewById(R.id.tombol_cancel);
//                image.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
//                dialog.show();
//
//            }
//        });

    }

//    public List<Results> fill_with_data() {
//        List<Tempat> data = new ArrayList<>();

//        new Async().execute("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-7.2799089%2C112.7972183&radius=5000&type=police&rankBy=distance&key=AIzaSyBVuRYeAWRZhzeF9c51pOUfAC93iP7FgBE");
//        String readJSON = getJSON("http://date.jsontest.com");
//        try{
//            JSONObject jsonObject = new JSONObject(readJSON);
//            Log.i(HalamanKeamanan.class.getName(), jsonObject.getString("date"));
//        } catch(Exception e){e.printStackTrace();}
//        finally{System.out.println("Success");
//        }

//        data.add(new Data("Polsek Sukolilo", "Jl. Manyar Kertoadi 1 No.701, Klampis Ngasem, Sukolilo, Kota SBY, Jawa Timur, Indonesia."));
//        data.add(new Data("Polsek Pulogadung", "Jl Cipingan Baru Raya Jakarta Timur, Tlp 021-4892844, Indonesia."));
//        data.add(new Data("Polsek Manyar", "Jl. Raya Manyar No. 41, Gresik, Jawa Timur, Indonesia."));

//        return tampung_result;
//    }

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
                Log.d("List Nama->", tampung_result.get(i).getName());
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public String getJSON(String address){
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(address);
        try{
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if(statusCode == 200){
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while((line = reader.readLine()) != null){
                    builder.append(line);
                }
            } else {
                Log.e(HalamanKeamanan.class.toString(), "Failedet JSON object");
            }
        }catch(ClientProtocolException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        return builder.toString();
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
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv);
            Adapter adapter = new Adapter(tampung_result, getApplication());
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(HalamanKeamanan.this));
        }
    }



}
