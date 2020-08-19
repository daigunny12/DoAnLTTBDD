package doan.android.appnhac.Adapter;

import android.content.Context;
import android.content.Intent;
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

public class SearchBaiHatAdapter extends RecyclerView.Adapter<SearchBaiHatAdapter.ViewHolder> {
    Context context;
    ArrayList<BaiHat> mangbaihat;

    public SearchBaiHatAdapter(Context context, ArrayList<BaiHat> mangbaihat) {
        this.context = context;
        this.mangbaihat = mangbaihat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dong_search_bai_hat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BaiHat baiHat = mangbaihat.get(position);
        holder.txttenbaihat.setText(baiHat.getSongName());
        holder.txtcasi.setText(baiHat.getSongSinger());
        Picasso.with(context).load(baiHat.getSongImage()).into(holder.imgbaihat);
    }

    @Override
    public int getItemCount() {
        return mangbaihat.size();
    }

    public  class  ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgbaihat, imgluotthich;
        TextView txtcasi, txttenbaihat;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgbaihat = itemView.findViewById(R.id.imageviewsearchbaihat);
            imgluotthich = itemView.findViewById(R.id.imageviewsearchluotthich);
            txtcasi = itemView.findViewById(R.id.textviewsearchcasi);
            txttenbaihat = itemView.findViewById(R.id.textviewsearchtenbaihat);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PlayNhacActivity.class);
                    intent.putExtra("cakhuc", mangbaihat.get(getPosition()));
                    context.startActivity(intent);
                }
            });
            imgluotthich.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataService dataService = APIService.getService();
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("luotthich", 1);
                    jsonObject.addProperty("idbaihat", mangbaihat.get(getPosition()).getId());
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
        }
    }
}
