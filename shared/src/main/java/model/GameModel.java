package model;

import chess.ChessGame;

public record GameModel(int gameID, String whiteUsername, String blackUsername,
                        String gameName) {}
