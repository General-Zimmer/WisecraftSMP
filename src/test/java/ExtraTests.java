import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.WorldMock;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import xyz.wisecraft.smp.WisecraftSMP;

public class ExtraTests {

    private ServerMock server;
    private WisecraftSMP plugin;

    @BeforeEach
    public void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(WisecraftSMP.class);
    }
    @Test
    @DisplayName("Test Extra tutorial teleport on new player join")
    void verifyTestEnvironment() {

        WorldMock world = server.addSimpleWorld("world");
        WorldMock tutorial = server.addSimpleWorld("tutorial");
        Player p = server.addPlayer();

    }
    @AfterEach
    public void tearDown() {
        MockBukkit.unmock();
    }
}
