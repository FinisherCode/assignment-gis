package sk.finishersapps.finisher.lovemydogo.acitivities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import sk.finishersapps.finisher.lovemydogo.R;
import sk.finishersapps.finisher.lovemydogo.model.data_objects.GenericMapObject;

/**
 * Handles all calls that wants to show something on the map.
 * It gets data through Intent.
 */
public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<GenericMapObject> mPoints = null;
    private boolean areTwoTypes;

    /**
     * Inits layout of activity and maps serialized data into ArrayList
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mPoints = (ArrayList<GenericMapObject>) getIntent().getSerializableExtra("nodes");
        checkIfAreTwoTypes();
        Log.d("wa", "ha");
    }

    private void checkIfAreTwoTypes() {
        areTwoTypes = false;
        for (GenericMapObject point: mPoints) {
            if(point.isTargetPoint()){
                areTwoTypes = true;
                break;
            }
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        for (GenericMapObject node : mPoints) {
            LatLng ba = new LatLng(node.getLatitude(), node.getLongitude());

            MarkerOptions marker = new MarkerOptions().position(ba).
                    title(node.getName());
            if(areTwoTypes){
                if (node.isTargetPoint()) {
                    marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                } else {
                    marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                }
            } else {
                marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            }
            mMap.addMarker(marker);
            mMap.animateCamera(CameraUpdateFactory.zoomTo(13), 2000, null);
        }
        mMap.setMyLocationEnabled(true);
        setMapToMyLocation();
    }

    /**
     * Moves camera to actual position on map with animation
     */
    private void setMapToMyLocation() {
        LocationManager manager = null;
        String bestProvider = null;
        manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Criteria mCriteria = new Criteria();
        bestProvider = String.valueOf(manager.getBestProvider(mCriteria, true));

        Location mLocation = manager.getLastKnownLocation(bestProvider);
        if (mLocation != null) {
            final double currentLatitude = mLocation.getLatitude();
            final double currentLongitude = mLocation.getLongitude();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLatitude, currentLongitude), 15));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(13), 2000, null);
        }
    }
}
