package modulationTests.findDependenciesTests.testmodules.circular;

import xyz.wisecraft.smp.modulation.ModuleClass;

import java.util.ArrayList;

public class CircularTest2 implements ModuleClass {
    @Override
    public void onEnable() {

    }

    @Override
    public ArrayList<Class<? extends ModuleClass>> getModuleDepends() {
        ArrayList<Class<? extends ModuleClass>> depends = new ArrayList<>();
        depends.add(CircularTest1.class);
        return depends;
    }
}