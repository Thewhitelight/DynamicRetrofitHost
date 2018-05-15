package cn.libery.switchhost.api;


import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author shizhiqiang on 2018/5/10.
 */
public class DynamicInterceptor implements Interceptor {

    private static String mHost = Api.getHost();
    private boolean mCanSwitchHost;

    public static void setHost(String host) {
        mHost = host;
    }

    public static String getmHost() {
        return mHost;
    }


    public DynamicInterceptor(boolean canSwitchHost) {
        this.mCanSwitchHost = canSwitchHost;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (mCanSwitchHost && !mHost.equals(Api.getHost())) {
            request = replaceHost(request);
        }
        return chain.proceed(request);
    }

    private Request replaceHost(Request request) {
        HttpUrl httpUrl = request.url();
        URL url = httpUrl.url();
        try {
            Class<?> classType = httpUrl.getClass();
            Field a = classType.getDeclaredField("host");
            a.setAccessible(true);
            a.set(httpUrl, mHost);

            Class<?> classType2 = request.getClass();
            Field b = classType2.getDeclaredField("url");
            b.setAccessible(true);
            b.set(request, httpUrl);

            Class<?> classType3 = url.getClass();
            Field c = classType3.getDeclaredField("host");
            c.setAccessible(true);
            c.set(url, mHost);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        request = request.newBuilder().url(httpUrl).build();
        return request;
    }

}
