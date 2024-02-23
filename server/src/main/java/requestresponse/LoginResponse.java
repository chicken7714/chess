package requestresponse;

import java.util.UUID;

public record LoginResponse (String username, UUID authToken) {}
