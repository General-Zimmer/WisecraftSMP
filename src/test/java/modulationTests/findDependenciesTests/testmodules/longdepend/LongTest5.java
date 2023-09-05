package modulationTests.findDependenciesTests.testmodules.longdepend;

import xyz.wisecraft.smp.modulation.ModuleClass;

import java.util.ArrayList;

public class LongTest5 implements ModuleClass {
    @Override
    public void onEnable() {

    }

    @Override
    public ArrayList<Class<? extends ModuleClass>> getModuleDepends() {
        ArrayList<Class<? extends ModuleClass>> depends = new ArrayList<>();
        depends.add(LongTest4.class);
        return depends;
    }
}
