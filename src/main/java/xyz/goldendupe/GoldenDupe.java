package xyz.goldendupe;

import bet.astral.goldenmessenger.GoldenMessenger;
import com.samjakob.spigui.SpiGUI;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.DecoratedPot;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;
import xyz.goldendupe.command.GDCommand;
import xyz.goldendupe.command.GDCommandInfo;
import xyz.goldendupe.listeners.GDListener;
import xyz.goldendupe.models.GDSpawn;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static xyz.goldendupe.utils.Resource.loadResourceAsTemp;
import static xyz.goldendupe.utils.Resource.loadResourceToFile;

public final class GoldenDupe extends JavaPlugin {
    public static final long FIRST_RELEASED = 1591254000L;
    public static final long NEW_RELEASE = 1591254000L;
    public static int season = 1;
    public static final long SEASON_1 = 1591254000L;
    private boolean isDebug = false;
    private SpiGUI spiGUI;
    public final NamespacedKey KEY_UNDUPABLE = new NamespacedKey(this, "undupable");
    private final Map<String, GDSpawn> spawns = new HashMap<>();
    private final Set<GDCommand> commands = new HashSet<>();
    private GoldenMessenger defaultMessenger;
    private GoldenMessenger commandMessenger;
    private GoldenMessenger debugMessenger;
    private YamlConfiguration commandConfig;
    private GDCommandInfo defaultCommandInfo;
    private List<Material> illegalDupe;
    private List<ItemStack> randomItems;
    private List<Material> illegalPlacement;
    private YamlConfiguration config;
    private PlayerDatabase playerDatabase;
    private Chat vaultChat = null;
    private Economy vaultEconomy = null;

    @Override
    public void onEnable() {

        // TODO upload files

        uploadUploads();

        // config.yml
        reloadConfig();
        // illegals.yml
        reloadIllegals();
        // Messengers
        reloadMessengers();
        // commands.yml
        loadCommands();
        // listeners (Reflected)
        loadListeners();

        spiGUI = new SpiGUI(this);

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

        playerDatabase = new PlayerDatabase(this);


        getComponentLogger().info("GoldenDupe has enabled!");
    }

    @Override
    public void onDisable() {


        getComponentLogger().info("GoldenDupe has disabled!");
    }


    public void reloadMessengers(){
        defaultMessenger = messenger(true, new File(getDataFolder(), "messages.yml"));
        commandMessenger = messenger(true, new File(getDataFolder(), "commands.yml"));
        debugMessenger = messenger(true, new File(getDataFolder(), "messages.yml")); // TODO

        for (GDCommand command : commands){
            command.reloadMessengers();
        }
    }
    private GoldenMessenger messenger(boolean debug, File file){
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        return new GoldenMessenger(this, configuration, new HashMap<>(), debug);
    }

