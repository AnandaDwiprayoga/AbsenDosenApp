package org.z1god.absenguruprivate.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.z1god.absenguruprivate.Model.SiswaModel;
import org.z1god.absenguruprivate.R;

import java.util.List;

public class SiswaAdapterAdd extends RecyclerView.Adapter<SiswaAdapterAdd.MyViewHolder> {

    List<SiswaModel> listSiswa;
    private OnButtonAddClicked onButtonAddClicked;

    public SiswaAdapterAdd(List<SiswaModel> listSiswa,OnButtonAddClicked onButtonAddClicked) {
        this.listSiswa = listSiswa;
        this.onButtonAddClicked = onButtonAddClicked;
    }

    @NonNull
    @Override
    public SiswaAdapterAdd.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_siswa_add, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_siswa_add, parent, false);
        }
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SiswaAdapterAdd.MyViewHolder holder, int position) {
        if (position != 0 && listSiswa != null){
            SiswaModel siswa = listSiswa.get(position-1);

            holder.tvNim.setText(siswa.getNim());
            holder.tvName.setText(siswa.getNama());
            holder.tvAddress.setText(siswa.getAlamat());
            holder.tvGender.setText(siswa.getGender());
            holder.tvKelas.setText(siswa.getKelas());
            holder.tvTanggalLahir.setText(siswa.getTanggal_lahir());

            holder.btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int realPosition = holder.getAdapterPosition() - 1;
                    onButtonAddClicked.onClick(listSiswa.get(realPosition).getNim());
                }
            });
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
        private ImageButton btnAdd;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNim = itemView.findViewById(R.id.tv_nim);
            tvName = itemView.findViewById(R.id.tv_nama);
            tvAddress = itemView.findViewById(R.id.tv_alamat);
            tvGender = itemView.findViewById(R.id.tv_gender);
            tvKelas = itemView.findViewById(R.id.tv_kelas);
            tvTanggalLahir = itemView.findViewById(R.id.tv_tanggal_lahir);
            btnAdd = itemView.findViewById(R.id.add_siswa);
        }
    }

    public interface OnButtonAddClicked {
        void onClick(String id_siswa);
    }
}
