package org.z1god.absenguruprivate.Utilitis;

import android.content.Context;
import android.content.SharedPreferences;

public class MyPreferences {
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "yoga_pref";
    private static final String DOSEN_IS_LOGIN = "USER_IS_LOGIN";
    private static final String ADMIN_IS_LOGIN  = "ADMIN_IS_LOGIN";
    private static final String NAME_DOSEN  = "NAME_ADMIN";
    private static final String ID_DOSEN  = "ID_DOSEN";

    public MyPreferences(Context context){
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void adminLogin(boolean state){
        sharedPreferences.edit().putBoolean(ADMIN_IS_LOGIN, state).apply();
    }

    public boolean isAdminLogin(){
        return sharedPreferences.getBoolean(ADMIN_IS_LOGIN, false);
    }

    public void dosenLogin(boolean state){
        sharedPreferences.edit().putBoolean(DOSEN_IS_LOGIN, state).apply();
    }

    public boolean isDosenLogin(){
        return sharedPreferences.getBoolean(DOSEN_IS_LOGIN, false);
    }

    public void setNameDosen(String name){
        sharedPreferences.edit().putString(NAME_DOSEN, name).apply();
    }

    public String getNameDosen(){
        return sharedPreferences.getString(NAME_DOSEN,"User");
    }

    public void setIdDosen(String id){
        sharedPreferences.edit().putString(ID_DOSEN, id).apply();
    }

    public String getIdDosen(){
        return sharedPreferences.getString(ID_DOSEN,null);
    }
}
