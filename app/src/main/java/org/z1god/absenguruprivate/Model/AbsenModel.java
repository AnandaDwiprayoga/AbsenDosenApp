package org.z1god.absenguruprivate.Model;

public class AbsenModel {
    private String tanggal, login, logout, id_dosen,id,latitude, longitude;

    public AbsenModel(String tanggal, String login, String logout, String id_dosen, String id, String latitude, String longitude) {
        this.tanggal = tanggal;
        this.login = login;
        this.logout = logout;
        this.id_dosen = id_dosen;
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getLogout() {
        return logout;
    }

    public void setLogout(String logout) {
        this.logout = logout;
    }

    public String getId_dosen() {
        return id_dosen;
    }

    public void setId_dosen(String id_dosen) {
        this.id_dosen = id_dosen;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
