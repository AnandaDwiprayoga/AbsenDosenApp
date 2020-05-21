package org.z1god.absenguruprivate.Model;

public class AmbilSiswa {
    private String id_dosen, id_siswa;

    public AmbilSiswa(String id_dosen, String id_siswa) {
        this.id_dosen = id_dosen;
        this.id_siswa = id_siswa;
    }

    public String getId_dosen() {
        return id_dosen;
    }

    public String getId_siswa() {
        return id_siswa;
    }
}
