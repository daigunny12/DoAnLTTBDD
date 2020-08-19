package doan.android.appnhac.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import doan.android.appnhac.Activity.BlurBuilder;
import doan.android.appnhac.Activity.DanhSachBaiHatActivity;
import doan.android.appnhac.Model.QuangCao;
import doan.android.appnhac.R;

public class BannerAdapter extends PagerAdapter {

    Context context ;
    ArrayList<QuangCao> arrayListbanner;

    public BannerAdapter(Context context, ArrayList<QuangCao> arrayListbanner) {
        this.context = context;
        this.arrayListbanner = arrayListbanner;
    }

    @Override
    public int getCount() {
        return arrayListbanner.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dong_banner, null);

        ImageView imgbackgroundbanner = view.findViewById(R.id.imageviewbackgrounbanner);
        ImageView imgNem = view.findViewById(R.id.imageviewbackgrounbannernen);
        TextView txttilesongbanner = view.findViewById(R.id.textviewtilebannerbaihat);
        TextView txtnoidung = view.findViewById(R.id.textviewnoidung);

        Picasso.with(context).load(arrayListbanner.get(position).getAdsImage()).into(imgbackgroundbanner);

        Bitmap bitmap;
        try {
            URL url = new URL(arrayListbanner.get(position).getAdsImage());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            bitmap =  BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.iconthuvien);
        }
        Bitmap bitmap1 = BlurBuilder.blur(context, bitmap);
        imgNem.setBackground(new BitmapDrawable(bitmap1));
        //Picasso.with(context).load(arrayListbanner.get(position).getAdsImage()).into(imgNem);
        txttilesongbanner.setText(arrayListbanner.get(position).getAdsTitle());
        txtnoidung.setText(arrayListbanner.get(position).getAdsContent());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DanhSachBaiHatActivity.class);
                intent.putExtra("banner", arrayListbanner.get(position));
                context.startActivity(intent);
            }
        });

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
