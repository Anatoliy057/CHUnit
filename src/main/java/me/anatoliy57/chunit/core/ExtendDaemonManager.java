package me.anatoliy57.chunit.core;

import com.laytonsmith.PureUtilities.DaemonManager;

public class ExtendDaemonManager extends DaemonManager {

    private final DaemonManager original;
    private boolean autoInit;

    public ExtendDaemonManager(DaemonManager daemonManager, boolean autoInit) {
        this.original = daemonManager;
        this.autoInit = autoInit;
    }

    public synchronized void setAutoInit(boolean autoInit) {
        this.autoInit = autoInit;
    }

    public boolean isAutoInit() {
        return autoInit;
    }

    @Override
    public void activateThread(Thread t) {
        original.activateThread(t);

        if(autoInit) {
            ExtendGlobals.GetInstance().initGlobalsForThread(t.getId());
        }
    }

    @Override
    public void deactivateThread(Thread t) {
        original.deactivateThread(t);
        ExtendGlobals.GetInstance().deleteGlobalsForThread(t.getId());
    }

    @Override
    public Thread[] getActiveThreads() {
        return original.getActiveThreads();
    }

    @Override
    public void waitForThreads() throws InterruptedException {
        original.waitForThreads();
    }
}
