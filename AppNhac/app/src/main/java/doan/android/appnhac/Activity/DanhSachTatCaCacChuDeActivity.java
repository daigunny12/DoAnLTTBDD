package doan.android.appnhac.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import doan.android.appnhac.Adapter.DanhSachTatCaChuDeAdapter;
import doan.android.appnhac.Model.Topic;
import doan.android.appnhac.R;
import doan.android.appnhac.Service.APIService;
import doan.android.appnhac.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DanhSachTatCaCacChuDeActivity extends AppCompatActivity {
    RecyclerView recyclerView ;
    Toolbar toolbar;
    ArrayList<Topic> mangAllChuDe;
    DanhSachTatCaChuDeAdapter danhSachTatCaChuDeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_tat_ca_cac_chu_de);
        init();
        GetData();
    }

    private void GetData() {
        DataService dataService = APIService.getService();
        Call<List<Topic>> callback = dataService.GetAllChuDe();
        callback.enqueue(new Callback<List<Topic>>() {
            @Override
            public void onResponse(Call<List<Topic>> call, Response<List<Topic>> response) {
                mangAllChuDe = (ArrayList<Topic>) response.body();
                danhSachTatCaChuDeAdapter = new DanhSachTatCaChuDeAdapter(DanhSachTatCaCacChuDeActivity.this, mangAllChuDe);
                recyclerView.setLayoutManager(new GridLayoutManager(DanhSachTatCaCacChuDeActivity.this, 1));
                recyclerView.setAdapter(danhSachTatCaChuDeAdapter);
            }

            @Override
            public void onFailure(Call<List<Topic>> call, Throwable t) {

            }
        });
    }

    private void init() {
        recyclerView = findViewById(R.id.recyclerviewAllchude);
        toolbar = findViewById(R.id.toolbarAllchude);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Tất Cả Chủ Đề");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
