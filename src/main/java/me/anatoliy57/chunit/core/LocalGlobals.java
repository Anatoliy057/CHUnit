package me.anatoliy57.chunit.core;

import com.laytonsmith.core.constructs.CNull;
import com.laytonsmith.core.natives.interfaces.Mixed;

import java.util.*;

public class LocalGlobals implements Map<String, Mixed> {

    private static final ThreadLocal<Map<String, Mixed>> instance = ThreadLocal.withInitial(HashMap::new);

    public static void SetGlobal(String name, Mixed value) {
        if (value instanceof CNull) {
            instance.get().remove(name);
        } else {
            instance.get().put(name, value);
        }
    }

    public static Mixed GetGlobalConstruct(String name) {
        return instance.get().getOrDefault(name, CNull.NULL);
    }

    @Override
    public int size() {
        return getCurrentGlobal().size();
    }

    @Override
    public boolean isEmpty() {
        return getCurrentGlobal().isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return getCurrentGlobal().containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return getCurrentGlobal().containsValue(value);
    }

    @Override
    public Mixed get(Object key) {
        return getCurrentGlobal().get(key);
    }

    @Override
    public Mixed put(String key, Mixed value) {
        return getCurrentGlobal().put(key, value);
    }

    @Override
    public Mixed remove(Object key) {
        return getCurrentGlobal().remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ? extends Mixed> m) {
        getCurrentGlobal().putAll(m);
    }

    @Override
    public void clear() {
    }

    @Override
    public Set<String> keySet() {
        return getCurrentGlobal().keySet();
    }

    @Override
    public Collection<Mixed> values() {
        return getCurrentGlobal().values();
    }

    @Override
    public Set<Entry<String, Mixed>> entrySet() {
        return getCurrentGlobal().entrySet();
    }

    private Map<String, Mixed> getCurrentGlobal() {
        return instance.get();
    }
}
