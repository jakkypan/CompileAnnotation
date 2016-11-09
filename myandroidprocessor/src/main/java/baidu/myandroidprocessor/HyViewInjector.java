/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package baidu.myandroidprocessor;

import java.util.LinkedHashMap;
import java.util.Map;

import com.example.ProxyInfo;

import android.app.Activity;
import android.view.View;

public class HyViewInjector {
    static final Map<Class<?>, AbstractInjector<Object>> INJECTORS =
            new LinkedHashMap<Class<?>, AbstractInjector<Object>>();

    public static void inject(Activity activity) {
        AbstractInjector<Object> injector = findInjector(activity);
        injector.inject(Finder.ACTIVITY, activity, activity);
    }

    public static void inject(Object target, View view) {
        AbstractInjector<Object> injector = findInjector(target);
        injector.inject(Finder.VIEW, target, view);
    }

    private static AbstractInjector<Object> findInjector(Object activity) {
        Class<?> clazz = activity.getClass();
        AbstractInjector<Object> injector = INJECTORS.get(clazz);
        if (injector == null) {
            try {
                Class injectorClazz = Class.forName(clazz.getName() + "$$"
                        + ProxyInfo.PROXY);
                injector = (AbstractInjector<Object>) injectorClazz.newInstance();
                INJECTORS.put(clazz, injector);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return injector;
    }
}
