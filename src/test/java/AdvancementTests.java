import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import xyz.wisecraft.smp.WisecraftSMP;

public class AdvancementTests {

    private ServerMock server;
    private WisecraftSMP plugin;

    @BeforeEach
    public void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(WisecraftSMP.class);
    }

    @AfterEach
    public void tearDown() {
        MockBukkit.unmock();
    }
}
