package me.anatoliy57.chunit;

import com.laytonsmith.PureUtilities.SimpleVersion;
import com.laytonsmith.PureUtilities.Version;
import com.laytonsmith.abstraction.Implementation;
import com.laytonsmith.core.extensions.AbstractExtension;
import com.laytonsmith.core.extensions.MSExtension;

@MSExtension("CHUnit")
public class LifeCycle extends AbstractExtension {
    public Version getVersion() {
        return new SimpleVersion(1, 0, 0);
    }

    @Override
    public void onStartup() {
        if(!Implementation.GetServerType().equals(Implementation.Type.SHELL)) {
            System.out.println("CHUnit " + getVersion() + " loaded.");
        }
    }

    @Override
    public void onShutdown() {
        if(!Implementation.GetServerType().equals(Implementation.Type.SHELL)) {
            System.out.println("CHUnit " + getVersion() + " unloaded.");
        }
    }
}
