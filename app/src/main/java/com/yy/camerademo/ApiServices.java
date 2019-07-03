package com.yy.camerademo;

import okhttp3.MultipartBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * @Version:V1.0
 * @Author:Guoy
 * @CreateTime:2017年3月1日
 * @Descrpiton:网络请求接口
 */
public interface ApiServices {

    ///////////////////////////////////首页接口///////////////////////////////////////////////////////


    /**
     * 通过 List<MultipartBody.Part> 传入多个part实现多文件上传
     *
     * @param part 每个part代表一个
     * @return 状态信息
     */
    @Multipart
    @POST("upload")
    Observable<UploadResultBean> uploadIcon(@Part() MultipartBody.Part part);

}
