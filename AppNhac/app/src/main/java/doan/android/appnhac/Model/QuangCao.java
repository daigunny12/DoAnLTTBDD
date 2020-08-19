package doan.android.appnhac.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class QuangCao implements Serializable {

    @SerializedName("IDSong")
    @Expose
    private List<String> iDSong = null;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("AdsImage")
    @Expose
    private String adsImage;
    @SerializedName("AdsContent")
    @Expose
    private String adsContent;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("AdsTitle")
    @Expose
    private String adsTitle;

    public List<String> getIDSong() {
        return iDSong;
    }

    public void setIDSong(List<String> iDSong) {
        this.iDSong = iDSong;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdsImage() {
        return adsImage;
    }

    public void setAdsImage(String adsImage) {
        this.adsImage = adsImage;
    }

    public String getAdsContent() {
        return adsContent;
    }

    public void setAdsContent(String adsContent) {
        this.adsContent = adsContent;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public String getAdsTitle() {
        return adsTitle;
    }

    public void setAdsTitle(String adsTitle) {
        this.adsTitle = adsTitle;
    }

}