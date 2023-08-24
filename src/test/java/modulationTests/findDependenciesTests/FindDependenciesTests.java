package modulationTests.findDependenciesTests;

import modulationTests.findDependenciesTests.testmodules.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import xyz.wisecraft.smp.modulation.ModuleClass;

import java.util.ArrayList;

public class FindDependenciesTests {

    private final ArrayList<ModuleClass> modules = new ArrayList<>();

    @BeforeEach
    public void setUp() {

    }

    @Test
    public void testFindDependencies() {
        modules.add(new TestModule1());
        modules.add(new TestModule2());

        ArrayList<String> strings = new ArrayList<>();

        for (int i = modules.size(); i > 0; i--) {
            ModuleClass module = modules.get(i-1);

            strings.add(module.getClass().getSimpleName());
        }
        System.out.println(strings);
        Assertions.assertArrayEquals(new String[]{"TestModule1", "Test"}, strings.toArray());
    }


    @AfterEach
    public void tearDown() {


        modules.clear();
    }
}
