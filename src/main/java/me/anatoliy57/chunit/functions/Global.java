package me.anatoliy57.chunit.functions;

import com.laytonsmith.PureUtilities.DaemonManager;
import com.laytonsmith.annotations.api;
import com.laytonsmith.core.ArgumentValidation;
import com.laytonsmith.core.Globals;
import com.laytonsmith.core.MSVersion;
import com.laytonsmith.core.constructs.*;
import com.laytonsmith.core.environments.Environment;
import com.laytonsmith.core.exceptions.CRE.*;
import com.laytonsmith.core.exceptions.ConfigRuntimeException;
import com.laytonsmith.core.functions.AbstractFunction;
import com.laytonsmith.core.functions.DataHandling;
import com.laytonsmith.core.natives.interfaces.Mixed;
import me.anatoliy57.chunit.core.ExtendDaemonManager;
import me.anatoliy57.chunit.core.ExtendGlobals;
import me.anatoliy57.chunit.core.OriginalGlobals;
import me.anatoliy57.chunit.util.ReflectionUtils;

import java.util.Set;

public class Global {
    public static String docs() {
        return "A set of functions for globals";
    }

    private static String GetNamespace(CArray array, Target t) {
        boolean first = true;
        StringBuilder b = new StringBuilder();

        for(int i = 0; (long)i < array.size(); ++i) {
            if (!first) {
                b.append(".");
            }

            first = false;
            b.append(array.get(i, t).val());
        }

        return b.toString();
    }

    @api
    public static class original_import extends AbstractFunction {

        public original_import() {
        }

        public String getName() {
            return "original_import";
        }

        public Integer[] numArgs() {
            return new Integer[]{1, 2};
        }

        public String docs() {
            return "mixed {key, [default]} This function likes \"" + new DataHandling._import().getName() + "\" but imports a value from the original global value register.";
        }

        public Class<? extends CREThrowable>[] thrown() {
            return new Class[]{CREIllegalArgumentException.class, CREIndexOverflowException.class};
        }

        public boolean isRestricted() {
            return true;
        }

        public MSVersion since() {
            return MSVersion.V3_3_4;
        }

        public Boolean runAsync() {
            return null;
        }

        public Mixed exec(Target t, Environment environment, Mixed... args) throws ConfigRuntimeException {
            String key;
            if (args[0].isInstanceOf(CString.TYPE)) {
                key = args[0].val();
            } else {
                if (!args[0].isInstanceOf(CArray.TYPE)) {
                    throw new CREIllegalArgumentException("Argument 1 in " + this.getName() + " must be a string or array.", t);
                }

                if (((CArray)args[0]).isAssociative()) {
                    throw new CREIllegalArgumentException("Associative arrays may not be used as keys in " + this.getName(), t);
                }

                key = Global.GetNamespace((CArray)args[0], t);
            }

            Mixed c;
            c = OriginalGlobals.GetGlobalConstruct(key);

            if (args.length == 2 && c instanceof CNull) {
                c = args[1];
            }

            return c;
        }
    }

    @api
    public static class original_export extends AbstractFunction {

        public original_export() {
        }

        public String getName() {
            return "original_export";
        }

        public Integer[] numArgs() {
            return new Integer[]{2};
        }

        public String docs() {
            return "void {key, value} Stores a value in the original global storage register (like \"" + new DataHandling._export().getName() + "\".";
        }

        public Class<? extends CREThrowable>[] thrown() {
            return new Class[]{CREIllegalArgumentException.class, CREIndexOverflowException.class};
        }

        public boolean isRestricted() {
            return true;
        }

        public MSVersion since() {
            return MSVersion.V3_3_4;
        }

        public Boolean runAsync() {
            return null;
        }

        public Mixed exec(Target t, Environment environment, Mixed... args) throws ConfigRuntimeException {
            String key;
            if (args[0].isInstanceOf(CString.TYPE)) {
                key = args[0].val();
            } else {
                if (!args[0].isInstanceOf(CArray.TYPE)) {
                    throw new CREIllegalArgumentException("Argument 1 in " + this.getName() + " must be a string or array.", t);
                }

                if (((CArray)args[0]).isAssociative()) {
                    throw new CREIllegalArgumentException("Associative arrays may not be used as keys in " + this.getName(), t);
                }

                key = GetNamespace((CArray)args[0], t);
            }

            Mixed c = args[1];
            OriginalGlobals.SetGlobal(key, c);
            return CVoid.VOID;
        }
    }

    @api
    public static class x_init_thread_globals extends AbstractFunction {

        public x_init_thread_globals() {
        }

        public String getName() {
            return "x_init_thread_globals";
        }

        public Integer[] numArgs() {
            return new Integer[]{0};
        }

        public String docs() {
            return  "void {} Initializes empty globals for current thread.";
        }

        public Class<? extends CREThrowable>[] thrown() {
            return new Class[]{CREUnsupportedOperationException.class};
        }

