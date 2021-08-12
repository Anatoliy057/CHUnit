package me.anatoliy57.chunit.functions;

import com.laytonsmith.annotations.api;
import com.laytonsmith.core.MSVersion;
import com.laytonsmith.core.constructs.*;
import com.laytonsmith.core.environments.Environment;
import com.laytonsmith.core.exceptions.CRE.CREIllegalArgumentException;
import com.laytonsmith.core.exceptions.CRE.CREIndexOverflowException;
import com.laytonsmith.core.exceptions.CRE.CREThrowable;
import com.laytonsmith.core.exceptions.ConfigRuntimeException;
import com.laytonsmith.core.functions.AbstractFunction;
import com.laytonsmith.core.functions.DataHandling;
import com.laytonsmith.core.natives.interfaces.Mixed;
import me.anatoliy57.chunit.core.LocalGlobals;

public class Globals {

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


    public static final String docs = "Set of function for interactions with globals variables locally for threads";

    @api
    public static class local_import extends AbstractFunction {

        public local_import() {
        }

        public String getName() {
            return "local_import";
        }

        public Integer[] numArgs() {
            return new Integer[]{1, 2};
        }

        public String docs() {
            return "mixed {key, [default]} This function likes \"" + new DataHandling._import().getName() + "\" but imports a value from the local thread global value.";
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

            Mixed c;
            c = LocalGlobals.GetGlobalConstruct(key);

            if (args.length == 2 && c instanceof CNull) {
                c = args[1];
            }

            return c;
        }
    }

    @api
    public static class local_export extends AbstractFunction {

        public local_export() {
        }

        public String getName() {
            return "local_export";
        }

        public Integer[] numArgs() {
            return new Integer[]{2};
        }

        public String docs() {
            return "void {key, value} Stores a value in the local global for thread (like \"" + new DataHandling._export().getName() + "\").";
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
            LocalGlobals.SetGlobal(key, c);
            return CVoid.VOID;
        }
    }
}
