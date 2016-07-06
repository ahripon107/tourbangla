package com.sfuronlabs.ripon.tourbangla.activities;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sfuronlabs.ripon.tourbangla.R;

public class MapsActivity extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;

    public MapsActivity() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);

        View v = inflater.inflate(R.layout.activity_maps, container, false);
        ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map123)).getMapAsync(this);
        if (mMap != null) {
            setUpMap();
        }

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public static MapsActivity NewInstanceOfMapsActivity(String lat, String longi) {
        MapsActivity myFragment = new MapsActivity();
        Bundle arguments = new Bundle();
        arguments.putString("lat", lat);
        arguments.putString("longi", longi);

        myFragment.setArguments(arguments);
        return myFragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SupportMapFragment mapFragment = ((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map123));

        if (mapFragment != null) {
            FragmentManager fM = getFragmentManager();
            fM.beginTransaction().remove(mapFragment).commit();
        }
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map123)).getMapAsync(this);

            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        String lat = getArguments().getString("lat");
        String longi = getArguments().getString("longi");
        mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(lat), Double.parseDouble(longi))).title("Marker"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(lat), Double.parseDouble(longi)), 14.0f));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
    }
}
