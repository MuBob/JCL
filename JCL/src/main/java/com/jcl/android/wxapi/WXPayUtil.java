package com.jcl.android.wxapi;

import android.content.Context;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

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
    private IWXAPI msgApi;

    public WXPayUtil(Context context){
        msgApi = WXAPIFactory.createWXAPI(context, null);
        // 将该app注册到微信
        msgApi.registerApp(APPID);
    }

    public void prepay(){
        PayReq request = new PayReq();

        request.appId = "wxd930ea5d5a258f4f";

        request.partnerId = "1900000109";

        request.prepayId= "1101000000140415649af9fc314aa427";

        request.packageValue = "Sign=WXPay";

        request.nonceStr= "1101000000140429eb40476f8896f4c9";

        request.timeStamp= "1398746574";

        request.sign= "7FFECB600D7157C5AA49810D2D8F28BC2811827B";

        msgApi.sendReq(request);

    }

}
