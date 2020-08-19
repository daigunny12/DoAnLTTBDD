package doan.android.appnhac.Fragment;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import doan.android.appnhac.R;

public class Fragment_Dia_Nhac extends Fragment {
    View view;
    CircleImageView imgDiaNhac;
    public ObjectAnimator objectAnimator;
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dia_nhac, container, false);
        imgDiaNhac = view.findViewById(R.id.imgeviewcircle);
        objectAnimator = ObjectAnimator.ofFloat(imgDiaNhac,"rotation", 0f,360f  );
        objectAnimator.setDuration(10000);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.setRepeatMode(ValueAnimator.RESTART);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.start();
        return view;
    }
    public void Playnhac(String hinhanh) {
        if(hinhanh.length() > 0 ){
            Picasso.with(getContext()).load(hinhanh).into(imgDiaNhac);
        }
    }

    public void PlaynhacThuVien(String hinhanh) {
        if(hinhanh.length() > 0 ){
            android.media.MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(hinhanh);

            byte [] data = mmr.getEmbeddedPicture();

            if(data != null)
            {
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                imgDiaNhac.setImageBitmap(bitmap);
            }
            else
            {
                imgDiaNhac.setImageResource(R.drawable.iconthuvien);
            }
        }
    }
}
