package com.example.fyp_1.Maps;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.fyp_1.R;
import com.example.fyp_1.model.GroceryShop;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MapToShop extends AppCompatActivity implements OnMapReadyCallback {
        // Map Components
        private GoogleMap mMap;
        private FusedLocationProviderClient mFusedLocationClient;
        private Button findGroceryShopBtn;
        private double myLat;
        private double myLng;
        //Grocery Shop list
        private ArrayList<GroceryShop> GroceryShops = new ArrayList<com.example.fyp_1.model.GroceryShop>();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_map_to_shop);

            findGroceryShopBtn = findViewById(R.id.findGroceryShopBtn);

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.GroceryShopMap);
            mapFragment.getMapAsync(this);

            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(MapToShop.this);

            if (ActivityCompat.checkSelfPermission(MapToShop.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(MapToShop.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                ActivityCompat.requestPermissions(MapToShop.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            }


            findGroceryShopBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    findGroceryShops();
                }
            });

        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
            if (requestCode == 100 && grantResults.length > 0 && (grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                getCurrentLocation();
            } else {
                Toast.makeText(MapToShop.this, "Permission Request Denied", Toast.LENGTH_SHORT).show();
            }
        }

        @SuppressLint("MissingPermission")
        public void getCurrentLocation() {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                    || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location != null) {
                            myLat = location.getLatitude();
                            myLng = location.getLongitude();
                            MarkerOptions options = new MarkerOptions().position(new LatLng(myLat, myLng)).title("My Location");
                            mMap.animateCamera(CameraUpdateFactory.newLatLng(options.getPosition()), 50, null);
                            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                            mMap.addMarker(options);
                        } else {
                            LocationRequest locationRequest = new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                    .setInterval(10000).setFastestInterval(1000).setNumUpdates(1);

                            LocationCallback locationCallback = new LocationCallback() {
                                @Override
                                public void onLocationResult(LocationResult locationResult) {
                                    Location lastLocation = locationResult.getLastLocation();
                                    myLat = lastLocation.getLatitude();
                                    myLng = lastLocation.getLongitude();
                                    MarkerOptions options = new MarkerOptions().position(new LatLng(myLat, myLng)).title("My Location");
                                    mMap.animateCamera(CameraUpdateFactory.newLatLng(options.getPosition()), 50, null);
                                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                                    mMap.addMarker(options);
                                }
                            };
                            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                        }
                    }
                });
            }
        }


        public void findGroceryShops() {
            RequestQueue queue = Volley.newRequestQueue(MapToShop.this);
            String stringUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + myLat + "," + myLng + "&radius=5000&types=supermarket&key=AIzaSyCqIy0Rj142OxMMmQkOKIiucvwo8T-QqEw";
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, stringUrl, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray results = response.getJSONArray("results");
                        for (int i = 0; i < results.length(); i++) {
                            JSONObject groceryStore = results.getJSONObject(i);
                            String name = groceryStore.getString("name");
                            JSONObject geometry = groceryStore.getJSONObject("geometry");
                            JSONObject location = geometry.getJSONObject("location");
                            double latitude = location.getDouble("lat");
                            double longitude = location.getDouble("lng");
                            GroceryShop groceryS = new GroceryShop(name, latitude, longitude);
                            GroceryShops.add(groceryS);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MapToShop.this, "Error", Toast.LENGTH_SHORT).show();
                }
            });
            queue.add(request);
            groceryMarkers();
        }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (mMap != null) {
            int permission = ContextCompat.checkSelfPermission(MapToShop.this, Manifest.permission.ACCESS_FINE_LOCATION);
            if (permission == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setZoomControlsEnabled(true);
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
                }
            }

        }
    }


        public void groceryMarkers() {
            for (GroceryShop shop : GroceryShops) {
                mMap.addMarker(new MarkerOptions().position(new LatLng(shop.getLat(), shop.getLng())).title(shop.getShopName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));

            }
        }


}