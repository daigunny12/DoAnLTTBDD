package doan.android.appnhac.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import doan.android.appnhac.Adapter.ViewPagerPlaylistnhac;
import doan.android.appnhac.Fragment.Fragment_Dia_Nhac;
import doan.android.appnhac.Fragment.Fragment_Play_Danh_Sach_Cac_Bai_Hat;
import doan.android.appnhac.Model.ChayNhac;
import doan.android.appnhac.Model.Playable;
import doan.android.appnhac.R;
import doan.android.appnhac.Service.OnClearFromRecentService;

public class PlayNhacActivityThuVien extends AppCompatActivity implements ChayNhac, Playable {

    LinearLayout linearLayout;
    Toolbar toolbarNhac;
    TextView txtTotaltimesong, txtTimesong;
    SeekBar skTime;
    String sname;
    ImageButton imgPlay, imgRepeat, imgNext, imgpre, imgradom;
    TextView txtTenBaiHatTenCaSi;
    ViewPager viewPagerNhac;
    Fragment_Dia_Nhac fragment_dia_nhac;
    Fragment_Play_Danh_Sach_Cac_Bai_Hat fragment_play_danh_sach_cac_bai_hat;

    public static  ArrayList<File> mySong  = new ArrayList<>();;

    public static ViewPagerPlaylistnhac adapternhac;
    static MediaPlayer mediaPlayer;
    int position;
    boolean repeat = false;
    boolean checkrandom = false;
    boolean next =  false;
    boolean isPlaying = false;
    NotificationManager notificationManager;
    CountDownTimer mHenGio;
    long mTime = 0;
    Dialog dialogHenGio;
    Boolean ktNS = true;
    EditText editTextNhapSo;
    RadioGroup radioGroup;
    TextView txtDongY;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        notificationManager.cancelAll();
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.HenGio){
            txtDongY.setVisibility(View.GONE);
            editTextNhapSo.setVisibility(View.GONE);
            radioGroup.setVisibility(View.VISIBLE);
            dialogHenGio.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nghe_nhac_thu_vien_view ,menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void setDialog() {
        dialogHenGio = new Dialog(this);
        dialogHenGio.setContentView(R.layout.alertdialog_hen_gio);

        editTextNhapSo = (EditText) dialogHenGio.findViewById(R.id.editTextNhapSo);
        radioGroup = (RadioGroup) dialogHenGio.findViewById(R.id.radioGroup);
        final Switch aSwitch = (Switch) dialogHenGio.findViewById(R.id.switchHenGio);
        txtDongY = (TextView) dialogHenGio.findViewById(R.id.texviewDongY);
        final TextView txtTime = (TextView) dialogHenGio.findViewById(R.id.txtTime);
        TextView txtNhapSoPhut = (TextView) dialogHenGio.findViewById(R.id.txtNhapSoPhut);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                aSwitch.setChecked(false);
                if(checkedId == R.id.radioButton1){
                    txtTime.setText("00:00:10");
                }
                if(checkedId == R.id.radioButton2){
                    txtTime.setText("00:30");
                }
                if(checkedId == R.id.radioButton3){
                    txtTime.setText("01:00");
                }
                if(checkedId == R.id.radioButton4){
                    txtTime.setText("02:00");
                }
                aSwitch.setChecked(true);
            }
        });

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    if(mHenGio != null){
                        mHenGio.cancel();
                    }

                    LocalTime time = LocalTime.parse(txtTime.getText());
                    Long mils = ChronoUnit.MILLIS.between(LocalTime.MIDNIGHT, time);
                    mHenGio = new CountDownTimer(mils, 100) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                            txtTime.setText(hms);
                        }

                        @Override
                        public void onFinish() {
                            mediaPlayer.pause();
                            imgPlay.setImageResource(R.drawable.iconplay);
                            if (fragment_dia_nhac.objectAnimator!=null){
                                fragment_dia_nhac.objectAnimator.pause();

                            }
                            CreateNotificationThuVien.createNotification(PlayNhacActivityThuVien.this, mySong.get(position).getName(),
                                    R.drawable.iconplay, position, mySong.size()-1);
                        }
                    }.start();
                }else{
                    mHenGio.cancel();
                }

            }
        });


        txtNhapSoPhut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ktNS){
                    txtDongY.setVisibility(View.VISIBLE);
                    editTextNhapSo.setVisibility(View.VISIBLE);
                    radioGroup.setVisibility(View.GONE);
                    editTextNhapSo.setEnabled(true);
                    ktNS = false;
                }else {
                    txtDongY.setVisibility(View.GONE);
                    editTextNhapSo.setVisibility(View.GONE);
                    radioGroup.setVisibility(View.VISIBLE);
                    editTextNhapSo.setEnabled(false);
                    ktNS = true;
                }
            }
        });

        txtDongY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aSwitch.setChecked(false);
                if(editTextNhapSo.getText().length() >0){
                    int t = Integer.parseInt(String.valueOf(editTextNhapSo.getText()));
                    int h = (t / 60);
                    int m = (t % 60);
                    String timeHHMM = String.format("%02d:%02d", h, m);
                    txtTime.setText(timeHHMM);
                    aSwitch.setChecked(true);
                }
                dialogHenGio.hide();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_nhac_thu_vien);

        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        mySong = (ArrayList) bundle.getParcelableArrayList("Song");
        //sname = mySong.get(position).getName().toString();
        position = bundle.getInt("pos",0);

        init();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createChannel();
            registerReceiver(broadcastReceiver , new IntentFilter("TRACKS_TRACKS"));
            startService(new Intent(getBaseContext() , OnClearFromRecentService.class));
        }

        CreateNotificationThuVien.createNotification(PlayNhacActivityThuVien.this, mySong.get(position).getName(),
                R.drawable.iconpause, 1, mySong.size()-1);

        eventClick();
        setDialog();
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action   = intent.getExtras().getString("actionname");
            switch (action){
                case CreateNotification.ACTIONPREVIUOS:
                    onTrackPrevious();
                    break;
                case CreateNotification.CHANNEL_PLAY:
                    if(isPlaying){
                        onTrackPause();
                    }else{
                        onTrackPlay();
                    }
                    break;
                case CreateNotification.CHANNEL_NEXT:
                    onTrackNext();
                    break;
            }
        }
    };

    private void createChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CreateNotification.CHANNEL_ID,
                    "DAI", NotificationManager.IMPORTANCE_LOW);
            notificationManager = getSystemService(NotificationManager.class);
            if(notificationManager != null){
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private void init() {
        linearLayout = findViewById(R.id.llThuVien);
        toolbarNhac = findViewById(R.id.toolbarplaynhac);
        txtTimesong = findViewById(R.id.textviewtimesong);
        txtTotaltimesong = findViewById(R.id.textviewtotaltimesong);
        skTime = findViewById(R.id.seekbarsong);
        imgPlay = findViewById(R.id.imgebuttonplay);
        imgNext = findViewById(R.id.imgebuttonnext);
        imgpre = findViewById(R.id.imgebuttonperview);
        imgradom = findViewById(R.id.imgebuttonsuffle);
        imgRepeat = findViewById(R.id.imgebuttonrepeat);
        viewPagerNhac = findViewById(R.id.viewpagerplaynhac);
        txtTenBaiHatTenCaSi = findViewById(R.id.textviewTenBaiHatTenCaSi);
        setSupportActionBar(toolbarNhac);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarNhac.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                mediaPlayer.stop();
                mySong.clear();
                if(mHenGio != null){
                    mHenGio.cancel();
                }
                notificationManager.cancelAll();
            }
        });
        toolbarNhac.setTitleTextColor(Color.WHITE);
        fragment_dia_nhac = new Fragment_Dia_Nhac();
        fragment_play_danh_sach_cac_bai_hat = new Fragment_Play_Danh_Sach_Cac_Bai_Hat();
        adapternhac = new ViewPagerPlaylistnhac(getSupportFragmentManager());
        adapternhac.addFragment(fragment_dia_nhac);
       adapternhac.addFragment(fragment_play_danh_sach_cac_bai_hat);
        viewPagerNhac.setAdapter(adapternhac);
        fragment_dia_nhac = (Fragment_Dia_Nhac) adapternhac.getItem(0);
        if(mySong.size() > 0){
            getSupportActionBar().setTitle("Thư viện");
            txtTenBaiHatTenCaSi.setText(mySong.get(position).getName() );
            new PlayMp3().execute(mySong.get(position).toString());
            imgPlay.setImageResource(R.drawable.iconpause);
        }

    }

    private void eventClick() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(adapternhac.getItem(0)!= null){
                    if(mySong.size()>0){
                        fragment_dia_nhac.PlaynhacThuVien(mySong.get(position).getPath());
                        setBackground(mySong.get(position).getPath());
                        handler.removeCallbacks(this);
                    }else{
                        handler.postDelayed(this, 300);
                    }
                }
            }
        }, 500);

        imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickPlay();
            }
        });

        imgRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRepeat();
            }
        });

        imgradom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRadom();
            }
        });

        skTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        imgNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickNext();
            }
        });

        imgpre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickPre();
            }
        });
    }

    private  void onClickPlay(){
        if (mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            imgPlay.setImageResource(R.drawable.iconplay);
            if (fragment_dia_nhac.objectAnimator!=null){
                fragment_dia_nhac.objectAnimator.pause();

            }
            CreateNotificationThuVien.createNotification(PlayNhacActivityThuVien.this, mySong.get(position).getName(),
                    R.drawable.iconplay, position, mySong.size()-1);
        }else{
            mediaPlayer.start();
            imgPlay.setImageResource(R.drawable.iconpause);
            if (fragment_dia_nhac.objectAnimator!=null){
                fragment_dia_nhac.objectAnimator.resume();
            }
            CreateNotificationThuVien.createNotification(PlayNhacActivityThuVien.this, mySong.get(position).getName(),
                    R.drawable.iconpause, position, mySong.size()-1);
        }
    }

    private  void onClickPre(){
        if(mySong.size() > 0){
            if(mediaPlayer.isPlaying() || mediaPlayer != null){
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
            if(position < (mySong.size())){
                imgPlay.setImageResource(R.drawable.iconpause);
                position -- ;
                if(position < 0 ){
                    position = mySong.size() - 1;
                }
                if (repeat == true){
                    position +=1;
                }
                if(checkrandom == true){
                    Random random = new  Random();
                    int index = random.nextInt(mySong.size());
                    if(index == position){
                        position = index - 1;
                    }
                    position = index;
                }

                if (fragment_dia_nhac.objectAnimator!=null){
                    fragment_dia_nhac.objectAnimator.resume();
                }

                CreateNotificationThuVien.createNotification(PlayNhacActivityThuVien.this, mySong.get(position).getName(),
                        R.drawable.iconpause, position, mySong.size()-1);

                new PlayMp3().execute(mySong.get(position).toString());
                fragment_dia_nhac.PlaynhacThuVien(mySong.get(position).getPath());
                setBackground(mySong.get(position).getPath());
                txtTenBaiHatTenCaSi.setText(mySong.get(position).getName() );
                updateTime();
            }
        }
        imgpre.setClickable(false);
        imgNext.setClickable(false);
        Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                imgpre.setClickable(true);
                imgNext.setClickable(true);
            }
        },1000);
    }

    private void onClickNext(){
        if(mySong.size() > 0){
            if(mediaPlayer.isPlaying() || mediaPlayer != null){
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
            if(position < (mySong.size())){
                imgPlay.setImageResource(R.drawable.iconpause);
                position ++;
                if (repeat == true){
                    if(position == 0 ){
                        position = mySong.size();
                    }
                    position -=1;
                }
                if(checkrandom == true){
                    Random random = new  Random();
                    int index = random.nextInt(mySong.size());
                    if(index == position){
                        position = index - 1;
                    }
                    position = index;
                }
                if(position > mySong.size() - 1){
                    position = 0;
                }

                if (fragment_dia_nhac.objectAnimator!=null){
                    fragment_dia_nhac.objectAnimator.resume();
                }

                CreateNotificationThuVien.createNotification(PlayNhacActivityThuVien.this, mySong.get(position).getName(),
                        R.drawable.iconpause, position, mySong.size()-1);

                new PlayMp3().execute(mySong.get(position).toString());
                fragment_dia_nhac.PlaynhacThuVien(mySong.get(position).getPath());
                setBackground(mySong.get(position).getPath());
                txtTenBaiHatTenCaSi.setText(mySong.get(position).getName());
                updateTime();
            }
        }
        imgpre.setClickable(false);
        imgNext.setClickable(false);
        Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                imgpre.setClickable(true);
                imgNext.setClickable(true);
            }
        },1000);
    }

    private void onClickRadom(){
        if(checkrandom == false){
            if(repeat == true){
                repeat = false;
                imgRepeat.setImageResource(R.drawable.iconrepeat);
                imgradom.setImageResource(R.drawable.iconshuffled);
            }
            imgradom.setImageResource(R.drawable.iconshuffled);
            checkrandom = true;
        }else{
            imgradom.setImageResource(R.drawable.iconsuffle);
            checkrandom = false;
        }
    }

    private void onClickRepeat(){
        if(repeat == false){
            if(checkrandom == true){
                checkrandom = false;
                imgRepeat.setImageResource(R.drawable.iconsyned);
                imgradom.setImageResource(R.drawable.iconsuffle);
            }
            imgRepeat.setImageResource(R.drawable.iconsyned);
            repeat = true;
        }else{
            imgRepeat.setImageResource(R.drawable.iconrepeat);
            repeat = false;
        }
    }

    @Override
    public void onChayNhac(int pos) {
        position = pos;
        CreateNotificationThuVien.createNotification(PlayNhacActivityThuVien.this, mySong.get(pos).getName(),
                R.drawable.iconpause, pos, mySong.size()-1);

        if(mySong.size() > 0){
            if(mediaPlayer.isPlaying() || mediaPlayer != null){
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
        }
        new PlayMp3().execute(mySong.get(pos).toString());
        setBackground(mySong.get(position).getPath());
        txtTenBaiHatTenCaSi.setText(mySong.get(pos).getName());
        fragment_dia_nhac.PlaynhacThuVien(mySong.get(position).getPath());
        updateTime();
    }

    @Override
    public void onTrackPrevious() {
        onClickPre();
    }

    @Override
    public void onTrackPlay() {
        onClickPlay();
        isPlaying = true;
    }

    @Override
    public void onTrackPause() {
        onClickPlay();
        isPlaying = false;
    }

    @Override
    public void onTrackNext() {
        onClickNext();
    }

    @Override
    public void onDesTroy() {
        super.onDestroy();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            notificationManager.cancelAll();
        }
        unregisterReceiver(broadcastReceiver);

    }


    class PlayMp3 extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            return strings[0];
        }

        @Override
        protected void onPostExecute(String baihat) {
            super.onPostExecute(baihat);

                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                    }
                });

                Uri u = Uri.parse(baihat);
                mediaPlayer = MediaPlayer.create(getApplicationContext(),u);
            mediaPlayer.start();
            TimeSong();
            updateTime();
        }
    }
    private void TimeSong() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        txtTotaltimesong.setText(simpleDateFormat.format(mediaPlayer.getDuration()));
        skTime.setMax(mediaPlayer.getDuration());
    }

    private void updateTime(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer != null){
                    skTime.setProgress(mediaPlayer.getCurrentPosition());
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
                    txtTimesong.setText(simpleDateFormat.format(mediaPlayer.getCurrentPosition()));
                    handler.postDelayed(this, 300);
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            next  = true;
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        },300);
        final Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(next == true){
                    if(position < (mySong.size())){
                        imgPlay.setImageResource(R.drawable.iconpause);
                        position ++;
                        if (repeat == true){
                            if(position == 0 ){
                                position = mySong.size();
                            }
                            position -=1;
                        }
                        if(checkrandom == true){
                            Random random = new  Random();
                            int index = random.nextInt(mySong.size());
                            if(index == position){
                                position = index - 1;
                            }
                            position = index;
                        }
                        if(position > mySong.size() - 1){
                            position = 0;
                        }
                        new PlayMp3().execute(mySong.get(position).toString());
                        fragment_dia_nhac.PlaynhacThuVien(mySong.get(position).getPath());
                        setBackground(mySong.get(position).getPath());
                        txtTenBaiHatTenCaSi.setText(mySong.get(position).getName());
                    }
                    imgpre.setClickable(false);
                    imgNext.setClickable(false);
                    Handler handler1 = new Handler();
                    handler1.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            imgpre.setClickable(true);
                            imgNext.setClickable(true);
                        }
                    },1000);
                    next = false;
                    handler1.removeCallbacks(this);
                }else {
                    handler1.postDelayed(this, 1000);
                }
            }
        }, 1000);
    }
    private  void  setBackground(String hinh){

        android.media.MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(hinh);

        byte [] data = mmr.getEmbeddedPicture();

        if(data != null)
        {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            Bitmap bitmap1 = BlurBuilder.blur(this, bitmap);
            BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap1);
            linearLayout.setBackground(bitmapDrawable);
        }

    }
}
