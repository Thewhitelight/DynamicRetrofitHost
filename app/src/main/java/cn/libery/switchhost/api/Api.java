package cn.libery.switchhost.api;


import java.util.concurrent.TimeUnit;

import cn.libery.switchhost.AppUtil;
import cn.libery.switchhost.BuildConfig;
import cn.libery.switchhost.model.Response;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author shizhiqiang on 2018/1/10.
 */

public class Api {

    public static final int REQUEST_SUCCESS = 200;

    private static final long DEFAULT_TIMEOUT = 60;
    public static final String DAILY_HOST = "v3.wufazhuce.com:8000";
    public static final String RELEASE_HOST = "v3.wufazhuce.com:8001";
    public static final String PREVIEW_HOST = "v3.wufazhuce.com:8002";

    public static final String PATH = "/api/";

    private static final String BASE_DAILY_URL = "http://" + DAILY_HOST + PATH;
    private static final String BASE_RELEASE_URL = "http://" + RELEASE_HOST + PATH;

    private ApiService service;

    public String getBaseUrl() {
        if (BuildConfig.DEBUG) {
            return BASE_DAILY_URL;
        } else {
            return BASE_RELEASE_URL;
        }
    }

    public static String getHost() {
        if (BuildConfig.DEBUG) {
            return DAILY_HOST;
        } else {
            return RELEASE_HOST;
        }
    }

    protected Api() {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        if (BuildConfig.DEBUG) {
            // Log信息拦截器
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            //设置 Debug Log 模式
            builder.addInterceptor(loggingInterceptor);
        }

        builder.addInterceptor(new DynamicInterceptor(AppUtil.canSwitchHost()));

        OkHttpClient client = builder.build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(getBaseUrl())
                .build();
        service = retrofit.create(ApiService.class);
    }


    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    private static class HttpResultFunc1<T> implements Function<Response<T>, T> {
        @Override
        public T apply(Response<T> t) {
            if (t.code == 0) {
                return t.data;
            } else {
                throw new ApiException(t.code, t.msg);
            }
        }
    }

    /**
     * 返回结果只有Result
     * 用来统一处理Http的resultCode
     */
    private static class HttpResultFunc2 implements Function<Response, Boolean> {
        @Override
        public Boolean apply(Response response) {
            if (response.code == REQUEST_SUCCESS) {
                return true;
            } else {
                throw new ApiException(response.code, response.msg);
            }
        }
    }

    protected <T> Flowable<T> doCompose(Flowable<Response<T>> observable) {
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new HttpResultFunc1<>());
    }

}
