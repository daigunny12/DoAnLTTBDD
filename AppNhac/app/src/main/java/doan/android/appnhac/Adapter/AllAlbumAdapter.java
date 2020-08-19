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

public class AllAlbumAdapter extends RecyclerView.Adapter<AllAlbumAdapter.ViewHolder> {
    Context context;
    ArrayList<Album> allAlbumarraylist;

    public AllAlbumAdapter(Context context, ArrayList<Album> allAlbumarraylist) {
        this.context = context;
        this.allAlbumarraylist = allAlbumarraylist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dong_all_album, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Album album = allAlbumarraylist.get(position);
        Picasso.with(context).load(album.getAlbumImage()).into(holder.imageView);
        holder.textView.setText(album.getAlbumName());
    }

    @Override
    public int getItemCount() {
        return allAlbumarraylist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgeviewAllAlbum);
            textView = itemView.findViewById(R.id.textviewTenAllAlbum);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DanhSachBaiHatActivity.class);
                    intent.putExtra("album", allAlbumarraylist.get(getPosition()));
                    context.startActivity(intent);
                }
            });
        }
    }
}
