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

import java.util.ArrayList;

import doan.android.appnhac.Activity.PlayNhacActivity;
import doan.android.appnhac.Model.BaiHat;
import doan.android.appnhac.Model.MessageLikes;
import doan.android.appnhac.R;
import doan.android.appnhac.Service.APIService;
import doan.android.appnhac.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaiHatHotAdapter extends RecyclerView.Adapter<BaiHatHotAdapter.ViewHoler>{
    Context context;
    ArrayList<BaiHat> arrayListbaihat;

    public BaiHatHotAdapter(Context context, ArrayList<BaiHat> arrayListbaihat) {
        this.context = context;
        this.arrayListbaihat = arrayListbaihat;
    }

    @NonNull
    @Override
    public ViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater  inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dong_bai_hat_hot, parent, false);
        return new ViewHoler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoler holder, int position) {
        BaiHat baiHat = arrayListbaihat.get(position);
        holder.txtten.setText(baiHat.getSongName());
        holder.txtcasi.setText(baiHat.getSongSinger());
        Picasso.with(context).load(baiHat.getSongImage()).into(holder.imghing);

    }

    @Override
    public int getItemCount() {
        return arrayListbaihat.size();
    }

    public class ViewHoler extends RecyclerView.ViewHolder{
        TextView txtten, txtcasi;
        ImageView imghing, imgluotthich;

        public ViewHoler(@NonNull View itemView) {
            super(itemView);
            txtten = itemView.findViewById(R.id.textviewTenBaiHatHot);
            txtcasi = itemView.findViewById(R.id.textviewcasibaihathot);
            imghing = itemView.findViewById(R.id.imgeviewBaiHatHot);
            imgluotthich = itemView.findViewById(R.id.imgeviewLuotthich);
            imgluotthich.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataService dataService = APIService.getService();
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("luotthich", 1);
                    jsonObject.addProperty("idbaihat", arrayListbaihat.get(getPosition()).getId());
                    Call<MessageLikes> callback = dataService.UpdateLuotThich(jsonObject);
                    callback.enqueue(new Callback<MessageLikes>() {
                        @Override
                        public void onResponse(Call<MessageLikes> call, Response<MessageLikes> response) {
                            MessageLikes messageLikes = new MessageLikes();
                            messageLikes = response.body();
                            if(messageLikes.getMessage().equals("Like success!")){
                                imgluotthich.setImageResource(R.drawable.iconloved);
                                Toast.makeText(context, "Đã thích", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(context, "Lỗi!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<MessageLikes> call, Throwable t) {

                        }
                    });
                    imgluotthich.setEnabled(false);
                }
            });
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlayNhacActivity.class);
                intent.putExtra("cakhuc", arrayListbaihat.get(getPosition()));
                context.startActivity(intent);
            }
        });
        }
    }
}
