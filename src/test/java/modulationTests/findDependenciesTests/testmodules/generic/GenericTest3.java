package modulationTests.findDependenciesTests.testmodules.generic;

import xyz.wisecraft.smp.modulation.ModuleClass;

import java.util.ArrayList;

public class GenericTest3 implements ModuleClass {
    @Override
    public void onEnable() {

    }

    @Override
    public ArrayList<Class<? extends ModuleClass>> getModuleDepends() {
        ArrayList<Class<? extends ModuleClass>> depends = new ArrayList<>();
        depends.add(GenericTest1.class);
        depends.add(GenericTest2.class);
        return depends;
    }
}