package bet.astral.wormhole.managers;

import bet.astral.messenger.v2.Messenger;
import bet.astral.messenger.v2.placeholder.collection.PlaceholderList;
import bet.astral.wormhole.objects.Request;
import bet.astral.wormhole.plugin.Translations;
import bet.astral.wormhole.plugin.WormholePlugin;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.*;

import static bet.astral.wormhole.plugin.Configuration.TeleportType.fromType;

public class RequestManager implements TickableManager {
    private WormholePlugin wormholePlugin;
    private Messenger messenger;
    @Getter
    private final Map<Request.Type, Map<UUID, List<Request>>> requests = new HashMap<>();

    public RequestManager(WormholePlugin wormholePlugin) {
        this.wormholePlugin = wormholePlugin;
        this.messenger = wormholePlugin.getMessenger();
    }

    public void request(Request.Type type, Player player, Player requested, Object extra) {
        // Get correct ticks for the player
        int ticks = wormholePlugin.getConfiguration().getTeleportRequestTime(player, fromType(type));
        Request request = new Request(type, player.getUniqueId(), requested.getUniqueId(), extra, ticks);

        // Add the request to the cache
        Map<UUID, List<Request>> requestMap = this.requests.computeIfAbsent(type, k -> new HashMap<>());
        List<Request> requestList = requestMap.get(player.getUniqueId());
        if (requestList == null) {
            requestList = new ArrayList<>();
        }
        requestList.removeIf(r->r.getRequested().getUniqueId().equals(requested.getUniqueId()));
        requestList.add(request);


        PlaceholderList placeholders = new PlaceholderList();
        placeholders.addAll(request.toPlaceholders());
        // Message about the request
        messenger.message(request.getPlayer(), switch (request.getType()) {
            case TO_PLAYER -> Translations.M_TPA_SENT_REQUEST;
            case PLAYER_HERE -> Translations.M_TPAHERE_SENT_REQUEST;
            case TO_OWN_HOME -> Translations.M_TPAMYHOME_SENT_REQUEST;
        }, placeholders);

        messenger.message(request.getPlayer(), switch (request.getType()) {
            case TO_PLAYER -> Translations.M_TPA_RECEIVED_REQUEST;
            case PLAYER_HERE -> Translations.M_TPAHERE_RECEIVED_REQUEST;
            case TO_OWN_HOME -> Translations.M_TPAMYHOME_RECEIVED_REQUEST;
        }, placeholders);
    }

    public Map<UUID, List<Request>> getRequests(Request.Type type){
        requests.putIfAbsent(type, new HashMap<>());
        return requests.get(type);
    }

    @Override
    public void tick() throws IllegalStateException {
        for (Request.Type type : Request.Type.values()) {
            tickRequests(type);
        }
    }

    /**
     * Ticks all the requests in the given type.
     *
     * @param type type
     */
    public void tickRequests(Request.Type type) {
        Map<UUID, List<Request>> map = requests.get(type);
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
