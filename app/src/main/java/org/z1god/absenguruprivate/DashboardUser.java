package org.z1god.absenguruprivate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.z1god.absenguruprivate.Adapter.AbsenAdapter;
import org.z1god.absenguruprivate.Adapter.SiswaAdapter;
import org.z1god.absenguruprivate.Model.AbsenModel;
import org.z1god.absenguruprivate.Model.MessageModel;
import org.z1god.absenguruprivate.Model.SiswaModel;
import org.z1god.absenguruprivate.Model.TestModel;
import org.z1god.absenguruprivate.Retrofit.ApiConfig;
import org.z1god.absenguruprivate.Utilitis.MyDate;
import org.z1god.absenguruprivate.Utilitis.MyPreferences;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.z1god.absenguruprivate.DosenAddSiswaActivity.KEY_ID_DOSEN_DASA;
import static org.z1god.absenguruprivate.DosenAddSiswaActivity.KEY_ID_SISWA_ADDED_SUCCESS;
import static org.z1god.absenguruprivate.MapsActivity.KEY_LATITUDE;
import static org.z1god.absenguruprivate.MapsActivity.KEY_LONGITUDE;

public class DashboardUser extends AppCompatActivity implements AbsenAdapter.OnItemClickListener {

    public static final String KEY_NAME_DOSEN = "KEY_NAME_DOSEN";
    public static final String KEY_ID_DOSEN = "KEY_ID_DOSEN";

    TextView tvGreeting;
    ProgressBar loading;
    RecyclerView rvAbsen;
    RecyclerView rvSiswa;
    AbsenAdapter adapterAbsen;
    SiswaAdapter adapterSiswa;
    List<AbsenModel> listAbsen;
    List<SiswaModel> listSiswa;

    public static final int RC_ADD_AVAILABLE = 111;

    String idDosen = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_user);

        tvGreeting = findViewById(R.id.tvGreetingUser);
        loading = findViewById(R.id.loadingUser);
        rvAbsen = findViewById(R.id.rv_user);
        rvSiswa = findViewById(R.id.rv_siswa_dosen);

        loading.setVisibility(View.VISIBLE);

        if (getIntent().getStringExtra(KEY_NAME_DOSEN) != null) {
            String nameDosen = getIntent().getStringExtra(KEY_NAME_DOSEN);
            tvGreeting.setText("Selamat datang, " + nameDosen);
        } else {
            tvGreeting.setText("Selamat datang, " + new MyPreferences(this).getNameDosen());
        }

        listAbsen = new ArrayList<>();
        listSiswa = new ArrayList<>();
        adapterAbsen = new AbsenAdapter(listAbsen, this);
        adapterSiswa = new SiswaAdapter(listSiswa);

        rvAbsen.setLayoutManager(new LinearLayoutManager(this));
        rvSiswa.setLayoutManager(new LinearLayoutManager(this));
        rvAbsen.setAdapter(adapterAbsen);
        rvSiswa.setAdapter(adapterSiswa);

        if (getIntent().getStringExtra(KEY_ID_DOSEN) != null) {
            idDosen = getIntent().getStringExtra(KEY_ID_DOSEN);
            getAbsenFromServer(idDosen);
            getSiswaFromServer(idDosen);
        } else {
            Toast.makeText(this, "Terdapat error pada program", Toast.LENGTH_SHORT).show();
            loading.setVisibility(View.INVISIBLE);
        }
    }

    private void getSiswaFromServer(String idDosen) {
        Call<List<SiswaModel>> call = ApiConfig.getApiService().getSiswaAdded(idDosen);
        call.enqueue(new Callback<List<SiswaModel>>() {
            @Override
            public void onResponse(Call<List<SiswaModel>> call, Response<List<SiswaModel>> response) {
                if (response.isSuccessful()) {
                    listSiswa = response.body();
                    adapterSiswa = new SiswaAdapter(listSiswa);
                    rvSiswa.setAdapter(adapterSiswa);
                } else {
                    Gson gson = new Gson();
                    try {
                        MessageModel errorBody = gson.fromJson(response.errorBody().string(), MessageModel.class);
                        Toast.makeText(DashboardUser.this, errorBody.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                loading.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<List<SiswaModel>> call, Throwable t) {
                Toast.makeText(DashboardUser.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                loading.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void getAbsenFromServer(String id_dosen) {
        Call<List<AbsenModel>> call = ApiConfig.getApiService().getAbsen(id_dosen);
        call.enqueue(new Callback<List<AbsenModel>>() {
            @Override
            public void onResponse(Call<List<AbsenModel>> call, Response<List<AbsenModel>> response) {
                if (response.isSuccessful()) {
                    listAbsen = response.body();
                    adapterAbsen = new AbsenAdapter(listAbsen, DashboardUser.this);
                    rvAbsen.setAdapter(adapterAbsen);
                } else {
                    Gson gson = new Gson();
                    try {
                        MessageModel errorBody = gson.fromJson(response.errorBody().string(), MessageModel.class);
                        Toast.makeText(DashboardUser.this, errorBody.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                loading.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<List<AbsenModel>> call, Throwable t) {
                Toast.makeText(DashboardUser.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                loading.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void logout(View view) {
        loading.setVisibility(View.VISIBLE);

        String currentTime = MyDate.getCurrentDateAndTime()[1];
        Call<Void> call = ApiConfig.getApiService().updateAbsen(new TestModel(currentTime));
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    Gson gson = new Gson();
                    try {
                        MessageModel errorBody = gson.fromJson(response.errorBody().string(), MessageModel.class);
                        Toast.makeText(DashboardUser.this, errorBody.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                loading.setVisibility(View.INVISIBLE);
                new MyPreferences(DashboardUser.this).dosenLogin(false);
                startActivity(new Intent(DashboardUser.this, MainActivity.class));
                finish();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                loading.setVisibility(View.INVISIBLE);

                Log.d("TAG", "onFailure: " + t.getCause());
                Toast.makeText(DashboardUser.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                new MyPreferences(DashboardUser.this).dosenLogin(false);
                startActivity(new Intent(DashboardUser.this, MainActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onClick(AbsenModel absen) {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra(KEY_LATITUDE, absen.getLatitude());
        intent.putExtra(KEY_LONGITUDE, absen.getLongitude());
        startActivity(intent);
    }

    public void showSiswaAvailable(View view) {
        if (idDosen != null){
            Intent intent = new Intent(this, DosenAddSiswaActivity.class);
            intent.putExtra(KEY_ID_DOSEN_DASA, idDosen);
            startActivityForResult(intent, RC_ADD_AVAILABLE);
        }else{
            Toast.makeText(this, "something wrong", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == RC_ADD_AVAILABLE){
                if (data != null) {
                    getAbsenFromServer(data.getStringExtra(KEY_ID_SISWA_ADDED_SUCCESS));
                    getSiswaFromServer(data.getStringExtra(KEY_ID_SISWA_ADDED_SUCCESS));
                }
            }
        }
    }
}
