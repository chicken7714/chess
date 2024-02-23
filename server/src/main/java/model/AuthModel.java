package model;

import java.util.UUID;

public record AuthModel(UUID authToken, String username) {}