package bet.astral.pluginsetup.plugin;

import bet.astral.cloudplusplus.minecraft.paper.bootstrap.InitAfterBootstrap;
import bet.astral.messenger.v2.Messenger;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

@Getter
public class Plugin extends JavaPlugin {
    private Messenger messenger;
    @Getter(AccessLevel.NONE)
    private List<InitAfterBootstrap> initAfterBootstrapList;

    public Plugin(Bootstrap bootstrapHandler) {
        this.messenger = bootstrapHandler.getMessenger();
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
}
