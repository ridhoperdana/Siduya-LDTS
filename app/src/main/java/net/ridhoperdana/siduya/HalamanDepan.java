package net.ridhoperdana.siduya;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HalamanDepan extends Activity implements LocationListener {

    private static final int MY_FINE_ACCESS = 123;
    private static final String[] INITIAL_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    MapFragment map;
    TextView lokasi_saya;
    String address = "";
    private LocationManager manager, manager_now;
    private LocationListener locationListener;
    private double lat;
    private double longt;
    private Location loca;
    private Context mContext;
    Button button;
    public List<Results> tampung_result = new ArrayList<>();
    Tempat request;

    private Location mLastLocation;

    private CardView cardKeamanan, cardKesehatan, cardTransportasi;

    Geocoder geocoder, geocoder_baru;
    List<Address> addresses, addresses_baru;

    String Alamat;
    String Alamat_saatini;
    public String kategori;
    private AutoCompleteTextView auto;
    private ProgressDialog dialog;

    private Double lat_search, long_search;
    private int flag = 0;
    private int flag_lokasi = 0;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        setContentView(R.layout.activity_halaman_depan);

        mContext = this;

        dialog = new ProgressDialog(this);
        lokasi_saya = (TextView) findViewById(R.id.lokasi_sekarang);
        cardKeamanan = (CardView) findViewById(R.id.card_keamanan);
        cardKesehatan = (CardView) findViewById(R.id.card_kesehatan);
        cardTransportasi = (CardView) findViewById(R.id.card_transportasi);
        button = (Button) findViewById(R.id.button_location);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                finish();
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        new Async().execute("go");

        auto = (AutoCompleteTextView)findViewById(R.id.input_cari);
        auto.setAdapter(new PlaceAdapter(this, R.layout.autocomplete));
        auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String description = (String) parent.getItemAtPosition(position);
                getLocationFromAddress(getApplicationContext(), description);
                Alamat = description.substring(0, 20);
                lokasi_saya.setText(Alamat);
                Log.d("Alamat search", description.substring(0, 20));
                lokasi_saya = new TextView(getApplicationContext());
                flag_lokasi = 0;
//                Log.d("masuk sini", "onitemclick");
                lat = lat_search;
                longt = long_search;
                InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(auto.getWindowToken(), 0);
            }
        });
        SendNewIntent();
    }

    private class Refresh implements View.OnClickListener{

        @Override
        public void onClick(View v) {
//            manager_now = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                return;
//            }
//            manager_now.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, HalamanDepan.this);
////                getLocation();
//            //                    getAddress();
//            dialog.setMessage("Sedang merefresh lokasi..");
//            dialog.setCancelable(false);
//            dialog.show();
//            Log.d("flag_lokasi->", String.valueOf(flag_lokasi));
//            if(flag_lokasi==1 && (lat == mLastLocation.getLatitude() || longt == mLastLocation.getLongitude()))
//            {
//                dialog.hide();
//            }
//            else if(Alamat_saatini!=null && flag_lokasi==0)
//            {
//                Log.d("masuk alamat saat ini", Alamat_saatini);
//                lokasi_saya.setText(Alamat_saatini);
//                Log.d("ini text sekarang->", lokasi_saya.getText().toString());
//                dialog.hide();
//            }
//            Log.d("Alamat refresh-->", Alamat_saatini);
//            //                new Async().execute("go1");

        }
    }

    private void SendNewIntent()
    {
        Log.d("masuk flag", "-->0");
        cardKeamanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HalamanDepan.this, HalamanKeamanan.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        HalamanDepan.this, new Pair<View, String>(view.findViewById(R.id.card_keamanan), getString(R.string.transition_name_name))
                );

                kategori = "Keamanan";
                intent.putExtra("Lat", lat);
                intent.putExtra("Lng", longt);
                intent.putExtra("kategori", kategori);
                ActivityCompat.startActivity(HalamanDepan.this, intent, options.toBundle());
            }
        });
        cardKesehatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_kesehatan = new Intent(HalamanDepan.this, HalamanKesehatan.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        HalamanDepan.this, new Pair<View, String>(v.findViewById(R.id.card_kesehatan), getString(R.string.transition_name_name))
                );

                kategori = "Kesehatan";
                intent_kesehatan.putExtra("Lat", lat);
                intent_kesehatan.putExtra("Lng", longt);
                intent_kesehatan.putExtra("kategori", kategori);
                ActivityCompat.startActivity(HalamanDepan.this, intent_kesehatan, options.toBundle());
            }
        });
        cardTransportasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_transportasi = new Intent(HalamanDepan.this, HalamanTransportasi.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        HalamanDepan.this, new Pair<View, String>(v.findViewById(R.id.card_transportasi), getString(R.string.transition_name_name))
                );

                kategori = "Transportasi";
                intent_transportasi.putExtra("Lat", lat);
                intent_transportasi.putExtra("Lng", longt);
                intent_transportasi.putExtra("kategori", kategori);
                ActivityCompat.startActivity(HalamanDepan.this, intent_transportasi, options.toBundle());
            }
        });
    }

    private void getLocation()
    {
        manager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLastLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (mLastLocation == null){
            mLastLocation = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        if (mLastLocation != null)
            Log.d("Location : ","Lat = "+ mLastLocation.getLatitude() + " Lng");
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private int getAddress() throws IOException {
        geocoder = new Geocoder(this, Locale.getDefault());

        addresses = geocoder.getFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        lat = mLastLocation.getLatitude();
        longt = mLastLocation.getLongitude();

        Alamat = addresses.get(0).getAddressLine(0);
//        String city = addresses.get(0).getLocality();
//        String state = addresses.get(0).getAdminArea();
//        String country = addresses.get(0).getCountryName();
//        String postalCode = addresses.get(0).getPostalCode();
//        String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
//        lokasi_saya = (TextView)findViewById(R.id.lokasi_sekarang);
//        lokasi_saya.setText(Alamat);
        Alamat_saatini = Alamat;
        return 1;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        longt = location.getLongitude();
        geocoder_baru = new Geocoder(this, Locale.getDefault());

        try {
            addresses_baru = geocoder.getFromLocation(lat, longt, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }
        Alamat = addresses.get(0).getAddressLine(0);
        Alamat_saatini = Alamat;
//        if(Alamat_saatini!=null)
        lokasi_saya.setText(Alamat_saatini);
        flag_lokasi = 1;
//        Alamat_saatini = null;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private class Async extends AsyncTask<String, Integer, Double>
    {
        @Override
        protected Double doInBackground(String... params) {
//            return null;
            getLocation();
            try {
                getAddress();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Double aDouble) {
            super.onPostExecute(aDouble);

            lokasi_saya.setText(Alamat);
            Log.d("Alamat auto-->", Alamat);
            dialog.hide();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Sedang mencari lokasi...");
            dialog.setCancelable(false);
            dialog.show();
        }
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

    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            lat_search = location.getLatitude();
            long_search = location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (Exception ex) {

            ex.printStackTrace();
        }
        return p1;
    }
}
