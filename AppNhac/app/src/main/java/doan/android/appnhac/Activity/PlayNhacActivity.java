package doan.android.appnhac.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
//import java.net.HttpURLConnection;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import doan.android.appnhac.Adapter.ViewPagerPlaylistnhac;
import doan.android.appnhac.Fragment.Fragment_Dia_Nhac;
import doan.android.appnhac.Fragment.Fragment_Play_Danh_Sach_Cac_Bai_Hat;
import doan.android.appnhac.Model.BaiHat;
import doan.android.appnhac.Model.ChayNhac;
import doan.android.appnhac.Model.MessageLikes;
import doan.android.appnhac.Model.Playable;
import doan.android.appnhac.R;
import doan.android.appnhac.Service.APIService;
import doan.android.appnhac.Service.DataService;
import doan.android.appnhac.Service.OnClearFromRecentService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayNhacActivity extends AppCompatActivity  implements Playable, ChayNhac {
    Toolbar toolbarNhac;
    LinearLayout llnhac;
    TextView txtTotaltimesong, txtTimesong;
    SeekBar skTime;
    ImageButton imgPlay, imgRepeat, imgNext, imgpre, imgradom;
    TextView txtTenBaiHatTenCaSi;
    ViewPager viewPagerNhac;
    Fragment_Dia_Nhac fragment_dia_nhac;
    Fragment_Play_Danh_Sach_Cac_Bai_Hat fragment_play_danh_sach_cac_bai_hat;
    public static ArrayList<BaiHat> mangbaihat = new ArrayList<>();
    public static ViewPagerPlaylistnhac adapternhac;
    MediaPlayer mediaPlayer;
    int position = 0;
    boolean repeat = false;
    boolean checkrandom = false;
    boolean next =  false;
    boolean isPlaying = false;
    NotificationManager notificationManager;
    CountDownTimer mcountDownTimer;
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
        if(item.getItemId() == R.id.DownloadMp3){
            Dexter.withActivity(PlayNhacActivity.this)
                    .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                            new DownloadMp3().execute(mangbaihat.get(position).getSongLink(),
                                    mangbaihat.get(position).getSongName(),
                                    mangbaihat.get(position).getSongSinger(),
                                    mangbaihat.get(position).getId()
                            );
                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                        }
                    }).check();
        }
        if(item.getItemId() == R.id.HenGio){
            txtDongY.setVisibility(View.GONE);
            editTextNhapSo.setVisibility(View.GONE);
            radioGroup.setVisibility(View.VISIBLE);
            dialogHenGio.show();
        }
        return super.onOptionsItemSelected(item);
    }


    class  DownloadMp3 extends  AsyncTask<String , Integer, String>{
        ProgressDialog progressDialog;
//            NotificationManagerCompat notificationManager ;
//            NotificationCompat.Builder builder ;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(PlayNhacActivity.this);
            progressDialog.setTitle("Đang tải xuống...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(100);
            progressDialog.setProgress(0);
            progressDialog.show();
//            notificationManager = NotificationManagerCompat.from(PlayNhacActivity.this);
//            builder = new NotificationCompat.Builder(PlayNhacActivity.this, CHANNEL_ID);
//            builder.setContentTitle("Download")
//                    .setContentText("Đang tải xuống")
//                    .setSmallIcon(R.drawable.icondownload)
//                    .setPriority(NotificationCompat.PRIORITY_LOW);
//
//            int PROGRESS_MAX = 100;
//            int PROGRESS_CURRENT = 0;
//            builder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false);
//            notificationManager.notify(0, builder.build());

        }

        @Override
        protected String doInBackground(String... strings) {
            int fileLenght = 0;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                fileLenght = urlConnection.getContentLength();
                File new_folder = new File("sdcard/dinhMp3");
                if(!new_folder.exists()){
                    new_folder.mkdir();
                }
                File input_flie = new File( new_folder,strings[1]+"_"+strings[2]+"_"+strings[3]+".mp3");
                InputStream inputStream = new BufferedInputStream(url.openStream(), 8192);
                byte[] data = new byte[1024];
                int tocal = 0;
                int count = 0;
                OutputStream outputStream = new FileOutputStream(input_flie);
                while ((count = inputStream.read(data)) != -1 ){
                    tocal += count;
                    outputStream.write(data, 0 ,count);
                    int progress = ((tocal * 100) / fileLenght);
                    publishProgress(progress);
                }
                outputStream.flush();
                inputStream.close();
                outputStream.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(PlayNhacActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(PlayNhacActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            return "Tải về hoàn tất...";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressDialog.setProgress(values[0]);
//            builder.setProgress(100, values[0], false);
//            notificationManager.notify(0, builder.build());
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.hide();
//            builder.setContentText("Tải hoàn tất")
//                    .setProgress(0,0,false);
           // notificationManager.cancel(0);
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.download_view ,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_nhac);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        GetDataIntent();
        init();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createChannel();
            registerReceiver(broadcastReceiver , new IntentFilter("TRACKS_TRACKS"));
            startService(new Intent(getBaseContext() , OnClearFromRecentService.class));
        }

        CreateNotification.createNotification(PlayNhacActivity.this, mangbaihat.get(0),
                R.drawable.iconpause, 1, mangbaihat.size()-1);
        eventClick();

        setDialog();

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
                            mcountDownTimer.cancel();
                            imgPlay.setImageResource(R.drawable.iconplay);
                            if (fragment_dia_nhac.objectAnimator!=null){
                                fragment_dia_nhac.objectAnimator.pause();

                            }
                            CreateNotification.createNotification(PlayNhacActivity.this, mangbaihat.get(position),
                                    R.drawable.iconplay, position, mangbaihat.size()-1);
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

    private void eventClick() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(adapternhac.getItem(0)!= null){
                    if(mangbaihat.size()>0){
                        fragment_dia_nhac.Playnhac(mangbaihat.get(0).getSongImage());
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

    private  void onClickPlay(){
        if (mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            mcountDownTimer.cancel();
            imgPlay.setImageResource(R.drawable.iconplay);
            if (fragment_dia_nhac.objectAnimator!=null){
                fragment_dia_nhac.objectAnimator.pause();

            }
            CreateNotification.createNotification(PlayNhacActivity.this, mangbaihat.get(position),
                    R.drawable.iconplay, position, mangbaihat.size()-1);
        }else{
            mediaPlayer.start();
            if(mTime  > 100){
                setViews();
            }
            imgPlay.setImageResource(R.drawable.iconpause);
            if (fragment_dia_nhac.objectAnimator!=null){
                fragment_dia_nhac.objectAnimator.resume();
            }
            CreateNotification.createNotification(PlayNhacActivity.this, mangbaihat.get(position),
                    R.drawable.iconpause, position, mangbaihat.size()-1);
        }
    }

    private  void onClickPre(){
        if(mangbaihat.size() > 0){
            if(mediaPlayer.isPlaying() || mediaPlayer != null){
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
            if(position < (mangbaihat.size())){
                imgPlay.setImageResource(R.drawable.iconpause);
                position -- ;
                if(position < 0 ){
                    position = mangbaihat.size() - 1;
                }
                if (repeat == true){
                    position +=1;
                }
                if(checkrandom == true){
                    Random random = new  Random();
                    int index = random.nextInt(mangbaihat.size());
                    if(index == position){
                        position = index - 1;
                    }
                    position = index;
                }
                if (fragment_dia_nhac.objectAnimator!=null){
                    fragment_dia_nhac.objectAnimator.resume();
                }

                CreateNotification.createNotification(PlayNhacActivity.this, mangbaihat.get(position),
                        R.drawable.iconpause, position, mangbaihat.size()-1);

                new  PlayMp3().execute(mangbaihat.get(position).getSongLink());
                fragment_dia_nhac.Playnhac(mangbaihat.get(position).getSongImage());
                //getSupportActionBar().setTitle(mangbaihat.get(position).getSongName());
                txtTenBaiHatTenCaSi.setText(mangbaihat.get(position).getSongName() + " - "+ mangbaihat.get(position).getSongSinger());
                setBackground(mangbaihat.get(position).getSongImage());
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
        if(mangbaihat.size() > 0){
            if(mediaPlayer.isPlaying() || mediaPlayer != null){
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
            if(position < (mangbaihat.size())){
                imgPlay.setImageResource(R.drawable.iconpause);
                position ++;
                if (repeat == true){
                    if(position == 0 ){
                        position = mangbaihat.size();
                    }
                    position -=1;
                }
                if(checkrandom == true){
                    Random random = new  Random();
                    int index = random.nextInt(mangbaihat.size());
                    if(index == position){
                        position = index - 1;
                    }
                    position = index;
                }
                if(position > mangbaihat.size() - 1){
                    position = 0;
                }

                if (fragment_dia_nhac.objectAnimator!=null){
                    fragment_dia_nhac.objectAnimator.resume();
                }

                CreateNotification.createNotification(PlayNhacActivity.this, mangbaihat.get(position),
                        R.drawable.iconpause, position, mangbaihat.size()-1);

                new  PlayMp3().execute(mangbaihat.get(position).getSongLink());
                fragment_dia_nhac.Playnhac(mangbaihat.get(position).getSongImage());
                //getSupportActionBar().setTitle(mangbaihat.get(position).getSongName());
                txtTenBaiHatTenCaSi.setText(mangbaihat.get(position).getSongName() + " - "+ mangbaihat.get(position).getSongSinger());
                setBackground(mangbaihat.get(position).getSongImage());
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

    private void GetDataIntent() {
        Intent intent = getIntent();
        mangbaihat.clear();
        if(intent != null) {
            if (intent.hasExtra("cakhuc")) {
                BaiHat baiHat = intent.getParcelableExtra("cakhuc");
                mangbaihat.add(baiHat);
            }
            if (intent.hasExtra("cacbaihat")) {
                ArrayList<BaiHat> mangbaihataArrayList = intent.getParcelableArrayListExtra("cacbaihat");
                mangbaihat = mangbaihataArrayList;
            }
        }
    }

    private void init() {
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
        llnhac = findViewById(R.id.llnhac);
        setSupportActionBar(toolbarNhac);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarNhac.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                mediaPlayer.stop();
                mangbaihat.clear();
                if(mcountDownTimer != null){
                    mcountDownTimer.cancel();
                }
                notificationManager.cancelAll();
                if(mHenGio != null){
                    mHenGio.cancel();
                }
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
        if(mangbaihat.size() > 0){
            getSupportActionBar().setTitle("");
            //getSupportActionBar().setTitle(mangbaihat.get(0).getSongName());
            txtTenBaiHatTenCaSi.setText(mangbaihat.get(0).getSongName() + " - "+ mangbaihat.get(0).getSongSinger());
            setBackground(mangbaihat.get(0).getSongImage());
            new PlayMp3().execute(mangbaihat.get(0).getSongLink());
            imgPlay.setImageResource(R.drawable.iconpause);
        }
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

    @Override
    public void onChayNhac(int pos) {
        position = pos;
            CreateNotification.createNotification(PlayNhacActivity.this, mangbaihat.get(pos),
                    R.drawable.iconpause, pos, mangbaihat.size()-1);

        if(mangbaihat.size() > 0){
            if(mediaPlayer.isPlaying() || mediaPlayer != null){
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
        }
            new  PlayMp3().execute(mangbaihat.get(pos).getSongLink());
            fragment_dia_nhac.Playnhac(mangbaihat.get(pos).getSongImage());
            //getSupportActionBar().setTitle(mangbaihat.get(pos).getSongName());
            txtTenBaiHatTenCaSi.setText(mangbaihat.get(pos).getSongName() + " - "+ mangbaihat.get(pos).getSongSinger());
            setBackground(mangbaihat.get(pos).getSongImage());
            updateTime();
    }


    class PlayMp3 extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {

            return strings[0];
        }

        @Override
        protected void onPostExecute(String baihat) {
            super.onPostExecute(baihat);
            try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                }
            });
            mediaPlayer.setDataSource(baihat);
            mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.start();
            mTime = 5000;
            setViews();
            TimeSong();
            updateTime();


        }
    }

    private void  setBackground(String hinh){
        try {
            URL url= new URL(hinh);
            Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            Bitmap  bitmap1 = BlurBuilder.blur(this, bitmap);
            BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap1);

            llnhac.setBackground(bitmapDrawable);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setViews(){
        if(mcountDownTimer != null){
            mcountDownTimer.cancel();
        }
        mcountDownTimer = new CountDownTimer(mTime,100) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTime = millisUntilFinished;
            }

            @Override
            public void onFinish() {
                DataService dataService = APIService.getService();
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("idBaiHat", mangbaihat.get(position).getId());
                Call<MessageLikes> callBack = dataService.UpdateLuotView(jsonObject);
                callBack.enqueue(new Callback<MessageLikes>() {
                    @Override
                    public void onResponse(Call<MessageLikes> call, Response<MessageLikes> response) {
                        MessageLikes messageLikes = new MessageLikes();
                        messageLikes = response.body();
                        if(messageLikes.getMessage().equals("Added")){
                            Log.d("ZZZ", "Da xem");
                        }else {
                            Log.d("ZZZ", "Loi!");
                        }
                    }

                    @Override
                    public void onFailure(Call<MessageLikes> call, Throwable t) {

                    }
                });


            }
        }.start();

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
                    if(position < (mangbaihat.size())){
                        imgPlay.setImageResource(R.drawable.iconpause);
                        position ++;
                        if (repeat == true){
                            if(position == 0 ){
                                position = mangbaihat.size();
                            }
                            position -=1;
                        }
                        if(checkrandom == true){
                            Random random = new  Random();
                            int index = random.nextInt(mangbaihat.size());
                            if(index == position){
                                position = index - 1;
                            }
                            position = index;
                        }
                        if(position > mangbaihat.size() - 1){
                            position = 0;
                        }
                        new  PlayMp3().execute(mangbaihat.get(position).getSongLink());
                        fragment_dia_nhac.Playnhac(mangbaihat.get(position).getSongImage());
                        //getSupportActionBar().setTitle(mangbaihat.get(position).getSongName());
                        setBackground(mangbaihat.get(position).getSongImage());
                        txtTenBaiHatTenCaSi.setText(mangbaihat.get(position).getSongName() + " - "+ mangbaihat.get(position).getSongSinger());
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

}
