package doan.android.appnhac.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import doan.android.appnhac.Adapter.DanhSachTheLoaiTheoChuDeAdapter;
import doan.android.appnhac.Model.Genre;
import doan.android.appnhac.Model.Topic;
import doan.android.appnhac.R;
import doan.android.appnhac.Service.APIService;
import doan.android.appnhac.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DanhSachTheLoaiTheoChuDeActivity extends AppCompatActivity {

    Topic topic;
    RecyclerView recyclerView;
    Toolbar toolbar;
    ArrayList<Genre> mangtheloai;
    DanhSachTheLoaiTheoChuDeAdapter deAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_the_loai_theo_chu_de);
        GetIntent();
        init();
        GetData();

    }

    private void GetData() {
        DataService dataService = APIService.getService();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("idchude", topic.getId());
        Call<List<Genre>> callback = dataService.Gettheloaitheochude(jsonObject);
        callback.enqueue(new Callback<List<Genre>>() {
            @Override
            public void onResponse(Call<List<Genre>> call, Response<List<Genre>> response) {
                mangtheloai = (ArrayList<Genre>) response.body();
                deAdapter = new DanhSachTheLoaiTheoChuDeAdapter(DanhSachTheLoaiTheoChuDeActivity.this, mangtheloai);
                recyclerView.setLayoutManager(new GridLayoutManager(DanhSachTheLoaiTheoChuDeActivity.this, 2));
                recyclerView.setAdapter(deAdapter);
            }

            @Override
            public void onFailure(Call<List<Genre>> call, Throwable t) {

            }
        });
    }

    private void init() {
        recyclerView = findViewById(R.id.recyclerviewtheloaitheochude);
        toolbar = findViewById(R.id.toolbartheloaithechude);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(topic.getTopicName());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void GetIntent() {
        Intent intent = getIntent();
        if(intent.hasExtra("chude")){
            topic = (Topic) intent.getSerializableExtra("chude");
        }
    }
}
