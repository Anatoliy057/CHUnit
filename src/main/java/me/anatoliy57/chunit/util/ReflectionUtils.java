package me.anatoliy57.chunit.util;

import com.laytonsmith.PureUtilities.DaemonManager;
import com.laytonsmith.core.Globals;
import com.laytonsmith.core.constructs.CClosure;
import com.laytonsmith.core.environments.Environment;
import com.laytonsmith.core.environments.StaticRuntimeEnv;
import com.laytonsmith.core.natives.interfaces.Mixed;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

public class ReflectionUtils {

    public static void SetGlobalsMap(Map<String, Mixed> globalsMap) {
        try {
            Field field = Globals.class.getDeclaredField("GLOBAL_CONSTRUCT");
            field.setAccessible(true);
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            field.set(null, globalsMap);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new Error("Unable to access Globals.GLOBAL_CONSTRUCT", e);
        }
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Mixed> GetGlobalsMap() {
        Map<String, Mixed> globalsMap = null;
        try {
            Field field = Globals.class.getDeclaredField("GLOBAL_CONSTRUCT");
            field.setAccessible(true);
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            globalsMap = (Map<String, Mixed>) field.get(null);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new Error("Unable to access Globals.GLOBAL_CONSTRUCT", e);
        }

        return globalsMap;
    }

    public static DaemonManager GetDaemonManager(Environment env) {
        DaemonManager daemonManager = null;
        try {
            StaticRuntimeEnv staticEnv = env.getEnv(StaticRuntimeEnv.class);
            Field field = StaticRuntimeEnv.class.getDeclaredField("daemonManager");
            field.setAccessible(true);
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            daemonManager = (DaemonManager) field.get(staticEnv);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new Error("Unable to access StaticRuntimeEnv.daemonManager", e);
        }

        return daemonManager;
    }

    public static void SetDaemonManager(Environment env, DaemonManager daemonManager) {
        try {
            StaticRuntimeEnv staticEnv = env.getEnv(StaticRuntimeEnv.class);
            Field field = StaticRuntimeEnv.class.getDeclaredField("daemonManager");
            field.setAccessible(true);
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            field.set(staticEnv, daemonManager);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new Error("Unable to access StaticRuntimeEnv.daemonManager", e);
        }
    }

    public static void SetEnvClosure(CClosure closure, Environment env) {
        try {
            Field field = CClosure.class.getDeclaredField("env");
            field.setAccessible(true);
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            field.set(closure, env);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new Error("Unable to access CClosure.env", e);
        }
    }
}
