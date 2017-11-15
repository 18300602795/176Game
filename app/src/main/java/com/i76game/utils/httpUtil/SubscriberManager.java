package com.i76game.utils.httpUtil;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2017/7/4.
 */

public class SubscriberManager {

    private final Map<Object, CompositeDisposable> SubscriptionMap = new HashMap<>();
    private static SubscriberManager instance = new SubscriberManager();

    private SubscriberManager() {
//        Subscriber<>
    }

    public static SubscriberManager getInstance() {
        return instance;
    }

    public void addSubscription(Object object, Disposable s) {
        CompositeDisposable compositeDisposable = SubscriptionMap.get(object);
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
            SubscriptionMap.put(object, compositeDisposable);
        }
        compositeDisposable.add(s);
    }

    public void removeSubscription(Object object) {
        CompositeDisposable compositeDisposable = SubscriptionMap.get(object);
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
        SubscriptionMap.remove(object);
    }

    public void AppExit() {
        try {
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
