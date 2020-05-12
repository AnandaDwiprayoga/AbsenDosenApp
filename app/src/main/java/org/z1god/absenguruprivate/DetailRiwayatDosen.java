package org.z1god.absenguruprivate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.z1god.absenguruprivate.Adapter.AbsenAdapter;
import org.z1god.absenguruprivate.Model.AbsenModel;
import org.z1god.absenguruprivate.Model.MessageModel;
import org.z1god.absenguruprivate.Retrofit.ApiConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailRiwayatDosen extends AppCompatActivity {

    public static final String KEY_RIWAYAT_NAME_DOSEN = "KEY_RIWAYAT_NAME_DOSEN";
    public static final String KEY_RIWAYAT_ID_DOSEN = "KEY_RIWAYAT_ID_DOSEN";

    ProgressBar loading;
    RecyclerView recyclerView;
    AbsenAdapter absenAdapter;
    List<AbsenModel> listAbsen;
    TextView tvName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_riwayat_dosen);

        loading = findViewById(R.id.loadingRiwayat);
        recyclerView = findViewById(R.id.rv_riwayat);

        loading.setVisibility(View.VISIBLE);

        listAbsen = new ArrayList<>();
        absenAdapter = new AbsenAdapter(listAbsen);
        tvName = findViewById(R.id.tv_name_dosen);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(absenAdapter);

        String name = getIntent().getStringExtra(KEY_RIWAYAT_NAME_DOSEN);
        tvName.setText("Riwayat Dosen " + name );

        getAbsenFromServer(getIntent().getStringExtra(KEY_RIWAYAT_ID_DOSEN));
    }

    private void getAbsenFromServer(String id) {
        Call<List<AbsenModel>> call = ApiConfig.getApiService().getAbsen(id);
        call.enqueue(new Callback<List<AbsenModel>>() {
            @Override
            public void onResponse(Call<List<AbsenModel>> call, Response<List<AbsenModel>> response) {
                if (response.isSuccessful()){
                    listAbsen = response.body();
                    absenAdapter = new AbsenAdapter(listAbsen);
                    recyclerView.setAdapter(absenAdapter);
                }else{
                    Gson gson = new Gson();
                    try {
                        MessageModel errorBody = gson.fromJson(response.errorBody().string(), MessageModel.class);
                        Toast.makeText(DetailRiwayatDosen.this, errorBody.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                loading.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<List<AbsenModel>> call, Throwable t) {
                Toast.makeText(DetailRiwayatDosen.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                loading.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void finish(View view) {
        finish();
    }
}
