package org.z1god.absenguruprivate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.z1god.absenguruprivate.Adapter.DosenAdapter;
import org.z1god.absenguruprivate.Adapter.SiswaAdapter;
import org.z1god.absenguruprivate.Model.DosenModel;
import org.z1god.absenguruprivate.Model.MessageModel;
import org.z1god.absenguruprivate.Model.SiswaModel;
import org.z1god.absenguruprivate.Retrofit.ApiConfig;
import org.z1god.absenguruprivate.Utilitis.MyPreferences;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.z1god.absenguruprivate.AddDosenActivity.KEY_MSG;
import static org.z1god.absenguruprivate.AddSiswaActivity.KEY_MESSAGE_ADD_SISWA;
import static org.z1god.absenguruprivate.DetailRiwayatDosen.KEY_RIWAYAT_ID_DOSEN;
import static org.z1god.absenguruprivate.DetailRiwayatDosen.KEY_RIWAYAT_NAME_DOSEN;

public class DashbordAdmin extends AppCompatActivity implements DosenAdapter.MyOnItemClickListener {

    ProgressBar loading;
    RecyclerView recyclerViewDosen, recyclerViewSiswa;
    List<DosenModel> listDosen;
    List<SiswaModel> listSiswa;
    DosenAdapter adapterDosen;
    SiswaAdapter adapterSiswa;
    Button buttonAddGuru, buttonAddSiswa;

    public static final int RC_ADD_DOSEN = 101;
    public static final int RC_ADD_SISWA = 121;
    DosenAdapter.MyOnItemClickListener onItemClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashbord_admin);

        recyclerViewDosen = findViewById(R.id.rv_guru);
        recyclerViewSiswa = findViewById(R.id.rv_siswa);
        loading = findViewById(R.id.loading_dashbord_dosen);
        buttonAddGuru = findViewById(R.id.btn_add_teacher);
        buttonAddSiswa = findViewById(R.id.btn_add_siswa);

        onItemClickListener = this;

        listDosen = new ArrayList<>();
        listSiswa = new ArrayList<>();
        adapterDosen = new DosenAdapter(listDosen, onItemClickListener);
        adapterSiswa = new SiswaAdapter(listSiswa);

        recyclerViewDosen.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSiswa.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewDosen.setAdapter(adapterDosen);
        recyclerViewSiswa.setAdapter(adapterSiswa);

        getDosenFromServer();
        getSiswaFromServer();

        buttonAddGuru.setOnClickListener(v -> {
            Intent addDosen = new Intent(this, AddDosenActivity.class);
            startActivityForResult(addDosen, RC_ADD_DOSEN);
        });

        buttonAddSiswa.setOnClickListener(v -> {
            Intent addSiswa = new Intent(this, AddSiswaActivity.class);
            startActivityForResult(addSiswa, RC_ADD_SISWA);
        });
    }

    private void getSiswaFromServer() {
        loading.setVisibility(View.VISIBLE);

        Call<List<SiswaModel>> call = ApiConfig.getApiService().getAllSiswa();
        call.enqueue(new Callback<List<SiswaModel>>() {
            @Override
            public void onResponse(Call<List<SiswaModel>> call, Response<List<SiswaModel>> response) {
                if (response.isSuccessful()) {
                    listSiswa = response.body();
                    adapterSiswa = new SiswaAdapter(listSiswa);
                    recyclerViewSiswa.setAdapter(adapterSiswa);
                } else {
                    Gson gson = new Gson();
                    try {
                        MessageModel errorBody = gson.fromJson(response.errorBody().string(), MessageModel.class);
                        Toast.makeText(DashbordAdmin.this, errorBody.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                loading.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<List<SiswaModel>> call, Throwable t) {
                Toast.makeText(DashbordAdmin.this, t.getMessage(), Toast.LENGTH_SHORT).show();

                loading.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void getDosenFromServer() {
        loading.setVisibility(View.VISIBLE);

        Call<List<DosenModel>> call = ApiConfig.getApiService().getAllDosen();
        call.enqueue(new Callback<List<DosenModel>>() {
            @Override
            public void onResponse(Call<List<DosenModel>> call, Response<List<DosenModel>> response) {
                if (response.isSuccessful()) {
                    listDosen = response.body();
                    adapterDosen = new DosenAdapter(listDosen, onItemClickListener);
                    recyclerViewDosen.setAdapter(adapterDosen);
                } else {
                    Gson gson = new Gson();
                    try {
                        MessageModel errorBody = gson.fromJson(response.errorBody().string(), MessageModel.class);
                        Toast.makeText(DashbordAdmin.this, errorBody.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                loading.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<List<DosenModel>> call, Throwable t) {
                Toast.makeText(DashbordAdmin.this, t.getMessage(), Toast.LENGTH_SHORT).show();

                loading.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (data != null) {
                if (requestCode == RC_ADD_DOSEN) {
                    String msg = data.getStringExtra(KEY_MSG);
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

                    getDosenFromServer();
                }else if(requestCode == RC_ADD_SISWA){
                    String msg = data.getStringExtra(KEY_MESSAGE_ADD_SISWA);
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

                    getSiswaFromServer();
                }
            }
        }
    }

    public void logout(View view) {
        new MyPreferences(this).adminLogin(false);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void onClick(DosenModel dosen) {
        Intent intent = new Intent(this, DetailRiwayatDosen.class);
        intent.putExtra(KEY_RIWAYAT_NAME_DOSEN, dosen.getNama());
        intent.putExtra(KEY_RIWAYAT_ID_DOSEN, dosen.getId_dosen());
        startActivity(intent);
    }
}

