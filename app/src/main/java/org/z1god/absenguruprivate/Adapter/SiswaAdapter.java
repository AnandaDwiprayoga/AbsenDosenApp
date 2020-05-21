package org.z1god.absenguruprivate.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.z1god.absenguruprivate.Model.SiswaModel;
import org.z1god.absenguruprivate.R;

import java.util.List;

public class SiswaAdapter extends RecyclerView.Adapter<SiswaAdapter.MyViewHolder> {

    List<SiswaModel> listSiswa;

    public SiswaAdapter(List<SiswaModel> listSiswa) {
        this.listSiswa = listSiswa;
    }

    @NonNull
    @Override
    public SiswaAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_siswa_admin, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_siswa_admin, parent, false);
        }
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SiswaAdapter.MyViewHolder holder, int position) {
        if (position != 0 && listSiswa != null){
            SiswaModel siswa = listSiswa.get(position-1);

            holder.tvNim.setText(siswa.getNim());
            holder.tvName.setText(siswa.getNama());
            holder.tvAddress.setText(siswa.getAlamat());
            holder.tvGender.setText(siswa.getGender());
            holder.tvKelas.setText(siswa.getKelas());
            holder.tvTanggalLahir.setText(siswa.getTanggal_lahir());
        }
    }

    @Override
    public int getItemCount() {
        return listSiswa.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNim,tvName,tvAddress,tvGender,tvKelas,tvTanggalLahir;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNim = itemView.findViewById(R.id.tv_nim);
            tvName = itemView.findViewById(R.id.tv_nama);
            tvAddress = itemView.findViewById(R.id.tv_alamat);
            tvGender = itemView.findViewById(R.id.tv_gender);
            tvKelas = itemView.findViewById(R.id.tv_kelas);
            tvTanggalLahir = itemView.findViewById(R.id.tv_tanggal_lahir);
        }
    }
}
