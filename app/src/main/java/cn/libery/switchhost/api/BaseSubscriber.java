package cn.libery.switchhost.api;


import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.List;

/**
 * @author shizhiqiang on 2018/1/10.
 */

public abstract class BaseSubscriber<T> implements Subscriber<T> {

    private List<Subscription> mSubscriptions;

    public BaseSubscriber(List<Subscription> subscriptions) {
        this.mSubscriptions = subscriptions;
    }

    @Override
    public void onSubscribe(Subscription s) {
        mSubscriptions.add(s);
        s.request(1);
    }

    @Override
    public void onError(Throwable e) {
    }

    @Override
    public void onComplete() {

    }

}
