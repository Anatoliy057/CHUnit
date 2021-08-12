package me.anatoliy57.chunit.functions;

import com.laytonsmith.annotations.api;
import com.laytonsmith.core.ArgumentValidation;
import com.laytonsmith.core.MSVersion;
import com.laytonsmith.core.Procedure;
import com.laytonsmith.core.constructs.CClosure;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.environments.Environment;
import com.laytonsmith.core.environments.GlobalEnv;
import com.laytonsmith.core.exceptions.CRE.CRECastException;
import com.laytonsmith.core.exceptions.CRE.CREInvalidProcedureException;
import com.laytonsmith.core.exceptions.CRE.CREThrowable;
import com.laytonsmith.core.exceptions.ConfigRuntimeException;
import com.laytonsmith.core.functions.AbstractFunction;
import com.laytonsmith.core.functions.DataHandling;
import com.laytonsmith.core.natives.interfaces.Mixed;
import me.anatoliy57.chunit.core.Function;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Functions {
    public static String docs() {
        return "A set of functions for environment.";
    }

    @api
    public static class to_func extends AbstractFunction {

        public to_func() {}

        public String getName() {
            return "to_func";
        }

        public Integer[] numArgs() {
            return new Integer[]{Integer.MAX_VALUE};
        }

        public String docs() {
            return "func {proc, [values...]} Save given procedure as variable with default arguments.";
        }

        public Class<? extends CREThrowable>[] thrown() {
            return new Class[]{CREInvalidProcedureException.class};
        }

        public boolean isRestricted() {
            return true;
        }

        public MSVersion since() {
            return MSVersion.V3_3_4;
        }

        public Mixed exec(Target t, Environment env, Mixed... args) throws ConfigRuntimeException {
            String procName = args[0].val();
            GlobalEnv global = env.getEnv(GlobalEnv.class);
            Map<String, Procedure> procedures = global.GetProcs();
            if (!procedures.containsKey(procName)) {
                throw new CREInvalidProcedureException("Unknown procedure \"" + procName + "\" in current environment", t);
            }
            Procedure proc = procedures.get(procName);
            Function func;
            if (args.length == 1) {
                func = new Function(proc);
            } else {
                func = new Function(proc, Arrays.copyOfRange(args, 1, args.length));
            }

            return func;
        }

        public Boolean runAsync() {
            return null;
        }
    }

    @api
    public static class do_func extends AbstractFunction {

        public do_func() {}

        public String getName() {
            return "do_func";
        }

        public Integer[] numArgs() {
            return new Integer[]{Integer.MAX_VALUE};
        }

        public String docs() {
            return "mixed {func, [values...]} Execution function with given arguments.";
        }

        public Class<? extends CREThrowable>[] thrown() {
            return new Class[]{CRECastException.class};
        }

        public boolean isRestricted() {
            return true;
        }

        public MSVersion since() {
            return MSVersion.V3_3_4;
        }

        public Mixed exec(Target t, Environment env, Mixed... args) throws ConfigRuntimeException {
            if(args[0].isInstanceOf(Function.TYPE)) {
                Mixed[] vals = new Mixed[args.length - 1];
                System.arraycopy(args, 1, vals, 0, args.length - 1);
                Function func = (Function) args[0];
                return func.execute(env, t, vals);
            } else {
                throw new CRECastException("Only a function (created from the to_func) can be sent to do_func()", t);
            }
        }

        public Boolean runAsync() {
            return null;
        }
    }

    @api
    public static class do_func_array extends AbstractFunction {

        public do_func_array() {}

        public String getName() {
            return "do_func_array";
        }

        public Integer[] numArgs() {
            return new Integer[]{2};
        }

        public String docs() {
            return "mixed {func, valueArray} Execution function with given array arguments.";
        }

        public Class<? extends CREThrowable>[] thrown() {
            return new Class[]{CRECastException.class};
        }

        public boolean isRestricted() {
            return true;
        }

        public MSVersion since() {
            return MSVersion.V3_3_4;
        }

        public Mixed exec(Target t, Environment env, Mixed... args) throws ConfigRuntimeException {
            if(args[0].isInstanceOf(Function.TYPE)) {
                List<Mixed> arrayArgs = ArgumentValidation.getArray(args[1], t).asList();
                Function func = (Function) args[0];
                return func.execute(arrayArgs, env, t);
            } else {
                throw new CRECastException("Only a function (created from the to_func) can be sent to do_func()", t);
            }
        }

        public Boolean runAsync() {
            return null;
        }
    }
}
