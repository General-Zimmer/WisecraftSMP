package modulationTests.findDependenciesTests.testmodules.longdepend;

import xyz.wisecraft.smp.modulation.ModuleClass;

import java.util.ArrayList;

public class LongTest7 implements ModuleClass {
    @Override
    public void onEnable() {

    }

    @Override
    public ArrayList<Class<? extends ModuleClass>> getModuleDepends() {
        ArrayList<Class<? extends ModuleClass>> depends = new ArrayList<>();
        depends.add(LongTest5.class);
        depends.add(LongTest6.class);
        depends.add(LongTest4.class);
        depends.add(LongTest3.class);
        return depends;
    }
}
