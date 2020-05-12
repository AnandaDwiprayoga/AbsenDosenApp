package org.z1god.absenguruprivate.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.z1god.absenguruprivate.Model.DosenModel;
import org.z1god.absenguruprivate.R;

import java.util.List;

public class DosenAdapter extends RecyclerView.Adapter<DosenAdapter.MyViewHolder> {

    private List<DosenModel> listDosen;
    private MyOnItemClickListener myOnItemClickListener;

    public DosenAdapter(List<DosenModel> listDosen,MyOnItemClickListener myOnItemClickListener) {
        this.listDosen = listDosen;
        this.myOnItemClickListener = myOnItemClickListener;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_guru, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_guru, parent, false);
        }
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //karna posisi 1 untuk header table
        if (position != 0 && listDosen != null) {
            //karena start posisi ditambah 1 maka harus dikurangi satu untuk mengambil
            DosenModel dsn = listDosen.get(position - 1);

            Picasso.get().load(dsn.getImg()).into(holder.ivDosen);
            holder.tvId.setText(dsn.getId_dosen());
            holder.tvNama.setText(dsn.getNama());
            holder.tvAlamat.setText(dsn.getAlamat());
            holder.tvGender.setText(dsn.getGender());
            holder.tvUsername.setText(dsn.getUsername());
            holder.tvPassword.setText(dsn.getPassword());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DosenModel dsn = listDosen.get(holder.getAdapterPosition() - 1);
                    myOnItemClickListener.onClick(dsn);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listDosen.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivDosen;
        TextView tvId, tvNama, tvAlamat, tvGender, tvUsername, tvPassword;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ivDosen = itemView.findViewById(R.id.iv_dosen);
            tvId = itemView.findViewById(R.id.tv_id);
            tvNama = itemView.findViewById(R.id.tv_nama);
            tvAlamat = itemView.findViewById(R.id.tv_alamat);
            tvGender = itemView.findViewById(R.id.tv_gender);
            tvUsername = itemView.findViewById(R.id.tv_username);
            tvPassword = itemView.findViewById(R.id.tv_password);
        }
    }

    public interface MyOnItemClickListener {
        void onClick(DosenModel dosen);
    }
}
