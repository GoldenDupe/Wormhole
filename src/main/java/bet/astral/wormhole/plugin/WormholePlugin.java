package bet.astral.wormhole.plugin;

import bet.astral.cloudplusplus.minecraft.paper.bootstrap.InitAfterBootstrap;
import bet.astral.messenger.v2.Messenger;
import bet.astral.wormhole.integration.MasterIntegration;
import bet.astral.wormhole.managers.PlayerCacheManager;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

@Getter
public class WormholePlugin extends JavaPlugin {
    public Messenger messenger;
    @Getter(AccessLevel.NONE)
    private List<InitAfterBootstrap> initAfterBootstrapList;
    private MasterIntegration masterIntegration = new MasterIntegration();

    public WormholePlugin(Bootstrap bootstrapHandler) {
        messenger = bootstrapHandler.getMessenger();
        for (InitAfterBootstrap initAfterBootstrap : bootstrapHandler) {
            initAfterBootstrapList.add(initAfterBootstrap);
        }
    }

    @Override
    public void onEnable() {
        initAfterBootstrapList.forEach(InitAfterBootstrap::init);
    }
    @Override
    public void onDisable() {

    }
    @Override
    public void onLoad() {

    }

    public PlayerCacheManager getPlayerCache() {
        return null;
    }
}
