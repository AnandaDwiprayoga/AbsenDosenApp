package org.z1god.absenguruprivate.Model;

public class DosenModel {
    private String id_dosen, nama,alamat,gender,img,username,password;

    public DosenModel(String id_dosen, String nama, String alamat, String gender, String img, String username, String password) {
        this.id_dosen = id_dosen;
        this.nama = nama;
        this.alamat = alamat;
        this.gender = gender;
        this.img = img;
        this.username = username;
        this.password = password;
    }

    public String getId_dosen() {
        return id_dosen;
    }

    public void setId_dosen(String id_dosen) {
        this.id_dosen = id_dosen;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
