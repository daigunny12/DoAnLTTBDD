package doan.android.appnhac.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import doan.android.appnhac.Model.BaiHat;
import doan.android.appnhac.Model.ChayNhac;
import doan.android.appnhac.R;

public class PlayNhacAdapter extends RecyclerView.Adapter<PlayNhacAdapter.ViewHolder> {
    Context context;
    ArrayList<BaiHat> mangbaigat;
    private ChayNhac chayNhac;
    int position;

    public PlayNhacAdapter(Context context, ArrayList<BaiHat> mangbaigat) {
        this.context = context;
        this.mangbaigat = mangbaigat;
        try {
            chayNhac = ((ChayNhac) context);
        } catch (ClassCastException e) {
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dong_play_bai_hat, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BaiHat baiHat = mangbaigat.get(position);
        holder.txtcasi.setText(baiHat.getSongSinger());
        holder.txttenbaihat.setText(baiHat.getSongName());
        holder.txtindex.setText(position +1 + "");
    }

    @Override
    public int getItemCount() {
        return mangbaigat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtindex, txttenbaihat, txtcasi;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtindex = itemView.findViewById(R.id.textviewplayindex);
            txtcasi = itemView.findViewById(R.id.textviewplaynhactencasi);
            txttenbaihat = itemView.findViewById(R.id.textviewplaynhactenbaihat);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int a = Integer.parseInt(txtindex.getText().toString());
                    chayNhac.onChayNhac(a-1);
            }
            });
        }
    }
}