    public void reloadConfig() {
        File configFile = new File(getDataFolder(), "config.yml");
        config = YamlConfiguration.loadConfiguration(configFile);

        setIfNotSet(config, "season", 1, Integer.class);

        season = config.getInt("season");
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

    private void loadCommands(){
        commandConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "commands.yml"));
        commandMessenger = new GoldenMessenger(this, commandConfig, false);
        defaultCommandInfo = GDCommandInfo.defaultValues(commandConfig);
        for (Class<?> command : new Reflections(getClassLoader(), "xyz.goldendupe.command").getSubTypesOf(GDCommand.class)){
            registerCommand(command);
        }
    }
    private void loadListeners(){
        for (Class<? extends GDListener> listener : new Reflections(getClassLoader(), "xyz.goldendupe.listeners").getSubTypesOf(GDListener.class)){
            registerListener(listener);
        }
    }

    public void reloadIllegals() {
        File file = new File(getDataFolder(), "illegals.yml");
        YamlConfiguration illegalConfig = YamlConfiguration.loadConfiguration(file);

        illegalDupe = new LinkedList<>();
        randomItems = new LinkedList<>();
        illegalPlacement = new LinkedList<>();


        addMaterials(illegalDupe, illegalConfig.getStringList("placement"));
        illegalDupe.add(Material.AIR);
        addMaterials(illegalPlacement, illegalConfig.getStringList("dupe"));

        List<String> randomIllegals = illegalConfig.getStringList("random.illegals");
        for (Material material : Material.values()){
            if (randomIllegals.contains(material.name())){
                continue;
            }
            randomItems.add(new ItemStack(material));
        }
        if (illegalConfig.getBoolean("random.all-goat-horns", false)){
            for (MusicInstrument instrument : Registry.INSTRUMENT){
                ItemStack itemStack = new ItemStack(Material.GOAT_HORN);
                MusicInstrumentMeta meta = (MusicInstrumentMeta) (itemStack.hasItemMeta() ? itemStack.getItemMeta() : Bukkit.getItemFactory().getItemMeta(Material.GOAT_HORN));
                meta.setInstrument(instrument);
                itemStack.setItemMeta(meta);
                randomItems.add(itemStack);
            }
        }
        if (illegalConfig.getBoolean("random.full-decorated-potteries", false)){
            for (Material material : Tag.ITEMS_BREAKS_DECORATED_POTS.getValues()){
                ItemStack itemStack = new ItemStack(Material.DECORATED_POT);
                BlockStateMeta meta = (BlockStateMeta) (itemStack.hasItemMeta() ? itemStack.getItemMeta() : Bukkit.getItemFactory().getItemMeta(Material.DECORATED_POT));
                DecoratedPot decoratedPot = (DecoratedPot) meta.getBlockState();
                decoratedPot.setSherd(DecoratedPot.Side.BACK, material);
                decoratedPot.setSherd(DecoratedPot.Side.FRONT, material);
                decoratedPot.setSherd(DecoratedPot.Side.LEFT, material);
                decoratedPot.setSherd(DecoratedPot.Side.RIGHT, material);
                meta.setBlockState(decoratedPot);
                itemStack.setItemMeta(meta);
                randomItems.add(itemStack);
            }
        }
        if (illegalConfig.getBoolean("random.all-enchanted-books", true)){
            for (Enchantment enchantment : Registry.ENCHANTMENT){
                for (int i = 0; i < enchantment.getMaxLevel(); i++){
                    ItemStack itemStack = new ItemStack(Material.ENCHANTED_BOOK);
                    EnchantmentStorageMeta meta = (EnchantmentStorageMeta) (itemStack.hasItemMeta() ? itemStack.getItemMeta() : Bukkit.getItemFactory().getItemMeta(Material.ENCHANTED_BOOK));
                    meta.addStoredEnchant(enchantment, i, false);
                    itemStack.setItemMeta(meta);
                    randomItems.add(itemStack);
                }
            }
        }
        for (int i = 0; i < illegalConfig.getInt("random.random-firework-boost", 0); i++){
            ItemStack itemStack = new ItemStack(Material.FIREWORK_ROCKET);
            FireworkMeta meta = (FireworkMeta) (itemStack.hasItemMeta() ? itemStack.getItemMeta() : Bukkit.getItemFactory().getItemMeta(Material.FIREWORK_ROCKET));
            meta.setPower(i);
            itemStack.setItemMeta(meta);
        }
    }

    public boolean canDupe(ItemStack itemStack){
        if (itemStack.hasItemMeta()){
            ItemMeta meta = itemStack.getItemMeta();
            PersistentDataContainer container = meta.getPersistentDataContainer();
            if (container.has(KEY_UNDUPABLE) && Boolean.TRUE.equals(container.get(KEY_UNDUPABLE, PersistentDataType.BOOLEAN))){
                return false;
            }
        }
        return !(illegalDupe.contains(itemStack.getType()));
    }

    public boolean canBePlaced(Block block) {
        return !illegalPlacement.contains(block.getType());
    }


    private void addMaterials(List<Material> materials, List<String> mats){
        for (String mat : mats){
            try {
                Material material = Material.valueOf(mat);
                if (!materials.contains(material)) {
                    materials.add(material);
                }
            } catch (IllegalStateException ignore){
            }
        }
    }

    private boolean cannotInject(Class<?> clazz){
        GDCommandInfo.DoNotReflect doNotReflect = clazz.getAnnotation(GDCommandInfo.DoNotReflect.class);
        if (doNotReflect != null){
            return true;
        }

        Season season = clazz   .getAnnotation(Season.class);
        if (season != null){
            // Make season specific classes easier to manage
            // This command is not supposed to be unlocked in this season
            return Arrays.stream(season.unlock()).noneMatch(val -> val == GoldenDupe.season) && !season.alwaysUnlocked();
        }
        return false;
    }

    public void registerListener(Listener listener){
        if (cannotInject(listener.getClass())){
            getLogger().warning("Couldn't initialize listener: "+ listener.getClass().getName() + " as it's not available in this season!");
            return;
        }

        getServer().getPluginManager().registerEvents(listener, this);
    }

    public void registerListener(Class<? extends GDListener> listener){
        if (cannotInject(listener)){
            getLogger().warning("Couldn't initialize listener: "+ listener.getName() + " as it's not available in this season or the class is cannot be reflected!");
            return;
        }
        try {
            Constructor<? extends GDListener> gdListenerConst = listener.getConstructor(GoldenDupe.class);
            GDListener gdListener = gdListenerConst.newInstance(this);

            getServer().getPluginManager().registerEvents(gdListener, this);
            getLogger().info("Registered listener: "+ listener.getName());
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    public void registerCommand(Class<?> clazz){
        if (cannotInject(clazz)){
            getLogger().info("Reflection based implementation for "+ clazz.getName() + " is not allowed. Skipping!");
            return;
        }
        Class<GDCommand> gdCommandClass;
        try {
            gdCommandClass = (Class<GDCommand>) clazz;
        } catch (ClassCastException e){
            return;
        }
        GDCommandInfo.Command command = clazz.getAnnotation(GDCommandInfo.Command.class);
        MemorySection commandSection = (MemorySection) commandConfig.getConfigurationSection(command.name());

        if (commandSection == null) {
            getLogger().severe("Command will not be initialized! Couldn't find command section for command "+ command.name().toUpperCase() + " in the COMMANDS.YML.");
            return;
        }
        GDCommandInfo gdCommandInfo = new GDCommandInfo(defaultCommandInfo, commandSection, command);

        try {
            Constructor<? extends GDCommand> goldenCommandConstructor = gdCommandClass.getDeclaredConstructor(GoldenDupe.class, GDCommandInfo.class);
            GDCommand goldenCommand = goldenCommandConstructor.newInstance(this, gdCommandInfo);
            CommandMap commandMap = getServer().getCommandMap();

            // goldendupe-default
            // goldendupe-donator
            // goldendupe-staff
            // goldendupe-admin
            commandMap.register("goldendupe-"+command.memberType().name().toLowerCase(), goldenCommand);
            commands.add(goldenCommand);
            getLogger().info("Registered command: goldendupe-"+command.memberType().name().toLowerCase()+":"+goldenCommand.getName());
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            getLogger().severe("Command will not be initialized! Couldn't reflect command "+ command.name().toUpperCase() + ". Because of this it will not be enabled!");
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
    }

    private void uploadUploads(){
        String[] files = new String[]{
                "commands|yml",
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

    private void addDefaults(String key, Configuration tempConfig, Configuration config){
        List<String> comment = tempConfig.getComments(key);
        if (!comment.isEmpty() && config.getInlineComments(key).isEmpty()){
            config.setComments(key, comment);
        }
        comment = tempConfig.getInlineComments(key);
        if (!comment.isEmpty() && config.getInlineComments(key).isEmpty()){
            config.setInlineComments(key, comment);
        }
        if (tempConfig.get(key) instanceof MemorySection section){
            for (String k : section.getKeys(false)) {
                addDefaults(k, tempConfig, config);
            }
        }
    }

    private FileConfiguration getConfig(File file){
        return YamlConfiguration.loadConfiguration(file);
    }

    public Map<String, GDSpawn> spawns() {
        return spawns;
    }

    public void setDebug(boolean debug){
        this.isDebug = debug;
    }

    public boolean isDebug() {
        return isDebug;
    }

    public GoldenMessenger messenger() {
        return defaultMessenger;
    }

    public GoldenMessenger debugMessenger() {
        return debugMessenger;
    }

    public GoldenMessenger commandMessenger() {
        return commandMessenger;
    }

    public PlayerDatabase playerDatabase() {
        return playerDatabase;
    }

	public void saveSpawns() {
        // TODO
	}

    public Chat vaultChat() {
        return vaultChat;
    }

    public Economy vaultEconomy() {
        return vaultEconomy;
    }

    public SpiGUI spiGUI() {
        return spiGUI;
    }
}
