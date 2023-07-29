import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import org.junit.jupiter.api.*;
import xyz.wisecraft.smp.WisecraftSMP;

public class WisecraftSMPTests {

    private ServerMock server;
    private WisecraftSMP plugin;

    @BeforeEach
    public void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(WisecraftSMP.class);
    }

    @Test
    @DisplayName("Verify that config files were loaded")
    void testConfigs() {
        Assertions.assertEquals(plugin.getTesting(), true);
        Assertions.assertNotNull(plugin.getModuleConfig());
        Assertions.assertNotNull(plugin.getConfig());
    }

    @AfterEach
    public void tearDown() {
        MockBukkit.unmock();
    }
}
