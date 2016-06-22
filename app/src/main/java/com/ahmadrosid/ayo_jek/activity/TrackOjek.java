package com.ahmadrosid.ayo_jek.activity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.ahmadrosid.ayo_jek.R;
import com.ahmadrosid.ayo_jek.helper.Constans;
import com.ahmadrosid.ayo_jek.model.Track;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import fr.quentinklein.slt.LocationTracker;

public class TrackOjek extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "TrackOjek";

    private GoogleMap mMap;
    private Context context;
    private LocationTracker tracker;
    private BitmapDescriptor iconMe;
    private BitmapDescriptor iconDriver;
    private DatabaseReference mDatabase;
    private String chanel;
    private Marker myMarker;
    private Marker ojekMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_ojek);

        context = this;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        chanel = getIntent().getStringExtra(Constans.CHANEL);

        iconMe = BitmapDescriptorFactory.fromResource(R.drawable.ic_user_location);
        iconMe = BitmapDescriptorFactory.fromResource(R.drawable.ic_bike_location);

        loadLocationBiker();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        } else {
            mMap.setMyLocationEnabled(true);
            tracker = new LocationTracker(context
            ) {
                @Override
                public void onLocationFound(Location location) {
                    // Do some stuff
                    log(String.valueOf(location.getLatitude()));
                    log(String.valueOf(location.getLongitude()));

                    LatLng my_location = new LatLng(location.getLatitude(), location.getLongitude());

                    if (myMarker == null) {
                        log("Marker null");
                        myMarker = mMap.addMarker(new MarkerOptions().position(my_location).icon(iconMe));
                        CameraPosition myPosition = new CameraPosition.Builder()
                                .target(my_location).zoom(15).build();
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(myPosition));
                    } else {
                        log("Marker remove and update!");
                        myMarker.remove();
                        myMarker = mMap.addMarker(new MarkerOptions().position(my_location).icon(iconMe));
                    }
                }

                @Override
                public void onTimeout() {
                    log("Connection timeout!");
                }
            };
            tracker.startListening();
        }
    }

    void loadLocationBiker() {
        mDatabase.child("tracking").child(chanel).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        Track data = dataSnapshot.getValue(Track.class);
                        LatLng lng = new LatLng(data.latitude, data.longitude);
                        if (ojekMarker != null) {
                            ojekMarker.remove();
                            ojekMarker = mMap.addMarker(new MarkerOptions().position(lng).icon(iconDriver));
                        }else{
                            ojekMarker = mMap.addMarker(new MarkerOptions().position(lng).icon(iconDriver));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });
    }

    void log(String s) {
        Log.d(TAG, "log: " + s);
    }
}
