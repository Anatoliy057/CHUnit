package me.anatoliy57.chunit.functions;

import com.laytonsmith.annotations.api;
import com.laytonsmith.core.MSVersion;
import com.laytonsmith.core.constructs.*;
import com.laytonsmith.core.environments.Environment;
import com.laytonsmith.core.exceptions.CRE.CRECastException;
import com.laytonsmith.core.exceptions.CRE.CREThrowable;
import com.laytonsmith.core.exceptions.ConfigRuntimeException;
import com.laytonsmith.core.functions.AbstractFunction;
import com.laytonsmith.core.natives.interfaces.Mixed;

import java.util.Map;

import static com.laytonsmith.core.functions.Threading.THREAD_ID_MAP;

public class Threading {

    public static String docs() {
        return "A set of functions for interacting with threads.";
    }

    public static abstract class FileFunction extends AbstractFunction {
        @Override
        public MSVersion since() {
            return MSVersion.V3_3_4;
        }

        @Override
        public Boolean runAsync() {
            return null;
        }

        @Override
        public boolean isRestricted() {
            return true;
        }

        @Override
        public Class<? extends CREThrowable>[] thrown() {
            return new Class[]{CRECastException.class};
        }
    }

    @api
    public static class dump_keys_threads extends FileFunction {

        @Override
        public String getName() {
            return "dump_keys_threads";
        }

        @Override
        public Integer[] numArgs() {
            return new Integer[]{0};
        }

        @Override
        public String docs() {
            return "array {} Returns an array of all threads keys that are currently running.";
        }

        @Override
        public Mixed exec(final Target t, final Environment env, final Mixed... args) throws ConfigRuntimeException {
            CArray carray = new CArray(t);
            synchronized (THREAD_ID_MAP) {
                for (Map.Entry<String, Thread> e :
                        THREAD_ID_MAP.entrySet()) {
                    if (e.getValue().isAlive()) {
                        carray.push(new CString(e.getKey(), t), t);
                    }
                }
            }
            return carray;
        }

        @Override
        public Class<? extends CREThrowable>[] thrown() {
            return null;
        }

        @Override
        public Boolean runAsync() {
            return null;
        }

        @Override
        public boolean isRestricted() {
            return true;
        }
    }

    @api
    public static class x_stop_thread extends FileFunction {

        @Override
        public String getName() {
            return "x_stop_thread";
        }

        @Override
        public Integer[] numArgs() {
            return new Integer[]{1};
        }

        @Override
        public String docs() {
            return "boolean {id} Stopping tracked thread named \'id\'. If successful returns true, else false."
                    + " If the thread performs a " + new x_safe_execute().getName() + "() function, the interrupting thread will wait for execution.";
        }

        @Override
        public Mixed exec(Target t, Environment env, Mixed... args) throws ConfigRuntimeException {
            String id = args[0].val();
            Thread thread;
            synchronized (THREAD_ID_MAP) {
                thread = THREAD_ID_MAP.get(id);
            }
            if (thread != null) {
                synchronized (thread) {
                    thread.stop();
                    return CBoolean.TRUE;
                }
            }
            return CBoolean.FALSE;
        }

        @Override
        public Class<? extends CREThrowable>[] thrown() {
            return null;
        }

        @Override
        public Boolean runAsync() {
            return null;
        }

        @Override
        public boolean isRestricted() {
            return true;
        }
    }

    @api
    public static class x_safe_execute extends FileFunction {

        @Override
        public String getName() {
            return "x_safe_execute";
        }

        @Override
        public Integer[] numArgs() {
            return new Integer[]{Integer.MAX_VALUE};
        }

        @Override
        public String docs() {
            return "void {[values...], closure} Executes the given closure. You can also send arguments"
                    + " to the closure, which it may or may not use, depending on the particular closure's"
                    + " definition. Unlike closure, it returns only void and will be executed even if the thread was stopped by a function" + new x_stop_thread().getName() + "().";
        }

        @Override
        public Mixed exec(Target t, Environment env, Mixed... args) throws ConfigRuntimeException {
            if (args[args.length - 1].isInstanceOf(CClosure.TYPE)) {
                Mixed[] vals = new Mixed[args.length - 1];
                System.arraycopy(args, 0, vals, 0, args.length - 1);
                CClosure closure = (CClosure) args[args.length - 1];
                synchronized (Thread.currentThread()) {
                    closure.executeCallable(vals);
                }
                return CVoid.VOID;
            } else {
                throw new CRECastException("Only a closure (created from the closure function) can be sent to execute()", t);
            }
        }

        @Override
        public Class<? extends CREThrowable>[] thrown() {
            return new Class[]{CRECastException.class};
        }

        @Override
        public Boolean runAsync() {
            return null;
        }

        @Override
        public boolean isRestricted() {
            return true;
        }
    }
}
