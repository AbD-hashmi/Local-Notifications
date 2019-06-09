package stickytimer.com.linkablenotification;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    PendingIntent resultPendingIntent;
    int NOTIFICATION_ID =0;
    String CHANNEL_ID ="channel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Create an Intent for the activity you want to start
        Intent resultIntent = new Intent(MainActivity.this, ResultActivity.class);
// Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            stackBuilder = TaskStackBuilder.create(MainActivity.this);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            stackBuilder.addNextIntentWithParentStack(resultIntent);
        }
// Get the PendingIntent containing the entire back stack
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            resultPendingIntent =
                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        }




        findViewById(R.id.notification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String imageurl="http://www.graytheschoolapp.com/images/grayapp.png";
                new sendNotification(MainActivity.this,"title","message",imageurl)
                        .execute(imageurl);
            }
        });
    }


   private class sendNotification extends AsyncTask<String, Void, Bitmap> {

       private Context mContext;
       private String title, message, imageUrl;

       public sendNotification(Context context, String title, String message, String imageUrl) {
           super();
           this.mContext = context;
           this.title = title;
           this.message = message;
           this.imageUrl = imageUrl;
       }

       @Override
       protected Bitmap doInBackground(String... params) {

           InputStream in;
           try {
               URL url = new URL(this.imageUrl);
               HttpURLConnection connection = (HttpURLConnection) url.openConnection();
               connection.setDoInput(true);
               connection.connect();
               in = connection.getInputStream();
               Bitmap myBitmap = BitmapFactory.decodeStream(in);
               return myBitmap;
           } catch (MalformedURLException e) {
               e.printStackTrace();
           } catch (IOException e) {
               e.printStackTrace();
           }
           return null;
       }

       @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
       @Override
       protected void onPostExecute(Bitmap result) {
           super.onPostExecute(result);
           Intent intent = new Intent(mContext, ResultActivity.class);
           intent.putExtra("key", "value");
           PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 100, intent, PendingIntent.FLAG_ONE_SHOT);

           NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        createNotificationChannel();
           Notification notif = null;
           if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
               notif = new Notification.Builder(mContext,CHANNEL_ID)
                       .setContentTitle(title)
                       .setContentText(message)
                       .setSmallIcon(R.mipmap.ic_launcher)
                       .setLargeIcon(result)
                       .setContentIntent(pendingIntent)
                       .setStyle(new Notification.BigPictureStyle().bigPicture(result))
                       .build();
           }




           notif.flags |= Notification.FLAG_AUTO_CANCEL;
           notificationManager.notify(1, notif);

           /*otificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(MainActivity.this,CHANNEL_ID);

           notificationBuilder.setAutoCancel(true)
                   .setDefaults(Notification.DEFAULT_ALL)
                   .setWhen(System.currentTimeMillis())
                   .setSmallIcon(R.mipmap.ic_launcher)
                   .setTicker("Dilip21")
                   .setStyle(new Notification.BigPictureStyle().bigPicture(result))
                   .setContentTitle("Default notification")
                   .setContentText("Lorem ipsum dolor sit amet, consectetur adipiscing elit.")
                   .setContentInfo("Info")
                   .setContentIntent(resultPendingIntent);
*/




       }

       private void createNotificationChannel() {
           // Create the NotificationChannel, but only on API 26+ because
           // the NotificationChannel class is new and not in the support library
           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
               CharSequence name = " sssd";
               String description = "sda";
               int importance = NotificationManager.IMPORTANCE_DEFAULT;
               NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
               channel.setDescription(description);

               channel.enableLights(true);
               channel.setLightColor(Color.RED);
               channel.enableVibration(true);
               channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
               channel.setShowBadge(false);
               // Register the channel with the system; you can't change the importance
               // or other notification behaviors after this
               NotificationManager notificationManager = getSystemService(NotificationManager.class);
               notificationManager.createNotificationChannel(channel);
           }
       }
   }
}

/*
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {


                    CHANNEL_ID = "my_channel_01";
                    CharSequence name = "my_channel";
                    String Description = "This is my channel";
                    int importance = NotificationManager.IMPORTANCE_DEFAULT;
                    NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                    mChannel.setDescription(Description);
                    mChannel.enableLights(true);
                    mChannel.setLightColor(Color.RED);
                    mChannel.enableVibration(true);
                    mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                    mChannel.setShowBadge(false);
                }


                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(MainActivity.this,CHANNEL_ID);

                notificationBuilder.setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setTicker("Dilip21")
                        .setContentTitle("Default notification")
                        .setContentText("Lorem ipsum dolor sit amet, consectetur adipiscing elit.")
                        .setContentInfo("Info")
                        .setContentIntent(resultPendingIntent);




                notificationManager = (NotificationManager) MainActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(1, notificationBuilder.build());


            }*/