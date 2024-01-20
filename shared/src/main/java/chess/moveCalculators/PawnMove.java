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

        if (pieceColor.equals(ChessGame.TeamColor.WHITE)) {
            for (int i = -1; i <= 1; i++) {
                ChessPosition potentialLanding = new ChessPosition(row + 1, col + i);
                if (i == 0) {
                    if (board.getPiece(potentialLanding) == null) {
                        if (row == 7) {
                            moves.add(new ChessMove(position, potentialLanding, ChessPiece.PieceType.BISHOP));
                            moves.add(new ChessMove(position, potentialLanding, ChessPiece.PieceType.KNIGHT));
                            moves.add(new ChessMove(position, potentialLanding, ChessPiece.PieceType.ROOK));
                            moves.add(new ChessMove(position, potentialLanding, ChessPiece.PieceType.QUEEN));
                        } else {
                            moves.add(new ChessMove(position, potentialLanding));
                            ChessPosition potentialDouble = new ChessPosition(row + 2, col);
                            if (row == 2 && (board.getPiece(potentialDouble) == null)) {
                                moves.add(new ChessMove(position, potentialDouble));
                            }
                        }
                    }
                } else {
                    if (board.getPiece(potentialLanding) != null && board.getPiece(potentialLanding).getTeamColor() != pieceColor) {
                        if (row == 7) {
                            moves.add(new ChessMove(position, potentialLanding, ChessPiece.PieceType.BISHOP));
                            moves.add(new ChessMove(position, potentialLanding, ChessPiece.PieceType.KNIGHT));
                            moves.add(new ChessMove(position, potentialLanding, ChessPiece.PieceType.ROOK));
                            moves.add(new ChessMove(position, potentialLanding, ChessPiece.PieceType.QUEEN));
                        } else {
                            moves.add(new ChessMove(position, potentialLanding));
                        }
                    }
                }
            }
        }

        if (pieceColor.equals(ChessGame.TeamColor.BLACK)) {
            for (int i = -1; i <= 1; i++) {
                ChessPosition potentialLanding = new ChessPosition(row - 1, col + i);
                if (i == 0) {
                    if (board.getPiece(potentialLanding) == null) {
                        if (row == 2) {
                            moves.add(new ChessMove(position, potentialLanding, ChessPiece.PieceType.BISHOP));
                            moves.add(new ChessMove(position, potentialLanding, ChessPiece.PieceType.KNIGHT));
                            moves.add(new ChessMove(position, potentialLanding, ChessPiece.PieceType.ROOK));
                            moves.add(new ChessMove(position, potentialLanding, ChessPiece.PieceType.QUEEN));
                        } else {
                            moves.add(new ChessMove(position, potentialLanding));
                            ChessPosition potentialDouble = new ChessPosition(row - 2, col);
                            if (row == 7 && (board.getPiece(potentialDouble) == null)) {
                                moves.add(new ChessMove(position, potentialDouble));
                            }
                        }
                    }
                } else {
                    if (board.getPiece(potentialLanding) != null && board.getPiece(potentialLanding).getTeamColor() != pieceColor) {
                        if (row == 2) {
                            moves.add(new ChessMove(position, potentialLanding, ChessPiece.PieceType.BISHOP));
                            moves.add(new ChessMove(position, potentialLanding, ChessPiece.PieceType.KNIGHT));
                            moves.add(new ChessMove(position, potentialLanding, ChessPiece.PieceType.ROOK));
                            moves.add(new ChessMove(position, potentialLanding, ChessPiece.PieceType.QUEEN));
                        } else {
                            moves.add(new ChessMove(position, potentialLanding));
                        }
                    }
                }
            }
        }

        return moves;
    }
}