package me.anatoliy57.chunit.functions;

import com.laytonsmith.annotations.api;
import com.laytonsmith.core.MSVersion;
import com.laytonsmith.core.Procedure;
import com.laytonsmith.core.constructs.*;
import com.laytonsmith.core.environments.Environment;
import com.laytonsmith.core.environments.GlobalEnv;
import com.laytonsmith.core.exceptions.CRE.CRECastException;
import com.laytonsmith.core.exceptions.CRE.CREIndexOverflowException;
import com.laytonsmith.core.exceptions.CRE.CREInvalidProcedureException;
import com.laytonsmith.core.exceptions.CRE.CREThrowable;
import com.laytonsmith.core.exceptions.ConfigRuntimeException;
import com.laytonsmith.core.functions.AbstractFunction;
import com.laytonsmith.core.natives.interfaces.Mixed;
import me.anatoliy57.chunit.core.Function;
import me.anatoliy57.chunit.core.ProcedureProxy;

import java.util.*;

public class Environments {
    public static String docs() {
        return "A set of functions for environment.";
    }

    public static final Map<String, Environment> environmentMap = new HashMap<>();

    @api
    public static class save_env extends AbstractFunction {

        public save_env() {
        }

        public String getName() {
            return "save_env";
        }

        public Integer[] numArgs() {
            return new Integer[]{1};
        }

        public String docs() {
            return "void {id} Save reference of environment by id.";
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
            String id = args[0].val();
            synchronized (environmentMap) {
                environmentMap.put(id, env);
            }

            return CVoid.VOID;
        }

        public Boolean runAsync() {
            return null;
        }
    }

    @api
    public static class remove_env extends AbstractFunction {

        public remove_env() {
        }

        public String getName() {
            return "remove_env";
        }

        public Integer[] numArgs() {
            return new Integer[]{1};
        }

        public String docs() {
            return "void {id} Remove reference of environment by id.";
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
            String id = args[0].val();
            synchronized (environmentMap) {
                environmentMap.remove(id);
            }

            return CVoid.VOID;
        }

        public Boolean runAsync() {
            return null;
        }
    }

    @api
    public static class x_replace_procedure extends AbstractFunction {

        public x_replace_procedure() {
        }

        public String getName() {
            return "x_replace_procedure";
        }

        public Integer[] numArgs() {
            return new Integer[]{2, 3};
        }

        public String docs() {
            return "void {[id], proc, replacement} Swaps one procedure for function in a saved environment (or current if two arguments passed).";
        }

        public Class<? extends CREThrowable>[] thrown() {
            return new Class[]{CRECastException.class, CREInvalidProcedureException.class, CREIndexOverflowException.class};
        }

        public boolean isRestricted() {
            return true;
        }

        public MSVersion since() {
            return MSVersion.V3_3_4;
        }

        public Mixed exec(Target t, Environment env, Mixed... args) throws ConfigRuntimeException {

            Environment savedEnv;
            if (args.length == 2) {
                savedEnv = env;
            } else {
                String id = args[0].val();
                synchronized (environmentMap) {
                    savedEnv = Optional.ofNullable(environmentMap.get(id)).orElseThrow(() -> {
                        throw new CREIndexOverflowException("No environment with this id \"" + id + "\" found", t);
                    });
                }
            }

            String proc = args[args.length - 2].val();
            Mixed replacement = args[args.length - 1];
            if (!replacement.isInstanceOf(Function.TYPE)) {
                throw new CRECastException("Expecting a function for argument 2", t);
            }
            Function func = (Function) replacement;

            GlobalEnv global = savedEnv.getEnv(GlobalEnv.class);
            Map<String, Procedure> procedures = global.GetProcs();
            if (!procedures.containsKey(proc)) {
                throw new CREInvalidProcedureException("Unknown procedure \"" + proc + "\" in saved environment", t);
            }

            Procedure val = new ProcedureProxy(proc, func, t);
            procedures.put(proc, val);

            return CVoid.VOID;
        }

        public Boolean runAsync() {
            return null;
        }
    }

    @api
    public static class x_remove_procedure extends AbstractFunction {

        public x_remove_procedure() {
        }

        public String getName() {
            return "x_remove_procedure";
        }

        public Integer[] numArgs() {
            return new Integer[]{1, 2};
        }

        public String docs() {
            return "boolean {[id], procName} Remove procedure from a saved environment (or current if two arguments passed).";
        }

        public Class<? extends CREThrowable>[] thrown() {
            return new Class[]{CREIndexOverflowException.class};
        }

        public boolean isRestricted() {
            return true;
        }

        public MSVersion since() {
            return MSVersion.V3_3_4;
        }

        public Mixed exec(Target t, Environment env, Mixed... args) throws ConfigRuntimeException {
            Environment savedEnv;
            String proc = args[args.length - 1].val();

            if (args.length == 1) {
                savedEnv = env;
            } else {
                String id = args[0].val();
                synchronized (environmentMap) {
                    savedEnv = Optional.ofNullable(environmentMap.get(id)).orElseThrow(() -> {
                        throw new CREIndexOverflowException("No environment with this id \"" + id + "\" found", t);
                    });
                }
            }

            GlobalEnv global = savedEnv.getEnv(GlobalEnv.class);
            Map<String, Procedure> procedures = global.GetProcs();

            return CBoolean.get(procedures.remove(proc) != null);
        }

        public Boolean runAsync() {
            return null;
        }
    }
}
