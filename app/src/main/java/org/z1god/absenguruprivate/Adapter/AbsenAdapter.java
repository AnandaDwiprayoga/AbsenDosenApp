package org.z1god.absenguruprivate.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.z1god.absenguruprivate.Model.AbsenModel;
import org.z1god.absenguruprivate.R;
import org.z1god.absenguruprivate.Utilitis.MyDate;

import java.util.List;

public class AbsenAdapter extends RecyclerView.Adapter<AbsenAdapter.MyViewHolder> {

    private List<AbsenModel> listAbsen;
    private OnItemClickListener onItemClickListener;

    public AbsenAdapter(List<AbsenModel> listAbsen, OnItemClickListener mOnItemClick) {
        this.listAbsen = listAbsen;
        onItemClickListener = mOnItemClick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_user_guru, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_user, parent, false);
        }
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //karna posisi 1 untuk header table
        if (position != 0 && listAbsen != null) {
            //karena start posisi ditambah 1 maka harus dikurangi satu untuk mengambil
            AbsenModel dsn = listAbsen.get(position - 1);

            holder.tvTanggal.setText(MyDate.convertDateToName(dsn.getTanggal()));
            holder.tvLogin.setText(dsn.getLogin());
            holder.tvLogout.setText(MyDate.convertTime(dsn.getLogout()));

            holder.actionMaps.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int realPosition = holder.getAdapterPosition() - 1;
                    onItemClickListener.onClick(listAbsen.get(realPosition));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listAbsen.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTanggal, tvLogin,tvLogout;
        ImageButton actionMaps;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTanggal = itemView.findViewById(R.id.tv_tanggal);
            tvLogin = itemView.findViewById(R.id.tv_waktuLogin);
            tvLogout = itemView.findViewById(R.id.tv_waktuLogout);
            actionMaps = itemView.findViewById(R.id.tv_listAction);
        }
    }

    public interface OnItemClickListener{
        void onClick(AbsenModel absen);
    }
}
