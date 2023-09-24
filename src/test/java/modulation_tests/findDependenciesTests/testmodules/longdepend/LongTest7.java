package modulation_tests.findDependenciesTests.testmodules.longdepend;

import xyz.wisecraft.smp.modulation.interfaces.Module;
import xyz.wisecraft.smp.modulation.ModuleClass;

import java.util.ArrayList;

public class LongTest7 extends ModuleClass {
    @Override
    public void onEnable() {

    }

    @Override
    public ArrayList<Class<? extends Module>> getModuleDepends() {
        ArrayList<Class<? extends Module>> depends = new ArrayList<>();
        depends.add(LongTest5.class);
        depends.add(LongTest6.class);
        depends.add(LongTest4.class);
        depends.add(LongTest3.class);
        return depends;
    }
}
