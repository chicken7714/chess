package request;

import java.util.UUID;

public record CreateGameRequest (String authToken, String gameName) {
}
