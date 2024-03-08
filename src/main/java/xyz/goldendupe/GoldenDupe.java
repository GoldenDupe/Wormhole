package xyz.goldendupe;

import bet.astral.cloudplusplus.CommandRegisterer;
import bet.astral.cloudplusplus.annotations.DoNotReflect;
import bet.astral.fluffy.FluffyCombat;
import bet.astral.fusionflare.FusionFlare;
import bet.astral.guiman.InventoryListener;
import bet.astral.messenger.placeholder.Placeholder;
import lombok.AccessLevel;
import lombok.Getter;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.incendo.cloud.SenderMapper;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.paper.PaperCommandManager;
import org.jetbrains.annotations.NotNull;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.*;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.goldendupe.listeners.GDListener;
import xyz.goldendupe.models.GDGlobalData;
import xyz.goldendupe.models.GDPlayer;
import xyz.goldendupe.utils.Position;
import xyz.goldendupe.utils.Seasons;
import xyz.goldendupe.utils.annotations.Season;
import xyz.goldendupe.command.defaults.ToggleItemsCommand;
import xyz.goldendupe.database.PlayerDatabase;
import xyz.goldendupe.database.astronauts.CommandSpyDatabase;
import xyz.goldendupe.database.astronauts.ReportDatabase;
import xyz.goldendupe.database.astronauts.ReportUserDatabase;
import xyz.goldendupe.messenger.GoldenMessenger;
import xyz.goldendupe.utils.Timer;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static xyz.goldendupe.utils.Resource.loadResourceAsTemp;
import static xyz.goldendupe.utils.Resource.loadResourceToFile;

public final class GoldenDupe extends JavaPlugin implements CommandRegisterer<GoldenDupe> {
    private static GoldenDupe instance;
    public static final Random random = new Random(System.nanoTime());
    public static final long FIRST_RELEASED = 1591254000L;
    public static final long NEW_RELEASE = 1591254000L;
    public static Seasons SEASON = Seasons.SEASON_1;
    private boolean isDebug = false;
    public final NamespacedKey KEY_UNDUPABLE = new NamespacedKey(this, "undupable");
    @Getter
    private final FusionFlare fusionFlare = new FusionFlare(this);
    private GoldenMessenger commandMessenger;
    private GoldenMessenger debugMessenger;
    private YamlConfiguration config;
    private PlayerDatabase playerDatabase;
    private ReportDatabase reportDatabase;
    private ReportUserDatabase reportUserDatabase;
    private CommandSpyDatabase commandSpyDatabase;
    @Getter
    private GDGlobalData globalData = new GDGlobalData(this);
    private Chat vaultChat = null;
    private Economy vaultEconomy = null;
    private LuckPerms luckPerms = null;
    @SuppressWarnings("FieldCanBeLocal")
    private PaperCommandManager<CommandSender> paperCommandManager;
    // Do NOT reset
    @Getter(AccessLevel.PUBLIC) private final Timer startTimer = new Timer();
    @Getter(AccessLevel.PUBLIC) private FluffyCombat fluffy;

    @Override
    public void onEnable() {
        fluffy = FluffyCombat.getPlugin(FluffyCombat.class);

        factions.registerAllyChatHandler((fPlayer, faction, ally, component) -> {

        });
        factions.registerAllyChatHandler((fPlayer, faction, ally, component) -> {

        });

        uploadUploads();
        instance = this;

        if (getServer().getPluginManager().getPlugin("Vault") != null){

            RegisteredServiceProvider<Chat> chatProvider = getServer().getServicesManager().getRegistration(Chat.class);
            if (chatProvider != null){
                vaultChat = chatProvider.getProvider();
            }
            RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
            if (economyProvider != null){
                vaultEconomy = economyProvider.getProvider();
            }
        }

        if (getServer().getPluginManager().getPlugin("LuckPerms") != null){
            luckPerms = LuckPermsProvider.get();
        }


        // config.yml
        reloadConfig();
        // global data inc. illegals
        globalData.reload();
        // messages.yml
        getLogger().info("Loading commands..!");
        loadCommands();
        getLogger().info("Loaded commands!");
        // listeners (Reflected)
        getLogger().info("Loading event listeners..!");
        loadListeners();
        getLogger().info("Loaded event listeners!");
        registerListener(new InventoryListener());

        // Messengers
        reloadMessengers();

        playerDatabase = new PlayerDatabase(this);
        reportUserDatabase = new ReportUserDatabase(this);
        reportDatabase = new ReportDatabase(this);
        commandSpyDatabase = new CommandSpyDatabase(this);

        getServer().getScheduler().runTaskTimer(this, ()->{
            PotionEffect speedEffect = new PotionEffect(PotionEffectType.SPEED, ToggleItemsCommand.RANDOM_ITEM_TICKS*2, 0, true, false, false, null);
            PotionEffect nightVisionEffect = new PotionEffect(PotionEffectType.NIGHT_VISION, ToggleItemsCommand.RANDOM_ITEM_TICKS*2, 1, true, false, false, null);
            for (Player player : getServer().getOnlinePlayers()){
                GDPlayer gdPlayer = playerDatabase.fromPlayer(player);
                if (gdPlayer.isToggled()){
                    ItemStack itemStack = globalData.getRandomItems().get(random.nextInt(globalData.getRandomItems().size()));
                    player.getInventory().addItem(itemStack);
                }
                if (gdPlayer.isToggleSpeed()){
                    if (!player.hasPotionEffect(PotionEffectType.SPEED) && player.hasPotionEffect(PotionEffectType.SPEED) && player.getPotionEffect(PotionEffectType.SPEED).getAmplifier()<1){
                        player.removePotionEffect(PotionEffectType.SPEED);
                        speedEffect.apply(player);
                    }
                }
                if (gdPlayer.isToggleNightVision()){
                    player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                    nightVisionEffect.apply(player);
                }
            }
        }, 20, ToggleItemsCommand.RANDOM_ITEM_TICKS);

        fusionFlare.ready();


        getComponentLogger().info("GoldenDupe has enabled!");
    }

