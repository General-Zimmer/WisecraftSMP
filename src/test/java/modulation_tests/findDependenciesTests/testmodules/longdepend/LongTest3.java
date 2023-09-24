package modulation_tests.findDependenciesTests.testmodules.longdepend;

import xyz.wisecraft.smp.modulation.interfaces.Module;
import xyz.wisecraft.smp.modulation.ModuleClass;

import java.util.ArrayList;

public class LongTest3 extends ModuleClass {
    @Override
    public void onEnable() {

    }

    @Override
    public ArrayList<Class<? extends Module>> getModuleDepends() {
        ArrayList<Class<? extends Module>> depends = new ArrayList<>();
        depends.add(LongTest1.class);
        depends.add(LongTest2.class);
        return depends;
    }
}
