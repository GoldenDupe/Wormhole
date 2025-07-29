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
    public static final TranslationKey D_PLAYER_WARP_CMD = TranslationKey.of("commands.playerwarp.description");
    public static final TranslationKey M_PLAYER_WARP_NO_WARPS = TranslationKey.of("message.playerwarp.no-homes");
    public static final TranslationKey M_PLAYER_WARP_CANNOT_TELEPORT = TranslationKey.of("message.playerwarp.cannot-teleport");

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
    public static final TranslationKey D_DEL_WARP_CMD = TranslationKey.of("commands.delwarp.description");
    public static final TranslationKey M_DEL_WARP_SUCCESS = TranslationKey.of("message.delwarp.success");
    public static final TranslationKey M_DEL_WARP_SELECT_HOME_TO_DELETE =  TranslationKey.of("message.delwarp.select-home-to-delete");
    public static final TranslationKey M_DEL_WARP_LIST_COMMA = TranslationKey.of("message.delwarp.list.comma");
    public static final TranslationKey M_DEL_WARP_LIST_HOME = TranslationKey.of("message.delwarp.list.entry.home");
    public static final TranslationKey M_DEL_WARP_LIST_WARP = TranslationKey.of("message.delwarp.list.entry.warp");

    public static final TranslationKey D_TPA_CMD = TranslationKey.of("commands.tpa.description");
    public static final TranslationKey M_TPA_CANNOT_TELEPORT = TranslationKey.of("message.tpa.cannot-teleport");
    public static final TranslationKey M_TPA_PLAYER_REQUESTED_OFFLINE =  TranslationKey.of("message.tpa.requested-offline");
    public static final TranslationKey M_TPA_PLAYER_SELF_OFFLINE =  TranslationKey.of("message.tpa.self-offline");
    public static final TranslationKey M_TPA_REQUEST_RAN_OUT_OF_TIME_SELF =  TranslationKey.of("message.tpa.request-ran-out-of-time.requested");
    public static final TranslationKey M_TPA_REQUEST_RAN_OUT_OF_TIME_REQUESTED =  TranslationKey.of("message.tpa.request-ran-out-of-time.requested");
    public static final TranslationKey M_TPA_SENT_REQUEST = TranslationKey.of("message.tpa.sent-request");
    public static final TranslationKey M_TPA_RECEIVED_REQUEST = TranslationKey.of("message.tpa.received-request");
    public static final TranslationKey M_TPA_TELEPORTING_PLAYER = TranslationKey.of("message.tpa.teleporting.player");
    public static final TranslationKey M_TPA_TELEPORTING_OTHER = TranslationKey.of("message.tpa.teleporting.requested");
    public static final TranslationKey M_TPA_TELEPORT_CANCELLED_MOVED_PLAYER = TranslationKey.of("message.tpa.cancelled.moved.player");
    public static final TranslationKey M_TPA_TELEPORT_CANCELLED_MOVED_OTHER = TranslationKey.of("message.tpa.cancelled.moved.requested");
    public static final TranslationKey M_TPA_TELEPORTING_IN_5_3_2_1_SECONDS = TranslationKey.of("message.tpa.teleporting.in-5-3-2-1-seconds");
    public static final TranslationKey M_TPA_PLAYER_OFFLINE = TranslationKey.of("message.tpa.cancelled.player-offline");
    public static final TranslationKey M_TPA_OTHER_OFFLINE = TranslationKey.of("message.tpa.cancelled.other-offline");

    public static final TranslationKey D_TPAHERE_CMD = TranslationKey.of("commands.tpahere.description");
    public static final TranslationKey M_TPAHERE_CANNOT_TELEPORT = TranslationKey.of("message.tpahere.cannot-teleport-here");
    public static final TranslationKey M_TPAHERE_PLAYER_REQUESTED_OFFLINE =  TranslationKey.of("message.tpahere.requested-offline");
    public static final TranslationKey M_TPAHERE_PLAYER_SELF_OFFLINE =  TranslationKey.of("message.tpahere.self-offline");
    public static final TranslationKey M_TPAHERE_REQUEST_RAN_OUT_OF_TIME_SELF =  TranslationKey.of("message.tpahere.request-ran-out-of-time.requested");
    public static final TranslationKey M_TPAHERE_REQUEST_RAN_OUT_OF_TIME_REQUESTED =  TranslationKey.of("message.tpahere.request-ran-out-of-time.requested");
    public static final TranslationKey M_TPAHERE_SENT_REQUEST = TranslationKey.of("message.tpahere.sent-request");
    public static final TranslationKey M_TPAHERE_RECEIVED_REQUEST = TranslationKey.of("message.tpahere.received-request");
    public static final TranslationKey M_TPAHERE_TELEPORTING_PLAYER = TranslationKey.of("message.tpa.teleporting.player");
    public static final TranslationKey M_TPAHERE_TELEPORTING_OTHER = TranslationKey.of("message.tpa.teleporting.requested");
    public static final TranslationKey M_TPAHERE_TELEPORT_CANCELLED_MOVED_PLAYER = TranslationKey.of("message.tpahere.cancelled.moved.player");
    public static final TranslationKey M_TPAHERE_TELEPORT_CANCELLED_MOVED_OTHER = TranslationKey.of("message.tpahere.cancelled.moved.requested");
    public static final TranslationKey M_TPAHERE_TELEPORTING_IN_5_3_2_1_SECONDS = TranslationKey.of("message.tpahere.teleporting.in-5-3-2-1-seconds");
    public static final TranslationKey M_TPAHERE_PLAYER_OFFLINE = TranslationKey.of("message.tpahere.cancelled.player-offline");
    public static final TranslationKey M_TPAHERE_OTHER_OFFLINE = TranslationKey.of("message.tpahere.cancelled.other-offline");

    public static final TranslationKey D_TPAMYHOME_CMD = TranslationKey.of("commands.tpmyhome.description");
    public static final TranslationKey M_TPAMYHOME_PLAYER_REQUESTED_OFFLINE =  TranslationKey.of("message.tpmyhome.requested-offline");
    public static final TranslationKey M_TPAMYHOME_PLAYER_SELF_OFFLINE =  TranslationKey.of("message.tpmyhome.self-offline");
    public static final TranslationKey M_TPAMYHOME_REQUEST_RAN_OUT_OF_TIME_SELF =  TranslationKey.of("message.tpmyhome.request-ran-out-of-time.requested");
    public static final TranslationKey M_TPAMYHOME_REQUEST_RAN_OUT_OF_TIME_REQUESTED =  TranslationKey.of("message.tpmyhome.request-ran-out-of-time.requested");
    public static final TranslationKey M_TPAMYHOME_SENT_REQUEST = TranslationKey.of("message.tpmyhome.sent-request");
    public static final TranslationKey M_TPAMYHOME_RECEIVED_REQUEST = TranslationKey.of("message.tpmyhome.received-request");
    public static final TranslationKey M_TPAMYHOME_TELEPORTING_PLAYER = TranslationKey.of("message.tpa.teleporting.player");
    public static final TranslationKey M_TPAMYHOME_TELEPORTING_OTHER = TranslationKey.of("message.tpa.teleporting.requested");
    public static final TranslationKey M_TPAMYHOME_TELEPORT_CANCELLED_MOVED_PLAYER = TranslationKey.of("message.tpamyhome.cancelled.moved.player");
    public static final TranslationKey M_TPAMYHOME_TELEPORT_CANCELLED_MOVED_OTHER = TranslationKey.of("message.tpamyhome.cancelled.moved.requested");
    public static final TranslationKey M_TPAMYHOME_TELEPORTING_IN_5_3_2_1_SECONDS = TranslationKey.of("message.tpamyhome.teleporting.in-5-3-2-1-seconds");
    public static final TranslationKey M_TPAMYHOME_PLAYER_OFFLINE = TranslationKey.of("message.tpamyhome.cancelled.player-offline");
    public static final TranslationKey M_TPAMYHOME_OTHER_OFFLINE = TranslationKey.of("message.tpamyhome.cancelled.other-offline");

    public static final TranslationKey D_TPACCEPT_CMD =  TranslationKey.of("commands.tpaccept.description");
    public static final TranslationKey D_TPHEREACCEPT_CMD =  TranslationKey.of("commands.tphereaccept.description");
    public static final TranslationKey D_TPHOMEACCEPT_CMD =  TranslationKey.of("commands.tphomeaccept.description");
    public static final TranslationKey M_TPACCEPT_NO_TELEPORT_REQUESTS = TranslationKey.of("message.tpaccept.no-teleport-requests");
    public static final TranslationKey M_TPACCEPT_TPA_ACCEPTED_PLAYER = TranslationKey.of("message.tpaccept.tpa.accepted.player");
    public static final TranslationKey M_TPACCEPT_TPAHERE_ACCEPTED_PLAYER = TranslationKey.of("message.tpaccept.tpahere.accepted.player");
    public static final TranslationKey M_TPACCEPT_TPAMYHOME_ACCEPTED_PLAYER = TranslationKey.of("message.tpaccept.tpamyhome.accepted.player");
    public static final TranslationKey M_TPACCEPT_TPA_ACCEPTED_OTHER = TranslationKey.of("message.tpaccept.tpa.accepted.other");
    public static final TranslationKey M_TPACCEPT_TPAHERE_ACCEPTED_OTHER = TranslationKey.of("message.tpaccept.tpahere.accepted.other");
    public static final TranslationKey M_TPACCEPT_TPAMYHOME_ACCEPTED_OTHER = TranslationKey.of("message.tpaccept.tpamyhome.accepted.other");

    public static final TranslationKey M_TPDENY_NO_TELEPORT_REQUESTS = TranslationKey.of("message.tpdeny.no-teleport-requests");

    public static final CaptionTranslationKey C_PLAYER_HOME_PARSE_EXCEPTION = CaptionTranslationKey.of("command.argument.player-home.parse-exception");
    public static final CaptionTranslationKey C_PLAYER_WARP_PARSE_EXCEPTION = CaptionTranslationKey.of("command.argument.player-warp.parse-exception");
    public static final CaptionTranslationKey C_PLAYER_SELF_PARSE_EXCEPTION = CaptionTranslationKey.of("command.argument.player-self.parse-exception");
    public static final CaptionTranslationKey C_PLAYER_ALREADY_REQUESTED_PARSE_EXCEPTION = CaptionTranslationKey.of("command.argument.player-requested.parse-exception");
    public static final CaptionTranslationKey C_PLAYER_HAS_NOT_SENT_REQUEST_EXCEPTION = CaptionTranslationKey.of("command.argument.acceptable-player.parse-exception");
    public static final CaptionTranslationKey C_PLAYER_HAS_NOT_RECEIVED_REQUEST_EXCEPTION = CaptionTranslationKey.of("command.argument.cancellable-player.parse-exception");


    // Home (edit/teleport) menu
    public static final TranslationKey G_HOME_TITLE = TranslationKey.of("gui.home.title");
    public static final TranslationKey G_HOME_OVERVIEW_NAME = TranslationKey.of("gui.home.button.overview.displayname");
    public static final TranslationKey G_HOME_OVERVIEW_DESCRIPTION = TranslationKey.of("gui.home.button.overview.description");
    public static final TranslationKey G_HOME_RETURN_NAME = TranslationKey.of("gui.home.button.return.displayname");
    public static final TranslationKey G_HOME_RETURN_DESCRIPTION = TranslationKey.of("gui.home.button.return.description");
    public static final TranslationKey G_HOME_TELEPORT_NAME = TranslationKey.of("gui.home.button.teleport.displayname");
    public static final TranslationKey G_HOME_TELEPORT_DESCRIPTION = TranslationKey.of("gui.home.button.teleport.lore");
    public static final TranslationKey G_HOME_REQUEST_OTHER_TELEPORT_NAME = TranslationKey.of("gui.home.button.teleport.displayname");
    public static final TranslationKey G_HOME_REQUEST_OTHER_TELEPORT_DESCRIPTION = TranslationKey.of("gui.home.button.teleport.lore");
    public static final TranslationKey G_HOME_RENAME_NAME = TranslationKey.of("gui.home.button.rename.displayname");
    public static final TranslationKey G_HOME_RENAME_DESCRIPTION = TranslationKey.of("gui.home.button.rename.lore");
    public static final TranslationKey M_HOME_RENAME_NO_PERMISSION = TranslationKey.of("message.home.relocate.no-permission");
    public static final TranslationKey G_HOME_RELOCATE_NAME = TranslationKey.of("gui.home.button.relocate.displayname");
    public static final TranslationKey G_HOME_RELOCATE_DESCRIPTION = TranslationKey.of("gui.home.button.relocate.lore");
    public static final TranslationKey M_HOME_RELOCATE_NO_PERMISSION = TranslationKey.of("message.home.relocate.no-permission");
    public static final TranslationKey G_HOME_DELETE_NAME = TranslationKey.of("gui.home.button.delete.displayname");
    public static final TranslationKey G_HOME_DELETE_DESCRIPTION = TranslationKey.of("gui.home.button.delete.lore");
    public static final TranslationKey M_HOME_DELETE_NO_PERMISSION = TranslationKey.of("message.home.delete.no-permission");

    // Homes menu
    public static final TranslationKey G_HOMES_TITLE = TranslationKey.of("gui.homes.title");
    public static final TranslationKey G_HOMES_NO_ENTRIES_NAME = TranslationKey.of("gui.homes.no-entries.displayname");
    public static final TranslationKey G_HOMES_NO_ENTRIES_DESCRIPTION = TranslationKey.of("gui.homes.no-entries.description");
    public static final TranslationKey G_HOMES_ENTRY_NAME = TranslationKey.of("gui.homes.entry.displayname");
    public static final TranslationKey G_HOMES_ENTRY_DESCRIPTION = TranslationKey.of("gui.homes.entry.description");
    public static final TranslationKey G_HOMES_NEXT_PAGE_NAME = TranslationKey.of("gui.homes.next-page.displayname");
    public static final TranslationKey G_HOMES_NEXT_PAGE_DESCRIPTION = TranslationKey.of("gui.homes.next-page.description");
    public static final TranslationKey G_HOMES_PREVIOUS_PAGE_NAME = TranslationKey.of("gui.homes.previous-page.displayname");
    public static final TranslationKey G_HOMES_PREVIOUS_PAGE_DESCRIPTION = TranslationKey.of("gui.homes.previous-page.description");

    // Invite player to home
    public static final TranslationKey M_HOME_INVITE_TITLE = TranslationKey.of("gui.invite-player.title");
    public static final TranslationKey M_HOME_INVITE_PLAYER_NAME = TranslationKey.of("gui.invite-player.button.invite.displayname");
    public static final TranslationKey M_HOME_INVITE_PLAYER_DESCRIPTION = TranslationKey.of("gui.invite-player.button.invite.description");

    // Delete home menu
    public static final TranslationKey G_DELETE_HOME_TITLE =  TranslationKey.of("gui.delete-home.title");
    public static final TranslationKey G_DELETE_HOME_CONFIRM_NAME = TranslationKey.of("gui.delete-home.confirm.displayname");
    public static final TranslationKey G_DELETE_HOME_CONFIRM_DESCRIPTION = TranslationKey.of("gui.delete-home.confirm.description");
    public static final TranslationKey G_DELETE_HOME_CANCEL_NAME = TranslationKey.of("gui.delete-home.cancel.displayname");
    public static final TranslationKey G_DELETE_HOME_CANCEL_DESCRIPTION = TranslationKey.of("gui.delete-home.cancel.description");

    // Rename menu
    public static final TranslationKey SG_RENAME_HOME_LINE_2 = TranslationKey.of("sign-gui.rename-home.line.2");
    public static final TranslationKey SG_RENAME_HOME_LINE_3 = TranslationKey.of("sign-gui.rename-home.line.3");
    public static final TranslationKey SG_RENAME_HOME_LINE_4 = TranslationKey.of("sign-gui.rename-home.line.4");
    public static final TranslationKey M_RENAME_HOME_BLANK = TranslationKey.of("message.rename-home.action.blank");
    public static final TranslationKey M_RENAME_HOME_EXISTS = TranslationKey.of("message.rename-home.action.already-exists");
    public static final TranslationKey M_RENAME_HOME_RENAMED = TranslationKey.of("message.rename-home.action.already-exists");
}