    public void onRestart() {
        // idk if you need this for anything
    }

    @Override
    public void onDisable() {


        getComponentLogger().info("GoldenDupe has disabled!");
    }


    public void reloadMessengers(){
        commandMessenger = loadMessenger(false, "messages.yml");
        debugMessenger = loadMessenger(true, "debug-messages.yml");
        CommandRegisterer.super.reloadMessengers();
    }

    // Updated it with fewer reflections
    public static GoldenDupe instance() {
        return instance;
    }

    public void requestSaveHome(GDPlayer player, Position home) {
        getHomes(player).put(home.getName(), home);
    }

    public void requestDeleteHome(GDPlayer player, String homeName) {
        getHomes(player).remove(homeName);
    }

    public Map<String, Position> getHomes(GDPlayer player) {
        return player.getHomes();
    }

    private GoldenMessenger loadMessenger(boolean debug, String name){
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(new File(getDataFolder(), name));
        GoldenMessenger goldenMessenger = new GoldenMessenger(configuration, new HashMap<>(), debug);
        getLogger().info("Loading placeholders for messages...");
        Map<String, Placeholder> placeholderMap = goldenMessenger.loadPlaceholders("placeholders");
        getLogger().info("Loaded placeholders for messages...");
        // Debug
        if (placeholderMap == null){
            placeholderMap = new HashMap<>();
        } else {
            placeholderMap = new HashMap<>(placeholderMap);
        }
        getLogger().info("Overriding message placeholders...");
        goldenMessenger.overrideDefaultPlaceholders(placeholderMap);
        getLogger().info("Overrode message placeholders...");
        return goldenMessenger;
    }


    public void reloadConfig() {
        File configFile = new File(getDataFolder(), "config.yml");
        config = YamlConfiguration.loadConfiguration(configFile);

        setIfNotSet(config, "season", 1, Integer.class);

        SEASON = Seasons.fromInt(config.getInt("season"));
        isDebug = config.getBoolean("debug");
    }

    @Override
    public @NotNull FileConfiguration getConfig() {
        return config;
    }

    public void setIfNotSet(Configuration configuration, String key, Object defaultValue, Class<?>... allowedTypes){
        if (configuration.get(key) != null){
            for (Class<?> clazz : allowedTypes){
                if (clazz.isInstance(configuration.get(key))){
                    return;
                }
            }
        }
        configuration.set(key, defaultValue);
    }

    private void loadListeners(){
        Class<?> command = GDListener.class;
        String pkg = command.getPackage().getName();
        try (ScanResult scanResult = new ClassGraph()
                .enableAllInfo().acceptPackages(pkg).scan()) {
            ClassInfoList classInfo = scanResult.getClassesImplementing(Listener.class);
            List<Class<?>> classes = classInfo.loadClasses();
            for (Class<?> clazz : classes) {
                registerListener(clazz);
            }
        }
    }


    public void registerListener(Listener listener){
        if (cannotInject(listener.getClass())){
            getLogger().warning("Couldn't initialize listener: "+ listener.getClass().getName() + " as it's not available in this season!");
            return;
        }

        getServer().getPluginManager().registerEvents(listener, this);
    }

