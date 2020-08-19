package doan.android.appnhac.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BaiHat implements Parcelable {

    @SerializedName("IDGenre")
    @Expose
    private List<String> iDGenre = null;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("SongName")
    @Expose
    private String songName;
    @SerializedName("SongImage")
    @Expose
    private String songImage;
    @SerializedName("SongLink")
    @Expose
    private String songLink;
    @SerializedName("SongSinger")
    @Expose
    private String songSinger;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("Likes")
    @Expose
    private String like;
    @SerializedName("Listen")
    @Expose
    private Integer listen;

    protected BaiHat(Parcel in) {
        iDGenre = in.createStringArrayList();
        id = in.readString();
        songName = in.readString();
        songImage = in.readString();
        songLink = in.readString();
        songSinger = in.readString();
        if (in.readByte() == 0) {
            v = null;
        } else {
            v = in.readInt();
        }
        like = in.readString();
    }

    public static final Creator<BaiHat> CREATOR = new Creator<BaiHat>() {
        @Override
        public BaiHat createFromParcel(Parcel in) {
            return new BaiHat(in);
        }

        @Override
        public BaiHat[] newArray(int size) {
            return new BaiHat[size];
        }
    };

    public List<String> getIDGenre() {
return iDGenre;
}

public void setIDGenre(List<String> iDGenre) {
this.iDGenre = iDGenre;
}

public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

public String getSongName() {
return songName;
}

public void setSongName(String songName) {
this.songName = songName;
}

public String getSongImage() {
return songImage;
}

public void setSongImage(String songImage) {
this.songImage = songImage;
}

public String getSongLink() {
return songLink;
}

public void setSongLink(String songLink) {
this.songLink = songLink;
}

public String getSongSinger() {
return songSinger;
}

public void setSongSinger(String songSinger) {
this.songSinger = songSinger;
}

public String getLikes() { return like; }

public void setLikes(String like) { this.like = like; }

public Integer getV() {
return v;
}

public void setV(Integer v) {
this.v = v;
}

    public Integer getListen() {
        return listen;
    }

    public void setListen(Integer listen) {
        this.listen = listen;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(iDGenre);
        dest.writeString(id);
        dest.writeString(songName);
        dest.writeString(songImage);
        dest.writeString(songLink);
        dest.writeString(songSinger);
        if (v == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(v);
        }
        dest.writeString(like);
    }
}