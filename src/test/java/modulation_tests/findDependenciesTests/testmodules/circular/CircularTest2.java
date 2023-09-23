package modulation_tests.findDependenciesTests.testmodules.circular;

import xyz.wisecraft.smp.modulation.Module;
import xyz.wisecraft.smp.modulation.models.ModuleClass;

import java.util.ArrayList;

public class CircularTest2 extends ModuleClass {
    @Override
    public void onEnable() {

    }

    @Override
    public ArrayList<Class<? extends Module>> getModuleDepends() {
        ArrayList<Class<? extends Module>> depends = new ArrayList<>();
        depends.add(CircularTest1.class);
        return depends;
    }
}