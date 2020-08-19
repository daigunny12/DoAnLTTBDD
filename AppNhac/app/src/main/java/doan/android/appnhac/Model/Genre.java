package doan.android.appnhac.Model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Genre implements Serializable {

@SerializedName("IDTopics")
@Expose
private List<String> iDTopics = null;
@SerializedName("_id")
@Expose
private String id;
@SerializedName("GenreName")
@Expose
private String genreName;
@SerializedName("GenreImage")
@Expose
private String genreImage;
@SerializedName("__v")
@Expose
private Integer v;

public List<String> getIDTopics() {
return iDTopics;
}

public void setIDTopics(List<String> iDTopics) {
this.iDTopics = iDTopics;
}

public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

public String getGenreName() {
return genreName;
}

public void setGenreName(String genreName) {
this.genreName = genreName;
}

public String getGenreImage() {
return genreImage;
}

public void setGenreImage(String genreImage) {
this.genreImage = genreImage;
}

public Integer getV() {
return v;
}

public void setV(Integer v) {
this.v = v;
}

}