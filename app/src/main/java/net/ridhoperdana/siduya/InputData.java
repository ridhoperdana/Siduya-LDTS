package net.ridhoperdana.siduya;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;

public class InputData extends AppCompatActivity {

    private Double latitude, longitude;
    private String nama, namautf,namautfpersen, alamat, alamatutf, alamatutfpersen;
    private EditText nama_tempat, alamat_tempat, nomor_tempat, kategori_tempat;
    private Button tombol;
    private String responsecode;
    private Toast notification;
    private String kategori;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_data);

        dialog = new ProgressDialog(this);

        latitude = getIntent().getDoubleExtra("value_lat", 1);
        longitude = getIntent().getDoubleExtra("value_longt", 1);
        nama = getIntent().getExtras().getString("value_nama");
        alamat = getIntent().getExtras().getString("value_alamat");
        kategori = getIntent().getExtras().getString("value_kategori");

        nama_tempat = (EditText)findViewById(R.id.input_nama_tempat);

        try {
            namautf = URLDecoder.decode(nama, "UTF-8");
            namautfpersen = URLEncoder.encode(namautf, "UTF-8").replace("+", " ");
            alamatutf = URLDecoder.decode(alamat, "UTF-8");
            alamatutfpersen = URLEncoder.encode(alamatutf, "UTF-8").replace("+", " ");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }



        nama_tempat.setText(namautf);

        alamat_tempat = (EditText)findViewById(R.id.input_alamat_tempat);
        alamat_tempat.setText(alamatutf);

        kategori_tempat = (EditText)findViewById(R.id.input_kategori_tempat);
        kategori_tempat.setText(kategori);

        nomor_tempat = (EditText)findViewById(R.id.input_nomor_tempat);
        nomor_tempat.getText().toString();

        tombol = (Button)findViewById(R.id.tombol_unggah);

//        tombol.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new Async().execute("http://ldts.cangkruk.info/public/uploaddata/"
//                        + kategori_tempat.getText().toString() + "/"
//                        + namautfpersen + "/"
//                        + alamatutfpersen + "/"
//                        + nomor_tempat.getText().toString() + "/"
//                        + latitude.toString() + "/"
//                        + longitude.toString());
//            }
//        });
        tombol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Async().execute("http://ldts.cangkruk.info/public/uploaddata");
            }
        });
    }

    public void getResponse(String url) {

        // Making HTTP request
        try {
            // defaultHttpClient
            ArrayList<NameValuePair> postParameters;
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpGet = new HttpPost(url);

            postParameters = new ArrayList<NameValuePair>();
            postParameters.add(new BasicNameValuePair("kategori", kategori_tempat.getText().toString()));
            postParameters.add(new BasicNameValuePair("namalayanan", namautf));
            postParameters.add(new BasicNameValuePair("alamat", alamatutf));
            postParameters.add(new BasicNameValuePair("notelpon", nomor_tempat.getText().toString()));
            postParameters.add(new BasicNameValuePair("lat", latitude.toString()));
            postParameters.add(new BasicNameValuePair("longitude", longitude.toString()));

            String url_post = httpGet.toString();
            httpGet.setEntity(new UrlEncodedFormEntity(postParameters));

            HttpResponse httpResponse = httpClient.execute(httpGet);
            Reader reader = new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8");

            responsecode = getStringFromInputStream(reader);
            if(responsecode=="sukses")
            {
                Log.d("Data sukses dimasukkan", responsecode);
            }
            else {
                Log.d("ini response codenya-->", responsecode);
                Log.d("error, ini url-->", url_post);
                Log.d("ini url parameter-->", url);
                Log.d("parameter-->", postParameters.toString());
            }
//            Gson baru = new Gson();
//            request = baru.fromJson(reader, Tempat.class);
//            Log.d("list->", request.)

//            for(int i=0; i<request.getResults().size(); i++)
//            {
//                tampung_result.add(request.getResults().get(i));
//                Log.d("List Nama->", tampung_result.get(i).getName());
//            }

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

    private class Async extends AsyncTask<String, Integer, Double>
    {

        @Override
        protected Double doInBackground(String... params) {
            getResponse(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Double aDouble) {
            super.onPostExecute(aDouble);
//            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_kesehatan);
//            Adapter adapter_kesehatan = new Adapter(tampung_result, getApplication());
//            recyclerView.setAdapter(adapter_kesehatan);
//            recyclerView.setLayoutManager(new LinearLayoutManager(HalamanKesehatan.this));
            dialog.hide();
            if(responsecode.equals("upload sukses"))
            {
                Toast.makeText(getApplicationContext(), "Berhasil menambahkan data telepon", Toast.LENGTH_SHORT).show();
                finish();
            }
            else
                Toast.makeText(getApplicationContext(), "Gagal menambahkan data telepon", Toast.LENGTH_SHORT).show();

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Sedang mengunggah data...");
            dialog.setCancelable(false);
            dialog.show();
        }
    }
}
