package requestresponse;

import java.util.UUID;

public record CreateGameRequest (UUID authToken, String gameName) {
}
