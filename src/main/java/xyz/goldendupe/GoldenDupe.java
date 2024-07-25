package xyz.goldendupe;

import bet.astral.fusionflare.FusionFlare;
import bet.astral.guiman.InventoryListener;
import bet.astral.messenger.v2.locale.LanguageTable;
import bet.astral.messenger.v2.locale.source.FileLanguageSource;
import bet.astral.messenger.v2.locale.source.LanguageSource;
import bet.astral.messenger.v2.paper.PaperMessenger;
import bet.astral.messenger.v2.permission.Permission;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AccessLevel;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.incendo.cloud.paper.PaperCommandManager;
import org.jetbrains.annotations.NotNull;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import xyz.goldendupe.anti.crash.CrashNotifier;
import xyz.goldendupe.anti.crash.Notifier;
import xyz.goldendupe.command.bootstrap.InitAfterBootstrap;
import xyz.goldendupe.database.PlayerDatabase;
import xyz.goldendupe.database.SpawnDatabase;
import xyz.goldendupe.database.astronauts.CommandSpyDatabase;
import xyz.goldendupe.datagen.GenerateFiles;
import xyz.goldendupe.datagen.SavedDataData;
import xyz.goldendupe.datagen.SettingsData;
import xyz.goldendupe.listeners.GDListener;
import xyz.goldendupe.messenger.Translations;
import xyz.goldendupe.models.GDSavedData;
import xyz.goldendupe.models.GDSettings;
import xyz.goldendupe.models.GDPlayer;
import xyz.goldendupe.models.impl.GDHome;
import xyz.goldendupe.models.serializer.GlobalSaveSerializer;
import xyz.goldendupe.models.serializer.SettingsSerializer;
import xyz.goldendupe.utils.MemberType;
import xyz.goldendupe.utils.Seasons;
import xyz.goldendupe.command.defaults.ToggleItemsCommand;
import xyz.goldendupe.messenger.GoldenMessenger;
import xyz.goldendupe.utils.Timer;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static xyz.goldendupe.utils.Resource.loadResourceAsTemp;
import static xyz.goldendupe.utils.Resource.loadResourceToFile;

public final class GoldenDupe extends JavaPlugin {
    private static GoldenDupe instance;
    public static final Random random = new Random(System.nanoTime());
    public static Seasons SEASON = Seasons.SEASON_1;
    private boolean isDebug = false;
    @Getter
    private boolean isDevelopmentServer;
    public final NamespacedKey KEY_UNDUPABLE = new NamespacedKey(this, "undupable");
    @Getter
    private final FusionFlare fusionFlare = new FusionFlare(this);
    private GoldenMessenger messenger;
    private YamlConfiguration config;
    private PlayerDatabase playerDatabase;
    @Getter
    private SpawnDatabase spawnDatabase;
    private CommandSpyDatabase commandSpyDatabase;
    @Getter
    // TODO initialize
    private GDSettings settings;
    @Getter
    private GDSavedData savedData;
    private Chat vaultChat = null;
    private Economy vaultEconomy = null;
    private LuckPerms luckPerms = null;
    @SuppressWarnings("FieldCanBeLocal")
    private PaperCommandManager<CommandSender> paperCommandManager;
    // Do NOT reset
    @Getter(AccessLevel.PUBLIC) private final Timer startTimer = new Timer();
    private List<InitAfterBootstrap> initAfterBootstraps;

    public GoldenDupe(GoldenDupeBootstrap boostrap){
        this.isDevelopmentServer = boostrap.getCommandRegister().isDebug();
        initAfterBootstraps = boostrap.initAfterBootstraps;
        messenger = boostrap.messenger;
        isDebug = boostrap.isDev();
    }
    private GoldenDupe() {
        throw new IllegalStateException("GoldenDupe cannot be used in non Paper (or forks) of it. Please update to latest Paper!");
    }

    public <T> T getJson(@NotNull Gson gson, @NotNull File file, Class<T> type) throws IOException {
        FileReader reader = new FileReader(file);
        T t = gson.fromJson(reader, type);
        reader.close();
        return t;
    }

