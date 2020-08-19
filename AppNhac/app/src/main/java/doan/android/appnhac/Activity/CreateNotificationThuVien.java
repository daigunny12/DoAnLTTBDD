package doan.android.appnhac.Activity;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import doan.android.appnhac.Model.BaiHat;
import doan.android.appnhac.R;
import doan.android.appnhac.Service.NotificationActionService;

public class CreateNotificationThuVien {
    public static final String CHANNEL_ID = "channel1";
    public static final String ACTIONPREVIUOS = "actionpreviuos";
    public static final String CHANNEL_PLAY = "actionplay";
    public static final String CHANNEL_NEXT = "actionnex";

    public static Notification notification;


    public static void createNotification(Context context, String baihat, int playbutton, int pos, int size){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
            MediaSessionCompat mediaSessionCompat = new MediaSessionCompat(context , "tag");

            Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.iconthuvien);

            Intent notificationIntent = new Intent(context, PlayNhacActivityThuVien.class);
            notificationIntent.addCategory(Intent. CATEGORY_LAUNCHER ) ;
            notificationIntent.setAction(Intent. ACTION_MAIN ) ;
            notificationIntent.setFlags(Intent. FLAG_ACTIVITY_CLEAR_TOP | Intent. FLAG_ACTIVITY_SINGLE_TOP );
            PendingIntent resultIntent = PendingIntent. getActivity (context, 0 , notificationIntent , 0 ) ;

            PendingIntent pendingIntentPrevious;
            int drw_previous;
//            if(pos == 0 ){
//                pendingIntentPrevious = null;
//                drw_previous = 0 ;
//            }else {
            Intent intentPrevious = new Intent(context, NotificationActionService.class)
                    .setAction(ACTIONPREVIUOS);
            pendingIntentPrevious = PendingIntent.getBroadcast(context, 0,
                    intentPrevious, PendingIntent.FLAG_CANCEL_CURRENT);
            drw_previous = R.drawable.iconpreview;
            // }


            Intent intentPlay = new Intent(context, NotificationActionService.class)
                    .setAction(CHANNEL_PLAY);
            PendingIntent pendingIntentPlay = PendingIntent.getBroadcast(context, 0,
                    intentPlay  , PendingIntent.FLAG_CANCEL_CURRENT);


            PendingIntent pendingIntentNext;
            int drw_next;
//            if(pos == size ){
//                pendingIntentNext = null;
//                drw_next = 0 ;
//            }else {
            Intent intentNext = new Intent(context, NotificationActionService.class)
                    .setAction(CHANNEL_NEXT);
            pendingIntentNext = PendingIntent.getBroadcast(context, 0,
                    intentNext, PendingIntent.FLAG_CANCEL_CURRENT);
            drw_next = R.drawable.iconnext;
            //}


            String str = baihat;
            int a = 0,b = 0,c = 0;
            for (int i = 0 ; i< str.length();i++){
                if(str.substring(i,i+1).equals("_")){
                    if(a == 0 ){
                        b = i;
                    }
                    if(a == 1){
                        c = i;
                    }
                    a += 1;
                }
            }

            if(str.substring(b, c).length()>0){
                str = str.substring(b+1, c);
            }else{
                str = "Nghệ sĩ không xác định";
            }

            notification = new NotificationCompat.Builder(context ,CHANNEL_ID)
                    .setSmallIcon(R.drawable.iconthuvien)
                    .setContentTitle(baihat)
                    .setContentText(str)
                    .setWhen(System.currentTimeMillis())
                    .setContentIntent(resultIntent)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setLargeIcon(icon)
                    .setOngoing(true)
                    .setOnlyAlertOnce(true)
                    .setShowWhen(false)
                    .addAction(drw_previous, "Previous", pendingIntentPrevious)
                    .addAction(playbutton, "Play", pendingIntentPlay)
                    .addAction(drw_next , "Next", pendingIntentNext)
                    .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                            .setShowActionsInCompactView(0,1,2)
                            .setMediaSession(mediaSessionCompat.getSessionToken()))
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .build();
            notificationManagerCompat.notify(1, notification);
        }
    }
}
