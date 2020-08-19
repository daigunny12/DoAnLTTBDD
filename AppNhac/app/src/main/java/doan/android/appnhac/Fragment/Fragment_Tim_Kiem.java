package doan.android.appnhac.Fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.SearchView;

import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import doan.android.appnhac.Activity.DanhSachBaiHatActivity;
import doan.android.appnhac.Adapter.AlbumAdapter;
import doan.android.appnhac.Adapter.PlaylistAdapter;
import doan.android.appnhac.Adapter.SearchBaiHatAdapter;
import doan.android.appnhac.Model.Album;
import doan.android.appnhac.Model.BaiHat;
import doan.android.appnhac.Model.Playlist;
import doan.android.appnhac.R;
import doan.android.appnhac.Service.APIService;
import doan.android.appnhac.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Tim_Kiem extends Fragment {
    private int waitingTime = 200;
    private CountDownTimer cntr;
    String textChange ;
    View view;
    Toolbar toolbarSearchbaihat;
    LinearLayout llBaiHat;
    LinearLayout llCaSi;
    LinearLayout llAlbum;
    LinearLayout llPlaylist;

    RecyclerView recyclerViewBaihat;
    RecyclerView recyclerViewCasi;
    RecyclerView recyclerViewAlbum;
    ListView recyclerViewPlaylist;

    TextView txtkhongcodulieu;
    SearchBaiHatAdapter searchBaiHatAdapterBaiHat;
    SearchBaiHatAdapter searchBaiHatAdapterCasi;
    AlbumAdapter albumAdapter;
    PlaylistAdapter playlistAdapter;
    ArrayList<Playlist> mangplaylist;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tim_kiem, container, false);
        toolbarSearchbaihat = view.findViewById(R.id.toolbarseach);

        recyclerViewBaihat      = view.findViewById(R.id.recyclerviewSearchbaihat);
        recyclerViewCasi        = view.findViewById(R.id.recyclerviewSearchCasi);
        recyclerViewAlbum       = view.findViewById(R.id.recyclerviewSearchAlbum);
        recyclerViewPlaylist    = view.findViewById(R.id.recyclerviewSearchPlaylist);

        llBaiHat                = view.findViewById(R.id.llBaiHat);
        llCaSi                = view.findViewById(R.id.llCaSi);
        llAlbum                = view.findViewById(R.id.llAlbum);
        llPlaylist                = view.findViewById(R.id.llPlaylist);

        txtkhongcodulieu = view.findViewById(R.id.textviewkhongcodulieu);

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbarSearchbaihat);
        toolbarSearchbaihat.setTitle("Tìm kiếm");
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_view, menu);
        MenuItem menuItem  = menu.findItem(R.id.menu_search);
        final SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                textChange = newText;
                if(cntr != null){
                    cntr.cancel();
                }
                cntr = new CountDownTimer(waitingTime, 500) {

                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        if(textChange.length() > 1){
                            txtkhongcodulieu.setVisibility(View.GONE);
                            new searchTheoTenBaiHat().execute(textChange);
                            new searchTheoTenCaSi().execute(textChange);
                            new searchTheoTenAlbum().execute(textChange);
                            new searchTheoTenPlaylist().execute(textChange);
                            if(     llAlbum.getVisibility() == View.GONE
                                    && llCaSi.getVisibility() == View.GONE
                                    && llBaiHat.getVisibility() == View.GONE
                                    && llPlaylist.getVisibility() == View.GONE
                            )
                            {
                                txtkhongcodulieu.setVisibility(View.VISIBLE);
                            }else {
                                txtkhongcodulieu.setVisibility(View.GONE);
                            }
                        }else{
                            llAlbum.setVisibility(View.GONE);
                            llCaSi.setVisibility(View.GONE);
                            llPlaylist.setVisibility(View.GONE);
                            llBaiHat.setVisibility(View.GONE);

                        }
                    }
                };
                cntr.start();
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private  class searchTheoTenBaiHat extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(final String... strings) {
            DataService dataService = APIService.getService();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("tukhoa", strings[0]);
            Call<List<BaiHat>> callback = dataService.GetSearchBaiHatTheoTenBaiHat(jsonObject);
            callback.enqueue(new Callback<List<BaiHat>>() {
                @Override
                public void onResponse(Call<List<BaiHat>> call, Response<List<BaiHat>> response) {
                    ArrayList<BaiHat> mangbaihat = (ArrayList<BaiHat>) response.body();
                    if(mangbaihat.size() >0 ){
                        searchBaiHatAdapterBaiHat = new SearchBaiHatAdapter(getActivity(), mangbaihat);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                        recyclerViewBaihat.setLayoutManager(linearLayoutManager);
                        recyclerViewBaihat.setAdapter(searchBaiHatAdapterBaiHat);
                        llBaiHat.setVisibility(View.VISIBLE);
                        txtkhongcodulieu.setVisibility(View.GONE);
                    }else {
                        llBaiHat.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<List<BaiHat>> call, Throwable t) {

                }
            });
            return null;
        }
    }

    private  class searchTheoTenCaSi extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(final String... strings) {
            DataService dataService = APIService.getService();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("tukhoa", strings[0]);
            Call<List<BaiHat>> callback = dataService.GetSearchBaiHatTheoTenCaSi(jsonObject);
            callback.enqueue(new Callback<List<BaiHat>>() {
                @Override
                public void onResponse(Call<List<BaiHat>> call, Response<List<BaiHat>> response) {
                    ArrayList<BaiHat> mangbaihat = (ArrayList<BaiHat>) response.body();
                    if(mangbaihat.size() >0 ){
                        searchBaiHatAdapterCasi = new SearchBaiHatAdapter(getActivity(), mangbaihat);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                        recyclerViewCasi.setLayoutManager(linearLayoutManager);
                        recyclerViewCasi.setAdapter(searchBaiHatAdapterCasi);
                        llCaSi.setVisibility(View.VISIBLE);
                        txtkhongcodulieu.setVisibility(View.GONE);
                    }else {
                        llCaSi.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<List<BaiHat>> call, Throwable t) {

                }
            });
            return null;
        }
    }

    private  class searchTheoTenAlbum extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(final String... strings) {
            DataService dataService = APIService.getService();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("tukhoa", strings[0]);
            Call<List<Album>> callback = dataService.GetSearchBaiHatTheoAlbum(jsonObject);
            callback.enqueue(new Callback<List<Album>>() {
                @Override
                public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {
                    ArrayList<Album> mangAlbumArrayList = (ArrayList<Album>) response.body ();
                    if(mangAlbumArrayList.size() > 0){
                        ArrayList<Album> albumArrayList = (ArrayList<Album>) response.body();
                        albumAdapter = new AlbumAdapter(getActivity(), albumArrayList);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                        recyclerViewAlbum.setLayoutManager(linearLayoutManager);
                        recyclerViewAlbum.setAdapter(albumAdapter);
                        llAlbum.setVisibility(View.VISIBLE);
                        txtkhongcodulieu.setVisibility(View.GONE);
                    }else{
                        llAlbum.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<List<Album>> call, Throwable t) {

                }
            });
            return null;
        }
    }

    private  class searchTheoTenPlaylist extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(final String... strings) {
            DataService dataService = APIService.getService();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("tukhoa", strings[0]);
            Call<List<Playlist>> callback = dataService.GetSearchBaiHatTheoPlaylist(jsonObject);
            callback.enqueue(new Callback<List<Playlist>>() {
                @Override
                public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                    mangplaylist = (ArrayList<Playlist>) response.body();
                    if(mangplaylist.size()>0) {
                        playlistAdapter = new PlaylistAdapter(getActivity(), android.R.layout.simple_list_item_1, mangplaylist);
                        recyclerViewPlaylist.setAdapter(playlistAdapter);
                        setListViewHeightBasedOnChildren(recyclerViewPlaylist);
                        llPlaylist.setVisibility(View.VISIBLE);
                        txtkhongcodulieu.setVisibility(View.GONE);
                    }else {
                        llPlaylist.setVisibility(View.GONE);
                    }

                    recyclerViewPlaylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getActivity(), DanhSachBaiHatActivity.class);
                            intent.putExtra("itemplaylist", mangplaylist.get(position));
                            startActivity(intent);
                        }
                    });
                }

                @Override
                public void onFailure(Call<List<Playlist>> call, Throwable t) {

                }
            });
            return null;
        }
    }
    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);

            if(listItem != null){
                // This next line is needed before you call measure or else you won't get measured height at all. The listitem needs to be drawn first to know the height.
                listItem.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                totalHeight += listItem.getMeasuredHeight();

            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

}
