package lark.core.config;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Andy Yuan on 2020/10/28.
 */
public class ConfigFactory<T extends Config<T>> {

    //private static volatile Config instance = null;
    private static final ConcurrentMap<Class, Object> instances = new ConcurrentHashMap<>();

    public static <T>T getInstance( Class<T> type ) {
        if ( instances.get(type) == null) {
            synchronized (instances) {
                if ( instances.get(type) == null ) {
                    T instance = build( type );
                    instances.put( type, instance );
                }
            }
        }
        return (T) instances.get(type);
    }

    private static <T>T build( Class<T> type ) {
        T t = null;
        try {
            t = type.newInstance();
            ( ( Config ) t ).load();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return t;
    }
}
