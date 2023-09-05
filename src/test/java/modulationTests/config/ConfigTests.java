package modulationTests.config;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import xyz.wisecraft.smp.WisecraftSMP;
import xyz.wisecraft.smp.util.UtilRandom;

import java.io.File;

public class ConfigTests {

    FileConfiguration moduleConfig = null;
    File moduleConfigFile = null;
    WisecraftSMP plugin = null;
    ServerMock server;

    @BeforeEach
    public void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(WisecraftSMP.class, true);
        moduleConfigFile = new File("modulation/configtests", "modules.yml");
        moduleConfig = WisecraftSMP.createModuleConfig(plugin, moduleConfigFile);
    }


    @Test
    public void testModuleConfig() {

    }

    @AfterEach
    public void tearDown() {

    }
}
