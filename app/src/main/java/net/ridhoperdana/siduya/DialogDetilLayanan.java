package net.ridhoperdana.siduya;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.location.*;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
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
import java.util.Collections;
import java.util.List;

public class DialogDetilLayanan extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private android.location.Location mCurrentLocation;
    private com.google.android.gms.maps.MapFragment maps;
    private MapFragment mMapFragment;
    private String nama, alamat;
    private TextView viewNama, viewAlamat, viewTelepon;
    private StringBuilder url;
    private String url_telepon;
    private List<DatabaseTelpon> tampungtelepon = new ArrayList<>();
    private String namautf, alamatutf;
    private String notelponjson;
    private Toast notif;
    private Double myLat, myLongt;
    private LocationManager manager;
    private Location mLastLocation;

    List<Results> list = Collections.emptyList();

    private final int[] MAP_TYPES = { GoogleMap.MAP_TYPE_SATELLITE,
            GoogleMap.MAP_TYPE_NORMAL,
            GoogleMap.MAP_TYPE_HYBRID,
            GoogleMap.MAP_TYPE_TERRAIN,
            GoogleMap.MAP_TYPE_NONE };
    private int curMapTypeIndex = 1;
    private Double lat, longt;
    DatabaseTelpon[] request;
    public LatLngBounds AUSTRALIA;
    private String kategori;
    private ImageView gambar_telepon;
    private ImageView tombol_navigasi;
    private ImageView tombol_tambah;
    private AsyncTask kelas_async;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        mGoogleApiClient = new GoogleApiClient.Builder( this )
                .addConnectionCallbacks( this )
                .addOnConnectionFailedListener( this )
                .addApi(LocationServices.API)
                .build();

        lat = getIntent().getDoubleExtra("value_lat", 1);
        longt = getIntent().getDoubleExtra("value_longt", 1);
        nama = getIntent().getExtras().getString("value_nama");
        alamat = getIntent().getExtras().getString("value_alamat");
        kategori = getIntent().getExtras().getString("value_kategori");

        try {
            namautf = URLEncoder.encode(nama, "UTF-8").replace("+", "%20");
            alamatutf = URLEncoder.encode(alamat, "UTF-8").replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        url_telepon = "http://cangkruk.info/ldts/public/notelpon/" + namautf;

        viewNama = (TextView)findViewById(R.id.nama_tempat);
        viewAlamat = (TextView)findViewById(R.id.alamat_tempat);
        viewTelepon = (TextView)findViewById(R.id.telepon);
        gambar_telepon = (ImageView)findViewById(R.id.telepon_gambar);

        kelas_async = new Async().execute(url_telepon);

        viewNama.setText(nama);
        viewAlamat.setText(alamat);

        mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.fragmentMap);
        if (mMapFragment == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mMapFragment = MapFragment.newInstance();
            fragmentTransaction.replace(R.id.fragmentMap, mMapFragment).commit();
        }

        if (mMapFragment != null) {
            mMapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    GoogleMap mMap = googleMap;


                    mCurrentLocation = LocationServices
                            .FusedLocationApi
                            .getLastLocation(mGoogleApiClient);

                    initCamera(mMap, mCurrentLocation);
                }
            });
        }

//        FloatingActionButton myFab = (FloatingActionButton)findViewById(R.id.fab);
//
//        myFab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
//                        Uri.parse("http://maps.google.com/maps?daddr=" + lat.toString() + "," + longt.toString() + ""));
//                startActivity(intent);
//            }
//        });

        tombol_navigasi = (ImageView)findViewById(R.id.tombol_navigasi);
        tombol_navigasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?daddr=" + lat.toString() + "," + longt.toString() + ""));
                startActivity(intent);
            }
        });
        tombol_tambah = (ImageView)findViewById(R.id.tambah_gambar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Async().execute(url_telepon);
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if( mGoogleApiClient != null && mGoogleApiClient.isConnected() ) {
            mGoogleApiClient.disconnect();
        }
    }

    private void initCamera( GoogleMap gmaps, android.location.Location location ) {
        LatLng pos = new LatLng(lat, longt );

        CameraPosition position = CameraPosition.builder()
                .target( pos )
                .zoom( 13f )
                .bearing( 0.0f )
                .tilt(0.0f)
                .build();
        gmaps.addMarker(new MarkerOptions().position(pos));

        gmaps.animateCamera( CameraUpdateFactory
                .newCameraPosition(position), null );

        gmaps.setMapType(MAP_TYPES[curMapTypeIndex]);
        gmaps.setTrafficEnabled(true);
        gmaps.setMyLocationEnabled(true);
        gmaps.getUiSettings().setZoomControlsEnabled(true);
    }

    @Override
    public void onConnected(Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public void getJSONFromUrl(String url) {

        // Making HTTP request
        try {
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);

            HttpResponse httpResponse = httpClient.execute(httpGet);
            Reader reader = new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8");
            Log.d("url---->", url);
            Gson baru = new Gson();
            try {
                request = baru.fromJson(reader, DatabaseTelpon[].class);
                for(int i = 0; i<request.length; i++)
                {
                    Log.d("isi dari request-->", request[i].getNotelpon());
                }
            }
            catch (ClassCastException e){
                notif.setText("Gagal mendapat data");
                notif.show();
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

    public class Async extends AsyncTask<String, Integer, Double>
    {

        @Override
        protected Double doInBackground(String... params) {
            getJSONFromUrl(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Double aDouble) {
            super.onPostExecute(aDouble);
            try{
                if(request.length==0)
                {
                    viewTelepon.setText("Tidak tersedia. Tambahkan?");
                }
                else
                {
                    viewTelepon.setText(request[0].getNotelpon());
                }
            }catch (Exception e)
            {
                Toast.makeText(getApplicationContext(), "Service sedang di-nonaktifkan, silahkan cobalagi nanti.", Toast.LENGTH_SHORT).show();
            }


//            super.isCancelled();
            if(viewTelepon.getText().toString().equals("Tidak tersedia. Tambahkan?"))
            {
                gambar_telepon.setVisibility(View.GONE);
                Log.d("tidak ada nomor", "hmmm");
            }
            else
            {
                tombol_tambah.setVisibility(View.GONE);
                Log.d("value getText", viewTelepon.getText().toString());
                Log.d("status async->", kelas_async.getStatus().toString());
            }
            tombol_tambah.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent_inputdata = new Intent(DialogDetilLayanan.this, InputData.class);
//                Intent i  = new Intent(context, DialogDetilLayanan.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent_inputdata.putExtra("value_nama", namautf);
                    intent_inputdata.putExtra("value_alamat", alamatutf);
                    intent_inputdata.putExtra("value_lat", lat);
                    intent_inputdata.putExtra("value_longt", longt);
                    intent_inputdata.putExtra("value_kategori", kategori);

                    startActivity(intent_inputdata);
                }
            });
            gambar_telepon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                if((viewTelepon.getText()!="..." ) && (viewTelepon.getText()!="Tidak tersedia. Tambahkan?" ))
//                {
//                    Intent intent = new Intent(Intent.ACTION_DIAL);
//                    intent.setData(Uri.parse("tel:" + viewTelepon.getText()));
//                    startActivity(intent);
//                }
//                else {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + viewTelepon.getText()));
                    startActivity(intent);

//                }
                }
            });
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            viewTelepon.setText("Tunggu sebentar..");
        }
    }
}