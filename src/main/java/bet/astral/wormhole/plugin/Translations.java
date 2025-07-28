package bet.astral.wormhole.plugin;

import bet.astral.messenger.v2.translation.TranslationKey;
import bet.astral.messenger.v3.cloud.translation.CaptionTranslationKey;
import org.jetbrains.annotations.NotNull;

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

    public static final TranslationKey D_TPA_CMD = TranslationKey.of("commands.tpa.description");
    public static final TranslationKey M_TPA_CANNOT_TELEPORT = TranslationKey.of("message.tpa.cannot-teleport");
    public static final TranslationKey M_TPA_PLAYER_REQUESTED_OFFLINE =  TranslationKey.of("message.tpa.requested-offline");
    public static final TranslationKey M_TPA_PLAYER_SELF_OFFLINE =  TranslationKey.of("message.tpa.self-offline");
    public static final TranslationKey M_TPA_REQUEST_RAN_OUT_OF_TIME_SELF =  TranslationKey.of("message.tpa.request-ran-out-of-time.requested");
    public static final TranslationKey M_TPA_REQUEST_RAN_OUT_OF_TIME_REQUESTED =  TranslationKey.of("message.tpa.request-ran-out-of-time.requested");
    public static final TranslationKey M_TPA_SENT_REQUEST = TranslationKey.of("message.tpa.sent-request");
    public static final TranslationKey M_TPA_RECEIVED_REQUEST = TranslationKey.of("message.tpa.received-request");

    public static final TranslationKey D_TPAHERE_CMD = TranslationKey.of("commands.tpahere.description");
    public static final TranslationKey M_TPAHERE_CANNOT_TELEPORT = TranslationKey.of("message.tpahere.cannot-teleport-here");
    public static final TranslationKey M_TPAHERE_PLAYER_REQUESTED_OFFLINE =  TranslationKey.of("message.tpahere.requested-offline");
    public static final TranslationKey M_TPAHERE_PLAYER_SELF_OFFLINE =  TranslationKey.of("message.tpahere.self-offline");
    public static final TranslationKey M_TPAHERE_REQUEST_RAN_OUT_OF_TIME_SELF =  TranslationKey.of("message.tpahere.request-ran-out-of-time.requested");
    public static final TranslationKey M_TPAHERE_REQUEST_RAN_OUT_OF_TIME_REQUESTED =  TranslationKey.of("message.tpahere.request-ran-out-of-time.requested");
    public static final TranslationKey M_TPAHERE_SENT_REQUEST = TranslationKey.of("message.tpahere.sent-request");
    public static final TranslationKey M_TPAHERE_RECEIVED_REQUEST = TranslationKey.of("message.tpahere.received-request");

    public static final TranslationKey D_TPAMYHOME_CMD = TranslationKey.of("commands.tpmyhome.description");
    public static final TranslationKey M_TPAMYHOME_PLAYER_REQUESTED_OFFLINE =  TranslationKey.of("message.tpmyhome.requested-offline");
    public static final TranslationKey M_TPAMYHOME_PLAYER_SELF_OFFLINE =  TranslationKey.of("message.tpmyhome.self-offline");
    public static final TranslationKey M_TPAMYHOME_REQUEST_RAN_OUT_OF_TIME_SELF =  TranslationKey.of("message.tpmyhome.request-ran-out-of-time.requested");
    public static final TranslationKey M_TPAMYHOME_REQUEST_RAN_OUT_OF_TIME_REQUESTED =  TranslationKey.of("message.tpmyhome.request-ran-out-of-time.requested");
    public static final TranslationKey M_TPAMYHOME_SENT_REQUEST = TranslationKey.of("message.tpmyhome.sent-request");
    public static final TranslationKey M_TPAMYHOME_RECEIVED_REQUEST = TranslationKey.of("message.tpmyhome.received-request");

    public static final CaptionTranslationKey C_HOME_PARSE_EXCEPTION = CaptionTranslationKey.of("command.argument.home.parse-exception");
    public static final CaptionTranslationKey C_PLAYER_SELF_PARSE_EXCEPTION = CaptionTranslationKey.of("command.argument.player-self.parse-exception");
    public static final CaptionTranslationKey C_PLAYER_ALREADY_REQUESTED_PARSE_EXCEPTION = CaptionTranslationKey.of("command.argument.player-requested.parse-exception");
}
