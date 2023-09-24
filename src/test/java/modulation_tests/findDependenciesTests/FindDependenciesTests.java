package modulation_tests.findDependenciesTests;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import modulation_tests.findDependenciesTests.testmodules.circular.CircularTest1;
import modulation_tests.findDependenciesTests.testmodules.circular.CircularTest2;
import modulation_tests.findDependenciesTests.testmodules.generic.GenericTest1;
import modulation_tests.findDependenciesTests.testmodules.generic.GenericTest2;
import modulation_tests.findDependenciesTests.testmodules.generic.GenericTest3;
import modulation_tests.findDependenciesTests.testmodules.longdepend.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import xyz.wisecraft.smp.WisecraftSMP;
import xyz.wisecraft.smp.modulation.interfaces.Module;
import xyz.wisecraft.smp.modulation.UtilModuleCommon;
import xyz.wisecraft.smp.modulation.ModuleClass;

import java.util.ArrayList;

@SuppressWarnings({"ALL", "EmptyMethod"})
public class FindDependenciesTests {

    private final ArrayList<ModuleClass> modules = new ArrayList<>();

    WisecraftSMP plugin;
    ServerMock server;

    @BeforeEach
    public void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(WisecraftSMP.class, true);
    }

    @Test
    public void basicDependTest() {
        modules.add(new GenericTest1());
        modules.add(new GenericTest2());

        ArrayList<String> strings = new ArrayList<>();

        ArrayList<ModuleClass> sortedModules = UtilModuleCommon.sortDependTrimmed(modules);

        for (int i = sortedModules.size(); i > 0; i--) {
            Module module = sortedModules.get(i-1);

            strings.add(module.getClass().getSimpleName());
        }
        Assertions.assertArrayEquals(new String[]{GenericTest1.class.getSimpleName(), GenericTest2.class.getSimpleName()}, strings.toArray());
    }

    @Test
    public void longDependTest() {
        modules.add(new LongTest7());
        modules.add(new GenericTest2());
        modules.add(new GenericTest3());
        modules.add(new LongTest4());
        modules.add(new LongTest6());
        modules.add(new LongTest1());
        modules.add(new LongTest2());
        modules.add(new LongTest3());
        modules.add(new GenericTest1());
        modules.add(new LongTest5());


        ArrayList<String> strings = new ArrayList<>();

        ArrayList<ModuleClass> sortedModules = UtilModuleCommon.sortDependTrimmed(modules);

        for (int i = sortedModules.size(); i > 0; i--) {
            Module module = sortedModules.get(i-1);

            module.getModuleDepends().forEach(dependency -> {
                if (!strings.contains(dependency.getSimpleName()))
                    throw new RuntimeException(module.getClass().getSimpleName() + " was loaded before " + dependency.getSimpleName() + "!");
            });

            strings.add(module.getClass().getSimpleName());
        }
        Assertions.assertArrayEquals(new String[]{GenericTest1.class.getSimpleName(), GenericTest2.class.getSimpleName(), GenericTest3.class.getSimpleName(), LongTest1.class.getSimpleName(), LongTest2.class.getSimpleName(), LongTest3.class.getSimpleName(), LongTest4.class.getSimpleName(), LongTest5.class.getSimpleName(), LongTest6.class.getSimpleName(), LongTest7.class.getSimpleName()},
                strings.toArray());
    }

    @Test
    public void testMissingDepend() {
        modules.add(new GenericTest1());
        modules.add(new GenericTest3());

        Exception exception = Assertions.assertThrows(RuntimeException.class,
                () -> UtilModuleCommon.sortDependTrimmed(modules));

        Assertions.assertArrayEquals(("Module: " + GenericTest3.class.getSimpleName() + " or doesn't exist.").toCharArray(),
                exception.getMessage().toCharArray());
    }

    @Test
    public void testDirectCircularDepend() {
        modules.add(new CircularTest1());
        modules.add(new CircularTest2());

        Exception exception = Assertions.assertThrows(UnsupportedOperationException.class,
                () -> UtilModuleCommon.sortDependTrimmed(modules));

        Assertions.assertArrayEquals("Direct circular dependency detected!".toCharArray(),
                exception.getMessage().toCharArray());
    }


    @AfterEach
    public void tearDown() {
        modules.clear();
        MockBukkit.unmock();
    }
}
