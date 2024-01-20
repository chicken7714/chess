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
        int row = this.position.getRow();
        int col = this.position.getColumn();

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                if ((row + i) >= 8 || (row + i) <= 1) {
                    continue;
                }
                if ((col + j) >= 8 || (col +j) <= 1) {
                    continue;
                }
                ChessPosition potentialLanding = new ChessPosition(row+i, col+j);
                if (board.getPiece(potentialLanding) == null) {
                    moves.add(new ChessMove(position, potentialLanding));
                } else {
                    if (board.getPiece(potentialLanding).getTeamColor() != pieceColor) {
                        moves.add(new ChessMove(position, potentialLanding));
                    }
                }
            }
        }
        return moves;
    }
}