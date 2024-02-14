package chess.moveCalculators;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.HashSet;

public class RookMove extends MoveCalculator {

    public RookMove(ChessBoard board, ChessPosition position, ChessGame.TeamColor pieceColor) {
        super(board, position, pieceColor);
    }

    public HashSet<ChessMove> generateMoves() {
        int row = position.getRow();
        int col = position.getColumn();

        for (int i = row + 1; i <= 8; i++) {
            if (pieceChecking(i, col)) break;
        }
        for (int i = row - 1; i >= 1; i--) {
            if (pieceChecking(i, col)) break;
        }
        for (int i = col + 1; i <= 8; i++) {
            if (pieceChecking(row, i)) break;
        }
        for (int i = col - 1; i >= 1; i--) {
            if (pieceChecking(row, i)) break;
        }

        return moves;
    }

    private boolean pieceChecking(int row, int col) {
        ChessPosition potentialLanding = new ChessPosition(row, col);
        if (board.getPiece(potentialLanding) != null) {
            if (board.getPiece(potentialLanding).getTeamColor() != pieceColor) {
                moves.add(new ChessMove(position, potentialLanding));
            }
            return true;
        } else {
            moves.add(new ChessMove(position, potentialLanding));
        }
        return false;
    }
}
