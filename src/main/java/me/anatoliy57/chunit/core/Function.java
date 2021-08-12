package me.anatoliy57.chunit.core;

import com.laytonsmith.PureUtilities.ClassLoading.ClassDiscovery;
import com.laytonsmith.PureUtilities.Version;
import com.laytonsmith.annotations.typeof;
import com.laytonsmith.core.Documentation;
import com.laytonsmith.core.MSVersion;
import com.laytonsmith.core.Procedure;
import com.laytonsmith.core.constructs.CClassType;
import com.laytonsmith.core.constructs.Construct;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.environments.Environment;
import com.laytonsmith.core.natives.interfaces.Mixed;
import com.laytonsmith.core.objects.AccessModifier;
import com.laytonsmith.core.objects.ObjectModifier;
import com.laytonsmith.core.objects.ObjectType;

import java.net.URL;
import java.util.*;

@typeof("me.ay57.chunit.function")
public class Function implements Mixed {

    public static final CClassType TYPE = CClassType.get(Function.class);
    private final List<Mixed> defaultArgs;
    private final UUID id;
    private final Procedure proc;
    private Target target;

    public Function(Procedure proc) {
        id = UUID.randomUUID();
        this.proc = proc;
        this.defaultArgs = new ArrayList<>();
    }

    public Function(Procedure proc, Mixed...args) {
        id = UUID.randomUUID();
        this.proc = proc;
        this.defaultArgs = Arrays.asList(args);
    }

    public Procedure getProcedure() {
        return proc;
    }

    public Mixed execute(Environment env, Target t, Mixed... args) {
        List<Mixed> allArgs = new ArrayList<>();
        allArgs.addAll(defaultArgs);
        allArgs.addAll(Arrays.asList(args));
        return proc.execute(allArgs, env, t);
    }

    public Mixed execute(List<Mixed> args, Environment env, Target t) {
        List<Mixed> allArgs = new ArrayList<>();
        allArgs.addAll(defaultArgs);
        allArgs.addAll(args);
        return proc.execute(allArgs, env, t);
    }

    @Override
    public String val() {
        return String.format("Function %s: %s", proc.getName(), Arrays.toString(defaultArgs.toArray()));
    }

    @Override
    public void setTarget(Target target) {
        this.target = target;
    }

    @Override
    public Target getTarget() {
        return target;
    }

    @Override
    public Mixed clone() throws CloneNotSupportedException {
        return this;
    }

    @Override
    public String getName() {
        typeof t = ClassDiscovery.GetClassAnnotation(this.getClass(), typeof.class);
        return t.value();
    }

    @Override
    public String docs() {
        return "Variable that contains procedure";
    }

    @Override
    public Version since() {
        return MSVersion.V3_3_4;
    }

    @Override
    public CClassType[] getSuperclasses() {
        return new CClassType[]{Mixed.TYPE};
    }

    @Override
    public CClassType[] getInterfaces() {
        return CClassType.EMPTY_CLASS_ARRAY;
    }

    @Override
    public ObjectType getObjectType() {
        return ObjectType.CLASS;
    }

    @Override
    public Set<ObjectModifier> getObjectModifiers() {
        return EnumSet.noneOf(ObjectModifier.class);
    }

    @Override
    public AccessModifier getAccessModifier() {
        return AccessModifier.PUBLIC;
    }

    @Override
    public CClassType getContainingClass() {
        return null;
    }

    @Override
    public boolean isInstanceOf(CClassType type) {
        if(type.getNativeType() != null) {
            return type.getNativeType().isAssignableFrom(this.getClass());
        }
        return Construct.isInstanceof(this, type);
    }

    @Override
    public boolean isInstanceOf(Class<? extends Mixed> type) {
        return type.isAssignableFrom(this.getClass());
    }

    @Override
    public CClassType typeof() {
        return CClassType.get(this.getClass());
    }

    @Override
    public URL getSourceJar()  {
        return ClassDiscovery.GetClassContainer(this.getClass());
    }

    @Override
    public Class<? extends Documentation>[] seeAlso() {
        return new Class[]{};
    }
}
