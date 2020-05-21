package org.z1god.absenguruprivate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.api.Api;
import com.google.gson.Gson;

import org.z1god.absenguruprivate.Adapter.SiswaAdapterAdd;
import org.z1god.absenguruprivate.Model.AmbilSiswa;
import org.z1god.absenguruprivate.Model.MessageModel;
import org.z1god.absenguruprivate.Model.SiswaModel;
import org.z1god.absenguruprivate.Retrofit.ApiConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DosenAddSiswaActivity extends AppCompatActivity implements SiswaAdapterAdd.OnButtonAddClicked{

    RecyclerView rvSiswa;
    SiswaAdapterAdd adapter;
    ProgressBar loading;
    List<SiswaModel> listSiswa;
    String id_dosen = null;

    public static final String KEY_ID_DOSEN_DASA = "KEY_ID_DOSEN_DASA";
    public static final String KEY_ID_SISWA_ADDED_SUCCESS = "KEY_ID_SISWA_ADDED_SUCCESS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dosen_add_siswa);

        rvSiswa = findViewById(R.id.rv_siswa);
        loading = findViewById(R.id.loadingAddSiswa);

        loading.setVisibility(View.VISIBLE);

        listSiswa = new ArrayList<>();
        adapter = new SiswaAdapterAdd(listSiswa, this);

        rvSiswa.setLayoutManager(new LinearLayoutManager(this));
        rvSiswa.setAdapter(adapter);

        id_dosen = getIntent().getStringExtra(KEY_ID_DOSEN_DASA);
        getAvailableSiswaFromServer(id_dosen);
    }

    private void getAvailableSiswaFromServer(String id_dosen) {
        Call<List<SiswaModel>> call = ApiConfig.getApiService().getSiswaAvailable(id_dosen);
        call.enqueue(new Callback<List<SiswaModel>>() {
            @Override
            public void onResponse(Call<List<SiswaModel>> call, Response<List<SiswaModel>> response) {
                if (response.isSuccessful()){
                    listSiswa = response.body();
                    adapter = new SiswaAdapterAdd(listSiswa, DosenAddSiswaActivity.this);
                    rvSiswa.setAdapter(adapter);
                }else{
                    Gson gson = new Gson();
                    try {
                        MessageModel errorBody = gson.fromJson(response.errorBody().string(), MessageModel.class);
                        Toast.makeText(DosenAddSiswaActivity.this, errorBody.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                loading.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<List<SiswaModel>> call, Throwable t) {
                Toast.makeText(DosenAddSiswaActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                loading.setVisibility(View.INVISIBLE);
            }
        });
    }


    public void finish(View view) {
        Intent intent = new Intent();
        intent.putExtra(KEY_ID_SISWA_ADDED_SUCCESS, id_dosen);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onClick(String id_siswa) {
        if (id_dosen != null){
            loading.setVisibility(View.VISIBLE);

            AmbilSiswa siswa = new AmbilSiswa(id_dosen,id_siswa);
            Call<MessageModel> call = ApiConfig.getApiService().postSiswaToDosen(siswa);
            call.enqueue(new Callback<MessageModel>() {
                @Override
                public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                    if (response.isSuccessful()){
                        Toast.makeText(DosenAddSiswaActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        getAvailableSiswaFromServer(id_dosen);
                    }else{
                        Gson gson = new Gson();
                        try {
                            MessageModel errorBody = gson.fromJson(response.errorBody().string(), MessageModel.class);
                            Toast.makeText(DosenAddSiswaActivity.this, errorBody.getMessage(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    loading.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onFailure(Call<MessageModel> call, Throwable t) {
                    Toast.makeText(DosenAddSiswaActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

                    loading.setVisibility(View.INVISIBLE);
                }
            });
        }
    }
}
