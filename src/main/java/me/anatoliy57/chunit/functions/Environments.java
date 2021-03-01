package me.anatoliy57.chunit.functions;

import com.laytonsmith.annotations.api;
import com.laytonsmith.core.ArgumentValidation;
import com.laytonsmith.core.Globals;
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
import com.laytonsmith.core.functions.ArrayHandling;
import com.laytonsmith.core.natives.interfaces.Mixed;
import me.anatoliy57.chunit.core.ProcClosure;

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
            return MSVersion.V3_0_2;
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
            return MSVersion.V3_0_2;
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
            return new Integer[]{3};
        }

        public String docs() {
            return "void {id, proc, replacement} Swaps one procedure for another (or given closure) in a saved environment.";
        }

        public Class<? extends CREThrowable>[] thrown() {
            return new Class[]{CREInvalidProcedureException.class, CREIndexOverflowException.class};
        }

        public boolean isRestricted() {
            return true;
        }

        public MSVersion since() {
            return MSVersion.V3_0_2;
        }

        public Mixed exec(Target t, Environment env, Mixed... args) throws ConfigRuntimeException {
            String id = args[0].val();
            String proc = args[1].val();

            Environment savedEnv;
            synchronized (environmentMap) {
                savedEnv = Optional.ofNullable(environmentMap.get(id)).orElseThrow(() -> {
                    throw new CREIndexOverflowException("No environment with this id \""+id+"\" found", t);
                });
            }
            GlobalEnv global = savedEnv.getEnv(GlobalEnv.class);
            Map<String, Procedure> procedures = global.GetProcs();

            if(!procedures.containsKey(proc)) {
                throw new CREInvalidProcedureException("Unknown procedure \"" + proc + "\" in saved environment", t);
            }

            Procedure val;
            Mixed replacement = args[2];
            Map<String, Procedure> currentProcedures = env.getEnv(GlobalEnv.class).GetProcs();
            if(replacement.isInstanceOf(CClosure.TYPE)) {
                val = new ProcClosure(proc, (CClosure) replacement, t);
            } else {
                val = Optional.ofNullable(currentProcedures.get(replacement.val())).orElseThrow(() -> {
                    throw new CREInvalidProcedureException("Unknown procedure \"" + replacement.val() + "\" in current environment", t);
                });
            }

            procedures.put(proc, val);

            return CVoid.VOID;
        }

        public Boolean runAsync() {
            return null;
        }
    }

    @api
    public static class x_add_procedure extends AbstractFunction {

        public x_add_procedure() {
        }

        public String getName() {
            return "x_add_procedure";
        }

        public Integer[] numArgs() {
            return new Integer[]{2, 3};
        }

        public String docs() {
            return "void {id, procName, [closure]} Add procedure (or given closure as procedure named by procName) in a saved environment.";
        }

        public Class<? extends CREThrowable>[] thrown() {
            return new Class[]{CRECastException.class, CREInvalidProcedureException.class, CREIndexOverflowException.class};
        }

        public boolean isRestricted() {
            return true;
        }

        public MSVersion since() {
            return MSVersion.V3_0_2;
        }

        public Mixed exec(Target t, Environment env, Mixed... args) throws ConfigRuntimeException {
            String id = args[0].val();
            String proc = args[1].val();

            Environment savedEnv;
            synchronized (environmentMap) {
                savedEnv = Optional.ofNullable(environmentMap.get(id)).orElseThrow(() -> {
                    throw new CREIndexOverflowException("No environment with this id \""+id+"\" found", t);
                });
            }
            GlobalEnv global = savedEnv.getEnv(GlobalEnv.class);
            Map<String, Procedure> procedures = global.GetProcs();

            if(procedures.containsKey(proc)) {
                throw new CREInvalidProcedureException("Procedure already exists \"" + proc + "\" in saved environment", t);
            }

            Procedure val;
            if(args.length == 3) {
                if(!args[2].isInstanceOf(CClosure.TYPE)) {
                    throw new CRECastException("Expecting a closure for argument 2", t);
                }
                CClosure closure = (CClosure) args[2];
                val = new ProcClosure(proc, closure, t);
            } else {
                Map<String, Procedure> currentProcedures = env.getEnv(GlobalEnv.class).GetProcs();

                val = Optional.ofNullable(currentProcedures.get(proc)).orElseThrow(() -> {
                    throw new CREInvalidProcedureException("Unknown procedure \"" + proc + "\" in current environment", t);
                });
            }

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
            return new Integer[]{2};
        }

        public String docs() {
            return "boolean {id, procName} Remove procedure from a saved environment.";
        }

        public Class<? extends CREThrowable>[] thrown() {
            return new Class[]{CREIndexOverflowException.class};
        }

        public boolean isRestricted() {
            return true;
        }

        public MSVersion since() {
            return MSVersion.V3_0_2;
        }

        public Mixed exec(Target t, Environment env, Mixed... args) throws ConfigRuntimeException {
            String id = args[0].val();
            String proc = args[1].val();

            Environment savedEnv;
            synchronized (environmentMap) {
                savedEnv = Optional.ofNullable(environmentMap.get(id)).orElseThrow(() -> {
                    throw new CREIndexOverflowException("No environment with this id \""+id+"\" found", t);
                });
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
