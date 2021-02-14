package me.anatoliy57.chunit.core;

import com.laytonsmith.core.Globals;
import com.laytonsmith.core.constructs.CNull;
import com.laytonsmith.core.natives.interfaces.Mixed;
import me.anatoliy57.chunit.util.ReflectionUtils;

import java.util.HashMap;
import java.util.Map;

public class OriginalGlobals {

    private static Map<String, Mixed> originalGlobals;

    public static void SetOriginalGlobal() {
        Map<String, Mixed> globalsMap = ReflectionUtils.GetGlobalsMap();
        if(globalsMap instanceof ExtendGlobals) {
            originalGlobals = new HashMap<>();
        } else {
            originalGlobals = globalsMap;
        }
    }

    public static void SetGlobal(String name, Mixed value) {
        synchronized (Globals.class) {
            if (value instanceof CNull) {
                originalGlobals.remove(name);
            } else {
                originalGlobals.put(name, value);
            }
        }
    }

    public static Mixed GetGlobalConstruct(String name) {
        synchronized (Globals.class) {
            return originalGlobals.getOrDefault(name, CNull.NULL);
        }
    }

    public static Map<String, Mixed> GetGlobals() {
        return originalGlobals;
    }
}
