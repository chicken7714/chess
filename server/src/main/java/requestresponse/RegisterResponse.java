package requestresponse;

import java.util.UUID;

public record RegisterResponse (String username, UUID authToken){
}
