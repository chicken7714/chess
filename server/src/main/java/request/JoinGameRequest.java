package request;

import java.util.UUID;

public record JoinGameRequest (UUID authToken, String playerColor, int gameID) {
}
