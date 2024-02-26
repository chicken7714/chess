package response;

import model.GameModel;

import java.util.Collection;

public record ListGameResponse (Collection<GameModel> games) {
}
