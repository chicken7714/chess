package chess.moveCalculators;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class KingMove extends MoveCalculator {
    public KingMove (ChessBoard board, ChessPosition position, ChessGame.TeamColor pieceColor) {
        super(board, position, pieceColor);
    }

    public HashSet<ChessMove> generateMoves() {
        return new HashSet<>();
    }
}