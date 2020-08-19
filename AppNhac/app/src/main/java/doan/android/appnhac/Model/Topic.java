package doan.android.appnhac.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Topic  implements Serializable {

@SerializedName("_id")
@Expose
private String id;
@SerializedName("TopicName")
@Expose
private String topicName;
@SerializedName("TopicImage")
@Expose
private String topicImage;
@SerializedName("__v")
@Expose
private Integer v;

public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

public String getTopicName() {
return topicName;
}

public void setTopicName(String topicName) {
this.topicName = topicName;
}

public String getTopicImage() {
return topicImage;
}

public void setTopicImage(String topicImage) {
this.topicImage = topicImage;
}

public Integer getV() {
return v;
}

public void setV(Integer v) {
this.v = v;
}

}