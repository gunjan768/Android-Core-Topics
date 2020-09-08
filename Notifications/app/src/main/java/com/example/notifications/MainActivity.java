package com.example.notifications;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.RemoteInput;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private NotificationManagerCompat notificationManagerCompat;
    private EditText editTextTitle, editTextMessage;
    private MediaSessionCompat mediaSessionCompat;
    static List<Message> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeInitialMessages();
        notificationManagerCompat = NotificationManagerCompat.from(this);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextMessage = findViewById(R.id.edit_text_message);
        mediaSessionCompat = new MediaSessionCompat(this, "Tag");
    }

    private void initializeInitialMessages()
    {
        messages = new ArrayList<>();

        messages.add(new Message("Good Morning", "Emilia"));

        // See in place of sender name we passed null because it is the message by me hence we don't want any name. When sender name is seen as null it will add
        // "me" in place of it ( see bottom below codes to get how ).
        messages.add(new Message("Hello", null));

        messages.add(new Message("Hi", "Gunjan"));
    }

    public void sendChannelOne(View view)
    {
        String title = editTextTitle.getText().toString();
        String message = editTextMessage.getText().toString();

        Intent activityIntent = new Intent(this, MainActivity.class);

        // We will open the 'activity' when the user will click on the notification hence we used getActivity(). There are many other options available also.
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, activityIntent, 0);

        Intent broadcastIntent = new Intent(this, NotificationReceiver.class);
        broadcastIntent.putExtra("toastMessage", message);

        // Here we are passing the flag "PendingIntent.FLAG_UPDATE_CURRENT" because we want to update the current message and overwrite the old one.
        PendingIntent actionIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Below line is used to convert the image into bitmap.
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.gunjan);

        // getCroppedBitmap() will return the cropped bitmap which will be in the shape of the circle.
        largeIcon = getCroppedBitmap(largeIcon);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // Here no need to for the lower version of android ( lower than android ) as 'App.CHANNEL_1_ID' will be ignored for the lower version of the android.
        Notification notification = new NotificationCompat.Builder(this, App.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.rotate)
                .setContentTitle(title)
                .setContentText(message)
                .setLargeIcon(largeIcon)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(getString(R.string.long_dummy_text))
                        .setBigContentTitle("Big content Title")
                        .setSummaryText("Summary Text"))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setColor(Color.RED)
                .setContentIntent(contentIntent)
                .addAction(R.mipmap.ic_launcher, "Toast", actionIntent)
                .setAutoCancel(true)      // It will dismiss the notification on opening it.
                .setOnlyAlertOnce(true)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setLights(Color.RED, 3000, 3000)
                .setSound(Uri.parse("uri://sadfasdfasdf.mp3"))
                .build();

        // If you want to push multiple notifications at the same time and want all of them to be stacked down ( i.e to show all of them ) then provide the
        // different id for different notification else it will overwrite the old one.
        notificationManagerCompat.notify(1, notification);
    }

    public void sendChannelTwo(View view)
    {
        String title = editTextTitle.getText().toString();
        String message = editTextMessage.getText().toString();

        // Here no need to for the lower version of android ( lower than android ) as 'App.CHANNEL_1_ID' will be ignored for the lower version of the android.
        Notification notification = new NotificationCompat.Builder(this, App.CHANNEL_2_ID)
                .setSmallIcon(R.drawable.home)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(new NotificationCompat.InboxStyle()
                        .addLine("This is line 1")      // Though you can add as much lines as you want but only first seven lines will be visible ( or may depend
                        .addLine("This is line 2")      // on the android version the user is using )
                        .addLine("This is line 3")
                        .addLine("This is line 4")
                        .addLine("This is line 5")
                        .addLine("This is line 6")
                        .addLine("This is line 7")
                        .addLine("This is line 8")
                        .setSummaryText("Summary Text"))
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        // If you want to push multiple notifications at the same time and want all of them to be stacked down ( i.e to show all of them ) then provide the
        // different id for different notification else it will overwrite the old one.
        notificationManagerCompat.notify(2, notification);
    }

    public void sendChannelThree(View viw)
    {
        String title = editTextTitle.getText().toString();
        String message = editTextMessage.getText().toString();

        Intent activityIntent = new Intent(this, MainActivity.class);

        // We will open the 'activity' when the user will click on the notification hence we used getActivity(). There are many other options available also.
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, activityIntent, 0);

        // Below line is used to convert the image into bitmap.
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.gunjan);
        Bitmap picture = BitmapFactory.decodeResource(getResources(), R.drawable.dora);

        // getCroppedBitmap() will return the cropped bitmap which will be in the shape of the circle.
        largeIcon = getCroppedBitmap(largeIcon);

        // Here no need to for the lower version of android ( lower than android ) as 'App.CHANNEL_1_ID' will be ignored for the lower version of the android.
        Notification notification = new NotificationCompat.Builder(this, App.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.rotate)
                .setContentTitle(title)
                .setContentText(message)
                .setLargeIcon(largeIcon)
                .setStyle(new NotificationCompat.BigPictureStyle()
                    .bigPicture(picture)
                    .bigLargeIcon(null))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setColor(Color.RED)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)      // It will dismiss the notification on opening it.
                .setOnlyAlertOnce(true)
                .build();

        // If you want to push multiple notifications at the same time and want all of them to be stacked down ( i.e to show all of them ) then provide the
        // different id for different notification else it will overwrite the old one.
        notificationManagerCompat.notify(1, notification);
    }

    public void sendChannelFour(View view)
    {
        String title = editTextTitle.getText().toString();
        String message = editTextMessage.getText().toString();

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.gunjan);

        // Here no need to for the lower version of android ( lower than android ) as 'App.CHANNEL_1_ID' will be ignored for the lower version of the android.
        Notification notification = new NotificationCompat.Builder(this, App.CHANNEL_2_ID)
                .setSmallIcon(R.drawable.home)
                .setContentTitle(title)
                .setContentText(message)
                .setLargeIcon(largeIcon)
                .addAction(R.drawable.thumb_down, "Dislike", null)
                .addAction(R.drawable.skip, "Previous", null)
                .addAction(R.drawable.pause, "Pause", null)
                .addAction(R.drawable.next, "Next", null)
                .addAction(R.drawable.thumb_up, "Like", null)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                    .setShowActionsInCompactView(1, 2, 3)
                    .setMediaSession(mediaSessionCompat.getSessionToken()))
                .setSubText("Sub Text")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        // If you want to push multiple notifications at the same time and want all of them to be stacked down ( i.e to show all of them ) then provide the
        // different id for different notification else it will overwrite the old one.
        notificationManagerCompat.notify(2, notification);
    }

    public void sendChannelFive(View view)
    {
        String title = editTextTitle.getText().toString();
        String message = editTextMessage.getText().toString();

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.gunjan);

        // Here no need to for the lower version of android ( lower than android ) as 'App.CHANNEL_1_ID' will be ignored for the lower version of the android.
        Notification notification = new NotificationCompat.Builder(this, App.CHANNEL_2_ID)
                .setSmallIcon(R.drawable.home)
                .setContentTitle(title)
                .setContentText(message)
                .setLargeIcon(largeIcon)
                .addAction(R.drawable.thumb_down, "Dislike", null)
                .addAction(R.drawable.skip, "Previous", null)
                .addAction(R.drawable.pause, "Pause", null)
                .addAction(R.drawable.next, "Next", null)
                .addAction(R.drawable.thumb_up, "Like", null)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(1, 2, 3))
                .setSubText("Sub Text")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        // If you want to push multiple notifications at the same time and want all of them to be stacked down ( i.e to show all of them ) then provide the
        // different id for different notification else it will overwrite the old one.
        notificationManagerCompat.notify(2, notification);
    }

    public void sendChannelSix(View view)
    {
        sendChannelSixNotification(this);
    }

    public static void sendChannelSixNotification(Context context)
    {
        Intent activityIntent = new Intent(context, MainActivity.class);

        // We will open the 'activity' when the user will click on the notification hence we used getActivity(). There are many other options available also.
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, activityIntent, 0);

        // RemoteInput is the input field for the notification.
        RemoteInput remoteInput = new RemoteInput.Builder("key_text_reply")
                .setLabel("Your answer...")
                .build();

        Intent replyIntent;
        PendingIntent replyPendingIntent = null;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            replyIntent = new Intent(context, DirectReplyReceiver.class);
            replyPendingIntent = PendingIntent.getBroadcast(context, 0, replyIntent, 0);
        }
        else
        {
            // Start the chat activity instead ( PendingIntent.getActivity ) and also cancel the notification with NotificationManagerCompat.cancel(id).
        }

        NotificationCompat.Action replyAction = new NotificationCompat.Action.Builder(R.drawable.thumb_up, "Reply", replyPendingIntent)
                .addRemoteInput(remoteInput)
                .build();

        NotificationCompat.MessagingStyle messagingStyle = new NotificationCompat.MessagingStyle("Me");
        messagingStyle.setConversationTitle("Group Chat");

        for(Message chatMessage : messages)
        {
            NotificationCompat.MessagingStyle.Message notificationMessage = new NotificationCompat.MessagingStyle.Message(
                    chatMessage.getText(), chatMessage.getTimeStamp(), chatMessage.getSender()
            );

            messagingStyle.addMessage(notificationMessage);
        }

        // Here no need to for the lower version of android ( lower than android ) as 'App.CHANNEL_1_ID' will be ignored for the lower version of the android.
        Notification notification = new NotificationCompat.Builder(context, App.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.rotate)
                .setStyle(messagingStyle)
                .addAction(replyAction)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setColor(Color.GREEN)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)      // It will dismiss the notification on opening it.
                .setOnlyAlertOnce(true)
                .build();

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(6, notification);
    }

    public void sendChannelSeven(View view)
    {
        final int progressMax = 100, progressStart = 0;

        // Here no need to for the lower version of android ( lower than android ) as 'App.CHANNEL_1_ID' will be ignored for the lower version of the android.
        final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, App.CHANNEL_2_ID)
                .setSmallIcon(R.drawable.home)
                .setContentTitle("Download")
                .setContentText("Download in progress...")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOngoing(true)
                .setColor(Color.BLUE)
                .setProgress(progressMax, progressStart, false);  // If you see set intermediate to true then you will see on-forth progress bar.

        // If you want to push multiple notifications at the same time and want all of them to be stacked down ( i.e to show all of them ) then provide the
        // different id for different notification else it will overwrite the old one.
        notificationManagerCompat.notify(7, notificationBuilder.build());

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                SystemClock.sleep(2000);

                for(int progress = 0; progress <= progressMax; progress += 10)
                {
                    notificationBuilder.setProgress(progressMax, progress, false);

                    notificationManagerCompat.notify(7, notificationBuilder.build());

                    SystemClock.sleep(1000);
                }

                notificationBuilder
                        .setContentText("Download Finished")
                        .setProgress(0, 0, false)
                        .setOngoing(false);

                notificationManagerCompat.notify(7, notificationBuilder.build());
            }
        }).start();
    }

    // Grouping notification will work only for android version less than Nougat. For higher version there is a built-in grouping notification.
    public void sendChannelEight(View view)
    {
        final String title1 = "Emilia", title2 = "Gunjan";
        final String message1 = "Emilia loves him", message2 = "Gunjan loves her";

        Notification notification1 = new NotificationCompat.Builder(this, App.CHANNEL_2_ID)
                .setSmallIcon(R.drawable.home)
                .setContentTitle(title1)
                .setContentText(message1)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setColor(Color.BLUE)
                .setGroup("Prem Pavitra Hai")
                .build();

        Notification notification2 = new NotificationCompat.Builder(this, App.CHANNEL_2_ID)
                .setSmallIcon(R.drawable.home)
                .setContentTitle(title2)
                .setContentText(message2)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setColor(Color.RED)
                .setGroup("Prem Pavitra Hai")
                .build();

        Notification summaryNotification = new NotificationCompat.Builder(this, App.CHANNEL_2_ID)
                .setSmallIcon(R.drawable.gunjan)
                .setStyle(new NotificationCompat.InboxStyle()
                    .addLine(title2+ " " + message2)
                    .addLine(title1 + " " + message1)
                    .setBigContentTitle("2 new messages")
                    .setSummaryText("All about love"))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setColor(Color.RED)
                .setGroup("Prem Pavitra Hai")
                .setGroupSummary(true)
                .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_CHILDREN)         // Only children will make noise.
                .build();

        SystemClock.sleep(2000);
        notificationManagerCompat.notify(8, notification1);

        SystemClock.sleep(2000);
        notificationManagerCompat.notify(9, notification2);

        SystemClock.sleep(2000);
        notificationManagerCompat.notify(10, summaryNotification);
    }

    public void sendChannelNine(View view)
    {
        // This is if user disable the notification as a whole.
        if(!notificationManagerCompat.areNotificationsEnabled())
        {
            openNotificationSettings();

            return;
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && isChannelBlocked(App.CHANNEL_1_ID))
        {
            openChannelSettings(App.CHANNEL_1_ID);

            return;
        }

        sendChannelNineNotification(this);
    }

    public void sendChannelNineNotification(Context context)
    {
        String title = editTextTitle.getText().toString();
        String message = editTextMessage.getText().toString();

        Intent activityIntent = new Intent(this, MainActivity.class);

        // We will open the 'activity' when the user will click on the notification hence we used getActivity(). There are many other options available also.
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, activityIntent, 0);

        Intent broadcastIntent = new Intent(this, NotificationReceiver.class);
        broadcastIntent.putExtra("toastMessage", message);

        // Here we are passing the flag "PendingIntent.FLAG_UPDATE_CURRENT" because we want to update the current message and overwrite the old one.
        PendingIntent actionIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Below line is used to convert the image into bitmap.
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.gunjan);

        // getCroppedBitmap() will return the cropped bitmap which will be in the shape of the circle.
        largeIcon = getCroppedBitmap(largeIcon);

        // Here no need to for the lower version of android ( lower than android ) as 'App.CHANNEL_1_ID' will be ignored for the lower version of the android.
        Notification notification = new NotificationCompat.Builder(this, App.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.rotate)
                .setContentTitle(title)
                .setContentText(message)
                .setLargeIcon(largeIcon)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(getString(R.string.long_dummy_text))
                        .setBigContentTitle("Big content Title")
                        .setSummaryText("Summary Text"))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setColor(Color.RED)
                .setContentIntent(contentIntent)
                .addAction(R.mipmap.ic_launcher, "Toast", actionIntent)
                .setAutoCancel(true)      // It will dismiss the notification on opening it.
                .setOnlyAlertOnce(true)
                .build();

        // If you want to push multiple notifications at the same time and want all of them to be stacked down ( i.e to show all of them ) then provide the
        // different id for different notification else it will overwrite the old one.
        notificationManagerCompat.notify(9, notification);
    }

    public void deleteChannel(View view)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationManager notificationManager = getSystemService(NotificationManager.class);

            // To delete the specific channel.
            notificationManager.deleteNotificationChannel(App.CHANNEL_3_ID);

            // To delete the specific group ( i.e group of channels ).
            //notificationManager.deleteNotificationChannelGroup(App.GROUP_1_ID);
        }
    }

    private void openNotificationSettings()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());

            startActivity(intent);
        }
        else
        {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));

            startActivity(intent);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean isChannelBlocked(String channelId)
    {
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        NotificationChannel notificationChannel = notificationManager.getNotificationChannel(channelId);

        // NotificationManager.IMPORTANCE_NONE means notification is disabled.
        return notificationChannel !=null && notificationChannel.getImportance() == NotificationManager.IMPORTANCE_NONE;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void openChannelSettings(String channelId)
    {
        Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
        intent.putExtra(Settings.EXTRA_CHANNEL_ID, channelId);

        startActivity(intent);
    }

    public void sendCustomNotification(View view)
    {
        // The part of the android system that displays notification is not in our own process, it's in another process. The thing is that we can access this
        // other process may be because of security issue and in order to pass out the layout to other process we need the RemoteViews. Remember that
        // RemoteViews doesn't support Constraint layout.
        RemoteViews collapseView = new RemoteViews(getPackageName(), R.layout.notification_collapsed);
        RemoteViews expandedView = new RemoteViews(getPackageName(), R.layout.notification_expanded);

        Intent clickIntent = new Intent(this, CustomNotificationReceiver.class);
        PendingIntent clickPendingIntent = PendingIntent.getBroadcast(this, 0, clickIntent, 0);

        collapseView.setTextViewText(R.id.text_view_collapsed_1, "Hello Emilia");

        expandedView.setImageViewResource(R.id.image_view_expanded, R.drawable.gunjan);

        // This is equivalent to set on click listener in RemoteViews.
        expandedView.setOnClickPendingIntent(R.id.image_view_expanded, clickPendingIntent);

        // To make the image clickable we normally attach onClick method but since it's not in our process hence we need to use PendingIntent. PendingIntent
        // allows the another app to execute our code on our behalf. PendingIntent can trigger broadcastReceiver or start an activity or service.
        Notification notification = new NotificationCompat.Builder(this, App.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.rotate)
                .setContentTitle("Title")
                .setContentText("This is custom notification")
                .setCustomContentView(collapseView)
                .setCustomBigContentView(expandedView)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .build();

        notificationManagerCompat.notify(10, notification);
    }

    public Bitmap getCroppedBitmap(Bitmap bitmap)
    {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);

        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        // Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);

        return output;
    }
}