package me.anatoliy57.chunit.functions;

import com.laytonsmith.PureUtilities.TermColors;
import com.laytonsmith.annotations.api;
import com.laytonsmith.core.MSVersion;
import com.laytonsmith.core.Static;
import com.laytonsmith.core.constructs.CVoid;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.environments.Environment;
import com.laytonsmith.core.exceptions.CRE.CREThrowable;
import com.laytonsmith.core.exceptions.ConfigRuntimeException;
import com.laytonsmith.core.functions.AbstractFunction;
import com.laytonsmith.core.natives.interfaces.Mixed;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Echo {
    public static String docs() {
        return "A set of functions for logs";
    }

    public static BufferedWriter out;

    static {
        out = new BufferedWriter(new OutputStreamWriter(new
                FileOutputStream(java.io.FileDescriptor.out), StandardCharsets.US_ASCII), 512);
    }

    @api
    public static class print extends AbstractFunction {

        public print() {
        }

        public String getName() {
            return "print";
        }

        public Integer[] numArgs() {
            return new Integer[]{1};
        }

        public String docs() {
            return "void {message} Prints a message";
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
            String mes = Static.MCToANSIColors(args[0].val());

            if (mes.contains("\u001b")) {
                mes = mes + TermColors.reset();
            }

            try {
                Echo.out.write(mes);
                out.flush();
            } catch (IOException ignored) {}

            return CVoid.VOID;
        }

        public Boolean runAsync() {
            return null;
        }
    }

    @api
    public static class println extends AbstractFunction {

        public println() {
        }

        public String getName() {
            return "println";
        }

        public Integer[] numArgs() {
            return new Integer[]{0, 1};
        }

        public String docs() {
            return "void {message} Prints a message and then terminate the line.";
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
            String mes;
            if (args.length != 0) {
                mes = Static.MCToANSIColors(args[0].val());
            } else {
                mes = "";
            }

            if (mes.contains("\u001b")) {
                mes = mes + TermColors.reset();
            }

            try {
                Echo.out.write(mes);
                Echo.out.write('\n');
                out.flush();
            } catch (IOException ignored) {}
            return CVoid.VOID;
        }

        public Boolean runAsync() {
            return null;
        }
    }
}
