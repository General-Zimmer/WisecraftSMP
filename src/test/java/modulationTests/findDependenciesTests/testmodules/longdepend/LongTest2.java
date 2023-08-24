package modulationTests.findDependenciesTests.testmodules.longdepend;

import modulationTests.findDependenciesTests.testmodules.generic.GenericTest1;
import xyz.wisecraft.smp.modulation.ModuleClass;

import java.util.ArrayList;

public class LongTest2 implements ModuleClass {
    @Override
    public void onEnable() {

    }

    @Override
    public ArrayList<Class<? extends ModuleClass>> getModuleDepends() {
        ArrayList<Class<? extends ModuleClass>> depends = new ArrayList<>();
        depends.add(LongTest1.class);
        depends.add(GenericTest1.class);
        return depends;
    }
}
