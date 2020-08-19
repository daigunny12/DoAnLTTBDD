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

import doan.android.appnhac.Activity.DanhSachBaiHatActivity;
import doan.android.appnhac.Model.Album;
import doan.android.appnhac.R;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder>{

    Context context;
    ArrayList<Album> mangAlbum;

    public AlbumAdapter(Context context, ArrayList<Album> mangAlbum) {
        this.context = context;
        this.mangAlbum = mangAlbum;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dong_album, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Album album = mangAlbum.get(position);
        holder.txtCaSiAlbum.setText(album.getAlbumSingerName());
        holder.txtTenAlbum.setText(album.getAlbumName());
        Picasso.with(context).load(album.getAlbumImage()).into(holder.imgAlbum);
    }

    @Override
    public int getItemCount() {
        return mangAlbum.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imgAlbum;
        TextView txtTenAlbum, txtCaSiAlbum;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAlbum = itemView.findViewById(R.id.imgeviewAlbum);
            txtTenAlbum = itemView.findViewById(R.id.textviewtenAlbum);
            txtCaSiAlbum = itemView.findViewById(R.id.textviewtencasiAlbum);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DanhSachBaiHatActivity.class);
                    intent.putExtra("album", mangAlbum.get(getPosition()));
                    context.startActivity(intent);
                }
            });
        }
    }
}
