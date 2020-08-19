package doan.android.appnhac.Model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Playlist implements Serializable {
    @SerializedName("IDSongs")
    @Expose
    private List<String> iDSongs = null;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("PlaylistName")
    @Expose
    private String playlistName;
    @SerializedName("PlaylistImage")
    @Expose
    private String playlistImage;
    @SerializedName("PlaylistIcon")
    @Expose
    private String playlistIcon;
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

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    public String getPlaylistImage() {
        return playlistImage;
    }

    public void setPlaylistImage(String playlistImage) {
        this.playlistImage = playlistImage;
    }

    public String getPlaylistIcon() {
        return playlistIcon;
    }

    public void setPlaylistIcon(String playlistIcon) {
        this.playlistIcon = playlistIcon;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }
}
