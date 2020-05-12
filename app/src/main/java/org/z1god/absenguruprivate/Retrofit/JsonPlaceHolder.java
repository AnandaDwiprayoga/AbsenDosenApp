package org.z1god.absenguruprivate.Retrofit;

import org.z1god.absenguruprivate.Model.AbsenModel;
import org.z1god.absenguruprivate.Model.DosenModel;
import org.z1god.absenguruprivate.Model.MessageModel;
import org.z1god.absenguruprivate.Model.TestModel;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface JsonPlaceHolder {

    @GET("dosen/")
    Call<List<DosenModel>> getAllDosen();

    @GET("dosen/login/")
    Call<DosenModel> loginDosen(@Query("username") String username, @Query("password") String password);

    @POST("dosen/")
    Call<MessageModel> postDosen(@Body DosenModel dsn);

    @POST("absensi/")
    Call<Void> postAbsen(@Body AbsenModel absen);

    @GET("absensi/")
    Call<List<AbsenModel>> getAbsen(@Query("id_dosen") String idDosen);

    @PUT("absensi")
    Call<Void> updateAbsen(@Body TestModel absen);
}
