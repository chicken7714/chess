package chess.moveCalculators;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;

public class BishopMove extends MoveCalculator {
    public BishopMove (ChessBoard board, ChessPosition position, ChessGame.TeamColor pieceColor) {
        super(board, position, pieceColor);
    }

    public HashSet<ChessMove> generateMoves() {
        int row = this.position.getRow() + 1;
        int col = this.position.getColumn() + 1;
        while (row <= 8 && col <= 8) {
            ChessPosition potentialLanding = new ChessPosition(row, col);
            if (board.getPiece(potentialLanding) != null) {
                if (board.getPiece(potentialLanding).getTeamColor() != pieceColor) {
                    moves.add(new ChessMove(position, potentialLanding));
                }
                break;
            } else {
                moves.add(new ChessMove(position, potentialLanding));
                row++;
                col++;
            }
        }
        row = this.position.getRow() - 1;
        col = this.position.getColumn() - 1;
        while (row >= 1 && col >= 1) {
            ChessPosition potentialLanding = new ChessPosition(row, col);
            if (board.getPiece(potentialLanding) != null) {
                if (board.getPiece(potentialLanding).getTeamColor() != pieceColor) {
                    moves.add(new ChessMove(position, potentialLanding));
                }
                break;
            } else {
                moves.add(new ChessMove(position, potentialLanding));
                row--;
                col--;
            }
        }

        row = this.position.getRow() + 1;
        col = this.position.getColumn() - 1;
        while (row <= 8 && col >= 1) {
            ChessPosition potentialLanding = new ChessPosition(row, col);
            if (board.getPiece(potentialLanding) != null) {
                if (board.getPiece(potentialLanding).getTeamColor() != pieceColor) {
                    moves.add(new ChessMove(position, potentialLanding));
                }
                break;
            } else {
                moves.add(new ChessMove(position, potentialLanding));
                row++;
                col--;
            }
        }

        row = this.position.getRow() - 1;
        col = this.position.getColumn() + 1;
        while (row >= 1 && col <= 8) {
            ChessPosition potentialLanding = new ChessPosition(row, col);
            if (board.getPiece(potentialLanding) != null) {
                if (board.getPiece(potentialLanding).getTeamColor() != pieceColor) {
                    moves.add(new ChessMove(position, potentialLanding));
                }
                break;
            } else {
                moves.add(new ChessMove(position, potentialLanding));
                row--;
                col++;
            }
        }
        return this.moves;
    }
}