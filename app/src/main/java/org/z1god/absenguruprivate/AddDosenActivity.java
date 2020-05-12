package org.z1god.absenguruprivate;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import org.z1god.absenguruprivate.Model.DosenModel;
import org.z1god.absenguruprivate.Model.MessageModel;
import org.z1god.absenguruprivate.Retrofit.ApiConfig;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddDosenActivity extends AppCompatActivity {

    EditText etIdDosen, etNameDosen, etAddress, etUsername, etPassword, etPasswordAgain;
    Spinner gender;
    Button btnChooseImg, btnSubmit;
    ImageView img;
    ProgressBar loading;

    private static final int MY_CAMERA_PERMISSION_CODE = 102;
    private static final int RC_CAMERA = 101;
    public static final String KEY_MSG = "key_msg";

    FirebaseStorage storage;
    StorageReference storageReference;

    Bitmap imgBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dosen);

        initView();
        initSpinnerGender();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        imgBitmap = null;

        btnChooseImg.setOnClickListener(v -> {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
            } else {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, RC_CAMERA);
            }
        });

        btnSubmit.setOnClickListener(v -> {
            if (!TextUtils.equals(etPassword.getText().toString(), etPasswordAgain.getText().toString())) {
                Toast.makeText(this, "Ulangi Password yang sesuai", Toast.LENGTH_SHORT).show();
                return;
            }

            loading.setVisibility(View.VISIBLE);

            if (    imgBitmap != null ||
                    !TextUtils.isEmpty(etIdDosen.getText()) ||
                    !TextUtils.isEmpty(etNameDosen.getText()) ||
                    !TextUtils.isEmpty(etAddress.getText()) ||
                    !TextUtils.isEmpty(etUsername.getText()) ||
                    !TextUtils.isEmpty(etPassword.getText())){
                uploadAndGetUrl();
            }else{
                Toast.makeText(this, "Lengkapi inputan ", Toast.LENGTH_SHORT).show();
                loading.setVisibility(View.VISIBLE);
            }

        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) return;

        if (requestCode == RC_CAMERA) {
            if (data != null) {
                imgBitmap = (Bitmap) data.getExtras().get("data");

                img.setVisibility(View.VISIBLE);
                img.setImageBitmap(imgBitmap);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, RC_CAMERA);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void initView() {
        etIdDosen = findViewById(R.id.etIdDosen);
        etNameDosen = findViewById(R.id.etNameDosen);
        etAddress = findViewById(R.id.etAddressDosen);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etPasswordAgain = findViewById(R.id.etPasswordAgain);
        btnChooseImg = findViewById(R.id.btnChooseImg);
        btnSubmit = findViewById(R.id.btnSubmit);
        img = findViewById(R.id.imgPlacaHolderImg);
        gender = findViewById(R.id.spinnerGender);
        loading = findViewById(R.id.loadingAddDosen);
    }

    private void initSpinnerGender() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(adapter);
    }

    private byte[] convertBitmapToByte(Bitmap imgBitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    private void uploadAndGetUrl() {
        StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
        ref.putBytes(convertBitmapToByte(imgBitmap))
                .addOnSuccessListener(taskSnapshot ->
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                DosenModel inputDosen = new DosenModel(
                                        etIdDosen.getText().toString(),
                                        etNameDosen.getText().toString(),
                                        etAddress.getText().toString(),
                                        gender.getSelectedItem().toString(),
                                        uri.toString(),
                                        etUsername.getText().toString(),
                                        etPassword.getText().toString());


                                Call<MessageModel> call = ApiConfig.getApiService().postDosen(inputDosen);
                                call.enqueue(new Callback<MessageModel>() {
                                    @Override
                                    public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                                        if (response.isSuccessful()) {
                                            MessageModel msg = response.body();

                                            Intent intent = new Intent();
                                            intent.putExtra(KEY_MSG, msg.getMessage());
                                            setResult(RESULT_OK, intent);
                                            finish();
                                        } else {
                                            Gson gson = new Gson();
                                            try {
                                                MessageModel errorBody = gson.fromJson(response.errorBody().string(), MessageModel.class);
                                                Toast.makeText(AddDosenActivity.this, errorBody.getMessage(), Toast.LENGTH_SHORT).show();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        loading.setVisibility(View.INVISIBLE);
                                    }

                                    @Override
                                    public void onFailure(Call<MessageModel> call, Throwable t) {
                                        Toast.makeText(AddDosenActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                        loading.setVisibility(View.INVISIBLE);
                                    }
                                });
                            }
                        })
                );
    }

}
