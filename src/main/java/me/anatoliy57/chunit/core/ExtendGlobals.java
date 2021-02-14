package me.anatoliy57.chunit.core;

import com.laytonsmith.core.constructs.CArray;
import com.laytonsmith.core.natives.interfaces.Mixed;

import java.util.*;

public class ExtendGlobals implements Map<String, Mixed> {

    private static final ExtendGlobals instance = new ExtendGlobals();

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

    public void clearThreadsGlobals() {
        for (Map<String, Mixed> global :
                threadsGlobals.values()) {
            global.clear();
        }
        threadsGlobals.clear();
    }

    void initGlobalsForThread(long threadId) {
        Map<String, Mixed> newGlobal = new HashMap<>();
        try {
            for (Entry<String, Mixed> entry :
                    OriginalGlobals.GetGlobals().entrySet()) {
                if(entry.getValue().isInstanceOf(CArray.TYPE)) {
                    CArray array = (CArray) entry.getValue();
                    newGlobal.put(entry.getKey(), array.deepClone(array.getTarget()));
                } else {
                    newGlobal.put(entry.getKey(), entry.getValue().clone());
                }
            }
        } catch (CloneNotSupportedException e) {
            throw new Error("Error while copies globals", e);
        }

        threadsGlobals.put(threadId, newGlobal);
    }

    void deleteGlobalsForThread(long threadId) {
        Optional.ofNullable(threadsGlobals.remove(threadId)).ifPresent(Map::clear);
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
