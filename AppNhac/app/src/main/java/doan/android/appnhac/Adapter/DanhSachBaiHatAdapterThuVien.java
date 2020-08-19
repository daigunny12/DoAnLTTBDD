package doan.android.appnhac.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import doan.android.appnhac.Activity.PlayNhacActivity;
import doan.android.appnhac.Activity.PlayNhacActivityThuVien;
import doan.android.appnhac.Model.BaiHat;
import doan.android.appnhac.Model.MessageLikes;
import doan.android.appnhac.R;
import doan.android.appnhac.Service.APIService;
import doan.android.appnhac.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DanhSachBaiHatAdapterThuVien extends  RecyclerView.Adapter<DanhSachBaiHatAdapterThuVien.ViewHolder>{
    Context context;
    ArrayList<File> mangbaihat;

    public DanhSachBaiHatAdapterThuVien(Context context, ArrayList<File> mangbaihat) {
        this.context = context;
        this.mangbaihat = mangbaihat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dong_danh_sach_bai_hat_thu_vien, parent , false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        File baiHat = mangbaihat.get(position);
        holder.txttenbaihat.setText(baiHat.getName());
        String str = baiHat.getName();
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
            holder.txttenCasi.setText(str.substring(b+1, c));
        }
    }

    @Override
    public int getItemCount() {
        return mangbaihat.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txttenbaihat;
        TextView txttenCasi;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txttenbaihat = itemView.findViewById(R.id.textviewtenbaihat);
            txttenCasi = itemView.findViewById(R.id.textviewtencasi);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PlayNhacActivityThuVien.class)
                        .putExtra("Song", mangbaihat)
                        .putExtra("pos", getPosition());
                     context.startActivity(intent);
                }
            });
        }
    }
}
