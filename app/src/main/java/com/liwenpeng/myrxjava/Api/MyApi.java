package com.liwenpeng.myrxjava.Api;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;

/**
 * liwenpeng
 * 2018/6/7 20:47
 */
public interface MyApi {
    @GET
    Observable<LoginResponse> login(@Body LoginRequest loginRequest);
    @GET
    Observable<RegisterResponse> register(@Body RegisterRequest registerRequest);

    @GET Observable<UserBaseInfoResponse> getUserBaseInfo(@Body UserBaseInfoRequest request);
    @GET Observable<UserExtraInfoResponse> getUserExtraInfo(@Body UserExtraInfoRequest request);



}
