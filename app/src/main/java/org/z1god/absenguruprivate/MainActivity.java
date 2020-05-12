package org.z1god.absenguruprivate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

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

    EditText username, password;
    ProgressBar loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyPreferences pref = new MyPreferences(this);
        if (pref.isAdminLogin()){
            startActivity(new Intent(MainActivity.this, DashbordAdmin.class));
            finish();
        }else if (pref.isDosenLogin()){
            Intent intent = new Intent(MainActivity.this, DashboardUser.class);
            intent.putExtra(KEY_NAME_DOSEN, pref.getNameDosen());
            intent.putExtra(KEY_ID_DOSEN, pref.getIdDosen() );
            startActivity(intent);
            finish();
        }

        Button btnLogin = findViewById(R.id.btn_login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loading = findViewById(R.id.loading_login);

        btnLogin.setOnClickListener(v -> {
            String usr = username.getText().toString();
            String pswd = password.getText().toString();

            if (usr.equalsIgnoreCase("admin") && pswd.equalsIgnoreCase("admin")){
                pref.adminLogin(true);
                startActivity(new Intent(MainActivity.this, DashbordAdmin.class));
                finish();
            }else{
                loading.setVisibility(View.VISIBLE);

                Call<DosenModel> loginDosen = ApiConfig.getApiService().loginDosen(usr,pswd);
                loginDosen.enqueue(new Callback<DosenModel>() {
                    @Override
                    public void onResponse(Call<DosenModel> call, Response<DosenModel> response) {
                        if (response.isSuccessful()){
                            String idDosen = response.body().getId_dosen();
                            String nameDosen = response.body().getNama();
                            String date = MyDate.getCurrentDateAndTime()[0];
                            String timeLogin = MyDate.getCurrentDateAndTime()[1];

                            AbsenModel absen = new AbsenModel(date,timeLogin,"00:00:00",idDosen,null);

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
        });
    }


}

