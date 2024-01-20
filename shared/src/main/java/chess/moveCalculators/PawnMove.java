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
        if (pieceColor == ChessGame.TeamColor.WHITE) {
            //Base Case
            if (pawnMoveCheck(row+1, col, false)) {
                moves.add(new ChessMove(position, new ChessPosition(row+1, col)));
            }
            //Front Row Check
            if (row == 2) {
                if (pawnMoveCheck(row+2, col, false)) {
                    moves.add(new ChessMove(position, new ChessPosition(row+2, col)));
                }
            }
            //Capture Cases
            for (int i = -1; i <= 1; i = i+2) {
                if (pawnMoveCheck(row+1, col+i, true)) {
                    moves.add(new ChessMove(position, new ChessPosition(row+1, col+i)));
                }
            }
        }
        return moves;
    }

    public boolean pawnMoveCheck(int row, int col, boolean capture) {
        ChessPosition potentialLanding = new ChessPosition(row, col);
        if (row > 8 || col > 8) {
            return false;
        }
        if (row < 1 || col < 1) {
            return false;
        }
        if (capture) {
            if (board.getPiece(potentialLanding) != null) {
                if (board.getPiece(potentialLanding).getTeamColor() != pieceColor) {
                    return true;
                }
            } else {
                return false;
            }
        }
        return board.getPiece(potentialLanding) == null;
    }
}