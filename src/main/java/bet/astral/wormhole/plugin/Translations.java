package bet.astral.wormhole.plugin;

import bet.astral.messenger.v2.translation.TranslationKey;
import bet.astral.messenger.v3.cloud.translation.CaptionTranslationKey;

public final class Translations {
    public static final TranslationKey M_WARP_NO_PERMISSION = TranslationKey.of("warp.teleport.no-permissions");
    public static final TranslationKey M_WARP_BANNED = TranslationKey.of("warp.teleport.banned");
    public static final TranslationKey M_WARP_COOLDOWN = TranslationKey.of("warp.teleport.banned");
    public static final TranslationKey M_WARP_WARPING = TranslationKey.of("warp.teleport.warping");

    public static final TranslationKey D_HOME_CMD = TranslationKey.of("commands.home.description");
    public static final TranslationKey M_HOME_NO_HOMES = TranslationKey.of("message.home.no-homes");
    public static final TranslationKey M_HOME_CANNOT_TELEPORT = TranslationKey.of("message.home.cannot-teleport");

    public static final TranslationKey D_HOMES_CMD = TranslationKey.of("commands.homes.description");
    public static final TranslationKey M_HOMES_LIST_COMMA = TranslationKey.of("message.homes.list.comma");
    public static final TranslationKey M_HOMES_LIST_HOME = TranslationKey.of("message.homes.list.entry.home");
    public static final TranslationKey M_HOMES_LIST_WARP = TranslationKey.of("message.homes.list.entry.warp");
    public static final TranslationKey M_HOMES = TranslationKey.of("message.homes");

    public static final TranslationKey D_SET_HOME_CMD = TranslationKey.of("commands.sethome.description");
    public static final TranslationKey M_SET_HOME_CANNOT_SET_CANCELLED = TranslationKey.of("message.sethome.cannot-set");
    public static final TranslationKey M_SET_HOME_RELOCATE = TranslationKey.of("message.sethome.relocate");
    public static final TranslationKey M_SET_HOME_CANNOT_SET_MAX_HOMES = TranslationKey.of("message.sethome.max-homes-and-warps");
    public static final TranslationKey M_SET_HOME_SUCCESS = TranslationKey.of("message.sethome.success");

    public static final TranslationKey D_DEL_HOME_CMD = TranslationKey.of("commands.delhome.description");
    public static final TranslationKey M_DEL_HOME_SELECT_HOME_TO_DELETE =  TranslationKey.of("commands.delhome.select-home-to-delete");
    public static final TranslationKey M_DEL_HOME_LIST_COMMA = TranslationKey.of("message.delhome.list.comma");
    public static final TranslationKey M_DEL_HOME_LIST_HOME = TranslationKey.of("message.delhome.list.entry.home");
    public static final TranslationKey M_DEL_HOME_LIST_WARP = TranslationKey.of("message.delhome.list.entry.warp");
    public static final TranslationKey M_DEL_HOME_SUCCESS = TranslationKey.of("message.delhome.success");

    public static final CaptionTranslationKey C_HOME_PARSE_EXCEPTION = CaptionTranslationKey.of("command.home.parse-exception");
}
