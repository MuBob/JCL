package com.jcl.android.getui;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;
import com.igexin.sdk.message.FeedbackCmdMessage;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.igexin.sdk.message.SetTagCmdMessage;
import com.jcl.android.R;
import com.jcl.android.activity.MessageActivity;
import com.jcl.android.obj.TouchuanMsg;

/**
 * 继承 GTIntentService 接收来自个推的消息, 所有消息在线程中回调, 如果注册了该服务, 则务必要在 AndroidManifest中声明, 否则无法接受消息<br>
 * onReceiveMessageData 处理透传消息<br>
 * onReceiveClientId 接收 cid <br>
 * onReceiveOnlineState cid 离线上线通知 <br>
 * onReceiveCommandResult 各种事件处理回执 <br>
 */
public class DemoIntentService extends GTIntentService {

    private static final String TAG = "GetuiSdkDemo";

    /**
     * 为了观察透传数据变化.
     */
    private static int cnt;
    public DemoIntentService() {

    }
    @Override
    public void onReceiveServicePid(Context context, int pid) {
        Log.d(TAG, "onReceiveServicePid -> " + pid);
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {
        String appid = msg.getAppid();
        String taskid = msg.getTaskId();
        String messageid = msg.getMessageId();
        byte[] payload = msg.getPayload();
        String pkg = msg.getPkgName();
        String cid = msg.getClientId();

        // 第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
        boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90001);
        Log.d(TAG, "call sendFeedbackMessage = " + (result ? "success" : "failed"));
        Log.d(TAG, "onReceiveMessageData -> " + "appid = " + appid + "\ntaskid = " + taskid + "\nmessageid = " + messageid + "\npkg = " + pkg
                + "\ncid = " + cid);
        if (payload == null) {
            Log.e(TAG, "receiver payload = null");
        } else {
            String data = new String(payload);
            Log.d(TAG, "receiver payload = " + data);
            showNotification(data,context);
        }
    }

    @Override
    public void onReceiveClientId(Context context, String clientid) {
        Log.e(TAG, "onReceiveClientId -> " + "clientid = " + clientid);
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean online) {
        Log.d(TAG, "onReceiveOnlineState -> " + (online ? "online" : "offline"));
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage cmdMessage) {
        Log.d(TAG, "onReceiveCommandResult -> " + cmdMessage);

        int action = cmdMessage.getAction();

        if (action == PushConsts.SET_TAG_RESULT) {
            setTagResult((SetTagCmdMessage) cmdMessage);
        } else if ((action == PushConsts.THIRDPART_FEEDBACK)) {
            feedbackResult((FeedbackCmdMessage) cmdMessage);
        }
    }

    private void setTagResult(SetTagCmdMessage setTagCmdMsg) {
        String sn = setTagCmdMsg.getSn();
        String code = setTagCmdMsg.getCode();

        String text = "设置标签失败, 未知异常";
        switch (Integer.valueOf(code)) {
            case PushConsts.SETTAG_SUCCESS:
                text = "设置标签成功";
                break;

            case PushConsts.SETTAG_ERROR_COUNT:
                text = "设置标签失败, tag数量过大, 最大不能超过200个";
                break;

            case PushConsts.SETTAG_ERROR_FREQUENCY:
                text = "设置标签失败, 频率过快, 两次间隔应大于1s且一天只能成功调用一次";
                break;

            case PushConsts.SETTAG_ERROR_REPEAT:
                text = "设置标签失败, 标签重复";
                break;

            case PushConsts.SETTAG_ERROR_UNBIND:
                text = "设置标签失败, 服务未初始化成功";
                break;

            case PushConsts.SETTAG_ERROR_EXCEPTION:
                text = "设置标签失败, 未知异常";
                break;

            case PushConsts.SETTAG_ERROR_NULL:
                text = "设置标签失败, tag 为空";
                break;

            case PushConsts.SETTAG_NOTONLINE:
                text = "还未登陆成功";
                break;

            case PushConsts.SETTAG_IN_BLACKLIST:
                text = "该应用已经在黑名单中,请联系售后支持!";
                break;

            case PushConsts.SETTAG_NUM_EXCEED:
                text = "已存 tag 超过限制";
                break;

            default:
                break;
        }

        Log.d(TAG, "settag result sn = " + sn + ", code = " + code + ", text = " + text);
    }

