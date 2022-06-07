package cn.jiguang.demo.jpush;

import android.app.AppOpsManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import cn.jiguang.demo.R;
import cn.jiguang.demo.baselibrary.ToastHelper;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.data.JPushLocalNotification;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Copyright(c) 2020 极光
 * Description
 */
public class MsgUtils {
    private MsgUtils() {
    }

    public static void buildLocalNotification(Context context, String tittle, String content){
        JPushLocalNotification ln = new JPushLocalNotification();
        ln.setBuilderId(0);
        ln.setContent(content);
        ln.setTitle(tittle);
        long id = System.currentTimeMillis()/1000;
        ln.setNotificationId(id) ;
        ln.setBroadcastTime(System.currentTimeMillis());
        Map<String , Object> map = new HashMap<String, Object>() ;
        map.put("name", "jpush") ;
        map.put("test", "111") ;
        JSONObject json = new JSONObject(map) ;
        ln.setExtras(json.toString()) ;
        JPushInterface.addLocalNotification(context, ln);
        ToastHelper.showOk(context, "添加本地通知，notification_id:" + id);
    }

}
