package doan.android.appnhac.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import doan.android.appnhac.Activity.DanhSachBaiHatActivity;
import doan.android.appnhac.Activity.DanhSachTatCaCacChuDeActivity;
import doan.android.appnhac.Activity.DanhSachTheLoaiTheoChuDeActivity;
import doan.android.appnhac.Model.Genre;
import doan.android.appnhac.Model.TheLoaiVaChuDe;
import doan.android.appnhac.Model.Topic;
import doan.android.appnhac.R;
import doan.android.appnhac.Service.APIService;
import doan.android.appnhac.Service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_ChuDe_TheLoai_ToDay extends Fragment {
    View view;
    HorizontalScrollView horizontalScrollView;
    TextView txtxemthemchudevatheloai;
    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chude_theloai_today, container, false);
        horizontalScrollView = view.findViewById(R.id.horizontalScrollView);
        txtxemthemchudevatheloai = view.findViewById(R.id.textviewxemthem);
        txtxemthemchudevatheloai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DanhSachTatCaCacChuDeActivity.class);
                startActivity(intent);
            }
        });
        GetData();
        return view;
    }

    private void GetData() {
        DataService dataService = APIService.getService();
        Call<TheLoaiVaChuDe> callback = dataService.GetCrategoryMusic();
        callback.enqueue(new Callback<TheLoaiVaChuDe>() {
            @Override
            public void onResponse(Call<TheLoaiVaChuDe> call, Response<TheLoaiVaChuDe> response) {
                TheLoaiVaChuDe theLoaiVaChuDe = response.body();
                final ArrayList<Topic> chuDeArraylist = new ArrayList<>();
                chuDeArraylist.addAll(theLoaiVaChuDe.getTopic());

                final ArrayList<Genre> theLoaiArraylist = new ArrayList<>();
                theLoaiArraylist.addAll(theLoaiVaChuDe.getGenre());

                LinearLayout linearLayout = new LinearLayout(getActivity());
                linearLayout.setOrientation(linearLayout.HORIZONTAL);

                LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(600, 330);
                layout.setMargins(10, 20 , 10 , 30 );
                for (int i = 0 ; i < (chuDeArraylist.size()); i++){
                    CardView cardView = new CardView(getActivity());
                    cardView.setRadius(10);
                    ImageView imageView = new ImageView(getActivity());
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    TextView textView = new TextView(getActivity());
                    textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.mautrang));
                    textView.setTextSize(16);
                    textView.setPadding(10, 10, 10, 10);
                    if(chuDeArraylist.get(i).getTopicImage() != null){
                        Picasso.with(getActivity()).load(chuDeArraylist.get(i).getTopicImage()).into(imageView);
                        textView.setText(chuDeArraylist.get(i).getTopicName());
                    }

                    cardView.setLayoutParams(layout);
                    cardView.addView(imageView);
                    cardView.addView(textView);
                    linearLayout.addView(cardView);
                    final int finalI = i;
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), DanhSachTheLoaiTheoChuDeActivity.class);
                            intent.putExtra("chude", chuDeArraylist.get(finalI));
                            startActivity(intent);
                        }
                    });
                }
                for (int j = 0 ; j < (theLoaiArraylist.size()); j++){
                    CardView cardView = new CardView(getActivity());
                    cardView.setRadius(10);
                    ImageView imageView = new ImageView(getActivity());
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    if(theLoaiArraylist.get(j).getGenreImage() != null){
                        Picasso.with(getActivity()).load(theLoaiArraylist.get(j).getGenreImage()).into(imageView);
                    }
                    cardView.setLayoutParams(layout);
                    cardView.addView(imageView);
                    linearLayout.addView(cardView);
                    final int finalJ = j;
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), DanhSachBaiHatActivity.class);
                            intent.putExtra("idtheloai", theLoaiArraylist.get(finalJ));
                            startActivity(intent);
                        }
                    });
                }
                horizontalScrollView.addView(linearLayout);
            }

            @Override
            public void onFailure(Call<TheLoaiVaChuDe> call, Throwable t) {
            }
        });

    }
}
