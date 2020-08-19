package doan.android.appnhac.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import doan.android.appnhac.Activity.DanhSachTheLoaiTheoChuDeActivity;
import doan.android.appnhac.Model.Topic;
import doan.android.appnhac.R;

public class DanhSachTatCaChuDeAdapter extends  RecyclerView.Adapter<DanhSachTatCaChuDeAdapter.ViewHolder>{
    Context context;
    ArrayList<Topic> mangChuDe;

    public DanhSachTatCaChuDeAdapter(Context context, ArrayList<Topic> mangChuDe) {
        this.context = context;
        this.mangChuDe = mangChuDe;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dong_cac_chu_de,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Topic topic = mangChuDe.get(position);
        Picasso.with(context).load(topic.getTopicImage()).into(holder.imageViewdongcacchude);
        holder.txtTenChuDe.setText(topic.getTopicName());
    }

    @Override
    public int getItemCount() {
        return mangChuDe.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageViewdongcacchude;
        TextView txtTenChuDe;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewdongcacchude = itemView.findViewById(R.id.imgeviewdongcacchude);
            txtTenChuDe = itemView.findViewById(R.id.textviewTenCacChuDe);
            imageViewdongcacchude.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DanhSachTheLoaiTheoChuDeActivity.class);
                    intent.putExtra("chude", mangChuDe.get(getPosition()));
                    context.startActivity(intent);
                }
            });
        }
    }
}
