package doan.android.appnhac.Service;

import java.util.List;

import doan.android.appnhac.Model.Album;
import doan.android.appnhac.Model.BaiHat;
import doan.android.appnhac.Model.Genre;
import doan.android.appnhac.Model.MessageLikes;
import doan.android.appnhac.Model.Playlist;
import doan.android.appnhac.Model.QuangCao;
import doan.android.appnhac.Model.TheLoaiVaChuDe;
import doan.android.appnhac.Model.Topic;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Body;
import com.google.gson.JsonObject;

public interface DataService {
    @GET("ads")
    Call<List<QuangCao>> GetDataBanner();

    @GET("Playlists/getTodayPlaylist")
    Call<List<Playlist>> GetPlaylistCurrenDay();

    @GET("getTodayTopicAndGenre")
    Call<TheLoaiVaChuDe> GetCrategoryMusic();

    @GET("Albums/getTodayAlbum")
    Call<List<Album>> GetAlbumHotToday();

    @GET("songs/get5songs")
    Call<List<BaiHat>> GetAlBaiHatHot();


    @POST("Ads/getSong")
    Call<List<BaiHat>> GetDanhSachTheoQuangCao(@Body JsonObject body);

    @POST("Playlists/getSongs")
    Call<List<BaiHat>> GetDanhSachTheoPlaylist(@Body JsonObject body);

    @GET("Playlists")
    Call<List<Playlist>> GetDanhSachPlaylist();

    @POST("Genres/getSongs")
    Call<List<BaiHat>> GetDanhSachTheoTheLoai(@Body JsonObject body);

    @GET("Topics")
    Call<List<Topic>> GetAllChuDe();

    @POST("Genres/getGenresOfATopic")
    Call<List<Genre>> Gettheloaitheochude(@Body JsonObject body);

    @GET("Albums")
    Call<List<Album>> GetAllAlbum();

    @POST("Albums/getSongs")
    Call<List<BaiHat>> GetDanhSachBaiHatTheoAlbum(@Body JsonObject body);

    @POST("Songs/updateLikes")
    Call<MessageLikes> UpdateLuotThich(@Body JsonObject body);

    @POST("Songs/findbyName")
    Call<List<BaiHat>> GetSearchBaiHatTheoTenBaiHat(@Body JsonObject body);

    @POST("Songs/findbySinger")
    Call<List<BaiHat>> GetSearchBaiHatTheoTenCaSi(@Body JsonObject body);

    @POST("Songs/findbyalbum")
    Call<List<Album>> GetSearchBaiHatTheoAlbum(@Body JsonObject body);

    @POST("Songs/findbyalbumsingername")
    Call<List<BaiHat>> GetSearchBaiHatTheoTenCaSiAlbum(@Body JsonObject body);

    @POST("Songs/findbyplaylist")
    Call<List<Playlist>> GetSearchBaiHatTheoPlaylist(@Body JsonObject body);

    @POST("Songs/addListen")
    Call<MessageLikes> UpdateLuotView(@Body JsonObject body);

    @GET("Songs/top5Listen")
    Call<List<BaiHat>> GetAllBatHatTop();

}
