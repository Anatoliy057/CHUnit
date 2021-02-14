package me.anatoliy57.chunit.functions;

import com.laytonsmith.annotations.api;
import com.laytonsmith.core.MSVersion;
import com.laytonsmith.core.constructs.CClosure;
import com.laytonsmith.core.constructs.CVoid;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.environments.Environment;
import com.laytonsmith.core.exceptions.CRE.CRECastException;
import com.laytonsmith.core.exceptions.CRE.CREThrowable;
import com.laytonsmith.core.exceptions.ConfigRuntimeException;
import com.laytonsmith.core.functions.AbstractFunction;
import com.laytonsmith.core.natives.interfaces.Mixed;
import me.anatoliy57.chunit.util.ReflectionUtils;

public class Closures {

    public static String docs() {
        return "A set of functions for manipulation of closures";
    }

    @api
    public static class set_current_env extends AbstractFunction {

        @Override
        public String getName() {
            return "set_current_env";
        }

        @Override
        public Integer[] numArgs() {
            return new Integer[]{1};
        }

        @Override
        public String docs() {
            return "void {} Replaces the internal closing environment with the current one";
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
            if(args[0].isInstanceOf(CClosure.TYPE)) {
                CClosure closure = (CClosure) args[0];
                ReflectionUtils.SetEnvClosure(closure, environment);
            } else {
                throw new CRECastException("Only a closure (created from the closure function) can be sent to "+getName()+"()", t);
            }

            return CVoid.VOID;
        }

        @Override
        public MSVersion since() {
            return MSVersion.V3_3_4;
        }
    }
}
