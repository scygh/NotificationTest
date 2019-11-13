package com.dingtu.notificationtest;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.provider.Settings;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

/**
 * description ：
 * author : scy
 * email : 1797484636@qq.com
 * date : 2019/11/13 13:44
 */
public class NotificationUtils {

    private static Context mContext;
    private static NotificationManager manager;

    /**
     * descirption: 初始化
     */
    public static NotificationUtils initNotification(Context context) {
        mContext = context;
        manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        return new NotificationUtils();
    }

    /**
     * descirption: 创建通知渠道 （调用前必须，判断是版本否是大于26）
     * channelId 可以随意但是必须全局唯一
     * channelName 展示给用户
     * importance
     * NotificationManager.IMPORTANCE_HIGH; 4
     * NotificationManager.IMPORTANCE_DEFAULT 3
     * NotificationManager.IMPORTANCE_LOW 2
     * NotificationManager.IMPORTANCE_MAX 5
     * NotificationManager.IMPORTANCE_MIN 1
     * NotificationManager.IMPORTANCE_NONE 0
     * NotificationManager.IMPORTANCE_UNSPECIFIED -1000
     */
    @TargetApi(26)
    public static void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        channel.setShowBadge(true);
        manager.createNotificationChannel(channel);
    }

    /**
    * descirption: 删除渠道
    */
    @TargetApi(26)
    public static void deleteNotificationChannel(String channelId) {
        manager.deleteNotificationChannel(channelId);
    }

    /**
     * descirption: 查询渠道是否打开
     */
    @TargetApi(26)
    public static void openNotificationChannel(String channelId) {
        if (manager.getNotificationChannel(channelId).getImportance() == NotificationManager.IMPORTANCE_NONE) {
            Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, mContext.getPackageName());
            intent.putExtra(Settings.EXTRA_CHANNEL_ID, channelId);
            mContext.startActivity(intent);
            Toast.makeText(mContext, "请手动将通知打开", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * descirption: 发送一条通知
     */
    public static void sendNotification(String channelId, String contentTitle, String contentText, int smallIcon, int largeIcon, int notifyId, PendingIntent pendingIntent) {
        Notification notification = new NotificationCompat.Builder(mContext, channelId)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(smallIcon)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), largeIcon))
                .setAutoCancel(true)
                .setNumber(3)
                .build();
        manager.notify(notifyId, notification);
    }
}
