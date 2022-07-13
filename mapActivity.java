package com.example.mycomputer.voicemobileapp;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;


public class mapActivity extends FragmentActivity implements OnMapReadyCallback {

    LatLng position;
    MarkerOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        String loc= globalapp.loc;
        position = getLocationFromAddress(this,loc);
        options = new MarkerOptions();
        options.position(position);
        options.title("My Place");
        options.snippet("Golden Temple");
        FragmentManager myFragmentManager = getSupportFragmentManager();
        SupportMapFragment mySupportMapFragment
                = (SupportMapFragment)myFragmentManager.findFragmentById(R.id.map);
        mySupportMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
try{
    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
    googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
    googleMap.addMarker(options);
}catch (Exception it)
{
    it.printStackTrace();
}


    }

    public LatLng getLocationFromAddress(Context context,String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);

            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return p1;
    }
}


