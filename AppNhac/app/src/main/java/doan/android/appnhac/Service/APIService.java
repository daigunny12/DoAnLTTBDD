package doan.android.appnhac.Service;

public class APIService {
    private  static final String basa_url = "https://giadinhmusicapp.herokuapp.com/api/";

    public static DataService getService(){
        return APIRetrofitClient.getClient(basa_url).create(DataService.class);
    }
}
