package net.ridhoperdana.siduya;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Fragment;
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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.android.gms.location.LocationListener;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Text;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HalamanDepan extends Activity{

    private static final int MY_FINE_ACCESS = 123;
    private static final String[] INITIAL_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    MapFragment map;
    TextView lokasi_saya;
    String address = "";
    private LocationManager manager;
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

    Geocoder geocoder;
    List<Address> addresses;

    String Alamat;
    public String kategori;
    private AutoCompleteTextView auto;
    private ProgressDialog dialog;

    private Double lat_search, long_search;
    private int flag=0;

//    CurrentAddress curr = new CurrentAddress();

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
        lokasi_saya = (TextView)findViewById(R.id.lokasi_sekarang);
        cardKeamanan = (CardView) findViewById(R.id.card_keamanan);
        cardKesehatan = (CardView) findViewById(R.id.card_kesehatan);
        cardTransportasi = (CardView)findViewById(R.id.card_transportasi);
//        button = (Button)findViewById(R.id.button_location);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getLocation();
//                try {
//                    getAddress();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });

        new Async().execute("go");

        auto = (AutoCompleteTextView)findViewById(R.id.input_cari);
        auto.setAdapter(new PlaceAdapter(this, R.layout.autocomplete));
        auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String description = (String) parent.getItemAtPosition(position);
                getLocationFromAddress(getApplicationContext(), description);
                lokasi_saya.setText(description.substring(0, 15));
                lokasi_saya = new TextView(getApplicationContext());
                flag = 1;
                Log.d("masuk sini", "onitemclick");
                lat = lat_search;
                longt = long_search;
            }
        });
//        SendIntent(flag);
//        Log.d("flag-->", String.valueOf(flag));
        SendNewIntent();
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
    private void SendIntent(int flag)
    {
        if(flag==0)
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
        else
        {
            Log.d("masuk flag", "-->1");
            cardKeamanan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(HalamanDepan.this, HalamanKeamanan.class);
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                            HalamanDepan.this, new Pair<View, String>(view.findViewById(R.id.card_keamanan), getString(R.string.transition_name_name))
                    );

                    kategori = "Keamanan";
                    intent.putExtra("Lat", lat_search);
                    intent.putExtra("Lng", long_search);
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
                    intent_kesehatan.putExtra("Lat", lat_search);
                    intent_kesehatan.putExtra("Lng", long_search);
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
                    intent_transportasi.putExtra("Lat", lat_search);
                    intent_transportasi.putExtra("Lng", long_search);
                    intent_transportasi.putExtra("kategori", kategori);
                    ActivityCompat.startActivity(HalamanDepan.this, intent_transportasi, options.toBundle());
                }
            });
        }
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

    private void getAddress() throws IOException {
        geocoder = new Geocoder(this, Locale.getDefault());

        addresses = geocoder.getFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        lat = mLastLocation.getLatitude();
        longt = mLastLocation.getLongitude();

        Alamat = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//        String city = addresses.get(0).getLocality();
//        String state = addresses.get(0).getAdminArea();
//        String country = addresses.get(0).getCountryName();
//        String postalCode = addresses.get(0).getPostalCode();
//        String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
//        lokasi_saya = (TextView)findViewById(R.id.lokasi_sekarang);
//        lokasi_saya.setText(Alamat);
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
