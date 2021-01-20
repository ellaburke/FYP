package com.example.fyp_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class getLocationActivity extends AppCompatActivity {

    //Initialise Variables
    Button btnLocation;
    TextView TextView1, TextView2, TextView3, TextView4, TextView5;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_location);

        //Assign Variables
        btnLocation = (Button) findViewById(R.id.btn_location);
        TextView1 = (TextView) findViewById(R.id.textview1);
        TextView2 = (TextView) findViewById(R.id.textview2);
        TextView3 = (TextView) findViewById(R.id.textview3);
        TextView4 = (TextView) findViewById(R.id.textview4);
        TextView5 = (TextView) findViewById(R.id.textview5);

        //Initialise fusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getLocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Request permission", Toast.LENGTH_LONG).show();
                    ActivityCompat.requestPermissions(getLocationActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }
                mLocationRequest = new LocationRequest();
                mLocationRequest.setInterval(10000);
                mLocationRequest.setFastestInterval(5000);
                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                fusedLocationProviderClient = new FusedLocationProviderClient(getApplicationContext());

                fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        double lat = locationResult.getLastLocation().getLatitude();
                        double lng = locationResult.getLastLocation().getLongitude();
                        TextView txtview1 = (TextView) findViewById(R.id.textview1);
                        txtview1.setText("Coordinate: " + String.valueOf(lat) + ", " + String.valueOf(lng));
                    }
                }, null);
            }

        });



    }

}
