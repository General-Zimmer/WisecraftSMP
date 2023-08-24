package modulationTests.findDependenciesTests.testmodules.longdepend;

import xyz.wisecraft.smp.modulation.ModuleClass;

import java.util.ArrayList;

public class LongTest4 implements ModuleClass {
    @Override
    public void onEnable() {

    }

    @Override
    public ArrayList<Class<? extends ModuleClass>> getModuleDepends() {
        ArrayList<Class<? extends ModuleClass>> depends = new ArrayList<>();
        depends.add(LongTest3.class);
        return depends;
    }
}
