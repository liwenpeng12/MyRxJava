package com.liwenpeng.myrxjava.Api;

/**
 * liwenpeng
 * 2018/6/8 16:18
 */
public class UserInfo {
    UserBaseInfoResponse userBaseInfoResponse;
    UserExtraInfoResponse userExtraInfoResponse;
    public UserInfo(UserBaseInfoResponse userBaseInfoResponse,UserExtraInfoResponse userExtraInfoResponse){
        this.userBaseInfoResponse=userBaseInfoResponse;
        this.userExtraInfoResponse=userExtraInfoResponse;
    }
}
