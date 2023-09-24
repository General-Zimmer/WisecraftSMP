package modulation_tests.findDependenciesTests.testmodules.circular;

import xyz.wisecraft.smp.modulation.interfaces.Module;
import xyz.wisecraft.smp.modulation.ModuleClass;

import java.util.ArrayList;

public class CircularTest1 extends ModuleClass {
    @Override
    public void onEnable() {

    }

    @Override
    public ArrayList<Class<? extends Module>> getModuleDepends() {
        ArrayList<Class<? extends Module>> depends = new ArrayList<>();
        depends.add(CircularTest2.class);
        return depends;
    }
}