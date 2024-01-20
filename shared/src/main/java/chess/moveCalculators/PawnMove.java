package chess.moveCalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;

public class PawnMove extends MoveCalculator{
    public PawnMove (ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    private void generateMoves() {
    }
}