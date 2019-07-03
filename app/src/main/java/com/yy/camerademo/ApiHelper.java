package com.yy.camerademo;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * 基本数据请求工具类
 */
public class ApiHelper {
    private Retrofit retrofit;
    private OkHttpClient okHttpClient = null;
    private ApiServices apiservices;
    private static ApiHelper INSTANCE = null;

    public ApiHelper(String url) {
        initOkHttpClicent();
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        apiservices = retrofit.create(ApiServices.class);
    }

    private void initOkHttpClicent() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // Log信息拦截器
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
            builder.addInterceptor(loggingInterceptor);
        }
        // http://www.jianshu.com/p/93153b34310e
        File cacheFile = new File(Constants.PATH_CACHE);
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
        Interceptor cacheInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();

                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_NETWORK)
                            .build();

                Response response = chain.proceed(request);

                    int maxAge = 0;
                    // 有网络时, 不缓存, 最大保存时长为0
                    response.newBuilder()
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .removeHeader("Pragma")
                            .build();

                return response;
            }
        };
        //设置缓存
        builder.addNetworkInterceptor(cacheInterceptor);
        builder.addInterceptor(cacheInterceptor);
        builder.cache(cache);
        //设置超时
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);
        okHttpClient = builder.build();
    }

    public static ApiHelper getInstance(String url) {
        //        if (INSTANCE == null) {
        //            synchronized (ApiHelper.class) {
        //                if (INSTANCE == null) {
        INSTANCE = new ApiHelper(url);
        //                }
        //            }
        //        }
        return INSTANCE;
    }

    ///////////////////////////////////首页接口///////////////////////////////////////////////////////

    /**
     * 上传用户头像
     *
     * @param parts 图片的part
     */
    public Observable<UploadResultBean> uploadIcon(List<MultipartBody.Part> parts) {
        return apiservices.uploadIcon(parts.get(0))
                .compose(RxUtil.<UploadResultBean>rxSchedulerHelper());
    }


}
