package me.anatoliy57.chunit.functions;

import com.laytonsmith.annotations.api;
import com.laytonsmith.core.MSVersion;
import com.laytonsmith.core.constructs.CClosure;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.environments.Environment;
import com.laytonsmith.core.exceptions.CRE.CRECastException;
import com.laytonsmith.core.exceptions.CRE.CREThrowable;
import com.laytonsmith.core.exceptions.ConfigRuntimeException;
import com.laytonsmith.core.functions.AbstractFunction;
import com.laytonsmith.core.natives.interfaces.Mixed;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class Executes {

    public static String docs() {
        return "A set of functions for specific execution of closures";
    }

    @api
    public static class execute_current extends AbstractFunction {

        @Override
        public String getName() {
            return "execute_current";
        }

        @Override
        public Integer[] numArgs() {
            return new Integer[]{Integer.MAX_VALUE};
        }

        @Override
        public String docs() {
            return "mixed {[values...], closure} Executes the given closure like execute(), but with current environment."
                    + " WARNING: the closure must use already defined variables and procedures to avoid compilation errors.";
        }

        @Override
        public Class<? extends CREThrowable>[] thrown() {
            return new Class[]{CRECastException.class};
        }

        @Override
        public boolean isRestricted() {
            return true;
        }

        @Override
        public Boolean runAsync() {
            return null;
        }

        @Override
        public Mixed exec(Target t, Environment environment, Mixed... args) throws ConfigRuntimeException {
            if(args[args.length - 1].isInstanceOf(CClosure.TYPE)) {
                Mixed[] vars = new Mixed[args.length - 1];
                System.arraycopy(args, 0, vars, 0, args.length - 1);
                CClosure closure = (CClosure) args[args.length - 1];
                setEnvironment(closure, environment);
                return closure.executeCallable(vars);
            } else {
                throw new CRECastException("Only a closure (created from the closure function) can be sent to "+getName()+"()", t);
            }
        }

        private void setEnvironment(CClosure closure, Environment env) {
            try {
                Field field = CClosure.class.getDeclaredField("env");
                field.setAccessible(true);
                Field modifiersField = Field.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);
                modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
                field.set(closure, env);
                modifiersField.setInt(field, field.getModifiers() | Modifier.FINAL);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                // ignored exceptions
            }
        }

        @Override
        public MSVersion since() {
            return MSVersion.V3_3_4;
        }
    }
}
