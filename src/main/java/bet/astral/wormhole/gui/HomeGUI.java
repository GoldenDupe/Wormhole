package bet.astral.wormhole.gui;

import bet.astral.guiman.background.Background;
import bet.astral.guiman.clickable.ClickContext;
import bet.astral.guiman.clickable.Clickable;
import bet.astral.guiman.clickable.ClickableBuilder;
import bet.astral.guiman.gui.InventoryGUI;
import bet.astral.guiman.gui.builders.InventoryGUIBuilder;
import bet.astral.guiman.utils.ChestRows;
import bet.astral.messenger.v2.Messenger;
import bet.astral.messenger.v2.placeholder.collection.PlaceholderCollection;
import bet.astral.messenger.v2.placeholder.collection.PlaceholderList;
import bet.astral.signman.SignGUIBuilder;
import bet.astral.wormhole.integration.Integration;
import bet.astral.wormhole.managers.PlayerCacheManager;
import bet.astral.wormhole.managers.RequestManager;
import bet.astral.wormhole.objects.Request;
import bet.astral.wormhole.objects.data.PlayerData;
import bet.astral.wormhole.objects.data.PlayerHome;
import bet.astral.wormhole.plugin.translation.Translations;
import bet.astral.wormhole.plugin.WormholePlugin;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ResolvableProfile;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public class HomeGUI {
    private final WormholePlugin plugin;
    private final Messenger messenger;

    @Contract(pure = true)
    public HomeGUI(@NotNull WormholePlugin plugin) {
        this.plugin = plugin;
        this.messenger = plugin.getMessenger();
    }

    public void openHomes(Player player, int page) {
        PlayerCacheManager playerCacheManager = plugin.getPlayerCache();
        PlayerData data = playerCacheManager.getCache(player);
        List<PlayerHome> homes = data.getHomes();

        InventoryGUIBuilder builder = InventoryGUI.builder(ChestRows.SIX)
                .messenger(messenger)
                .title(Translations.G_HOMES_TITLE)
                .background(Background.border(ChestRows.SIX, Clickable.noTooltip(Material.BLACK_STAINED_GLASS_PANE), Clickable.noTooltip(Material.LIGHT_GRAY_STAINED_GLASS_PANE)));
        formatHomesMenu(builder, homes, page);
        builder.build().open(player);
    }

    public void formatHomesMenu(InventoryGUIBuilder builder, @NotNull List<PlayerHome> homes, int page) {
        if (homes.isEmpty()) {
            builder.clickable(13, Clickable.builder(Material.OAK_SIGN)
                    .title(Translations.G_HOMES_NO_ENTRIES_NAME)
                    .description(Translations.G_HOMES_NO_ENTRIES_DESCRIPTION));
            return;
        }

        final int homesPerRow = 7;
        final int maxRows = 4;
        final int homesPerPage = homesPerRow * maxRows;
        final int[] baseSlots = {10, 11, 12, 13, 14, 15, 16};

        int totalPages = (int) Math.ceil((double) homes.size() / homesPerPage);
        page = Math.max(0, Math.min(page, totalPages - 1));

        int startIndex = page * homesPerPage;
        int endIndex = Math.min(startIndex + homesPerPage, homes.size());
        List<PlayerHome> pageHomes = homes.subList(startIndex, endIndex);

        for (int row = 0; row < maxRows; row++) {
            int rowStart = row * homesPerRow;
            int rowEnd = Math.min(rowStart + homesPerRow, pageHomes.size());

            if (rowStart >= pageHomes.size()) break;

            List<PlayerHome> rowHomes = pageHomes.subList(rowStart, rowEnd);
            int offset = row * 9;

            int startSlotIndex = (homesPerRow - rowHomes.size()) / 2;

            for (int i = 0; i < rowHomes.size(); i++) {
                int slot = baseSlots[startSlotIndex + i] + offset;
                clickableHome(builder, rowHomes.get(i), slot);
            }
        }

        final int finalPage = page;
        if (page > 0) {
            builder.clickable(45, Clickable.builder(Material.ARROW)
                    .title(Translations.G_HOMES_PREVIOUS_PAGE_NAME)
                    .description(Translations.G_HOMES_PREVIOUS_PAGE_DESCRIPTION)
                    .actionGeneral(context -> openHomes(context.getWho(), finalPage - 1)));
        }

        if (page < totalPages - 1) {
            builder.clickable(53, Clickable.builder(Material.ARROW)
                    .title(Translations.G_HOMES_NEXT_PAGE_NAME)
                    .description(Translations.G_HOMES_NEXT_PAGE_DESCRIPTION)
                    .actionGeneral(context -> openHomes(context.getWho(), finalPage + 1)));
        }
    }


    public void clickableHome(@NotNull InventoryGUIBuilder builder, @NotNull PlayerHome home, int slot) {
        Player player = Bukkit.getPlayer(home.getOwnerId());
        PlayerData data = plugin.getPlayerCache().getCache(player);
        ItemStack icon = home.getIconOrDefault().clone();
        if (data.getPrimaryHome() != null && data.getPrimaryHome() == home.getUniqueId()) {
            icon.setData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true);
        } else {
            icon.setData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, false);
        }

        builder.clickable(slot,
                Clickable.builder(icon)
                        .title(Translations.G_HOMES_ENTRY_NAME)
                        .description(Translations.G_HOMES_ENTRY_DESCRIPTION)
                        .placeholderGenerator(_ -> new PlaceholderList(home.toPlaceholders()))
                        .hideItemFlags()
                        .data("home", home.getUniqueId())
                        .data("owner", home.getOwnerId())
                        .actionGeneral(this::handleHomeClick));
    }

    public void handleHomeClick(@NotNull ClickContext context) {
        Player player = context.getWho();
        UUID home = (UUID) context.getClickable().getData().get("home");
        openHomeMenu(player, home);
    }

    public PlayerHome makeSureHomeExists(Player player, UUID homeId) {
        PlayerCacheManager playerCacheManager = plugin.getPlayerCache();
        PlayerData data = playerCacheManager.getCache(player);
        PlayerHome home = data.getHome(homeId);
        if (home == null) {
            openHomes(player, 0);
            return null;
        }
        return home;
    }

    /**
     * Opens the main overview of a home. Allows managing of the home and teleporting to the home via GUI based buttohns
     *
     * @param player player
     * @param homeId home id
     */
    public void openHomeMenu(Player player, UUID homeId) {
        PlayerHome home = makeSureHomeExists(player, homeId);
        if (home == null) {
            return;
        }

        Function<Player, PlaceholderCollection> placeholderGenerator = _ -> new PlaceholderList(home.toPlaceholders());

        ClickableBuilder teleport = Clickable.builder(Material.LIME_WOOL)
                .title(Translations.G_HOME_TELEPORT_NAME)
                .description(Translations.G_HOME_TELEPORT_DESCRIPTION)
                .actionGeneral(context -> {
                }).placeholderGenerator(placeholderGenerator);
        ClickableBuilder tpaMyHome = Clickable.builder(Material.ANVIL).hideItemFlags()
                .permission("wormhole.tpamyhome")
                .priority(10)
                .title(Translations.G_HOME_REQUEST_OTHER_TELEPORT_NAME)
                .description(Translations.G_HOME_REQUEST_OTHER_TELEPORT_DESCRIPTION)
                .actionGeneral(action -> openTeleportHomeRequestMenu(player, homeId, 0))
                .placeholderGenerator(placeholderGenerator);

        PlayerData data = plugin.getPlayerCache().getCache(player);
        ItemStack icon = home.getIconOrDefault().clone();
        if (data.getPrimaryHome() != null && data.getPrimaryHome() == home.getUniqueId()) {
            icon.setData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true);
        } else {
            icon.setData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, false);
        }

        InventoryGUIBuilder builder = InventoryGUI.builder(3)
                .title(Translations.G_HOME_TITLE)
                .background(Background.border(3, Clickable.noTooltip(Material.BLACK_STAINED_GLASS), Clickable.noTooltip(Material.LIGHT_GRAY_STAINED_GLASS)))

                // Description of the home
                .clickable(4, Clickable.builder(icon)
                        .title(Translations.G_HOME_OVERVIEW_NAME)
                        .description(Translations.G_HOME_OVERVIEW_DESCRIPTION)
                        .actionGeneral(context -> {
                            PlayerData playerData = plugin.getPlayerCache().getCache(player);
                            if (playerData.getPrimaryHome() == null || playerData.getPrimaryHome() != homeId) {
                                playerData.setPrimaryHome(home.getUniqueId());
                            } else {
                                playerData.setPrimaryHome(null);
                            }

                            plugin.getPlayerCache().save(playerData);
                            openHomeMenu(player, homeId);
                        })
                        .placeholderGenerator(placeholderGenerator))

                // Teleport
                .clickable(10, teleport)
                .clickable(11, teleport)
                .clickable(12, teleport)
                .clickable(13, teleport)
                // Teleport other players here, if the player has permission
                .clickable(12, tpaMyHome)
                .clickable(13, tpaMyHome)

                // Rename
                .clickable(14, Clickable.builder(Material.ANVIL)
                        .hideItemFlags()
                        .permission("wormhole.home.rename")
                        .permissionMessage(Translations.M_HOME_RENAME_NO_PERMISSION)
                        .displayIfNoPermissions()
                        .title(Translations.G_HOME_RENAME_NAME)
                        .description(Translations.G_HOME_RENAME_DESCRIPTION)
                        .actionGeneral(action -> openHomeRenameMenu(action.getWho(), homeId))
                        .placeholderGenerator(placeholderGenerator))
                // Relocate
                .clickable(15, Clickable.builder(Material.IRON_HOE)
                        .hideItemFlags()
                        .permission("wormhole.home.relocate")
                        .permissionMessage(Translations.M_HOME_RELOCATE_NO_PERMISSION)
                        .displayIfNoPermissions()
                        .title(Translations.G_HOME_RELOCATE_NAME)
                        .description(Translations.G_HOME_RELOCATE_DESCRIPTION)
                        .actionGeneral(action -> openConfirmRelocateMenu(player, homeId))
                        .placeholderGenerator(placeholderGenerator))
                // Delete
                .clickable(16, Clickable.builder(Material.FLINT_AND_STEEL)
                        .hideItemFlags()
                        .permission("wormhole.home.delete")
                        .permissionMessage(Translations.M_HOME_DELETE_NO_PERMISSION)
                        .displayIfNoPermissions()
                        .title(Translations.G_HOME_DELETE_NAME)
                        .description(Translations.G_HOME_DELETE_DESCRIPTION)
                        .actionGeneral(action -> openHomeDeleteMenu(action.getWho(), homeId))
                        .placeholderGenerator(placeholderGenerator))
                // Return
                .clickable(22, Clickable.builder(Material.BARRIER)
                        .title(Translations.G_HOME_RETURN_NAME)
                        .description(Translations.G_HOME_RETURN_DESCRIPTION)
                        .hideItemFlags()
                        .actionGeneral(action -> openHomes(action.getWho(), 0)));

        builder.messenger(messenger).placeholderGenerator(placeholderGenerator).build().open(player);
    }

    public void openConfirmRelocateMenu(Player player, UUID homeId) {
        PlayerHome home = makeSureHomeExists(player, homeId);
        if (home == null) {
            return;
        }

        if (!player.hasPermission("wormhole.home.relocate")) {
            openHomes(player, 0);
            return;
        }

        Function<Player, PlaceholderCollection> placeholderGenerator = _ -> new PlaceholderList(home.toPlaceholders());

        ClickableBuilder confirm = Clickable.builder(Material.LIME_WOOL)
                .title(Translations.G_RELOCATE_HOME_CONFIRM_NAME)
                .description(Translations.G_RELOCATE_HOME_CONFIRM_DESCRIPTION)
                .hideItemFlags()
                .actionGeneral(context -> {

                    // Update the location and directly update the database
                    home.relocate(player.getLocation());
                    plugin.getPlayerWarpDataManager().saveWarp(home);
                    messenger.message(player, Translations.M_RELOCATE_HOME_RELOCATED, placeholderGenerator.apply(player));

                    // Open the new updated info
                    openHomeMenu(player, homeId);

                }).placeholderGenerator(placeholderGenerator);
        ClickableBuilder cancel = Clickable.builder(Material.RED_WOOL)
                .title(Translations.G_RELOCATE_HOME_CANCEL_NAME)
                .description(Translations.G_RELOCATE_HOME_CANCEL_DESCRIPTION)
                .hideItemFlags()
                .actionGeneral(context -> {
                    openHomeMenu(player, homeId);
                }).placeholderGenerator(placeholderGenerator);

        InventoryGUIBuilder builder = InventoryGUI.builder(3)
                .title(Translations.G_RELOCATE_HOME_TITLE)
                .background(Background.border(3, Clickable.noTooltip(Material.BLACK_STAINED_GLASS), Clickable.noTooltip(Material.LIGHT_GRAY_STAINED_GLASS)))
                // Return
                .clickable(22, Clickable.builder(Material.BARRIER).hideItemFlags().actionGeneral(action -> openHomeMenu(player, homeId)))
                // CONFIRM
                .clickable(10, confirm)
                .clickable(11, confirm)
                .clickable(12, confirm)
                .clickable(13, confirm)

                // CANCEL
                .clickable(15, cancel)
                .clickable(16, cancel)
                .clickable(17, cancel)
                .clickable(18, cancel);

        builder.messenger(messenger).placeholderGenerator(placeholderGenerator).build().open(player);
    }

    public void openTeleportHomeRequestMenu(Player player, UUID homeId, int page) {
        PlayerCacheManager playerCacheManager = plugin.getPlayerCache();
        PlayerData data = playerCacheManager.getCache(player);
        PlayerHome home = data.getHome(homeId);

        InventoryGUIBuilder builder = InventoryGUI.builder(ChestRows.SIX)
                .messenger(messenger)
                .title(Translations.M_HOME_INVITE_TITLE)
                .background(Background.border(ChestRows.SIX, Clickable.noTooltip(Material.BLACK_STAINED_GLASS_PANE), Clickable.noTooltip(Material.LIGHT_GRAY_STAINED_GLASS_PANE)));

        final int homesPerRow = 7;
        final int maxRows = 4;
        final int homesPerPage = homesPerRow * maxRows;
        final int[] baseSlots = {10, 11, 12, 13, 14, 15, 16};

        RequestManager requestManager = plugin.getRequestManager();

        List<Player> players = new java.util.ArrayList<>(Bukkit.getOnlinePlayers().stream().toList());
        players.remove(player);
        players.removeIf(p -> requestManager.getSentRequests(Request.Type.TO_OWN_HOME).get(player.getUniqueId()).stream().anyMatch(r -> r.getRequested().equals(p)));

        int totalPages = (int) Math.ceil((double) players.size() / homesPerPage);
        page = Math.max(0, Math.min(page, totalPages - 1));

        int startIndex = page * homesPerPage;
        int endIndex = Math.min(startIndex + homesPerPage, players.size());
        List<Player> pagePlayers = players.subList(startIndex, endIndex);

        for (int row = 0; row < maxRows; row++) {
            int rowStart = row * homesPerRow;
            int rowEnd = Math.min(rowStart + homesPerRow, pagePlayers.size());

            if (rowStart >= pagePlayers.size()) break;

            List<Player> rowPlayers = pagePlayers.subList(rowStart, rowEnd);
            int offset = row * 9;

            int startSlotIndex = (homesPerRow - rowPlayers.size()) / 2;

            for (int i = 0; i < rowPlayers.size(); i++) {
                int slot = baseSlots[startSlotIndex + i] + offset;
                Player currentPlayer = players.get(slot);
                ItemStack head = ItemStack.of(Material.PLAYER_HEAD);
                head.setData(DataComponentTypes.PROFILE, ResolvableProfile.resolvableProfile(currentPlayer.getPlayerProfile()));
                builder.clickable(slot, Clickable.builder(head).title(Translations.M_HOME_INVITE_PLAYER_NAME).description(Translations.M_HOME_INVITE_PLAYER_DESCRIPTION)
                        .placeholderGenerator(player1 -> {
                            PlaceholderList placeholders = new PlaceholderList();
                            placeholders.add("name", player1.getName());
                            placeholders.add("displayname", player1.getName());
                            placeholders.add("uuid", player1.getName());
                            return placeholders;
                        })
                        .data("who", currentPlayer.getUniqueId())
                        .data("home", homeId)
                        .actionGeneral(action -> {
                            Player oPlayer = Bukkit.getPlayer((UUID) action.getClickable().getData("who"));
                            if (oPlayer != null) {
                                requestManager.request(Request.Type.TO_OWN_HOME, player, oPlayer, home);
                            }
                        })
                )
                ;
            }
        }

        // TODO REPLACE TO CORRECT

        final int finalPage = page;
        if (page > 0) {
            builder.clickable(45, Clickable.builder(Material.ARROW)
                    .title(Translations.G_HOME_INVITE_PREVIOUS_PAGE_NAME)
                    .description(Translations.G_HOME_INVITE_PREVIOUS_PAGE_DESCRIPTION)
                    .actionGeneral(context -> openHomes(context.getWho(), finalPage - 1)));
        }

        if (page < totalPages - 1) {
            builder.clickable(53, Clickable.builder(Material.ARROW)
                    .title(Translations.G_HOME_INVITE_NEXT_PAGE_NAME)
                    .description(Translations.G_HOME_INVITE_NEXT_PAGE_DESCRIPTION)
                    .actionGeneral(context -> openHomes(context.getWho(), finalPage + 1)));
        }
        builder.build().open(player);
    }

    /**
     * Opens a sign which allows renaming of a home with the given home id
     *
     * @param player player
     * @param homeId players home id
     */
    public void openHomeRenameMenu(Player player, UUID homeId) {
        PlayerHome home = makeSureHomeExists(player, homeId);
        if (home == null) {
            return;
        }
        if (!player.hasPermission("wormhole.home.rename")) {
            openHomes(player, 0);
            return;
        }

        SignGUIBuilder builder = new SignGUIBuilder(PlainTextComponentSerializer.plainText())
                .setLine(1, Translations.SG_RENAME_HOME_LINE_2)
                .setLine(2, Translations.SG_RENAME_HOME_LINE_3)
                .setLine(3, Translations.SG_RENAME_HOME_LINE_4)
                .setHandler(() -> List.of(
                        (p, result) -> {
                            PlaceholderList placeholders = new PlaceholderList(home.toPlaceholders());
                            final String plain = result.getFirstPlain();
                            if (plain.isBlank()) {
                                messenger.message(p, Translations.M_RENAME_HOME_BLANK, placeholders);
                                return;
                            }

                            PlayerData playerData = plugin.getPlayerCache().getCache(p);
                            PlayerHome foundHome = playerData.getHome(plain);
                            if (foundHome != null && !homeId.equals(foundHome.getUniqueId())) {
                                messenger.message(p, Translations.M_RENAME_HOME_EXISTS, placeholders);
                                return;
                            }

                            if (foundHome != null && homeId.equals(foundHome.getUniqueId())) {
                                openHomeMenu(player, homeId);
                                return;
                            }

                            Integration integration = plugin.getMasterIntegration();
                            if (!integration.canRenamePlayerHome(player, home.getName(), plain)) {
                                return;
                            }

                            placeholders.add("old_name", home.getName());
                            placeholders.add("new_name", plain);
                            messenger.message(player, Translations.M_RENAME_HOME_RENAMED, placeholders);
                            playerData.renameHome(home, plain);
                            plugin.getPlayerWarpDataManager().saveWarp(home);
                        }
                ));

        builder.setMessenger(messenger)
                .setPlaceholderGenerator(_ -> new PlaceholderList(home.toPlaceholders()))
                .build().open(player);
    }

    /**
     * Opens a delete menu which has a confirm and cancel buttons.
     *
     * @param player player
     * @param homeId own home id
     */
    public void openHomeDeleteMenu(Player player, UUID homeId) {
        PlayerHome home = makeSureHomeExists(player, homeId);
        if (home == null) {
            return;
        }

        if (!player.hasPermission("wormhole.home.delete")) {
            openHomes(player, 0);
            return;
        }

        Function<Player, PlaceholderCollection> placeholderGenerator = _ -> new PlaceholderList(home.toPlaceholders());

        ClickableBuilder confirm = Clickable.builder(Material.LIME_WOOL)
                .title(Translations.G_DELETE_HOME_CONFIRM_NAME)
                .description(Translations.G_DELETE_HOME_CONFIRM_DESCRIPTION)
                .hideItemFlags()
                .actionGeneral(context -> {
                    // Delete the data from cache and database
                    home.setExists(false);
                    PlayerData playerData = plugin.getPlayerCache().getCache(player);
                    playerData.removeHome(home);
                    plugin.getPlayerWarpDataManager().deleteWarp(home);
                    // Message about the removal
                    messenger.message(player, Translations.M_DELETE_HOME_DELETED,  placeholderGenerator.apply(player));
                }).placeholderGenerator(placeholderGenerator);
        ClickableBuilder cancel = Clickable.builder(Material.RED_WOOL)
                .title(Translations.G_DELETE_HOME_CANCEL_NAME)
                .description(Translations.G_DELETE_HOME_CANCEL_DESCRIPTION)
                .hideItemFlags()
                .actionGeneral(context -> {
                    openHomeMenu(player, homeId);
                }).placeholderGenerator(placeholderGenerator);

        InventoryGUIBuilder builder = InventoryGUI.builder(3)
                .title(Translations.G_DELETE_HOME_TITLE)
                .background(Background.border(3, Clickable.noTooltip(Material.BLACK_STAINED_GLASS), Clickable.noTooltip(Material.LIGHT_GRAY_STAINED_GLASS)))
                // Return
                .clickable(22, Clickable.builder(Material.BARRIER).hideItemFlags().actionGeneral(action -> openHomeMenu(player, homeId)))
                // CONFIRM
                .clickable(10, confirm)
                .clickable(11, confirm)
                .clickable(12, confirm)
                .clickable(13, confirm)

                // CANCEL
                .clickable(15, cancel)
                .clickable(16, cancel)
                .clickable(17, cancel)
                .clickable(18, cancel);

        builder.messenger(messenger).placeholderGenerator(placeholderGenerator).build().open(player);
    }
}