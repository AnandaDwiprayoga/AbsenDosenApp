package org.z1god.absenguruprivate;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import org.z1god.absenguruprivate.Model.MessageModel;
import org.z1god.absenguruprivate.Model.SiswaModel;
import org.z1god.absenguruprivate.Retrofit.ApiConfig;

import java.io.IOException;
import java.time.Year;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddSiswaActivity extends AppCompatActivity {
    private EditText etNim, etName, etAdress,etKelas;
    private Spinner gender;
    private Button btnChooseDate, btnSubmit;
    private ProgressBar loading;

    public static final String KEY_MESSAGE_ADD_SISWA = "KEY_MESSAGE_ADD_SISWA";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_siswa);

        etNim = findViewById(R.id.etNimSiswa);
        etName = findViewById(R.id.etNameSiswa);
        etAdress = findViewById(R.id.etAddressSiswa);
        gender =  findViewById(R.id.spinnerGender);
        etKelas = findViewById(R.id.etKelasSiswa);
        btnChooseDate = findViewById(R.id.btnChoseDateBirth);
        btnSubmit = findViewById(R.id.btnSubmit);
        loading = findViewById(R.id.loadingAddSiswa);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(adapter);

        btnChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddSiswaActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String datePicked = year + "-" + month + "-" + dayOfMonth;
                        btnChooseDate.setText(datePicked);
                    }
                }, year, month, day);

                datePickerDialog.show();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);

                String nim = etNim.getText().toString();
                String nama = etName.getText().toString();
                String address = etAdress.getText().toString();
                String kelas = etKelas.getText().toString();
                String jk = gender.getSelectedItem().toString();
                String tanggalLahir = null;

                if (!btnChooseDate.getText().toString().equalsIgnoreCase("Pilih Tanggal Lahir")){
                    tanggalLahir = btnChooseDate.getText().toString();
                }

                SiswaModel sw = new SiswaModel(nim,nama,address,jk,tanggalLahir,kelas);
                Call<MessageModel> call = ApiConfig.getApiService().postSiswa(sw);
                call.enqueue(new Callback<MessageModel>() {
                    @Override
                    public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                        if (response.isSuccessful()){
                            Intent intent = new Intent();
                            intent.putExtra(KEY_MESSAGE_ADD_SISWA, response.body().getMessage());
                            setResult(RESULT_OK,intent);
                            finish();
                        }else{
                            Gson gson = new Gson();
                            try {
                                MessageModel errorBody = gson.fromJson(response.errorBody().string(), MessageModel.class);
                                Toast.makeText(AddSiswaActivity.this, errorBody.getMessage(), Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        loading.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onFailure(Call<MessageModel> call, Throwable t) {
                        Toast.makeText(AddSiswaActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        loading.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });
    }
}
