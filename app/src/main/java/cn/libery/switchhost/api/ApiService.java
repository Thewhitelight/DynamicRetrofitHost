package cn.libery.switchhost.api;

import cn.libery.switchhost.model.Response;
import io.reactivex.Flowable;
import retrofit2.http.GET;

/**
 * @author shizhiqiang on 2018/5/15.
 */
public interface ApiService {

    @GET("news/feed/v51/")
    Flowable<Response> article();
}
