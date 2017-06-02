package com.jcl.android.wxapi;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jcl.android.R;
import com.jcl.android.net.RequestManager;
import com.jcl.android.utils.Utils;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import net.sourceforge.simcpux.MD5;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 微信支付
 * Created by Administrator on 2017/6/1.
 */
public class WXPayUtil {
    //商户号
    public static final String PARTNER = "1480796542";
    //登陆账号
    public static final String USER_ID = "1480796542@1480796542";
    //登陆密码
    public static final String USER_PWD = "812785";
    //appId
    public static final String APPID = "wx9d3bff9b821f5917";
    //商户密钥
    public static final String KEY="";
    public static final String DEVICE_INFO="WEB";
    private IWXAPI msgApi;
    private Context context;

    public WXPayUtil(Context context) {
        this.context = context;
        msgApi = WXAPIFactory.createWXAPI(context, null);
        // 将该app注册到微信
        msgApi.registerApp(APPID);
    }

    private static final String TAG = "WXPayUtil";

    /**
     * 生成微信支付订单
     * @param bodyLast
     * @param out_trade_no
     * @param total_fee
     * @param ip
     * @param detail
     * @param attach
     */
    public void prepay(String bodyLast, String out_trade_no, int total_fee,String ip, String detail, String attach) {
        Map<String, String> params=new HashMap<>();
        params.put("appid", APPID);
        params.put("mch_id", USER_ID);
        params.put("device_info", DEVICE_INFO);
        params.put("nonce_str", Utils.generateRandom(32));
        params.put("body", context.getString(R.string.app_name)+"-"+bodyLast);
        params.put("out_trade_no", out_trade_no);
        params.put("total_fee", String.valueOf(total_fee));
        params.put("spbill_create_ip", ip);
        params.put("notify_url", "http://www.weixin.qq.com/wxpay/pay.php");
        params.put("trade_type", "APP");
        params.put("sign",createSign("UTF-8",params));
        String url=createUrl(params);
        RequestManager.addRequest(new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.i(TAG, "WXPayUtil.onResponse: s="+s);
                try {
                    JSONObject obj=new JSONObject(s);
                    String return_code=obj.optString("return_code");
                    if("SUCCESS".equals(return_code)){
                        String result_code = obj.optString("result_code");
                        if("SUCCESS".equals(result_code)){
                            String prepay_id = obj.optString("prepay_id");
                            String trade_type = obj.optString("trade_type");
                            pay(prepay_id);
                        }else {
                            String err_code = obj.optString("err_code");
                            String err_code_des = obj.optString("err_code_des");
                            Log.i(TAG, "WXPayUtil.onResponse: err_code="+err_code+", err_code_des"+err_code_des);
                            Toast.makeText(context, err_code, Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        String return_msg=obj.optString("return_msg");
                        Toast.makeText(context, return_msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(TAG, "WXPayUtil.onErrorResponse: e="+volleyError.getMessage());
            }
        }), context);
    }

    /**
     * 发起微信支付
     */
    public void pay(String prepareId){
        Map<String, String> signParams=new HashMap<>();
        signParams.put("appid", APPID);
        signParams.put("partnerid", USER_ID);
        signParams.put("prepayid", prepareId);
        signParams.put("noncestr", Utils.generateRandom(32));
        signParams.put("timestamp", String.valueOf(System.currentTimeMillis()));
        signParams.put("package", "Sign=WXPay");
        PayReq req = new PayReq();
        req.appId = signParams.get("appid");
        req.partnerId = signParams.get("partnerid");
        req.prepayId = signParams.get("prepayid");
        req.nonceStr = signParams.get("noncestr");
        req.timeStamp = signParams.get("timestamp");
        req.packageValue = signParams.get("package");
        req.sign = createSign("UTF-8",signParams);
        Log.i(TAG, "WXPayUtil.pay: req="+req);
        msgApi.sendReq(req);
    }
    private String createUrl(Map<String, String> params) {
        StringBuffer url = new StringBuffer("https://api.mch.weixin.qq.com/pay/unifiedorder");
        Set<Map.Entry<String, String>> es = params.entrySet();//所有参与传参的参数按照accsii排序（升序）
        Iterator<Map.Entry<String, String>> iterator = es.iterator();
        int a=0;
        while(iterator.hasNext()) {
            if(a==0){
                url.append("?");
            }else {
                url.append("&");
            }
            a++;
            Map.Entry<String, String> next = iterator.next();
            String k = next.getKey();
            String v = next.getValue();
            if(null != v && !"".equals(v)) {
                url.append(k + "=" + v );
            }
        }

        return url.toString();
    }

    /**
     * 微信支付签名算法sign
     * @param characterEncoding
     * @param parameters
     * @return
     */
    @SuppressWarnings("unchecked")
    private static String createSign(String characterEncoding, Map<String,String> parameters){
        StringBuffer sb = new StringBuffer();
        Set<Map.Entry<String, String>> es = parameters.entrySet();//所有参与传参的参数按照accsii排序（升序）
        Iterator<Map.Entry<String, String>> iterator = es.iterator();
        while(iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            String k = next.getKey();
            String v = next.getValue();
            if(null != v && !"".equals(v)
                    && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + KEY);
        String sign = MD5.MD5Encode(sb.toString()).toUpperCase();
        return sign;
    }
}