        public boolean isRestricted() {
            return true;
        }

        public MSVersion since() {
            return MSVersion.V3_3_4;
        }

        public Mixed exec(Target t, Environment env, Mixed... args) throws ConfigRuntimeException {
            synchronized (Globals.class) {
                ExtendGlobals.GetInstance().initGlobalsForCurrentThread();
            }


            return CVoid.VOID;
        }

        public Boolean runAsync() {
            return null;
        }
    }

    @api
    public static class keys_globals extends AbstractFunction {

        public keys_globals() {
        }

        public String getName() {
            return "keys_globals";
        }

        public Integer[] numArgs() {
            return new Integer[]{0};
        }

        public String docs() {
            return  "void {} Return array of keys globals";
        }

        public Class<? extends CREThrowable>[] thrown() {
            return new Class[]{CREUnsupportedOperationException.class};
        }

        public boolean isRestricted() {
            return true;
        }

        public MSVersion since() {
            return MSVersion.V3_3_4;
        }

        public Mixed exec(Target t, Environment env, Mixed... args) throws ConfigRuntimeException {
            CArray array = new CArray(t);
            synchronized (Globals.class) {
                ExtendGlobals extendGlobals = ExtendGlobals.GetInstance();
                Set<String> keys = (extendGlobals == null ? OriginalGlobals.GetGlobals() : extendGlobals).keySet();
                for (String key :
                        keys) {
                    array.push(new CString(key, t), t);
                }
            }

            return array;
        }

        public Boolean runAsync() {
            return null;
        }
    }

    @api
    public static class is_auto_globals extends AbstractFunction {

        public is_auto_globals() {
        }

        public String getName() {
            return "is_auto_globals";
        }

        public Integer[] numArgs() {
            return new Integer[]{0};
        }

        public String docs() {
            return  "boolean {} Returns whether globals for threads are automatically initialized.";
        }

        public Class<? extends CREThrowable>[] thrown() {
            return new Class[]{};
        }

        public boolean isRestricted() {
            return true;
        }

        public MSVersion since() {
            return MSVersion.V3_3_4;
        }

        public Mixed exec(Target t, Environment env, Mixed... args) throws ConfigRuntimeException {
            DaemonManager daemonManager = ReflectionUtils.GetDaemonManager(env);
            if(daemonManager instanceof ExtendDaemonManager) {
                return CBoolean.get(((ExtendDaemonManager) daemonManager).isAutoInit());
            }

            return CBoolean.FALSE;
        }

        public Boolean runAsync() {
            return null;
        }
    }

    @api
    public static class x_remove_thread_globals extends AbstractFunction {

        public x_remove_thread_globals() {
        }

        public String getName() {
            return "x_remove_thread_globals";
        }

        public Integer[] numArgs() {
            return new Integer[]{0};
        }

        public String docs() {
            return "void {} Delete globals for the current thread. If no globals for the thread are init, nothing happen.";
        }

        public Class<? extends CREThrowable>[] thrown() {
            return new Class[]{};
        }

        public boolean isRestricted() {
            return true;
        }

        public MSVersion since() {
            return MSVersion.V3_3_4;
        }

        public Mixed exec(Target t, Environment env, Mixed... args) throws ConfigRuntimeException {
            synchronized (Globals.class) {
                ExtendGlobals.GetInstance().deleteGlobalsForCurrentThread();
            }

            return CVoid.VOID;
        }

        public Boolean runAsync() {
            return null;
        }
    }

    @api
    public static class x_set_auto_globals extends AbstractFunction {

        public x_set_auto_globals() {
        }

        public String getName() {
            return "x_set_auto_globals";
        }

        public Integer[] numArgs() {
            return new Integer[]{1};
        }

        public String docs() {
            return  "void {auto} Sets automatically initialize globals for new threads.";
        }

        public Class<? extends CREThrowable>[] thrown() {
            return new Class[]{};
        }

        public boolean isRestricted() {
            return true;
        }

        public MSVersion since() {
            return MSVersion.V3_3_4;
        }

        public Mixed exec(Target t, Environment env, Mixed... args) throws ConfigRuntimeException {
            boolean autoInit = ArgumentValidation.getBooleanish(args[0], t);

            synchronized (Globals.class) {
                DaemonManager daemonManager = ReflectionUtils.GetDaemonManager(env);
                if (daemonManager instanceof ExtendDaemonManager) {
                    ExtendDaemonManager extendManager = (ExtendDaemonManager) daemonManager;
                    extendManager.setAutoInit(autoInit);
                } else {
                    ExtendDaemonManager extendManager = new ExtendDaemonManager(daemonManager, autoInit);
                    ReflectionUtils.SetDaemonManager(env, extendManager);
                }
            }

            return CVoid.VOID;
        }

        public Boolean runAsync() {
            return null;
        }
    }
}
