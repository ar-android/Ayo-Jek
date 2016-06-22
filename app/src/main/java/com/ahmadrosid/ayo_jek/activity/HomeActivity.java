package com.ahmadrosid.ayo_jek.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ahmadrosid.ayo_jek.R;
import com.ahmadrosid.ayo_jek.helper.Constans;
import com.ahmadrosid.ayo_jek.helper.PreferenceHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import fr.quentinklein.slt.LocationTracker;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "HomeActivity : ";
    private GoogleMap mMap;
    private BitmapDescriptor iconMe;
    private Context context;
    private LocationTracker tracker;
    private double LATITUDE_ORIGIN;
    private double LONGITUDE_ORIGIN;
    private Marker myMarker;
    private TextView full_name;
    private TextView email;
    private Button btn_pesan_ojek;
    private TextView origin;
    private TextView destination;
    private int LOCATION_DESTINATION = 0;
    private double LATITUDE_DESTINATION;
    private double LONGITUDE_DESTINATION;
    private GoogleApiClient mGoogleApiClient;
    private String full_name_user;
    private String email_user;
    private String uid_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        full_name = (TextView) header.findViewById(R.id.full_name);
        email = (TextView) header.findViewById(R.id.email);
        origin = (TextView) findViewById(R.id.origin);
        destination = (TextView) findViewById(R.id.destination);
        btn_pesan_ojek = (Button) findViewById(R.id.btn_pesan_ojek);
        iconMe = BitmapDescriptorFactory.fromResource(R.drawable.ic_user_location);

        navigationView.setNavigationItemSelectedListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        btn_pesan_ojek.setOnClickListener(this);
        destination.setOnClickListener(this);

        getDataProfile();
    }

    /**
     * GET data profile
     */
    void getDataProfile() {
        PreferenceHelper preferenceHelper = new PreferenceHelper(getApplicationContext());
        Map<String, Object> data = preferenceHelper.getDataProfile();
        log(TAG + data.get(Constans.FULL_NAME));
        full_name_user = data.get(Constans.FULL_NAME).toString();
        email_user = data.get(Constans.EMAIL).toString();
        uid_user = data.get(Constans.UID).toString();
        full_name.setText(full_name_user);
        email.setText(email_user);
    }

    void log(String s) {
        Log.d(TAG, "log: " + s);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), SignActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            // Handle the camera action
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            askPermission();
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
                        LATITUDE_ORIGIN = location.getLatitude();
                        LONGITUDE_ORIGIN = location.getLongitude();
                        displayLocation(origin, LATITUDE_ORIGIN, LONGITUDE_ORIGIN);
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
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    @TargetApi(Build.VERSION_CODES.M)
    private void askPermission() {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("GPS");
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_COARSE_LOCATION))
            permissionsNeeded.add("Access Coarse Location");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                            }
                        });
                return;
            }
            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return;
        }

    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
            {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted
                } else {
                    // Permission Denied
                    Toast.makeText(this, "Some Permission is Denied", Toast.LENGTH_SHORT)
                            .show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pesan_ojek:
                if (TextUtils.isEmpty(destination.getText())) {
                    Snackbar snack = Snackbar.make(v, "Lokasi Tujuan Harus Ada", Snackbar.LENGTH_LONG);

                    snack.getView().addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                        public boolean canDisplaySnackbar;

                        @Override
                        public void onViewAttachedToWindow(View v) {
                            canDisplaySnackbar = false;
                        }

                        @Override
                        public void onViewDetachedFromWindow(View v) {
                            new CountDownTimer(3000, 1000) {
                                public void onTick(long millisUntilFinished) {
                                }

                                public void onFinish() {
                                    canDisplaySnackbar = false;
                                }
                            }.start();
                        }
                    });
                    snack.show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), Booking.class);
                    intent.putExtra(Constans.LATITUDE_ORIGIN, String.valueOf(LATITUDE_ORIGIN));
                    intent.putExtra(Constans.LONGITUDE_ORIGIN, String.valueOf(LONGITUDE_ORIGIN));
                    intent.putExtra(Constans.LATITUDE_DESTINATION, String.valueOf(LATITUDE_DESTINATION));
                    intent.putExtra(Constans.LONGITUDE_DESTINATION, String.valueOf(LONGITUDE_DESTINATION));
                    intent.putExtra(Constans.LOCATION_NAME_ORIGIN, origin.getText().toString());
                    intent.putExtra(Constans.LOCATION_NAME_DESTINATION, destination.getText().toString());
                    startActivity(intent);
                }
                break;
            case R.id.destination:
                pickLocation(LOCATION_DESTINATION);
                break;
        }
    }

    /**
     * Pick location
     *
     * @param REQUEST_PLACE_PICKER
     */
    public void pickLocation(int REQUEST_PLACE_PICKER) {
        log("onClick: etLocationPickup clicked");
        try {
            PlacePicker.IntentBuilder intentBuilder =
                    new PlacePicker.IntentBuilder();
            Intent intent = intentBuilder.build(this);

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                intent.putExtra("primary_color", getResources().getColor(R.color.colorPrimary));
                intent.putExtra("primary_color_dark", getResources().getColor(R.color.colorPrimaryDark));
            }

            startActivityForResult(intent, REQUEST_PLACE_PICKER);

        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null)
            if (requestCode == LOCATION_DESTINATION
                    && resultCode == Activity.RESULT_OK) {

                // The user has selected a place. Extract the name and address.
                final Place place = PlacePicker.getPlace(this, data);

                LATITUDE_DESTINATION = place.getLatLng().latitude;
                LONGITUDE_DESTINATION = place.getLatLng().longitude;

                final CharSequence name = place.getName();
                final CharSequence address = place.getAddress();
                if (address.length() > 5) {
                    destination.setText(name);
                    destination.setTextColor(Color.BLACK);
                } else {
                    String coordLocation = "( " + String.valueOf(place.getLatLng().latitude) + ", " + String.valueOf(place.getLatLng().longitude) + " )";
                    try {
                        log(coordLocation);
                        List<Address> listAddressFrom = getAddress(place.getLatLng());
                        if (listAddressFrom != null) {

                            String lokasi = listAddressFrom.get(0).getAddressLine(0) + ", " + listAddressFrom.get(0).getAddressLine(1);
                            destination.setText(lokasi);
                            destination.setTextColor(Color.BLACK);
                        } else {
                            destination.setText(coordLocation);
                            destination.setTextColor(Color.BLACK);
                        }
                    } catch (IndexOutOfBoundsException e) {
                        e.printStackTrace();
                        destination.setText(coordLocation);
                        destination.setTextColor(Color.BLACK);
                        log(TAG + "onLocationChanged: index out of bounds");
                    }
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
    }

    /**
     * Get addrees name
     *
     * @param point
     * @return string - string
     */
    public List<Address> getAddress(LatLng point) {
        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this);
            if (point.latitude != 0 || point.longitude != 0) {
                addresses = geocoder.getFromLocation(point.latitude,
                        point.longitude, 1);
                String address = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getAddressLine(1);
                String country = addresses.get(0).getAddressLine(2);

                log(address + " - " + city + " - " + country);

                return addresses;

            } else {
                Toast.makeText(this, "latitude and longitude are null",
                        Toast.LENGTH_LONG).show();
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log(e.toString());
            return null;
        }
    }

    /***
     * Display location name from latitude and longitude
     *
     * @param addres
     * @param latitude
     * @param longitude
     */
    private void displayLocation(TextView addres, double latitude, double longitude) {
        try {
            Geocoder geo = new Geocoder(this.getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geo.getFromLocation(latitude, longitude, 1);
            if (addresses.isEmpty()) {
                addres.setText("Waiting for Location");
            } else {
                if (addresses.size() > 0) {
                    addres.setText(addresses.get(0).getFeatureName());
                    addres.setTextColor(Color.BLACK);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
