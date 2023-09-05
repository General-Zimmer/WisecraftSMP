package modulationTests.findDependenciesTests.testmodules.longdepend;

import xyz.wisecraft.smp.modulation.ModuleClass;

import java.util.ArrayList;

public class LongTest3 implements ModuleClass {
    @Override
    public void onEnable() {

    }

    @Override
    public ArrayList<Class<? extends ModuleClass>> getModuleDepends() {
        ArrayList<Class<? extends ModuleClass>> depends = new ArrayList<>();
        depends.add(LongTest1.class);
        depends.add(LongTest2.class);
        return depends;
    }
}
