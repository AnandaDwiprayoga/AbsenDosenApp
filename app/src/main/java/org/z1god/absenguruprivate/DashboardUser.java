package org.z1god.absenguruprivate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.z1god.absenguruprivate.Adapter.AbsenAdapter;
import org.z1god.absenguruprivate.Model.AbsenModel;
import org.z1god.absenguruprivate.Model.MessageModel;
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

public class DashboardUser extends AppCompatActivity {

    public static final String KEY_NAME_DOSEN = "KEY_NAME_DOSEN";
    public static final String KEY_ID_DOSEN = "KEY_ID_DOSEN";

    TextView tvGreeting;
    ProgressBar loading;
    RecyclerView rvAbsen;
    AbsenAdapter adapter;
    List<AbsenModel> listAbsen;

    String idDosen = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_user);

        tvGreeting = findViewById(R.id.tvGreetingUser);
        loading = findViewById(R.id.loadingUser);
        rvAbsen = findViewById(R.id.rv_user);

        loading.setVisibility(View.VISIBLE);

        if (getIntent().getStringExtra(KEY_NAME_DOSEN) != null) {
            String nameDosen = getIntent().getStringExtra(KEY_NAME_DOSEN);
            tvGreeting.setText("Selamat datang, " + nameDosen);
        } else {
            tvGreeting.setText("Selamat datang, " + new MyPreferences(this).getNameDosen());
        }

        listAbsen = new ArrayList<>();
        adapter = new AbsenAdapter(listAbsen);

        rvAbsen.setLayoutManager(new LinearLayoutManager(this));
        rvAbsen.setAdapter(adapter);

        if (getIntent().getStringExtra(KEY_ID_DOSEN) != null) {
            idDosen = getIntent().getStringExtra(KEY_ID_DOSEN);
            getAbsenFromServer(idDosen);
        } else {
            Toast.makeText(this, "Gagal mendapatkan absen", Toast.LENGTH_SHORT).show();
            loading.setVisibility(View.INVISIBLE);
        }
    }

    private void getAbsenFromServer(String id_dosen) {
        Call<List<AbsenModel>> call = ApiConfig.getApiService().getAbsen(id_dosen);
        call.enqueue(new Callback<List<AbsenModel>>() {
            @Override
            public void onResponse(Call<List<AbsenModel>> call, Response<List<AbsenModel>> response) {
                if (response.isSuccessful()) {
                    listAbsen = response.body();
                    adapter = new AbsenAdapter(listAbsen);
                    rvAbsen.setAdapter(adapter);
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
}
