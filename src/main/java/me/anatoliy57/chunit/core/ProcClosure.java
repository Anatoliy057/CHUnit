package me.anatoliy57.chunit.core;

import com.laytonsmith.core.Procedure;
import com.laytonsmith.core.constructs.CClosure;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.environments.Environment;
import com.laytonsmith.core.natives.interfaces.Mixed;

import java.util.Collections;
import java.util.List;

public class ProcClosure extends Procedure implements Cloneable {

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
