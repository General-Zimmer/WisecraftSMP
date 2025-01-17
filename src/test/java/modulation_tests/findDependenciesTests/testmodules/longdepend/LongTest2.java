package modulation_tests.findDependenciesTests.testmodules.longdepend;

import modulation_tests.findDependenciesTests.testmodules.generic.GenericTest1;
import xyz.wisecraft.smp.modulation.interfaces.Module;
import xyz.wisecraft.smp.modulation.ModuleClass;

import java.util.ArrayList;

public class LongTest2 extends ModuleClass {
    @Override
    public void onEnable() {

    }

    @Override
    public ArrayList<Class<? extends Module>> getModuleDepends() {
        ArrayList<Class<? extends Module>> depends = new ArrayList<>();
        depends.add(LongTest1.class);
        depends.add(GenericTest1.class);
        return depends;
    }
}
