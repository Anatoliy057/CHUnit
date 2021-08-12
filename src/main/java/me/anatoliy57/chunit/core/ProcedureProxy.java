package me.anatoliy57.chunit.core;

import com.laytonsmith.core.Procedure;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.environments.Environment;
import com.laytonsmith.core.natives.interfaces.Mixed;

import java.util.Collections;
import java.util.List;

public class ProcedureProxy extends Procedure {
    private final String name;
    private Function func;

    public ProcedureProxy(String name, Function func, Target t) {
        super(name, null, Collections.EMPTY_LIST, null, t);

        this.name = name;
        this.func = func;
    }

    @Override
    public Mixed execute(List<Mixed> args, Environment oldEnv, Target t) {
        return func.execute(args, oldEnv, t);
    }

    @Override
    public Procedure clone() throws CloneNotSupportedException {
        ProcedureProxy clone = (ProcedureProxy)super.clone();
        clone.func = (Function)this.func.clone();

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