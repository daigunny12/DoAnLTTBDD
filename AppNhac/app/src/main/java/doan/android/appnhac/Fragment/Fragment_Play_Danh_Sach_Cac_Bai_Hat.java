package doan.android.appnhac.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import doan.android.appnhac.Activity.PlayNhacActivity;
import doan.android.appnhac.Activity.PlayNhacActivityThuVien;
import doan.android.appnhac.Adapter.PlayNhacAdapter;
import doan.android.appnhac.Adapter.PlayNhacAdapterThuVien;
import doan.android.appnhac.Model.ChayNhac;
import doan.android.appnhac.R;

public class Fragment_Play_Danh_Sach_Cac_Bai_Hat extends Fragment {
    View view;
    RecyclerView recyclerViewplay;
    PlayNhacAdapter playNhacAdapter;
    PlayNhacAdapterThuVien playNhacAdapterThuVien;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_play_danh_sach_cac_bai_hat, container , false);
        recyclerViewplay = view.findViewById(R.id.recyclerviewPlayBaiHat);
        if(PlayNhacActivity.mangbaihat.size() >0){
            playNhacAdapter = new PlayNhacAdapter(getActivity(), PlayNhacActivity.mangbaihat);
            recyclerViewplay.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerViewplay.setAdapter(playNhacAdapter);
        }

        if(PlayNhacActivityThuVien.mySong.size() >0){
            playNhacAdapterThuVien = new PlayNhacAdapterThuVien(getActivity(), PlayNhacActivityThuVien.mySong);
            recyclerViewplay.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerViewplay.setAdapter(playNhacAdapterThuVien);
        }
        return view;
    }
}
