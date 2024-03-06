package request;

import java.util.UUID;

public record JoinGameRequest (String authToken, String playerColor, int gameID) {
}
