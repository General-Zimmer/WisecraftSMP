package modulation_tests.findDependenciesTests.testmodules.generic;

import xyz.wisecraft.smp.modulation.interfaces.Module;
import xyz.wisecraft.smp.modulation.ModuleClass;

import java.util.ArrayList;

public class GenericTest3 extends ModuleClass {
    @Override
    public void onEnable() {

    }

    @Override
    public ArrayList<Class<? extends Module>> getModuleDepends() {
        ArrayList<Class<? extends Module>> depends = new ArrayList<>();
        depends.add(GenericTest1.class);
        depends.add(GenericTest2.class);
        return depends;
    }
}