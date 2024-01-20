package chess.moveCalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;

public class KingMove extends MoveCalculator {
    public KingMove (ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    private void generateMoves() {
    }
}