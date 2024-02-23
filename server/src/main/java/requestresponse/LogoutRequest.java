package requestresponse;

import java.util.UUID;

public record LogoutRequest (UUID authToken) {
}