    private void feedbackResult(FeedbackCmdMessage feedbackCmdMsg) {
        String appid = feedbackCmdMsg.getAppid();
        String taskid = feedbackCmdMsg.getTaskId();
        String actionid = feedbackCmdMsg.getActionId();
        String result = feedbackCmdMsg.getResult();
        long timestamp = feedbackCmdMsg.getTimeStamp();
        String cid = feedbackCmdMsg.getClientId();

        Log.d(TAG, "onReceiveCommandResult -> " + "appid = " + appid + "\ntaskid = " + taskid + "\nactionid = " + actionid + "\nresult = " + result
                + "\ncid = " + cid + "\ntimestamp = " + timestamp);
    }

    private boolean keep=true;
//    public static List<TouchuanMsg> touchuanMsgList=new ArrayList<TouchuanMsg>();
    public static TouchuanMsg touchuanMsg;
    @SuppressLint("NewApi")
	private void showNotification(String data,Context context){  
		final int NOTIFICATION_FLAG = 1;  
		// 在Android进行通知处理，首先需要重系统哪里获得通知管理器NotificationManager，它是一个系统Service。  
//        ActivityManager activitymanager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        RunningTaskInfo info = activitymanager.getRunningTasks(1).get(0);
//        String shortClassName = info.topActivity.getShortClassName();    //类名
//        Log.i("ClassName", "shortClassName===="+shortClassName);
        
        if(!TextUtils.isEmpty(data))
        {
        	touchuanMsg = new Gson().fromJson(data, TouchuanMsg.class);
//        	touchuanMsgList.add(touchuanMsg);  
        	
        	 PendingIntent pendingIntent3 = null;
//          if(TextUtils.equals(shortClassName, ".activity.HomeActivity"))
//          {
//        	  //Activity存在，点击发送广播   选项卡跳转
//        	  Intent intent = new Intent();
//        	  Intent intent = new Intent(DemoIntentService.this,HomeActivity.class);
//        	  intent.setAction("touchuan");
//        	  pendingIntent3 = PendingIntent.getBroadcast(this, 0, intent, 0);
//          }else{
          
          NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);  
          Intent intent = new Intent(DemoIntentService.this,MessageActivity.class);
          intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//          intent.putExtra("tongzhi", "tongzhi");
          pendingIntent3 = PendingIntent.getActivity(this, 0,  
   				 intent, PendingIntent.FLAG_UPDATE_CURRENT);  
//          }
            // 通过Notification.Builder来创建通知，注意API Level  
            // API16之后才支持  
           Notification notify3 = null;
           notify3 = new Notification.Builder(this)  
           .setSmallIcon(R.drawable.logo)  
           .setTicker(touchuanMsg.getTitle())  
           .setContentTitle(touchuanMsg.getTitle())  
           .setContentText(touchuanMsg.getContent())  
           .setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.notice))
           .setContentIntent(pendingIntent3).
           setNumber(1).build(); // 需要注意build()是在API  
            notify3.defaults = Notification.DEFAULT_VIBRATE;//默认震动
            // level16及之后增加的，API11可以使用getNotificatin()来替代  
            notify3.flags |= Notification.FLAG_AUTO_CANCEL; // FLAG_AUTO_CANCEL表明当通知被用户点击时，通知将被清除。  
            // 通知id需要唯一，要不然会覆盖前一条通知  
		    int notifyId = (int) System.currentTimeMillis();  
            manager.notify(notifyId, notify3);// 步骤4：通过通知管理器来发起通知。如果id不同，则每click，在status哪里增加一个提示  

        } 
    }  
}
