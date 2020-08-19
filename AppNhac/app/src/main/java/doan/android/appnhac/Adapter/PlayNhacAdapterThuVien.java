package doan.android.appnhac.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;

import doan.android.appnhac.Model.ChayNhac;
import doan.android.appnhac.R;

public class PlayNhacAdapterThuVien extends RecyclerView.Adapter<PlayNhacAdapterThuVien.ViewHolder> {

    Context context;
    ArrayList<File> mySong;
    ChayNhac chayNhac;
    int position;

    public PlayNhacAdapterThuVien(Context context, ArrayList<File> mySong) {
        this.context = context;
        this.mySong = mySong;
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
        File file = mySong.get(position);
        holder.txttenbaihat.setText(file.getName());
        holder.txtindex.setText(position +1 + "");
        String str = file.getName();
        int a = 0,b = 0,c = 0;
        for (int i = 0 ; i< str.length();i++){
            if(str.substring(i,i+1).equals("_")){
                if(a == 0 ){
                    b = i;
                }
                if(a == 1){
                    c = i;
                }
                a += 1;
            }
        }
        if(str.substring(b, c).length()>0){
            holder.txtcasi.setText(str.substring(b+1, c));
        }else{
            holder.txtcasi.setText("Nghệ sĩ không xác định");
        }
    }

    @Override
    public int getItemCount() {
        return mySong.size();
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
