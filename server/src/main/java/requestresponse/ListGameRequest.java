package requestresponse;

import java.util.UUID;

public record ListGameRequest (UUID authToken) {
}
