package me.anatoliy57.chunit.core;

import com.laytonsmith.core.natives.interfaces.Mixed;
import me.anatoliy57.chunit.util.ReflectionUtils;

import java.util.*;

public class ExtendGlobals implements Map<String, Mixed> {

    private static ExtendGlobals instance;

    public static void InitInstance() {
        Map<String, Mixed> globals = ReflectionUtils.GetGlobalsMap();
        if(globals instanceof ExtendGlobals) {
            instance = (ExtendGlobals) globals;
        } else {
            instance = new ExtendGlobals();
            ReflectionUtils.SetGlobalsMap(instance);
        }
    }

    public static ExtendGlobals GetInstance() {
        return instance;
    }

    private final Map<Long, Map<String, Mixed>> threadsGlobals;

    private ExtendGlobals() {
        threadsGlobals = new HashMap<>();
    }

    public void initGlobalsForCurrentThread() {
        initGlobalsForThread(Thread.currentThread().getId());
    }

    public void deleteGlobalsForCurrentThread() {
        deleteGlobalsForThread(Thread.currentThread().getId());
    }

    void initGlobalsForThread(long threadId) {
        threadsGlobals.putIfAbsent(threadId, new HashMap<>());
    }

    void deleteGlobalsForThread(long threadId) {
        Optional.ofNullable(threadsGlobals.remove(threadId)).ifPresent(Map::clear);
    }

    public Map<Long, Map<String, Mixed>> getThreadsGlobals() {
        return threadsGlobals;
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
        OriginalGlobals.GetGlobals().clear();
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
        long threadId = Thread.currentThread().getId();
        return threadsGlobals.getOrDefault(threadId, OriginalGlobals.GetGlobals());
    }
}
