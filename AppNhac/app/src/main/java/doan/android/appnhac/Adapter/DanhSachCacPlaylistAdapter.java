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
import doan.android.appnhac.Model.Playlist;
import doan.android.appnhac.R;

public class DanhSachCacPlaylistAdapter extends  RecyclerView.Adapter<DanhSachCacPlaylistAdapter.ViewHolder>{
    Context context;
    ArrayList<Playlist> mangPlaylist;

    public DanhSachCacPlaylistAdapter(Context context, ArrayList<Playlist> mangPlaylist) {
        this.context = context;
        this.mangPlaylist = mangPlaylist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dong_danh_sach_cac_playlist, parent, false);
        return new  ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Playlist playlist = mangPlaylist.get(position);
        Picasso.with(context).load(playlist.getPlaylistImage()).into(holder.imgPlaylis);
        holder.txtTenDanhSachPlaylist.setText(playlist.getPlaylistName());

    }

    @Override
    public int getItemCount() {
        return mangPlaylist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgPlaylis;
        TextView txtTenDanhSachPlaylist;
        TextView txtTenCaSiPlaylist;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPlaylis = itemView.findViewById(R.id.imgeviewdanhsachcacplaylist);
            txtTenDanhSachPlaylist = itemView.findViewById(R.id.textviewtendanhsachcacplaylist);
            //txtTenCaSiPlaylist = itemView.findViewById(R.id.textviewtencasiplaylist);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DanhSachBaiHatActivity.class);
                    intent.putExtra("itemplaylist", mangPlaylist.get(getPosition()));
                    context.startActivity(intent);
                }
            });
        }
    }
}
