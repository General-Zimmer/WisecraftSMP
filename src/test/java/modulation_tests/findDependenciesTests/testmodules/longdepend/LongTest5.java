package modulation_tests.findDependenciesTests.testmodules.longdepend;

import xyz.wisecraft.smp.modulation.Module;
import xyz.wisecraft.smp.modulation.models.ModuleClass;

import java.util.ArrayList;

public class LongTest5 extends ModuleClass {
    @Override
    public void onEnable() {

    }

    @Override
    public ArrayList<Class<? extends Module>> getModuleDepends() {
        ArrayList<Class<? extends Module>> depends = new ArrayList<>();
        depends.add(LongTest4.class);
        return depends;
    }
}
