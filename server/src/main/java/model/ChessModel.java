package model;

import chess.ChessGame;

record ChessModel(int gameID, String whiteUsername, String blackUsername,
                  String gameName, ChessGame game) {}
