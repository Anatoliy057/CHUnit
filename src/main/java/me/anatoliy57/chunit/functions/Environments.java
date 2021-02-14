package me.anatoliy57.chunit.functions;

import com.laytonsmith.annotations.api;
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
import com.laytonsmith.core.natives.interfaces.Mixed;

import java.util.*;

public class Environments {
    public static String docs() {
        return "A set of functions for environment";
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
            return "void {id} Save reference of environment by id";
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
            return "void {id} Remove reference of environment by id";
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
            return "void {id, proc, replacement} Swaps one procedure for another (or given closure) in a saved environment";
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
                throw new CREInvalidProcedureException("Unknown procedure \"" + proc + '"', t);
            }

            Procedure val;
            Mixed replacement = args[2];
            if(replacement.isInstanceOf(CClosure.TYPE)) {
                val = new ProcClosure(proc, (CClosure) replacement, t);
            } else {
                val = Optional.ofNullable(procedures.get(replacement.val())).orElseThrow(() -> {
                    throw new CREInvalidProcedureException("Unknown procedure \"" + replacement.val() + '"', t);
                });
            }

            procedures.put(proc, val);

            return CVoid.VOID;
        }

        public Boolean runAsync() {
            return null;
        }

        private static class ProcClosure extends Procedure implements Cloneable {

            private final String name;
            private CClosure closure;

            public ProcClosure(String name, CClosure closure, Target t) {
                super(name, null, Collections.EMPTY_LIST, null, t);

                this.name = name;
                this.closure = closure;
            }

            @Override
            public Mixed execute(List<Mixed> args, Environment oldEnv, Target t) {
                Mixed[] values = new Mixed[args.size()];
                args.toArray(values);
                return closure.executeCallable(values);
            }

            @Override
            public Procedure clone() throws CloneNotSupportedException {
                ProcClosure clone = (ProcClosure)super.clone();

                if(closure != null) {
                    clone.closure = this.closure.clone();
                }

                return clone;
            }

            @Override
            public void definitelyNotConstant() {
                super.definitelyNotConstant();
            }

            @Override
            public String getName() {
                return name;
            }
        }
    }
}
