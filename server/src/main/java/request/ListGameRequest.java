package request;

import java.util.UUID;

public record ListGameRequest (UUID authToken) {
}
