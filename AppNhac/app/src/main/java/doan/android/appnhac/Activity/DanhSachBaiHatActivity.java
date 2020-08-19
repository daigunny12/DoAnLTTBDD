package doan.android.appnhac.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import com.google.gson.JsonObject;
import android.widget.Toast;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import doan.android.appnhac.Adapter.DanhSachBaiHatAdapter;
import doan.android.appnhac.Model.Album;
import doan.android.appnhac.Model.BaiHat;
import doan.android.appnhac.Model.Genre;
import doan.android.appnhac.Model.Playlist;
import doan.android.appnhac.Model.QuangCao;
import doan.android.appnhac.R;
import doan.android.appnhac.Service.APIService;
import doan.android.appnhac.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DanhSachBaiHatActivity extends AppCompatActivity {

    ArrayList<BaiHat> mangbaihat;
    CoordinatorLayout coordinatorLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar toolbar;
    RecyclerView recyclerViewdanhsachbaihat;
    FloatingActionButton floatingActionButton;
    QuangCao quangCao;
    ImageView imgdanhsachcakhuc;
    DanhSachBaiHatAdapter danhSachBaiHatAdapter ;
    Playlist playlist;
    Genre genre;
    Album album;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_bai_hat);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        DataIntent();

        anhxa();
        init();
        if(quangCao != null && !quangCao.getAdsTitle().equals("")){
            setValueInView(quangCao.getAdsTitle(),quangCao.getAdsImage());
            getDataQuangCao(quangCao.getId());
        }
        if(playlist != null && !playlist.getPlaylistName().equals("")){
            setValueInView(playlist.getPlaylistName(),playlist.getPlaylistImage());
            getDataPlaylist(playlist.getId());
        }
        if(genre != null && !genre.getGenreImage().equals("")){
            setValueInView(genre.getGenreName(),genre.getGenreImage());
            getDataTheLoai(genre.getId());
        }
        if(album != null && !album.getAlbumImage().equals("")){
            setValueInView(album.getAlbumName(),album.getAlbumImage());
            getDataAlbum(album.getId());
        }
    }

    private void getDataAlbum(String id) {
        DataService dataService = APIService.getService();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("idalbum", id);
        Call<List<BaiHat>> callback = dataService.GetDanhSachBaiHatTheoAlbum(jsonObject);
        callback.enqueue(new Callback<List<BaiHat>>() {
            @Override
            public void onResponse(Call<List<BaiHat>> call, Response<List<BaiHat>> response) {
                if(response.body() != null) {
                    mangbaihat = (ArrayList<BaiHat>) response.body();
                    danhSachBaiHatAdapter = new DanhSachBaiHatAdapter(DanhSachBaiHatActivity.this, mangbaihat);
                    recyclerViewdanhsachbaihat.setLayoutManager(new LinearLayoutManager(DanhSachBaiHatActivity.this));
                    recyclerViewdanhsachbaihat.setAdapter(danhSachBaiHatAdapter);
                    eventClick();
                }
            }

            @Override
            public void onFailure(Call<List<BaiHat>> call, Throwable t) {

            }
        });
    }

    private void getDataTheLoai(String id) {
        DataService dataService = APIService.getService();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("idtheloai", id);
        Call<List<BaiHat>> callback = dataService.GetDanhSachTheoTheLoai(jsonObject);
        callback.enqueue(new Callback<List<BaiHat>>() {
            @Override
            public void onResponse(Call<List<BaiHat>> call, Response<List<BaiHat>> response) {
                if(response.body() != null) {
                    mangbaihat = (ArrayList<BaiHat>) response.body();
                    danhSachBaiHatAdapter = new DanhSachBaiHatAdapter(DanhSachBaiHatActivity.this, mangbaihat);
                    recyclerViewdanhsachbaihat.setLayoutManager(new LinearLayoutManager(DanhSachBaiHatActivity.this));
                    recyclerViewdanhsachbaihat.setAdapter(danhSachBaiHatAdapter);
                    eventClick();
                }
            }

            @Override
            public void onFailure(Call<List<BaiHat>> call, Throwable t) {

            }
        });
    }

    private void getDataPlaylist(String id) {
       DataService dataService = APIService.getService();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("idplaylist", id);
        Call<List<BaiHat>> callback = dataService.GetDanhSachTheoPlaylist(jsonObject);
        callback.enqueue(new Callback<List<BaiHat>>() {
            @Override
            public void onResponse(Call<List<BaiHat>> call, Response<List<BaiHat>> response) {
                if(response.body() != null) {
                    mangbaihat = (ArrayList<BaiHat>) response.body();
                    danhSachBaiHatAdapter = new DanhSachBaiHatAdapter(DanhSachBaiHatActivity.this, mangbaihat);
                    recyclerViewdanhsachbaihat.setLayoutManager(new LinearLayoutManager(DanhSachBaiHatActivity.this));
                    recyclerViewdanhsachbaihat.setAdapter(danhSachBaiHatAdapter);
                    eventClick();
                }
            }
            @Override
            public void onFailure(Call<List<BaiHat>> call, Throwable t) {
                Log.d("tb","thatbai");
            }
        });
    }

    private void getDataQuangCao(String id) {
        DataService dataService = APIService.getService();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("idquangcao", id);
        Call<List<BaiHat>> callback = dataService.GetDanhSachTheoQuangCao(jsonObject);
        callback.enqueue(new Callback<List<BaiHat>>() {
            @Override
            public void onResponse(Call<List<BaiHat>> call, Response<List<BaiHat>> response) {
                if(response.body() != null) {
                    mangbaihat = (ArrayList<BaiHat>) response.body();
                    danhSachBaiHatAdapter = new DanhSachBaiHatAdapter(DanhSachBaiHatActivity.this, mangbaihat);
                    recyclerViewdanhsachbaihat.setLayoutManager(new LinearLayoutManager(DanhSachBaiHatActivity.this));
                    recyclerViewdanhsachbaihat.setAdapter(danhSachBaiHatAdapter);
                    eventClick();
                }

            }

            @Override
            public void onFailure(Call<List<BaiHat>> call, Throwable t) {
                Log.d("tb","thatbai");
            }
        });
    }

    private void setValueInView(String ten, String hinh) {
        collapsingToolbarLayout.setTitle(ten);
        try {
            URL url= new URL(hinh);
            Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            Bitmap  bitmap1 = BlurBuilder.blur(this, bitmap);
            BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap1);

            collapsingToolbarLayout.setBackground(bitmapDrawable);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Picasso.with(this).load(hinh).into(imgdanhsachcakhuc);
    }

    private void init() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        floatingActionButton.setEnabled(false);
    }


    private void anhxa() {
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        collapsingToolbarLayout = findViewById(R.id.collapsingtoolbar);
        toolbar  = findViewById(R.id.toolbardangsach);
        recyclerViewdanhsachbaihat = findViewById(R.id.recyclerdanhsachbaihat);
        floatingActionButton = findViewById(R.id.floatingactionbutton);
        imgdanhsachcakhuc = findViewById(R.id.imgeviewdanhsachcakhuc);
    }

    private void DataIntent() {
        Intent intent = getIntent();
        if(intent != null){
            if(intent.hasExtra("banner")){
                quangCao = (QuangCao) intent.getSerializableExtra("banner");
                //Toast.makeText(this, quangCao.getSongName(), Toast.LENGTH_SHORT).show();
            }
            if(intent.hasExtra("itemplaylist")){
                playlist = (Playlist) intent.getSerializableExtra("itemplaylist");
                //Toast.makeText(this, playlist.getPlaylistImage(), Toast.LENGTH_SHORT).show();
            }
            if(intent.hasExtra("idtheloai")){
                genre = (Genre) intent.getSerializableExtra("idtheloai");
            }
            if(intent.hasExtra("album")){
                album = (Album) intent.getSerializableExtra("album");
            }
        }
    }
    private void eventClick(){
        floatingActionButton.setEnabled(true);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DanhSachBaiHatActivity.this, PlayNhacActivity.class);
                intent.putExtra("cacbaihat", mangbaihat);
                startActivity(intent);

            }
        });
    }
}
