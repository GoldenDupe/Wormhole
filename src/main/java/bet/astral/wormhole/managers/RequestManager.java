package bet.astral.wormhole.managers;

import bet.astral.messenger.v2.Messenger;
import bet.astral.messenger.v2.placeholder.collection.PlaceholderList;
import bet.astral.wormhole.objects.Request;
import bet.astral.wormhole.objects.Teleport;
import bet.astral.wormhole.objects.data.Warp;
import bet.astral.wormhole.plugin.translation.Translations;
import bet.astral.wormhole.plugin.WormholePlugin;
import bet.astral.wormhole.plugin.configuration.Configuration;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static bet.astral.wormhole.plugin.configuration.Configuration.TeleportType.fromType;

public class RequestManager implements TickableManager {
    private WormholePlugin wormholePlugin;
    private Messenger messenger;
    @Getter
    private final Map<Request.Type, Map<UUID, List<Request>>> sentRequests = new HashMap<>();
    private final Map<Request.Type, Map<UUID, List<Request>>> receivedRequests = new HashMap<>();
    @Getter
    private final Map<UUID, Request> latestReceivedRequest = new HashMap<>();
    @Getter
    private final Map<UUID, Request> latestSentRequest = new HashMap<>();

    public RequestManager(@NotNull WormholePlugin wormholePlugin) {
        this.wormholePlugin = wormholePlugin;
        this.messenger = wormholePlugin.getMessenger();
    }

    public void deny(Request request) {

    }


