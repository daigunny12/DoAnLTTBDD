package doan.android.appnhac.Model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Album  implements Serializable {

    @SerializedName("IDSongs")
    @Expose
    private List<String> iDSongs = null;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("AlbumName")
    @Expose
    private String albumName;
    @SerializedName("AlbumSingerName")
    @Expose
    private String albumSingerName;
    @SerializedName("AlbumImage")
    @Expose
    private String albumImage;
    @SerializedName("__v")
    @Expose
    private Integer v;

    public List<String> getIDSongs() {
        return iDSongs;
    }

    public void setIDSongs(List<String> iDSongs) {
        this.iDSongs = iDSongs;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumSingerName() {
        return albumSingerName;
    }

    public void setAlbumSingerName(String albumSingerName) {
        this.albumSingerName = albumSingerName;
    }

    public String getAlbumImage() {
        return albumImage;
    }

    public void setAlbumImage(String albumImage) {
        this.albumImage = albumImage;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

}
