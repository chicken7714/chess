package chess.moveCalculators;

import chess.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class PawnMove extends MoveCalculator{
    public PawnMove (ChessBoard board, ChessPosition position, ChessGame.TeamColor pieceColor) {
        super(board, position, pieceColor);
    }

    public HashSet<ChessMove> generateMoves() {
        int row = position.getRow();
        int col = position.getColumn();

        return moves;
    }
}