    public void accept(@NotNull Request request) {
        Player player = request.getPlayer().getPlayer();
        Player requested = request.getPlayer().getPlayer();
        Object extra = request.getExtraInfo();
        Request.Type type = request.getType();

        PlaceholderList placeholders = new PlaceholderList();
        placeholders.addAll(request.toPlaceholders());

        messenger.message(request.getPlayer(), switch (request.getType()) {
            case TO_PLAYER -> Translations.M_TPACCEPT_TPA_ACCEPTED_PLAYER;
            case PLAYER_HERE -> Translations.M_TPACCEPT_TPAHERE_ACCEPTED_PLAYER;
            case TO_OWN_HOME -> Translations.M_TPACCEPT_TPAMYHOME_ACCEPTED_PLAYER;
        }, placeholders);

        messenger.message(request.getRequested(), switch (request.getType()) {
            case TO_PLAYER -> Translations.M_TPACCEPT_TPA_ACCEPTED_OTHER;
            case PLAYER_HERE -> Translations.M_TPACCEPT_TPAHERE_ACCEPTED_OTHER;
            case TO_OWN_HOME -> Translations.M_TPACCEPT_TPAMYHOME_ACCEPTED_OTHER;
        }, placeholders);

        // Accept it finally
        request.setAccepted(true);

        // Add the teleport to the teleport manager
        TeleportManager teleportManager = wormholePlugin.getTeleportManager();
        int ticksLeft = wormholePlugin.getConfiguration().getTeleportDelay(player, Configuration.TeleportType.fromType(type));

        assert player != null;
        teleportManager.addTeleport(
                new Teleport(
                        wormholePlugin,
                        player,
                        requested,
                        ticksLeft,
                        _ -> switch (type) {
                            case TO_PLAYER -> requested.getLocation();
                            case PLAYER_HERE -> player.getLocation();
                            case TO_OWN_HOME -> extra instanceof Warp warp ? warp.getLocation() : null;
                        },
                        _ -> {
                            Location location = switch (type) {
                                case TO_PLAYER -> requested.getLocation();
                                case PLAYER_HERE -> requested.getPlayer().getLocation();
                                case TO_OWN_HOME -> extra instanceof Warp warp ? warp.getLocation() : null;
                            };

                            if (location == null) {
                                wormholePlugin.getComponentLogger().error(Component.text("Couldn't teleport %s to %s's home as it does not exist!"), requested.getName(), player.getName());
                                return false;
                            }
                            return true;
                        },
                        switch (type) {
                            case TO_PLAYER -> Translations.M_TPA_TELEPORTING_PLAYER;
                            case PLAYER_HERE -> Translations.M_TPAHERE_TELEPORTING_PLAYER;
                            case TO_OWN_HOME -> Translations.M_TPAMYHOME_TELEPORTING_PLAYER;
                        },
                        switch (type) {
                            case TO_PLAYER -> Translations.M_TPA_TELEPORTING_OTHER;
                            case PLAYER_HERE -> Translations.M_TPAHERE_TELEPORTING_OTHER;
                            case TO_OWN_HOME -> Translations.M_TPAMYHOME_TELEPORTING_OTHER;
                        },
                        switch (type) {
                            case TO_PLAYER -> Translations.M_TPA_TELEPORT_CANCELLED_MOVED_PLAYER;
                            case PLAYER_HERE ->  Translations.M_TPAHERE_TELEPORT_CANCELLED_MOVED_PLAYER;
                            case TO_OWN_HOME ->  Translations.M_TPAMYHOME_TELEPORT_CANCELLED_MOVED_PLAYER;
                        },
                        switch (type) {
                            case TO_PLAYER -> Translations.M_TPA_TELEPORT_CANCELLED_MOVED_OTHER;
                            case PLAYER_HERE ->  Translations.M_TPAHERE_TELEPORT_CANCELLED_MOVED_OTHER;
                            case TO_OWN_HOME ->  Translations.M_TPAMYHOME_TELEPORT_CANCELLED_MOVED_OTHER;
                        },
                        switch (type) {
                            case TO_PLAYER -> Translations.M_TPA_TELEPORTING_IN_5_3_2_1_SECONDS;
                            case PLAYER_HERE ->  Translations.M_TPAHERE_TELEPORTING_IN_5_3_2_1_SECONDS;
                            case TO_OWN_HOME ->  Translations.M_TPAMYHOME_TELEPORTING_IN_5_3_2_1_SECONDS;
                        },
                        switch (type) {
                            case TO_PLAYER -> Translations.M_TPA_PLAYER_OFFLINE;
                            case PLAYER_HERE ->  Translations.M_TPAHERE_PLAYER_OFFLINE;
                            case TO_OWN_HOME ->  Translations.M_TPAMYHOME_PLAYER_OFFLINE;
                        },
                        switch (type) {
                            case TO_PLAYER -> Translations.M_TPA_OTHER_OFFLINE;
                            case PLAYER_HERE ->  Translations.M_TPAHERE_OTHER_OFFLINE;
                            case TO_OWN_HOME ->  Translations.M_TPAMYHOME_OTHER_OFFLINE;
                        },
                        null
                ));
    }
    public void request(Request.Type type, Player player, @NotNull Player requested, Object extra) {
        // Get correct ticks for the player
        int ticks = wormholePlugin.getConfiguration().getTeleportRequestTime(player, fromType(type));
        Request request = new Request(type, player.getUniqueId(), requested.getUniqueId(), extra, ticks);

        // Add the request to the cache
        this.sentRequests.computeIfAbsent(type, k -> new HashMap<>());
        updateMap(sentRequests.get(type), player.getUniqueId(), requested.getUniqueId(), request);
        this.receivedRequests.computeIfAbsent(type, k -> new HashMap<>());
        updateMap(sentRequests.get(type), requested.getUniqueId(), player.getUniqueId(), request);

        latestSentRequest.put(player.getUniqueId(), request);
        latestReceivedRequest.put(requested.getUniqueId(), request);

        PlaceholderList placeholders = new PlaceholderList();
        placeholders.addAll(request.toPlaceholders());
        // Message about the request
        messenger.message(request.getPlayer(), switch (request.getType()) {
            case TO_PLAYER -> Translations.M_TPA_SENT_REQUEST;
            case PLAYER_HERE -> Translations.M_TPAHERE_SENT_REQUEST;
            case TO_OWN_HOME -> Translations.M_TPAMYHOME_SENT_REQUEST;
        }, placeholders);

        messenger.message(request.getRequested(), switch (request.getType()) {
            case TO_PLAYER -> Translations.M_TPA_RECEIVED_REQUEST;
            case PLAYER_HERE -> Translations.M_TPAHERE_RECEIVED_REQUEST;
            case TO_OWN_HOME -> Translations.M_TPAMYHOME_RECEIVED_REQUEST;
        }, placeholders);
    }

    public void sortLatestRequest(Player player) {
        Request received = findLatestRequest(player, receivedRequests);
        this.latestReceivedRequest.put(player.getUniqueId(), received);
        Request sent = findLatestRequest(player, sentRequests);
        this.latestSentRequest.put(player.getUniqueId(), sent);
    }

    public Request findLatestRequest(Player player, Map<Request.Type, Map<UUID, List<Request>>> requests) {
        UUID who = player.getUniqueId();
        Request latest = latestSentRequest.get(who);

        for (Request.Type type : Request.Type.values()) {
            Map<UUID, List<Request>> map = requests.get(type);
            if (map == null || !map.containsKey(who)) continue;

            List<Request> list = map.get(who);
            if (list == null || list.isEmpty()) continue;

            for (Request r : list) {
                if (latest == null || r.getTimeSent() > latest.getTimeSent()) {
                    latest = r;
                }
            }
        }

        return latest;
    }


