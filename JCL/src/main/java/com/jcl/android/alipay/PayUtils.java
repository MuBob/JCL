package com.jcl.android.alipay;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import org.apache.commons.lang.RandomStringUtils;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.jcl.android.C;
import com.jcl.android.utils.SharePerfUtil;


public class PayUtils {

	//商户PID
		public static final String PARTNER = "2088021095491177";
		//商户收款账号
		public static final String SELLER = "maggie_ma@speedy-express.com";
		//商户私钥，pkcs8格式
		public static final String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJ" +
				"dAgEAAoGBAOTpUVNOZ/PgQOZs+S3KEMrw540LzcBZ1Ug7MeLMkhyXYRXSGGHzc/wMUTi1BKewR" +
				"kriH52ljTiIkoFqVQIBu6L6oTF1Uf5C6QMtJgVmUit/hR+qz4QIWD464GCzlw08H0xgNJ3HQKO6" +
				"ed12edIzSBsvNTmws2e1l0FSkUm" +
				"/CmPdAgMBAAECgYEAxV7y8wxIjztNWiCdnMB8SC78x11lEzOhtTzs32uZk" +
				"7NmT0UuJs6ds7X/FhDmKZvQj+J2dhv3LsZ648EF+Mv7toH3WqY3rtfxdEumWEM" +
				"urXl7MWOgOh9glWsloc5O6g2iS9B8ivgCZUSyU37CgsmhlysThVoJ6FtawCz7eew" +
				"v6eECQQD7g62UOI9qms0g7F7wa5dLoxbH4i9sKqDEZdwzLOtQmr9PoBni+MfHNXvG" +
				"TMeff2aP2fNRuHbHijNuGzgP5PKJAkEA6P5xWzu0jTLxBZzH1ET7DSZBFkbI3eSlCM" +
				"rkT6C6E3As/ULGg3IuWC3qFuYC0znnQYGEyLI8O7OnOt8P0t1htQJBAL/oEAh5Q4yOwx" +
				"ds41/EaDNxJA+tmIuK1xIjumldy8q72F9UBIx3UE/CkkeTC/m6BrKaFRr0icTkwSsxH4y" +
				"ZHykCQCbKTdcRnqKva38ytfY6Mc3jo4xw5npC2dMX7GDxcmlQEL1tg51Ywix+IG4Fh" +
				"0zhZAX86T18pxiNdXcQshK6Ns0CQHJi2UK/HPdyUpN/8Rd4OwwoMpWrvXTikUI" +
				"Bx+M1nTfvE3EwFZ8dWEkPrjn9/XuaOsx7ivjQkNhrzZ2v+bpfE/M=";
		//支付宝公钥
		public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCFIAkVffDkryZ49adsRdW1ZRWxD8ZGbXgBaLoQhyuFIn7JooD1PwVsC3zC66+ksAFqB/t7U7+ZtFFZHYx4bcZmuGhvxrNFdL4kwbsJPvSaI+/ZZ7O1yMt5675GjzXArOnnRx2UxfJOz0OYomudJfOQ8CQY677I1saZb/We6DswcQIDAQAB";

		
		private  Activity context;
		private  Handler mHandler;
		
		private static final int SDK_PAY_FLAG = 1;  //支付标识
		private static String HUIDIAO_URL = "";
		
		/**
		 * call alipay sdk pay. 调用SDK支付
		 * 
		 */
		public void pay(Context context,Handler mHandler,String jine,String payflag,String priceid) {
			this.context = (Activity) context;
			this.mHandler = mHandler;
			String zhifucontent = "";
			if(payflag.equals("0")){//充值
				zhifucontent = "钱包充值";
				HUIDIAO_URL =C.BASE_URL+"/alipay";
			}else if(payflag.equals("1")){//担保
				zhifucontent = "担保交易";
				HUIDIAO_URL =C.BASE_URL+"/vouch";
			}
			// 订单
			String orderInfo = getOrderInfo("支付宝支付", zhifucontent, jine,priceid);

			// 对订单做RSA 签名
			String sign = sign(orderInfo);
			try {
				// 仅需对sign 做URL编码
				sign = URLEncoder.encode(sign, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			// 完整的符合支付宝参数规范的订单信息
			final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
					+ getSignType();
			
			PayRunnable payRunnable = new PayRunnable(payInfo);
			
			// 必须异步调用
			Thread payThread = new Thread(payRunnable);
			payThread.start();
		}
		/**
		 * 支付线程
		 * @author xuelei
		 *
		 */
		public class PayRunnable implements Runnable{
			private String payInfo;
            public PayRunnable(String payInfo){
            	this.payInfo = payInfo;
            }
			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(context);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo);

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
			
		}
		

		/**
		 * create the order info. 创建订单信息
		 * 
		 */
		public String getOrderInfo(String subject, String body, String price,String priceid) {
			// 签约合作者身份ID
			String orderInfo = "partner=" + "\"" + PARTNER + "\"";

			// 签约卖家支付宝账号
			orderInfo += "&seller_id=" + "\"" + SELLER + "\"";
			
            if(priceid ==null){
            	// 商户网站唯一订单号
    			orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";
            }else{
            	// 商户网站唯一订单号
    			orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo(priceid) + "\"";
            }
			

			// 商品名称
			orderInfo += "&subject=" + "\"" + subject + "\"";

			// 商品详情
			orderInfo += "&body=" + "\"" + body + "\"";

			// 商品金额
			orderInfo += "&total_fee=" + "\"" + price + "\"";

			// 服务器异步通知页面路径
			orderInfo += "&notify_url=" + "\"" + HUIDIAO_URL
					+ "\"";

			// 服务接口名称， 固定值
			orderInfo += "&service=\"mobile.securitypay.pay\"";

			// 支付类型， 固定值
			orderInfo += "&payment_type=\"1\"";

			// 参数编码， 固定值
			orderInfo += "&_input_charset=\"utf-8\"";

			// 设置未付款交易的超时时间
			// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
			// 取值范围：1m～15d。
			// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
			// 该参数数值不接受小数点，如1.5h，可转换为90m。
			orderInfo += "&it_b_pay=\"30m\"";

			// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
			// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

			// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
			orderInfo += "&return_url=\"m.alipay.com\"";

			// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
			// orderInfo += "&paymethod=\"expressGateway\"";

			return orderInfo;
		}

		/**
		 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
		 * 商户订单号 以20位userid加6位随机数组成
		 * 
		 */
		public String getOutTradeNo() {
			String key = SharePerfUtil.getUserId();
			String random = RandomStringUtils.randomNumeric(6);
			
			return key+random;
		}
		public String getOutTradeNo(String goodsid) {
			String random = RandomStringUtils.randomNumeric(6);
			
			return goodsid+random;
		}

		/**
		 * sign the order info. 对订单信息进行签名
		 * 
		 * @param content
		 *            待签名订单信息
		 */
		public String sign(String content) {
			return SignUtils.sign(content, RSA_PRIVATE);
		}

		/**
		 * get the sign type we use. 获取签名方式
		 * 
		 */
		public String getSignType() {
			return "sign_type=\"RSA\"";
		}
}
