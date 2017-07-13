package com.jcl.android.mypay;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.jcl.android.bean.BaseBean;
import com.jcl.android.net.GsonRequest;
import com.jcl.android.net.RequestManager;
import com.jcl.android.net.UrlCat;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 账户支付4100
 接口格式:http://www.xxxx.com/submit?postStr=xxxxxxxxxxxxxxxxxx

 type:" 4100"
 operate:"A"

 data输入参数
 参数	            名称	            说明
 userid	        用户ID
 mobile         收款账户
 price          	交易金额
 type	            支付场景	    3 支付

 输出参数
 数据项	数据子项	说明
 code		结果 0.服务异常 1.成功
 msg		服务端消息

 * Created by Administrator on 2017/6/22.
 */
public class MyPayUtil {

    private Context context;
    public MyPayUtil(Context context){
        this.context=context;
    }
    private MyPayRequestData payRequestDataBean;
    private String payRequestData;
    private String jsonRequest;

    private static final String TAG = "MyPayUtil";
    public void pay(String userId, String toMobile, String price){
        payRequestDataBean=new MyPayRequestData(userId, toMobile, price, 3);
        payRequestData = new Gson().toJson(payRequestDataBean);
        jsonRequest = new Gson().toJson(new MyPayPostRequest(payRequestData));
        RequestManager.addRequest(new StringRequest(jsonRequest, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.i(TAG, "MyPayUtil.onResponse: res="+s);
                try {
                    JSONObject obj=new JSONObject(s);
                    int code=obj.optInt("code");
                    String msg=obj.optString("msg");
                    if(code==0){
                        Log.i(TAG, "MyPayUtil.onResponse: 返回异常");

                    }else {
                        Log.i(TAG, "MyPayUtil.onResponse: 返回成功");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i(TAG, "MyPayUtil.onErrorResponse: error="+volleyError);
            }
        }), context);
        RequestManager.addRequest(new GsonRequest<BaseBean>(
                Request.Method.GET,
                UrlCat.getMyPayUrl(jsonRequest),
                BaseBean.class, null, null,
                new Response.Listener<BaseBean>() {
                    @Override
                    public void onResponse(BaseBean baseBean) {
                        Log.i(TAG, "MyPayUtil.onResponse: res="+baseBean);
                        int code=Integer.parseInt(baseBean.getCode());
                        String msg=baseBean.getMsg();
                        if(code==0){
                            Log.i(TAG, "MyPayUtil.onResponse: 返回异常");

                        }else {
                            Log.i(TAG, "MyPayUtil.onResponse: 返回成功");
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.i(TAG, "MyPayUtil.onErrorResponse: error="+volleyError);
                    }
                }
        ), this);
    }

    private class MyPayPostRequest{
        private String type="4100";//:" 4100"
        private String operate="A";//:"A"

        private String data;//输入参数

        public MyPayPostRequest(String data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "MyPayPostRequest{" +
                    "type='" + type + '\'' +
                    ", operate='" + operate + '\'' +
                    ", data='" + data + '\'' +
                    '}';
        }
    }
    private class MyPayRequestData{
        private String userid;
        private String mobile;
        private String price;
        private int type;

        public MyPayRequestData(String userid, String mobile, String price, int type) {
            this.userid = userid;
            this.mobile = mobile;
            this.price = price;
            this.type = type;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "userid='" + userid + '\'' +
                    ", mobile='" + mobile + '\'' +
                    ", price='" + price + '\'' +
                    ", type=" + type +
                    '}';
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }

}
