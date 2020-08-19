package doan.android.appnhac.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import doan.android.appnhac.Adapter.DanhSachCacPlaylistAdapter;
import doan.android.appnhac.Model.Playlist;
import doan.android.appnhac.R;
import doan.android.appnhac.Service.APIService;
import doan.android.appnhac.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DanhSachCacPlaylistActivity extends AppCompatActivity {

    Toolbar toolbar ;
    RecyclerView recyclerView;
    ArrayList<Playlist> mangPlaylist;
    DanhSachCacPlaylistAdapter danhSachCacPlaylistAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_cac_playlist);
        anhxa();
        init();
        getData();
    }

    private void getData() {
        DataService dataService = APIService.getService();
        Call<List<Playlist>> callback = dataService.GetDanhSachPlaylist();
        callback.enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                mangPlaylist = (ArrayList<Playlist>) response.body();
                danhSachCacPlaylistAdapter = new DanhSachCacPlaylistAdapter(DanhSachCacPlaylistActivity.this,mangPlaylist);
                recyclerView.setLayoutManager(new GridLayoutManager(DanhSachCacPlaylistActivity.this, 2));
                recyclerView.setAdapter(danhSachCacPlaylistAdapter);
            }

            @Override
            public void onFailure(Call<List<Playlist>> call, Throwable t) {

            }
        });
    }

    private void init() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Play Lists");
        toolbar.setTitleTextColor(getResources().getColor(R.color.mautim));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void anhxa() {
        toolbar = findViewById(R.id.toolbardanhsachcacplaylist);
        recyclerView = findViewById(R.id.recyclerviewdanhsachcacplaylist);

    }
}
