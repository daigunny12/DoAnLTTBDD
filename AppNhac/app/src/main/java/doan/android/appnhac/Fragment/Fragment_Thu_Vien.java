package doan.android.appnhac.Fragment;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

import doan.android.appnhac.Activity.DanhSachBaiHatActivity;
import doan.android.appnhac.Activity.MainActivity;
import doan.android.appnhac.Activity.PlayNhacActivityThuVien;
import doan.android.appnhac.Adapter.DanhSachBaiHatAdapterThuVien;
import doan.android.appnhac.R;

public class Fragment_Thu_Vien extends Fragment {
    RecyclerView recyclerViewdanhsachbaihat;
    DanhSachBaiHatAdapterThuVien danhSachBaiHatAdapterThuVien;
    ArrayList<File> arrayList;
    Toolbar toolbarThuVien;

    View view;
    String [] items;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.thu_vien_view, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.QuetNhac:
                arrayList.clear();
                danhSachBaiHatAdapterThuVien.notifyDataSetChanged();
                runtimePermisstion();
                danhSachBaiHatAdapterThuVien = new DanhSachBaiHatAdapterThuVien(getActivity(), arrayList);
                recyclerViewdanhsachbaihat.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerViewdanhsachbaihat.setAdapter(danhSachBaiHatAdapterThuVien);
                Toast.makeText(getActivity(), "Quét hoàn tất", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_thu_vien, container, false);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        recyclerViewdanhsachbaihat = view.findViewById(R.id.listviewThuVien);
        runtimePermisstion();

        toolbarThuVien = view.findViewById(R.id.toolbarThuVien);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbarThuVien);
        toolbarThuVien.setTitle("Thư viện");
        setHasOptionsMenu(true);
        danhSachBaiHatAdapterThuVien = new DanhSachBaiHatAdapterThuVien(getActivity(), arrayList);
        recyclerViewdanhsachbaihat.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewdanhsachbaihat.setAdapter(danhSachBaiHatAdapterThuVien);

        return view;
    }

    public void runtimePermisstion(){
        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        display();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }
    public ArrayList<File> findSong(File file){
        ArrayList<File> arrayList = new ArrayList<>();
        File[] fields = file.listFiles();
        for (File singgleFile: fields){
            if (singgleFile.isDirectory()&& !singgleFile.isHidden()){
                arrayList.addAll(findSong(singgleFile));
            }else {
                if(!singgleFile.getName().equals("com.zing.mp3")){
                    if(singgleFile.getName().endsWith(".mp3") ||
                            singgleFile.getName().endsWith(".wav")){
                        arrayList.add(singgleFile);
                    }
                }
            }
        }
    return arrayList;
    }

    void display(){
        final ArrayList<File> mySongs = findSong(Environment.getExternalStorageDirectory());
        arrayList = mySongs;

    }
}
