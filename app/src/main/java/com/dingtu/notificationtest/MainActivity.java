package com.dingtu.notificationtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.RemoteInput;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    PendingIntent pi;
    NotificationManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, NotificationBroadcastReceiver.class);
        intent.setAction("con.dsy.click");
        pi = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationUtils.initNotification(this);
        NotificationUtils.createNotificationChannel("2", "普通消息", NotificationManager.IMPORTANCE_HIGH);
    }

    /**
    * descirption: 4.1之前使用
    */
    public void notification() {
        Notification notification = new Notification();
        notification.icon = R.mipmap.ic_launcher;
        notification.tickerText = "hello world";
        notification.when = System.currentTimeMillis();
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        //notification.setLatestEventInfo(this,"asd","this is notification",pi);高版本已被弃用
        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1,notification);
    }

    /**
    * descirption: 只能用在4.1以上,之后被已经被NotificationCompat.Builder替代
    */
    @TargetApi(21)
    public void NotificationBuilderPattern() {
        Notification.Builder builder = new Notification.Builder(MainActivity.this)
                .setContentTitle("a")
                .setContentText("das")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pi)
                .setOngoing(true)
                .setStyle(new Notification.BigTextStyle().bigText("das"))
                .setVisibility(Notification.VISIBILITY_PUBLIC);
    }

    /**
    * descirption: 可用 兼容
    */
    public void notifiMyself() {
        Intent i = new Intent(this,NotificationBroadcastReceiver.class);
        i.setAction("com.dsy.cancle");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,1,i,PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) { //>=4.1
            builder.setSmallIcon(R.mipmap.ic_launcher);
        }
        builder.setContentTitle("丁诗仪")
                .setContentText("两周年快乐")
                .setContentIntent(pi)//设置点击后跳转
                .setAutoCancel(true)//设置点击后消失
                .setDeleteIntent(pendingIntent)//设置滑动消失
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher_round))
                .setNumber(520)
                .setOngoing(false)//是否是正在进行中的通知,如果设置为true 左右滑动就不会被删除
                .setOnlyAlertOnce(true)
                .setProgress(1000,520,false)
                .setStyle(getBigPictureStyle())
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setVibrate(new long[] {3000,1000,500,700,500,300})
                .setSubText("沈程阳")//添加一行
                .setTicker("沈程阳来信啦")
                .setWhen(System.currentTimeMillis())
                .setColor(Color.RED)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .build().notify();

    }

    public NotificationCompat.BigPictureStyle getBigPictureStyle() {
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle("你就是个乖乖，你就是个豆子！");
        bigPictureStyle.setSummaryText("你就是一个憨憨！两周年快乐！");
        bigPictureStyle.bigPicture(BitmapFactory.decodeResource(getResources(), R.drawable.a2));
        return bigPictureStyle;
    }

    public NotificationCompat.BigTextStyle getBigTextStyle() {
        String title = "我是通知的标题";
        String conttext = "我是一个通知";
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle(title);
        bigTextStyle.setSummaryText(conttext);
        bigTextStyle.bigText("一二三四五，上山打老虎！");
        return bigTextStyle;
    }

    public class NotificationBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals("com.xxx.xxx.click")) {
                //处理点击事件
                Toast.makeText(MainActivity.this, "click", Toast.LENGTH_SHORT).show();
            }

            if (action.equals("com.xxx.xxx.cancel")) {
                //处理滑动清除和点击删除事件
                Toast.makeText(MainActivity.this, "cancle", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void notifi(View view) {
        tapNotification();
    }

    public void senddefault(View view) {
        if (Build.VERSION.SDK_INT  >= Build.VERSION_CODES.O) {
            NotificationUtils.openNotificationChannel("2");
            PendingIntent intent = PendingIntent.getActivity(this, 2, new Intent(this, SecondActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationUtils.sendNotification("2", "我会跳舞哦", "你会不会呀", R.drawable.a2, R.drawable.dsy, 2, intent);
        }
    }

    int index = 0;

    /**
     * 弹出通知提醒
     */
    private void tapNotification(){
        index = index +1 ;
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        //准备intent
        Intent clickIntent = new Intent(this, NotificationBroadcastReceiver.class);
        clickIntent.setAction("com.dsy.click");
        // 构建 PendingIntent
        PendingIntent clickPI = PendingIntent.getBroadcast(this, 1, clickIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        //准备intent
        Intent cacelIntent = new Intent(this, NotificationBroadcastReceiver.class);
        cacelIntent.setAction("com.dsy.cancel");
        // 构建 PendingIntent
        PendingIntent cacelPI = PendingIntent.getBroadcast(this, 2, cacelIntent, PendingIntent.FLAG_CANCEL_CURRENT );

        //准备intent
        Intent fullscreenIntent = new Intent(this, NotificationBroadcastReceiver.class);
        fullscreenIntent.setAction("com.dsy.fullscreen");
        // 构建 PendingIntent
        PendingIntent fullscreenPI = PendingIntent.getBroadcast(this, 2, fullscreenIntent, PendingIntent.FLAG_UPDATE_CURRENT );

        String channelID = "1";

        //Notification.Builder builder = new Notification.Builder(MainActivity.this);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, channelID);

        String title = "两周年开心呀";
        String conttext = "我好想你呀！";
        //NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        //bigTextStyle.setBigContentTitle(title);
        //bigTextStyle.setSummaryText(conttext);
        //bigTextStyle.bigText("一二三西思思");
        //builder.setStyle(bigTextStyle);

        //NotificationCompat.MessagingStyle messagingStyle = new NotificationCompat.MessagingStyle("UserName");
        //messagingStyle.addMessage("message",System.currentTimeMillis(),"JulyYu");
        //messagingStyle.setConversationTitle("Messgae Title");
        //builder.setStyle(messagingStyle);


        //NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        //inboxStyle.setBigContentTitle(title);
        //inboxStyle.setSummaryText(conttext);
        //inboxStyle.addLine("A");
        //inboxStyle.addLine("B");


        //RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.activity_main);
        builder.setSmallIcon(R.drawable.a1)
                .setContentTitle(title)//设置通知标题
                .setContentText(conttext)//设置通知内容
                .setContentIntent(clickPI)// 设置pendingIntent,点击通知时就会用到
                .setAutoCancel(true)//设为true，点击通知栏移除通知
                .setDeleteIntent(cacelPI)//设置pendingIntent,左滑右滑通知时就会用到
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.dsy))//设置大图标
                .setStyle(getBigPictureStyle())
                .setNumber(index)//显示在右边的数字
                .setOngoing(false)//设置是否是正在进行中的通知，默认是false
                .setOnlyAlertOnce(false)//设置是否只通知一次
                .setProgress(100, 20, false)
                //.setStyle(messagingStyle)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setVibrate(new long[] {3000,1000,500,700,500,300})//延迟3秒，然后震动1000ms，再延迟500ms，接着震动700ms,最后再延迟500ms，接着震动300ms。
                .setLights(Color.RED,2000,Color.BLUE)
                //.setDefaults(Notification.DEFAULT_LIGHTS)
                .setSubText("我爱你")
                .setTicker("通知测试")//提示
                .setUsesChronometer(true)
                .setWhen(System.currentTimeMillis())
                .setLocalOnly(true)//设置此通知是否仅与当前设备相关。如果设置为true，通知就不能桥接到其他设备上进行远程显示。
                .setShowWhen(true);
        if(index % 5 == 0){
            builder.setSortKey("A");//设置针对一个包内的通知进行排序的键值
        }else if(index % 5 == 1){
            builder.setSortKey("B");//设置针对一个包内的通知进行排序的键值
        }else if(index % 5 == 2){
            builder.setSortKey("C");//设置针对一个包内的通知进行排序的键值
        }else if(index % 5 == 3){
            builder.setSortKey("D");//设置针对一个包内的通知进行排序的键值
        }else if(index % 5 == 4){
            builder.setSortKey("E");//设置针对一个包内的通知进行排序的键值
        }
        //响应紧急状态的全屏事件（例如来电事件），也就是说通知来的时候，跳过在通知区域点击通知这一步，直接执行fullScreenIntent代表的事件
        //builder.setFullScreenIntent(fullscreenPI, true);
        //Bundle bundle = new Bundle();
        //builder.setExtras(bundle);
        //                if(index % 2 == 0){
        //                    builder.setCategory(NotificationCompat.CATEGORY_CALL);
        //                }else if(index % 2 == 1){
        //                    builder.setCategory(NotificationCompat.CATEGORY_EMAIL);
        //                }

        builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);//悬挂通知（横幅）

        /*builder.setCustomBigContentView(remoteViews)//设置通知的布局
                .setCustomHeadsUpContentView(remoteViews)//设置悬挂通知的布局
                .setCustomContentView(remoteViews);*/
        //builder.setChronometerCountDown()//已舍弃

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//Android7.0以上

            //                if(index == 1){
            //                    builder.setGroupSummary(true);//设置是否为一组通知的第一个显示
            //                }
            //                builder.setGroup("notification_test");//捆绑通知


            //准备intent
            Intent replyPendingIntent = new Intent(this, NotificationBroadcastReceiver.class);
            replyPendingIntent.setAction("com.dsy.replypending");
            replyPendingIntent.putExtra("messageId", index);
            // 构建 PendingIntent
            PendingIntent replyPendingPI = PendingIntent.getBroadcast(this, 2, replyPendingIntent, PendingIntent.FLAG_UPDATE_CURRENT );

            builder.setRemoteInputHistory(new String[]{"这条通知可以点击下面按钮直接回复..."});
            //创建一个可添加到通知操作的 RemoteInput.Builder 实例。 该类的构造函数接受系统用作文本输入密钥的字符串。 之后，手持式设备应用使用该密钥检索输入的文本。
            RemoteInput remoteInput = new RemoteInput.Builder("key_text_reply")
                    .setLabel("回复")
                    .build();

            //使用 addRemoteInput() 向操作附加 RemoteInput 对象。
            NotificationCompat.Action action = new NotificationCompat.Action.Builder(R.mipmap.ic_launcher_round, "点击直接回复", replyPendingPI)
                    .addRemoteInput(remoteInput)
                    .build();

            //对通知应用操作。
            builder.addAction(action);

        }

        //builder.setBadgeIconType(NotificationCompat.BADGE_ICON_NONE);//设置角标类型（无效）
        //builder.setSettingsText();已舍弃
        //builder.setShortcutId("100");
        //builder.setColorized(true);//启用通知的背景颜色设置
        //builder.setColor(Color.RED);

        //builder.setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_SUMMARY);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//Android 8.0以上
            String channelName = "我是通知渠道";
            NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
            //channel.setShowBadge(true);//显示通知角标
            //boolean aa = channel.canShowBadge();
            //channel.setBypassDnd(true);// 设置绕过请勿打扰模式
            //boolean ca = channel.canBypassDnd();
            channel.enableLights(true);//设置通知出现时的闪灯（如果 android 设备支持的话）
            channel.enableVibration(true);// 设置通知出现时的震动（如果 android 设备支持的话）
            channel.setDescription("AAAAAAAAAA");//设置渠道的描述信息
            //channel.setGroup("AAAA");
            channel.setImportance(NotificationManager.IMPORTANCE_HIGH);
            channel.setLightColor(Color.YELLOW);
            //channel.setLockscreenVisibility();
            channel.setName("wweqw");
            //channel.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + "dsy"), new AudioAttributes.Builder().build());
            channel.setVibrationPattern(new long[]{200, 200, 1000, 200, 1000, 200});
            notificationManager.createNotificationChannel(channel);
            //创建通知时指定channelID
            builder.setChannelId(channelID);
            //builder.setTimeoutAfter(5000);//设置超时时间，超时之后自动取消（Android8.0有效）
        }

        Notification notification = builder.build();
        //锁屏时显示通知
        builder.setPublicVersion(notification);
        notificationManager.notify(index, notification);
    }
}
