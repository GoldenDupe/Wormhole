package bet.astral.wormhole.plugin;

import bet.astral.cloudplusplus.minecraft.paper.bootstrap.InitAfterBootstrap;
import bet.astral.guiman.GUIMan;
import bet.astral.messenger.v2.Messenger;
import bet.astral.messenger.v3.minecraft.paper.PaperMessenger;
import bet.astral.signman.SignGUI;
import bet.astral.wormhole.gui.HomeGUI;
import bet.astral.wormhole.integration.MasterIntegration;
import bet.astral.wormhole.listeners.ConnectionListener;
import bet.astral.wormhole.listeners.RespawnLocationListener;
import bet.astral.wormhole.managers.PlayerCacheManager;
import bet.astral.wormhole.managers.RequestManager;
import bet.astral.wormhole.managers.TeleportManager;
import bet.astral.wormhole.managers.data.PlayerDataManager;
import bet.astral.wormhole.managers.data.PlayerWarpDataManager;
import bet.astral.wormhole.managers.data.sql.SQLDatabase;
import bet.astral.wormhole.managers.data.sql.SQLitePlayerDataManager;
import bet.astral.wormhole.managers.data.sql.SQLitePlayerWarpManager;
import bet.astral.wormhole.objects.data.PlayerHome;
import bet.astral.wormhole.plugin.configuration.Configuration;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Getter
public class WormholePlugin extends JavaPlugin {
    public Messenger messenger;
    @Getter(AccessLevel.NONE)
    private List<InitAfterBootstrap> initAfterBootstrapList = new ArrayList<>();
    private MasterIntegration masterIntegration = new MasterIntegration();
    private TeleportManager teleportManager;
    private RequestManager requestManager;
    @Getter(AccessLevel.NONE)
    private PlayerCacheManager playerCacheManager;
    private PlayerWarpDataManager<PlayerHome> playerWarpDataManager;
    private PlayerDataManager playerDataManager;
    private Configuration configuration;
    private HomeGUI homeGUI;
    private final SQLDatabase sqlDatabase = new SQLDatabase();


    public WormholePlugin(Bootstrap bootstrapHandler) {
        messenger = bootstrapHandler.getMessenger();
        for (InitAfterBootstrap initAfterBootstrap : bootstrapHandler) {
            initAfterBootstrapList.add(initAfterBootstrap);
        }

    }

    @Override
    public void onEnable() {
        initAfterBootstrapList.forEach(InitAfterBootstrap::init);
        GUIMan.init(this);
        SignGUI.init(this, true);
        PaperMessenger.init(this);

        configuration = new Configuration(new File(getDataFolder(), "config.json"));

        try {
            sqlDatabase.connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        playerWarpDataManager = new SQLitePlayerWarpManager(sqlDatabase);
        playerDataManager = new SQLitePlayerDataManager(this, sqlDatabase);
        playerCacheManager = new PlayerCacheManager(this);

        requestManager = new RequestManager(this);
        teleportManager = new TeleportManager(this);
        homeGUI = new HomeGUI(this);

        getServer().getPluginManager().registerEvents(new RespawnLocationListener(this), this);
        getServer().getPluginManager().registerEvents(new ConnectionListener(this), this);

        getServer().getAsyncScheduler().runAtFixedRate(this, task->{
            requestManager.tick();
            teleportManager.tick();
        }, 100, 50, TimeUnit.MILLISECONDS);
    }
    @Override
    public void onDisable() {
        try {
            sqlDatabase.disconnect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void onLoad() {

    }

    public PlayerCacheManager getPlayerCache() {
        return playerCacheManager;
    }

}
