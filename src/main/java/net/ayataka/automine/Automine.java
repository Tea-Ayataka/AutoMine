package net.ayataka.automine;

import lombok.Getter;
import net.ayataka.automine.config.GlobalConfig;
import net.ayataka.automine.data.DataManager;
import net.ayataka.automine.key.KeyHandler;
import net.ayataka.automine.listener.BlockEventListener;
import net.ayataka.automine.listener.ServerJoinListener;
import net.ayataka.automine.packet.PacketHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Automine.MOD_ID, name = Automine.MOD_NAME, version = Automine.VERSION, acceptableRemoteVersions = "*", acceptedMinecraftVersions = "[1.9,)")
public class Automine {
    public static final String MOD_ID = "automine";
    public static final String MOD_NAME = "AutoMine";
    public static final String VERSION = "1.0";
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    // Activated Status (Client Side)
    public static boolean isActivated;

    // Listeners
    private final BlockEventListener blockEventListener = new BlockEventListener();
    private final ServerJoinListener serverJoinListener = new ServerJoinListener();

    // Data store (Server Side)
    @Getter
    private final DataManager dataManager = new DataManager();

    // Packet handler
    @Getter
    private PacketHandler packetHandler;

    // Key handler
    @SideOnly(Side.CLIENT)
    private KeyHandler keyHandler;

    // GlobalConfig (.cfg manager)
    @Getter
    private GlobalConfig config;

    /**
     * This is the instance of your mod as created by Forge. It will never be null.
     */
    @Mod.Instance(MOD_ID)
    public static Automine INSTANCE;

    /**
     * This is the first initialization event. Register tile entities here.
     * The registry events below will have fired prior to entry to this method.
     */
    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        this.config = new GlobalConfig(event.getSuggestedConfigurationFile());
    }

    /**
     * This is the second initialization event. Register custom recipes
     */
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this.serverJoinListener);
        MinecraftForge.EVENT_BUS.register(this.blockEventListener);

        this.packetHandler = new PacketHandler();
    }

    @SideOnly(Side.CLIENT)
    @Mod.EventHandler
    public void initClient(FMLInitializationEvent event) {
        this.keyHandler = new KeyHandler();
        MinecraftForge.EVENT_BUS.register(this.keyHandler);
    }
}
