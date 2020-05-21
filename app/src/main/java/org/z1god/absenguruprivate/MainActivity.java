package org.z1god.absenguruprivate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import org.z1god.absenguruprivate.Model.AbsenModel;
import org.z1god.absenguruprivate.Model.DosenModel;
import org.z1god.absenguruprivate.Model.MessageModel;
import org.z1god.absenguruprivate.Retrofit.ApiConfig;
import org.z1god.absenguruprivate.Utilitis.MyDate;
import org.z1god.absenguruprivate.Utilitis.MyPreferences;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.z1god.absenguruprivate.DashboardUser.KEY_ID_DOSEN;
import static org.z1god.absenguruprivate.DashboardUser.KEY_NAME_DOSEN;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_ID = 105;
    EditText username, password;
    ProgressBar loading;

    MyPreferences pref;

    FusedLocationProviderClient mFusedLocatioProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pref = new MyPreferences(this);

        if (pref.isAdminLogin()) {
            startActivity(new Intent(MainActivity.this, DashbordAdmin.class));
            finish();
        } else if (pref.isDosenLogin()) {
            Intent intent = new Intent(MainActivity.this, DashboardUser.class);
            intent.putExtra(KEY_NAME_DOSEN, pref.getNameDosen());
            intent.putExtra(KEY_ID_DOSEN, pref.getIdDosen());
            startActivity(intent);
            finish();
        }

        mFusedLocatioProvider = LocationServices.getFusedLocationProviderClient(this);

        Button btnLogin = findViewById(R.id.btn_login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loading = findViewById(R.id.loading_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLastLocation();
            }
        });
        btnLogin.setOnClickListener(v -> {
            String usr = username.getText().toString();
            String pswd = password.getText().toString();

            if (usr.equalsIgnoreCase("admin") && pswd.equalsIgnoreCase("admin")) {
                pref.adminLogin(true);
                startActivity(new Intent(MainActivity.this, DashbordAdmin.class));
                finish();
            } else {
                loading.setVisibility(View.VISIBLE);
                getLastLocation();
            }
        });
    }




    private boolean checkPermissionsLocation(){
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }
    private void requestPermissionsLocation(){
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_ID){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLocation();
            }
        }
    }

    private boolean isLocationEnabled(){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    private void getLastLocation(){
        if(checkPermissionsLocation()){
            if(isLocationEnabled()){
                mFusedLocatioProvider
                        .getLastLocation()
                        .addOnCompleteListener(new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if(location == null){
                                    requestNewLocationData();
                                }else{
                                    loginUser(location);
                                }
                            }
                        });
            }else{
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        }else{
            requestPermissionsLocation();
        }
    }

    private void requestNewLocationData(){

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocatioProvider = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocatioProvider.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );
    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            loginUser(mLastLocation);
        }
    };

    private void loginUser(Location loc){
        Call<DosenModel> loginDosen = ApiConfig.getApiService().loginDosen(username.getText().toString(), password.getText().toString());
        loginDosen.enqueue(new Callback<DosenModel>() {
            @Override
            public void onResponse(Call<DosenModel> call, Response<DosenModel> response) {
                if (response.isSuccessful()) {
                    String latitude = String.valueOf(loc.getLatitude());
                    String longitude = String.valueOf(loc.getLongitude());
                    String idDosen = response.body().getId_dosen();
                    String nameDosen = response.body().getNama();
                    String date = MyDate.getCurrentDateAndTime()[0];
                    String timeLogin = MyDate.getCurrentDateAndTime()[1];

                    Log.e("TAG", "latitude : " + latitude );
                    Log.e("TAG", "longitude : " + longitude );

                    AbsenModel absen = new AbsenModel(date, timeLogin, "00:00:00", idDosen, null,latitude,longitude);

                    Call<Void> callAbsen = ApiConfig.getApiService().postAbsen(absen);
                    callAbsen.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            loading.setVisibility(View.INVISIBLE);
                            pref.dosenLogin(true);
                            pref.setIdDosen(idDosen);
                            pref.setNameDosen(nameDosen);

                            Intent intent = new Intent(MainActivity.this, DashboardUser.class);
                            intent.putExtra(KEY_NAME_DOSEN, nameDosen);
                            intent.putExtra(KEY_ID_DOSEN, idDosen);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(MainActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
                            loading.setVisibility(View.INVISIBLE);
                        }
                    });
                }else{
                    Gson gson = new Gson();
                    try {
                        MessageModel errorBody = gson.fromJson(response.errorBody().string(), MessageModel.class);
                        Toast.makeText(MainActivity.this, errorBody.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    loading.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<DosenModel> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                loading.setVisibility(View.INVISIBLE);
            }
        });
    }

}

