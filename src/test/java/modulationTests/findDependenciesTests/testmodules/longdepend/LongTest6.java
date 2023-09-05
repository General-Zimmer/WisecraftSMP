package modulationTests.findDependenciesTests.testmodules.longdepend;

import xyz.wisecraft.smp.modulation.ModuleClass;

import java.util.ArrayList;

public class LongTest6 implements ModuleClass {
    @Override
    public void onEnable() {

    }

    @Override
    public ArrayList<Class<? extends ModuleClass>> getModuleDepends() {
        ArrayList<Class<? extends ModuleClass>> depends = new ArrayList<>();
        depends.add(LongTest5.class);
        depends.add(LongTest1.class);
        return depends;
    }
}