    @Override
    public void onEnable() {
        instance = this;
        PaperMessenger.init(this);

        GenerateFiles generateFiles = new GenerateFiles();
        getLogger().info("Patching settings and global data...");
	    try {
		    generateFiles.generate(getDataFolder());
	    } catch (IOException e) {
		    throw new RuntimeException(e);
	    }
        getLogger().info("Loading settings and global data...");
	    try {
            this.settings = getJson(generateFiles.gson, new File(getDataFolder(), "config.json"), GDSettings.class);
            this.savedData = getJson(generateFiles.gson, new File(getDataFolder(), "global-data.json"), GDSavedData.class);

            if (settings == null) {
                getLogger().severe("Couldn't fetch settings properly.");
                getServer().getPluginManager().disablePlugin(this);
                Bukkit.shutdown();
                return;
            }
            if (savedData == null) {
                getLogger().severe("Couldn't fetch saved global data properly.");
                getServer().getPluginManager().disablePlugin(this);
                Bukkit.shutdown();
                return;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        getLogger().info("Initializing after bootstrapped commands.");
        initAfterBootstraps.forEach(InitAfterBootstrap::init);

        getLogger().info("Trying to find Vault");
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

        getLogger().info("Trying to find LuckPerms");
        if (getServer().getPluginManager().getPlugin("LuckPerms") != null){
            luckPerms = LuckPermsProvider.get();
        }


        // config.yml
        reloadConfig();
        // global data inc. illegals
        settings.reload();
        // listeners (Reflected)
        loadListeners();
        registerListener(new InventoryListener());

        playerDatabase = new PlayerDatabase(this);
        commandSpyDatabase = new CommandSpyDatabase(this);

        spawnDatabase = new SpawnDatabase(this);
        spawnDatabase.load();

        registerListener(new CrashNotifier(this, new Notifier()));

        getServer().getScheduler().runTaskTimer(this, ()->{
            PotionEffect speedEffect = new PotionEffect(PotionEffectType.SPEED, ToggleItemsCommand.RANDOM_ITEM_TICKS*2, 0, true, false, false);
            PotionEffect nightVisionEffect = new PotionEffect(PotionEffectType.NIGHT_VISION, ToggleItemsCommand.RANDOM_ITEM_TICKS*2, 1, true, false, false);
            for (Player player : getServer().getOnlinePlayers()){
                GDPlayer gdPlayer = playerDatabase.fromPlayer(player);
                if (gdPlayer.isToggled()) {
                    // Random items from this list will randomized, but they will have empty NBT
                    ItemStack itemStack = settings.getRandomItemData().getAllowedItems().get(random.nextInt(settings.getRandomItemData().getAllowedItems().size()));
                    // using patchRandomItem randomizes it, if it can be randomized even more
                    itemStack = settings.patchRandomItem(itemStack, random);

                    // Adding the items to a map allows checking if player could actually hold the item
                    Map<Integer, ItemStack> items = player.getInventory().addItem(itemStack);
                    // If the map is not empty, the player couldn't hold the item
                    if (!items.isEmpty()){
                        for (Map.Entry<Integer, ItemStack> entry : items.entrySet()){
                            // Spawning the item as a dropped item and making the player who dropped it
                            // The player will have pickup priority for the item
                            player.getWorld().dropItem(player.getLocation(), entry.getValue(), (item)->{
                                item.setThrower(player.getUniqueId());
                            });
                        }
                    }
                    gdPlayer.setGeneratedRandomItems(gdPlayer.getGeneratedRandomItems()+1);
                    savedData.setItemsGenerated(savedData.getItemsGenerated()+1);
                }


                if (gdPlayer.isToggleSpeed()){
                    if (!player.hasPotionEffect(PotionEffectType.SPEED) || player.hasPotionEffect(PotionEffectType.SPEED) && player.getPotionEffect(PotionEffectType.SPEED).getAmplifier()<2){
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
        getServer().getAsyncScheduler().runAtFixedRate(this, (t)->{
            if (getSettings().isGlobalChatMute()){
                messenger().broadcast(Permission.of(MemberType.MODERATOR.permissionOf("mutechat")), Translations.TIMED_MUTECHAT_REMINDER_1);
                Bukkit.broadcast(Component.text("True"));
            }
        }, 100, 1, TimeUnit.SECONDS);
        getServer().getAsyncScheduler().runAtFixedRate(this, (t)->{
            if (getSettings().isGlobalChatMute()) {
                messenger().broadcast(Permission.of(MemberType.MODERATOR.permissionOf("mutechat")), Translations.TIMED_MUTECHAT_REMINDER_30);
            }
        }, 100, 30, TimeUnit.SECONDS);



        fusionFlare.ready();
        getComponentLogger().info("GoldenDupe has enabled!");
    }

    public void onRestart() {
        // idk if you need this for anything
    }

    @Override
    public void onDisable() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(GDSettings.class, new SettingsSerializer())
                .registerTypeAdapter(GDSavedData.class, new GlobalSaveSerializer())
                .disableHtmlEscaping()
                        .setPrettyPrinting().create();
        write(new File(getDataFolder(), "config.json"), settings, gson);
        write(new File(getDataFolder(), "global-data.json"), savedData, gson);
        getComponentLogger().info("GoldenDupe has disabled!");
    }

    <T> void write(File file, T value, Gson gson){
        if (!file.exists()){
            if (!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
	        try {
		        file.createNewFile();
	        } catch (IOException e) {
		        throw new RuntimeException(e);
	        }
        }

	    try {
		    BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(gson.toJson(value));
            writer.flush();
            writer.close();
	    } catch (IOException e) {
		    throw new RuntimeException(e);
	    }
    }

    // Updated it with fewer reflections
    public static GoldenDupe instance() {
        return instance;
    }

    public void requestSaveHome(GDPlayer player, GDHome home) {
        getHomes(player).put(home.getName().toLowerCase(), home);
    }

    public void requestDeleteHome(GDPlayer player, String homeName) {
        getHomes(player).remove(homeName.toLowerCase());
    }

    public Map<String, GDHome> getHomes(GDPlayer player) {
        return player.getHomes();
    }

    public void reloadConfig() {
        File configFile = new File(getDataFolder(), "config.yml");
        config = YamlConfiguration.loadConfiguration(configFile);

        setIfNotSet(config, "season", 1, Integer.class);

        SEASON = Seasons.fromInt(config.getInt("season"));
        isDebug = config.getBoolean("debug");
    }

    @Deprecated(forRemoval = true)
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
        getServer().getPluginManager().registerEvents(listener, this);
    }

    public void registerListener(Class<?> listener){
        if (listener.isInterface()) {
            return;
        }
        Constructor<?> constructor;
        try {
            constructor = getConstructor(listener, GoldenDupe.class);
            try {
                constructor.setAccessible(true);
                Listener eventListener = (Listener) constructor.newInstance(this);
                if (isDebug)
                    getLogger().info("Registering listener: "+ listener.getName());

                getServer().getPluginManager().registerEvents(eventListener, this);
                if (isDebug)
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
                    if (isDebug)
                        getLogger().info("Registering listener: "+ listener.getName());
                    getServer().getPluginManager().registerEvents(eventListener, this);
                    if (isDebug)
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

    Constructor<?> getConstructor(Class<?> clazz, Class<?>... params) throws NoSuchMethodException {
        try {
            return clazz.getConstructor(params);
        } catch (NoSuchMethodException var4) {
            return clazz.getDeclaredConstructor(params);
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
        return messenger;
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

    public LuckPerms luckPerms() {
        return luckPerms;
    }

    public CommandSpyDatabase commandSpyDatabase() {
        return commandSpyDatabase;
    }


    public void reloadMessengers() {
	    try {
		    LanguageSource source = FileLanguageSource.gson(messenger, Locale.US, new File(getDataFolder(), "messages.json"), MiniMessage.miniMessage());
            LanguageTable table = LanguageTable.of(source);
            messenger.registerLanguageTable(Locale.US, table);
            messenger.setDefaultLocale(source);
            messenger.loadTranslations(List.copyOf(Translations.translations()));
	    } catch (IOException e) {
		    throw new RuntimeException(e);
	    }
    }
}
