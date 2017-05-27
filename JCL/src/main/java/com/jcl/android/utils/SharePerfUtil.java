package com.jcl.android.utils;

import android.R.integer;

import com.jcl.android.SP;


public class SharePerfUtil {

    public static boolean put(String key, Object content) {
        return SPUtil.set(key, content);
    }

    public static <T> T get(String key, int defaul) {
        return (T) SPUtil.get(key, (Object) defaul);
    }

    public static <T> T get(String key, long defaul) {
        return (T) SPUtil.get(key, (Object) defaul);
    }

    public static String getString(String key) {
        return (String) SPUtil.get(key, "");
    }

    public static int getInt(String key) {
        return (Integer) SPUtil.get(key, 0);
    }

    public static boolean getBoolean(String key) {
        return (Boolean) SPUtil.get(key, false);
    }

    public static float getFloat(String key) {
        return (Float) SPUtil.get(key, 0.0f);
    }
    
    public static boolean remove(String key) {
        return SPUtil.remove(key);
    }
    /**
     * 保存用户登录返回的信息
     * @param loginUserInfo json字符串
     * @return boolean 
     */
	public static boolean saveLoginUserInfo(String loginUserInfo){
    	return put(SP.SP_LOGIN_USERINFO, loginUserInfo);
    }
    /**
     * 获取登录返回的userinfo
     * @return
     */
    public static String getLoginUserInfo(){
    	return getString(SP.SP_LOGIN_USERINFO);
    }

    
    /**
     * 保存用户登录的用户id
     * @param loginUserId json字符串
     * @return boolean 
     */
	public static boolean saveUserId(String loginUserId){
    	return put(SP.SP_LOGIN_USERID, loginUserId);
    }
	 /**
     * 获取登录返回的userid
     * @return
     */
    public static String getUserId(){
    	return getString(SP.SP_LOGIN_USERID);
    }
    
    
    public static boolean saveType(String loginType){
    	return put(SP.SP_LOGIN_TYPE, loginType);
    }
	 
    public static String getType(){
    	return getString(SP.SP_LOGIN_TYPE);
    }
    
    public static boolean saveIcount(String loginIcount){
    	return put(SP.SP_LOGIN_ICOUNT, loginIcount);
    }
	 
    public static String getIcount(){
    	return getString(SP.SP_LOGIN_ICOUNT);
    }
    
    /**
     * 保存用户登录的企业id
     * @param loginUserId json字符串
     * @return boolean 
     */
	public static boolean saveCompanyId(String id){
    	return put(SP.SP_LOGIN_USERID, id);
    }
	 /**
     * 获取登录返回的企业id
     * @return
     */
    public static String getCompanyId(){
    	return getString(SP.SP_LOGIN_USERID);
    }
    
    /**
     * 保存登录类别        0:个人1：企业
     * @param loginUserId json字符串
     * @return boolean 
     */
	public static boolean saveSubmittype(String type){
    	return put(SP.SP_LOGIN_USERTYPE, type);
    }
	 /**
     * 获取登录类别
     * @return
     */
    public static String getSubmittype(){
    	return getString(SP.SP_LOGIN_USERTYPE);
    }
    
    /**
     * 保存登录账号
     * @param loginname json字符串
     * @return boolean 
     */
	public static boolean saveLoginName(String type){
    	return put(SP.SP_LOGIN_NAME, type);
    }
	 /**
     * 获取登录账号
     * @return
     */
    public static String getLoginName(){
    	return getString(SP.SP_LOGIN_NAME);
    }
    
    /**
     * 保存登录密码
     * @param loginpwd json字符串
     * @return boolean 
     */
	public static boolean saveLoginPwd(String type){
    	return put(SP.SP_LOGIN_PWD, type);
    }
	 /**
     * 获取登录密码
     * @return
     */
    public static String getLoginPwd(){
    	return getString(SP.SP_LOGIN_PWD);
    }
    
    
    
    /**
     * 获取联系热
     * @return
     */
    public static String getLinkMan(){
    	return getString(SP.SP_LINKMAN);
    }
    
    /**
     * 保存用户登录的联系人
     * @param linkman json字符串
     * @return boolean 
     */
	public static boolean saveLinkMan(String linkman){
    	return put(SP.SP_LINKMAN, linkman);
    }
	
	/**
     * 获取公司名称
     * @return
     */
    public static String getCompanyName(){
    	return getString(SP.SP_COMPANY_NAME);
    }
    
    /**
     * 保存公司名称
     * @param linkman json字符串
     * @return boolean 
     */
	public static boolean saveCompanyName(String companyName){
    	return put(SP.SP_COMPANY_NAME, companyName);
    }
	 /**
     * 保存头像地址
     * @param 
     * @return boolean 
     */
	public static boolean savePersonHead(String url){
    	return put(SP.SP_PERSON_HEAD, url);
    }
	
	/**
     * 保存位置上传是否开启
     * @param 
     * @return boolean 
     */
	public static boolean updataLoaction(Boolean type){
    	return put(SP.SP_UPDATA_LOCATION, type);
    }
	 /**
     * 保存位置上传是否开启
     * @return
     */
    public static Boolean getUpdataLoaction(){
    	return getBoolean(SP.SP_UPDATA_LOCATION);
    }
    /**
     * 保存开启实时上传位置的车辆id
     * @return
     */
    public static boolean saveCarid(String id)
    {
    	return put(SP.SP_CARID,id);
    }
    /**
     * 获取开启实时上传位置的车辆id
     * @return
     */
    public static String getCarid()
    {
    	return getString(SP.SP_CARID);
    }
    
    /**
     * 保存邀请码
     * @param loginpwd json字符串
     * @return boolean 
     */
	public static boolean saveInvitecode(String type){
    	return put(SP.SP_INVITECODE, type);
    }
	 /**
     * 获取邀请码
     * @return
     */
    public static String getInvitecode(){
    	return getString(SP.SP_INVITECODE);
    }
    
    
    /**
     * 保存 认证标示
     * @param loginpwd json字符串
     * @return boolean 
     */
	public static boolean saveIsauth(String type){
    	return put(SP.SP_ISAUTH, type);
    }
	 /**
     * 获取 认证标识
     * @return
     */
    public static String getIsauth(){
    	return getString(SP.SP_ISAUTH);
    }
	
    //退出当前账号，清除当前用户信息
    public static void loginOut(){
    	remove(SP.SP_LOGIN_USERINFO);
    	remove(SP.SP_LOGIN_USERID);
    	remove(SP.SP_LOGIN_USERTYPE);
    	remove(SP.SP_LOGIN_NAME);
    	remove(SP.SP_LOGIN_PWD);
    	remove(SP.SP_ISLOGIN);
    	remove(SP.SP_INVITECODE);
    	remove(SP.SP_INVITECODE);
    }
}

