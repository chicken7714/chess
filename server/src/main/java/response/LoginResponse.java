package response;

import java.util.UUID;

public record LoginResponse (String username, String authToken) {}