    public void registerListener(Class<?> listener){
        if (cannotInject(listener)){
            getLogger().warning("Couldn't initialize listener: "+ listener.getName() + " as it's not available in this season or the class is cannot be reflected!");
            return;
        }
        Constructor<?> constructor;
        try {
            constructor = getConstructor(listener, GoldenDupe.class);
            try {
                constructor.setAccessible(true);
                Listener eventListener = (Listener) constructor.newInstance(this);
                getServer().getPluginManager().registerEvents(eventListener, this);
                getLogger().info("Registered listener: "+ listener.getName());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("Failed to initialize event constructor with params GoldenDupe.class", e);
            }
        } catch (NoSuchMethodException ignore) {
	        try {
                constructor = getConstructor(listener);
                try {
                    constructor.setAccessible(true);
                    Listener eventListener = (Listener) constructor.newInstance();
                    getServer().getPluginManager().registerEvents(eventListener, this);
                    getLogger().info("Registered listener: "+ listener.getName());
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException("Failed to initialize event constructor no params", e);
                }
	        } catch (NoSuchMethodException e) {
		        throw new RuntimeException("Failed to find correct constructor for class: "+ listener.getName(), e);
	        }
        }
    }

    public void registerPermission(String name) {
        org.bukkit.permissions.Permission bukkitPermission = new org.bukkit.permissions.Permission(name);
        if (getServer().getPluginManager().getPermission(name)==null) {
            getServer().getPluginManager().addPermission(bukkitPermission);
        }
    }
    public boolean cannotInject(Class<?> clazz){
        DoNotReflect doNotReflect = clazz.getAnnotation(DoNotReflect.class);
        if (doNotReflect != null){
            return true;
        }

        Season season = clazz.getAnnotation(Season.class);
        if (season != null){
            // Make season specific classes easier to manage
            // This command is not supposed to be unlocked in this season
            return Arrays.stream(season.unlock()).noneMatch(val -> val == GoldenDupe.SEASON.asInt()) && !season.alwaysUnlocked();
        }
        return false;
    }

    private void loadCommands(){
        paperCommandManager = new PaperCommandManager<>(
                this,
                ExecutionCoordinator.asyncCoordinator(),
                SenderMapper.identity()
        );
        paperCommandManager.registerBrigadier();
        paperCommandManager.registerAsynchronousCompletions();

        registerCommands(
                List.of(
                        "xyz.goldendupe.command.admin",
                        "xyz.goldendupe.command.defaults",
                        "xyz.goldendupe.command.donator",
                        "xyz.goldendupe.command.staff",
                        "xyz.goldendupe.command.og"
                ),
                paperCommandManager
        );

        reloadMessengers();
    }

    private void uploadUploads(){
        String[] files = new String[]{
                "config|yml",
                "illegals|yml",
                "messages|yml",
        };
        for (String name : files){
            name = name.replace("dm/", "discord-messages/");

            String[] split = name.split("\\|");
            String fileName = split[0];
            String ending = split[1];
            File fileTemp = loadResourceAsTemp("/upload/"+fileName, ending);
            File file = loadResourceToFile("/upload/"+fileName, ending, new File(getDataFolder(), fileName+"."+ending), true);
            if (ending.matches("(?i)yml") || ending.matches("(?i)yaml")){
                loadConfig(getConfig(fileTemp), getConfig(file), file);
            }
        }
    }

    private void loadConfig(FileConfiguration tempConfig, FileConfiguration config, File file){
        Set<String> keys = tempConfig.getKeys(false);
        for (String key : keys){
            addDefaults(key, tempConfig, config);
        }
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void addDefaults(String key, Configuration tempConfig, Configuration config) {
        List<String> comment = tempConfig.getComments(key);
        if (!comment.isEmpty() && config.getInlineComments(key).isEmpty()) {
            config.setComments(key, comment);
        }
        comment = tempConfig.getInlineComments(key);
        if (!comment.isEmpty() && config.getInlineComments(key).isEmpty()) {
            config.setInlineComments(key, comment);
        }
        Object value = tempConfig.get(key); // Retrieve the value from the tempConfig
        if (value instanceof ConfigurationSection section) {
            for (String k : section.getKeys(false)) {
                addDefaults(key + "." + k, tempConfig, config); // Append current key
            }
        }
    }


    private FileConfiguration getConfig(File file){
        return YamlConfiguration.loadConfiguration(file);
    }

    public void setDebug(boolean debug){
        this.isDebug = debug;
    }

    public boolean isDebug() {
        return isDebug;
    }

    public GoldenMessenger messenger() {
        return commandMessenger;
    }

    public GoldenMessenger debugMessenger() {
        return debugMessenger;
    }

    @Override
    public GoldenDupe plugin() {
        return this;
    }

    public GoldenMessenger commandMessenger() {
        return commandMessenger;
    }

    public PlayerDatabase playerDatabase() {
        return playerDatabase;
    }


    public Chat vaultChat() {
        return vaultChat;
    }

    public Economy vaultEconomy() {
        return vaultEconomy;
    }

    public ReportDatabase reportDatabase() {
        return reportDatabase;
    }

    public ReportUserDatabase reportUserDatabase() {
        return reportUserDatabase;
    }

    public LuckPerms luckPerms() {
        return luckPerms;
    }

    public CommandSpyDatabase commandSpyDatabase() {
        return commandSpyDatabase;
    }

}
