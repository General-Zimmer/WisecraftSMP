package modulationTests.findDependenciesTests.testmodules;

import xyz.wisecraft.smp.modulation.ModuleClass;

import java.util.ArrayList;

public class TestModule2 implements ModuleClass {
    @Override
    public void onEnable() {

    }

    @Override
    public ArrayList<Class<? extends ModuleClass>> getModuleDepends() {
        ArrayList<Class<? extends ModuleClass>> depends = new ArrayList<>();
        depends.add(TestModule1.class);
        return depends;
    }
}