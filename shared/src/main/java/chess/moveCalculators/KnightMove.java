package chess.moveCalculators;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class KnightMove extends MoveCalculator {
    public KnightMove (ChessBoard board, ChessPosition position, ChessGame.TeamColor pieceColor) {
        super(board, position, pieceColor);
    }

    public HashSet<ChessMove> generateMoves() {
        //Every knight has 8 possible moves
        //Move 1: row+2, col+1
        //Move 2: row+1, col+2
        //Move 3: row-1, col+1
        //Move 4: row-2, col+1
        //Move 5: row-2, col-1
        //Move 6: row-1, col-2
        //Move 7: row+1, col-2
        //Move 8: row+2, col-1
        int row = position.getRow();
        int col = position.getColumn();
        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                if (i == j || i == 0 || j == 0 || (i+j == 0)) {
                    continue;
                }
                if (validMoveCheck(row + i, col + j)) {
                    moves.add(new ChessMove(position, new ChessPosition(row+i,col+j)));
                }
            }
        }
        return moves;
    }
}