    private void updateMap(Map<UUID, List<Request>> map, UUID player, UUID requested, Request request) {
        List<Request> requestList = map.get(player);
        if (requestList == null) {
            requestList = new ArrayList<>();
        }
        requestList.removeIf(r->r.getRequested().getUniqueId().equals(requested));
        requestList.add(request);
        map.put(player, requestList);
    }

    public Map<UUID, List<Request>> getSentRequests(Request.Type type){
        sentRequests.putIfAbsent(type, new HashMap<>());
        return sentRequests.get(type);
    }

    public Map<UUID, List<Request>> getReceivedRequests(Request.Type type){
        receivedRequests.putIfAbsent(type, new HashMap<>());
        return receivedRequests.get(type);
    }

    @Override
    public void tick() throws IllegalStateException {
        for (Request.Type type : Request.Type.values()) {
            tickRequests(type);
        }

        // Fix latest requests
        for (Player player : wormholePlugin.getServer().getOnlinePlayers()) {
            latestReceivedRequest.remove(player.getUniqueId());
        }
    }

    /**
     * Ticks all the requests in the given type.
     *
     * @param type type
     */
    public void tickRequests(Request.Type type) {
        Map<UUID, List<Request>> map = sentRequests.get(type);
        if (map == null) {
            return;
        }
        for (Map.Entry<UUID, List<Request>> entry : map.entrySet()) {
            List<Request> requests = tickRequests(entry.getValue());
            map.put(entry.getKey(), requests);
        }
    }

    /**
     * Ticks all requests in the list. Returns a new list with updated requests. Expired, Denied, Accepted requests are removed from the list.
     * Sends message if a teleport request is going to expire or expires.
     * Checks if a player is offline or online and expires the request if the player is offline
     *
     * @param requests requests
     * @return new list of still going requests
     */
    public List<Request> tickRequests(List<Request> requests) {
        List<Request> returnList = new ArrayList<>();
        for (Request request : requests) {
            if (request.isAccepted() || request.isDenied()) {
                continue;
            }
            PlaceholderList placeholders = new PlaceholderList();

            // Tick the time
            request.tick();

            // Check if either player is offline, if so then cancel the request and send message if the other player is online
            if (request.isEitherPlayerOffline()) {
                placeholders.addAll(request.toPlaceholders());
                if (request.isPlayerOffline() && !request.isRequestedOffline()) {
                    // The player who requested is offline -> send to the requested player message
                    messenger.message(request.getRequested(), switch (request.getType()) {
                        case TO_PLAYER -> Translations.M_TPA_PLAYER_SELF_OFFLINE;
                        case PLAYER_HERE -> Translations.M_TPAHERE_PLAYER_SELF_OFFLINE;
                        case TO_OWN_HOME -> Translations.M_TPAMYHOME_PLAYER_SELF_OFFLINE;
                    }, placeholders);
                } else if (request.isRequestedOffline() && !request.isPlayerOffline()) {
                    // The player who was requested is offline -> send to the requestee a message
                    messenger.message(request.getPlayer(), switch (request.getType()) {
                        case TO_PLAYER -> Translations.M_TPA_PLAYER_REQUESTED_OFFLINE;
                        case PLAYER_HERE -> Translations.M_TPAHERE_PLAYER_REQUESTED_OFFLINE;
                        case TO_OWN_HOME -> Translations.M_TPAMYHOME_PLAYER_REQUESTED_OFFLINE;
                    }, placeholders);
                }
                continue;
            }

            /*
            if (request.getTicksLeft() == 100) {
                // Not used
            }
             */

            if (request.getTicksLeft() <= 0) {
                // Expired
                messenger.message(request.getPlayer(), switch (request.getType()) {
                    case TO_PLAYER -> Translations.M_TPA_REQUEST_RAN_OUT_OF_TIME_SELF;
                    case PLAYER_HERE -> Translations.M_TPAHERE_REQUEST_RAN_OUT_OF_TIME_SELF;
                    case TO_OWN_HOME -> Translations.M_TPAMYHOME_REQUEST_RAN_OUT_OF_TIME_SELF;
                }, placeholders);

                messenger.message(request.getPlayer(), switch (request.getType()) {
                    case TO_PLAYER -> Translations.M_TPA_REQUEST_RAN_OUT_OF_TIME_REQUESTED;
                    case PLAYER_HERE -> Translations.M_TPAHERE_REQUEST_RAN_OUT_OF_TIME_REQUESTED;
                    case TO_OWN_HOME -> Translations.M_TPAMYHOME_REQUEST_RAN_OUT_OF_TIME_REQUESTED;
                }, placeholders);
                continue;
            }
            returnList.add(request);
        }

        return returnList;
    }
}
