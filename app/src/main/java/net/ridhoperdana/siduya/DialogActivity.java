package net.ridhoperdana.siduya;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.location.*;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import java.util.Collections;
import java.util.List;

public class DialogActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private android.location.Location mCurrentLocation;
    private Double latitude, longitude;
    private com.google.android.gms.maps.MapFragment maps;
    private MapFragment mMapFragment;
    private String nama, alamat;
    private TextView viewNama, viewAlamat;

    List<Results> list = Collections.emptyList();
//    List<Results> list = Collections.emptyList();

    private final int[] MAP_TYPES = { GoogleMap.MAP_TYPE_SATELLITE,
            GoogleMap.MAP_TYPE_NORMAL,
            GoogleMap.MAP_TYPE_HYBRID,
            GoogleMap.MAP_TYPE_TERRAIN,
            GoogleMap.MAP_TYPE_NONE };
    private int curMapTypeIndex = 1;
    private Double lat, longt;

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

        viewNama = (TextView)findViewById(R.id.nama_tempat);
        viewAlamat = (TextView)findViewById(R.id.alamat_tempat);

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
                            .getLastLocation( mGoogleApiClient );

                    initCamera(mMap, mCurrentLocation);
                }
            });


        }

        FloatingActionButton myFab = (FloatingActionButton)findViewById(R.id.fab);

        myFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?daddr=" + lat.toString() + "," + longt.toString() + ""));
                startActivity(intent);
            }
        });

       /* initListeners();*/
    }

    /*private void initListeners() {
        mMapFragment.getMapAsync(new GoogleMap.OnMarkerClickListener(this));
        getMap().setOnMapLongClickListener(this);
        getMap().setOnInfoWindowClickListener(this);
        getMap().setOnMapClickListener(this);
    }*/

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
                .zoom( 16f )
                .bearing( 0.0f )
                .tilt(0.0f)
                .build();

        gmaps.addMarker(new MarkerOptions().position(pos));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tpsLoc, zoomLevel2));

        gmaps.animateCamera( CameraUpdateFactory
                .newCameraPosition(position), null );

        gmaps.setMapType(MAP_TYPES[curMapTypeIndex]);
        gmaps.setTrafficEnabled(true);
        gmaps.setMyLocationEnabled(true);
        gmaps.getUiSettings().setZoomControlsEnabled(true);
    }

    @Override
    public void onConnected(Bundle bundle) {

        /*mCurrentLocation = LocationServices
                .FusedLocationApi
                .getLastLocation( mGoogleApiClient );

        initCamera(mMmCurrentLocation);*/
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }




}
