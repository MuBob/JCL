package com.jcl.android.push;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import com.baidu.speechsynthesizer.SpeechSynthesizer;
import com.baidu.speechsynthesizer.SpeechSynthesizerListener;
import com.baidu.speechsynthesizer.publicutility.DataInfoUtils;
import com.baidu.speechsynthesizer.publicutility.SpeechError;
import com.baidu.speechsynthesizer.publicutility.SpeechLogger;
import com.jcl.android.C;
import com.jcl.android.R;
import com.jcl.android.SP;
import com.jcl.android.activity.DetailFindActivity;
import com.jcl.android.application.JCLApplication;
import com.jcl.android.getui.DemoIntentService;
import com.jcl.android.getui.PushBean;
import com.jcl.android.obj.TouchuanMsg;
import com.jcl.android.utils.SharePerfUtil;
import com.jcl.android.view.MyToast;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SpeechPushInfoActivity extends Activity implements  SpeechSynthesizerListener ,OnClickListener{

    protected static final int UI_LOG_TO_VIEW = 0;
    protected static final int UI_TOAST = 1;
    private static SpeechSynthesizer speechSynthesizer;
    private static TextView tv_content;
    private Handler uiHandler;
    private Toast mToast;
    private ImageView btn_back;
    private TextView tv_qiangdan;
    private static TextView tv_clock;
    
    /** 指定license路径，需要保证该路径的可读写权限 */
    private static final String LICENCE_FILE_NAME = Environment.getExternalStorageDirectory()
            + "/tts/baidu_tts_licence.dat";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.logStringCache = Utils.getLogText(getApplicationContext());

        Resources resource = this.getResources();
        String pkgName = this.getPackageName();
        
        
        
        setContentView(R.layout.activity_push_info);
        
        
        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_clock=(TextView) findViewById(R.id.tv_clock);
        tv_qiangdan=(TextView) findViewById(R.id.tv_qiangdan);
        btn_back=(ImageView)findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyPushMessageReceiver.touchuanMsgList.clear();
				finish();
			}
		});
        tv_qiangdan.setOnClickListener(this);
        tv_content.setMovementMethod(new ScrollingMovementMethod());
        
         
        tv_content.setText(Utils.title+Utils.description);

        System.loadLibrary("gnustl_shared");
        // 部分版本不需要BDSpeechDecoder_V1
        try {
            System.loadLibrary("BDSpeechDecoder_V1");
        } catch (UnsatisfiedLinkError e) {
            SpeechLogger.logD("load BDSpeechDecoder_V1 failed, ignore");
        }
        System.loadLibrary("bd_etts");
        System.loadLibrary("bds");

        if (!new File(LICENCE_FILE_NAME).getParentFile().exists()) {
            new File(LICENCE_FILE_NAME).getParentFile().mkdirs();
        }
        // 复制license到指定路径
        InputStream licenseInputStream = getResources().openRawResource(R.raw.temp_license_2015_07_03);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(LICENCE_FILE_NAME);
            byte[] buffer = new byte[1024];
            int size = 0;
            while ((size = licenseInputStream.read(buffer, 0, 1024)) >= 0) {
                SpeechLogger.logD("size written: " + size);
                fos.write(buffer, 0, size);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                licenseInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        speechSynthesizer =
                SpeechSynthesizer.newInstance(SpeechSynthesizer.SYNTHESIZER_AUTO, getApplicationContext(), "holder",
                        this);
        // 请替换为语音开发者平台注册应用得到的apikey和secretkey (在线授权)
        speechSynthesizer.setApiKey("BfmFFNsUE50TKDbehG6QwuSw", "Zga2cqBTOOdrHFkRs8b31uzmKSeyDhkY");
        // 请替换为语音开发者平台上注册应用得到的App ID (离线授权)
        speechSynthesizer.setAppId("6369834");
        // 设置授权文件路径
        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_LICENCE_FILE, LICENCE_FILE_NAME);
        // TTS所需的资源文件，可以放在任意可读目录，可以任意改名
        String ttsTextModelFilePath =
                getApplicationContext().getApplicationInfo().dataDir + "/lib/libbd_etts_text.dat.so";
        String ttsSpeechModelFilePath =
                getApplicationContext().getApplicationInfo().dataDir + "/lib/libbd_etts_speech_female.dat.so";
        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, ttsTextModelFilePath);
        speechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, ttsSpeechModelFilePath);
        DataInfoUtils.verifyDataFile(ttsTextModelFilePath);
        DataInfoUtils.getDataFileParam(ttsTextModelFilePath, DataInfoUtils.TTS_DATA_PARAM_DATE);
        DataInfoUtils.getDataFileParam(ttsTextModelFilePath, DataInfoUtils.TTS_DATA_PARAM_SPEAKER);
        DataInfoUtils.getDataFileParam(ttsTextModelFilePath, DataInfoUtils.TTS_DATA_PARAM_GENDER);
        DataInfoUtils.getDataFileParam(ttsTextModelFilePath, DataInfoUtils.TTS_DATA_PARAM_CATEGORY);
        DataInfoUtils.getDataFileParam(ttsTextModelFilePath, DataInfoUtils.TTS_DATA_PARAM_LANGUAGE);
        speechSynthesizer.initEngine();
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        uiHandler = new Handler(getMainLooper()) {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case UI_TOAST:
                        mToast.setText((CharSequence) msg.obj);
                        mToast.show();
                        break;
                    default:
                        break;
                }
            }
        };
        
        
        if(SharePerfUtil.getBoolean(SP.SP_IS_OPEN_SPEECH))
        {
        	speech(tv_content.getText().toString());
        }
        
        
        
        
    }

    /**
     * 读文字方法
     * 
     * ***/
    private static void speech(String str)
    {
    	setParams();
        int ret = speechSynthesizer.speak(tv_content.getText().toString());
//		if (ret != 0) {
//		    logError("开始合成器失败：" + errorCodeAndDescription(ret));
//		} else {
//		    logDebug("开始工作，请等待数据...");
//		}
    }

    private static void setParams() {
        // speechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "0");
        // speechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOLUME, "5");
        // speechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEED, "5");
        // speechSynthesizer.setParam(SpeechSynthesizer.PARAM_PITCH, "5");
        // speechSynthesizer.setParam(SpeechSynthesizer.PARAM_AUDIO_ENCODE, "1");
        // speechSynthesizer.setParam(SpeechSynthesizer.PARAM_AUDIO_RATE, "4");
        // speechSynthesizer.setParam(SpeechSynthesizer.PARAM_LANGUAGE, "ZH");
        // speechSynthesizer.setParam(SpeechSynthesizer.PARAM_NUM_PRON, "0");
        // speechSynthesizer.setParam(SpeechSynthesizer.PARAM_ENG_PRON, "0");
        // speechSynthesizer.setParam(SpeechSynthesizer.PARAM_PUNC, "0");
        // speechSynthesizer.setParam(SpeechSynthesizer.PARAM_BACKGROUND, "0");
        // speechSynthesizer.setParam(SpeechSynthesizer.PARAM_STYLE, "0");
        // speechSynthesizer.setParam(SpeechSynthesizer.PARAM_TERRITORY, "0");
    }

    @Override
    public void onStartWorking(SpeechSynthesizer arg0) {
        logDebug("开始工作，请等待数据...");
    }

    @Override
    public void onSpeechStart(SpeechSynthesizer synthesizer) {
        logDebug("朗读开始");
    }

    @Override
    public void onSpeechResume(SpeechSynthesizer synthesizer) {
        logDebug("朗读继续");
    }

    @Override
    public void onSpeechProgressChanged(SpeechSynthesizer synthesizer, int progress) {
        uiHandler.sendMessage(uiHandler.obtainMessage(UI_TOAST, "朗读进度" + progress));
    }

    @Override
    public void onSpeechPause(SpeechSynthesizer synthesizer) {
        logDebug("朗读已暂停");
    }

    @Override
    public void onSpeechFinish(SpeechSynthesizer synthesizer) {
        logDebug("朗读已停止");
    }

    @Override
    public void onNewDataArrive(SpeechSynthesizer synthesizer, byte[] audioData, boolean isLastData) {
        logDebug("新的音频数据：" + audioData.length + (isLastData ? ("end") : ""));
    }

    @Override
    public void onError(SpeechSynthesizer synthesizer, SpeechError error) {
        logError("发生错误：" + error);
    }

    @Override
    public void onCancel(SpeechSynthesizer synthesizer) {
        logDebug("已取消");
    }

    @Override
    public void onBufferProgressChanged(SpeechSynthesizer synthesizer, int progress) {
        uiHandler.sendMessage(uiHandler.obtainMessage(UI_TOAST, "缓冲进度" + progress));
    }

    @Override
    public void onSynthesizeFinish(SpeechSynthesizer arg0) {
        // TODO Auto-generated method stub
        logDebug("合成已完成");
    }

    private void logDebug(String logMessage) {
        logMessage(logMessage, Color.BLUE);
    }

    private void logError(String logMessage) {
        logMessage(logMessage, Color.RED);
    }

    private void logMessage(String logMessage, int color) {
        Spannable colorfulLog = new SpannableString(logMessage + "\n");
        colorfulLog.setSpan(new ForegroundColorSpan(color), 0, logMessage.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        uiHandler.sendMessage(uiHandler.obtainMessage(UI_LOG_TO_VIEW, colorfulLog));
    }

    private String errorCodeAndDescription(int errorCode) {
        String errorDescription = "错误码：";
        return errorDescription + "(" + errorCode + ")";
    }
     
    private static SoundPool soundPool;
    public static void play()
    {
    	
    	soundPool= new SoundPool(1,AudioManager.STREAM_MUSIC,4);
    	soundPool.load(JCLApplication.getContext(),R.raw.notice,1);
    	soundPool.play(1,1, 1, 0, 0, 1);
    }
    
    
    static TouchuanMsg pushBean;
    public static void updataInfo(TouchuanMsg msg)
    {
    	play();
    	if(pushBean!=null)
    	{
    		
    		pushBean=msg;
    		tv_content.setText(msg.getContent());
    		speech(tv_content.getText().toString());
    		timer = new Timer();
			// 创建一个TimerTask
			TimerTask timerTask = new TimerTask() {
				// 倒数10秒
				int i = 30;
				@Override
				public void run() {
					// 定义一个消息传过去
					Message msg = new Message();
					msg.what = i--;
					handler.sendMessage(msg);
				}
			};
			// TimerTask是个抽象类,实现了Runnable接口，所以TimerTask就是一个子线程
			timer.schedule(timerTask, 0, 1000);
    	}
    }

    
    private static Timer timer;
	 // 定义Handler
    static	Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				// Handler处理消息
				if (msg.what > 0) {
					tv_clock.setText(""+msg.what+"s");
				} else if (msg.what == 0) {
					tv_clock.setText(""+msg.what+"s");
					// 结束Timer计时器
					timer.cancel();
					MyPushMessageReceiver.touchuanMsgList.remove(0);
					if(MyPushMessageReceiver.touchuanMsgList.size()>0)
					{
						updataInfo(DemoIntentService.touchuanMsg);
					}
				}
			}
		};
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_qiangdan:
			if(pushBean==null)
			{
				MyToast.showToast(this, "暂无可抢订单");
				return;
			}
			switch (pushBean.getClassify()) {
			case 0:
				startActivity(DetailFindActivity.newInstance(this,
						C.INTENT_TYPE_FIND_GOODS,pushBean.getBizid()));
				
				break;
			case 1:
				startActivity(DetailFindActivity.newInstance(this,
						C.INTENT_TYPE_FIND_CAR,pushBean.getBizid()));
				break;
			case 3:
				
				break;
			case 4:
	
				break;

			default:
				break;
			}
			
			break;
		default:
			break;
		}
	}
    
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		speechSynthesizer.resume();
		
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
//		speechSynthesizer.pause();
	}
	
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	// 结束Timer计时器
    	
    	if(timer!=null)
    	{
    		timer.cancel();
    	}
    	if(speechSynthesizer!=null)
    	{
//    		speechSynthesizer.cancel();
    	}
    	MyPushMessageReceiver.touchuanMsgList.clear();
    }
    
}
