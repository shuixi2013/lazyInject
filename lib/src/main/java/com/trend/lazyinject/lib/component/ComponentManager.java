package com.trend.lazyinject.lib.component;

import android.text.TextUtils;

import com.trend.lazyinject.annotation.DebugLog;
import com.trend.lazyinject.annotation.Inject;
import com.trend.lazyinject.annotation.Name;
import com.trend.lazyinject.annotation.InjectComponent;
import com.trend.lazyinject.lib.cache.ProviderCache;
import com.trend.lazyinject.lib.di.ComponentContainer;
import com.trend.lazyinject.lib.di.DIImpl;
import com.trend.lazyinject.lib.log.LOG;
import com.trend.lazyinject.lib.proxy.InterfaceProxy;
import com.trend.lazyinject.lib.utils.ValidateUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Global Name Manager
 * Created by ganyao on 2017/3/16.
 */

public class ComponentManager {

    private static final String TAG = "ComponentManager";
    public static Map<Object,Object> components = new ConcurrentHashMap<>();

    private static Map<String,Class> typeMap = new ConcurrentHashMap<>();


    public static <T> void registerComponent(Class<T> type, T instance) {
        registerComponent(type, instance, true);
    }

    public static <T> void registerComponent(Class<T> type, T instance, boolean cacheProvider) {
        if (!type.isInstance(instance))
            return;
        Name name = (Name) type.getAnnotation(Name.class);
        if (name != null) {
            if (!TextUtils.isEmpty(name.value())) {
                registerComponent(type, instance, name.value());
                return;
            }
        }
        components.put(type, instance);
        if (cacheProvider) {
            ComponentBuilder.registerProviderAsync(type);
        }
    }

    public static <T> void registerComponent(Class type, T instance, String key) {
        if (!type.isInstance(instance))
            return;
        components.put(type, instance);
        typeMap.put(key, type);
    }

    @DebugLog
    public static <T> T getComponent(Class<T> type) {
        T t = (T) components.get(type);
        if (t != null)
            return t;
        synchronized (type) {
            t = (T) components.get(type);
            if (t == null) {
                for (Object inst:components.values()) {
                    if (type.isInstance(inst)) {
                        return (T) inst;
                    }
                }
                BuildWrapper<T> wrapper = ComponentBuilder.buildWrapper(type);
                if (wrapper != null) {
                    t = wrapper.component;
                    if (!wrapper.noCache) {
                        registerComponent(type, t, false);
                    }
                }
            }
            return t;
        }
    }

    public static <T> T getComponent(String key) {
        Class type = typeMap.get(key);
        if (type == null)
            return null;
        return (T) getComponent(type);
    }

    public static void removeComponent(Class type) {
        synchronized (type) {
            Object o = components.remove(type);
            if (o != null) {
                if (o instanceof IComponentDestroy) {
                    Destroyed destroyed = ((IComponentDestroy) o).onComponentDestroyed();
                    destroyed.isDestroyed = true;
                }
            }
        }
    }


    public static Object getComponentImpl(Class component, Object[] componentImpls) {
        if (ValidateUtil.isEmpty(componentImpls))
            return null;
        for (Object impl:componentImpls) {
            if (component.isInstance(impl)) {
                return impl;
            }
        }
        return null;
    }


}
