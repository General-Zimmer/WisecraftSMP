package modulationTests.findDependenciesTests.testmodules;

import xyz.wisecraft.smp.modulation.ModuleClass;

import java.util.ArrayList;

public class TestModule1 implements ModuleClass {
    @Override
    public void onEnable() {

    }

    @Override
    public ArrayList<Class<? extends ModuleClass>> getModuleDepends() {
        return ModuleClass.super.getModuleDepends();
    }
}
