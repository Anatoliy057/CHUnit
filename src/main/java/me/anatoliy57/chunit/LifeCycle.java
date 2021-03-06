package me.anatoliy57.chunit;

import com.laytonsmith.PureUtilities.SimpleVersion;
import com.laytonsmith.PureUtilities.Version;
import com.laytonsmith.abstraction.Implementation;
import com.laytonsmith.core.extensions.AbstractExtension;
import com.laytonsmith.core.extensions.MSExtension;
import me.anatoliy57.chunit.core.ExtendGlobals;
import me.anatoliy57.chunit.core.OriginalGlobals;
import me.anatoliy57.chunit.functions.Global;

@MSExtension("CHUnit")
public class LifeCycle extends AbstractExtension {
    public Version getVersion() {
        return new SimpleVersion(1, 1, 0);
    }

    @Override
    public void onStartup() {
        if(!Implementation.GetServerType().equals(Implementation.Type.SHELL)) {
            System.out.println("CHUnit " + getVersion() + " loaded.");
        }

        OriginalGlobals.InitOriginalGlobal();
        ExtendGlobals.InitInstance();
    }

    @Override
    public void onShutdown() {
        if(!Implementation.GetServerType().equals(Implementation.Type.SHELL)) {
            System.out.println("CHUnit " + getVersion() + " unloaded.");
        }
    }
}
