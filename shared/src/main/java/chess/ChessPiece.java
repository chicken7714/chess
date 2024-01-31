package chess;

import chess.moveCalculators.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType type;


    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    public ChessPiece(ChessPiece otherPiece) {
        this.pieceColor = otherPiece.pieceColor;
        this.type = otherPiece.type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        MoveCalculator moves = switch (this.type) {
            case PieceType.KING -> new KingMove(board, myPosition, this.pieceColor);
            case PieceType.QUEEN -> new QueenMove(board, myPosition, this.pieceColor);
            case PieceType.BISHOP -> new BishopMove(board, myPosition, this.pieceColor);
            case PieceType.KNIGHT -> new KnightMove(board, myPosition, this.pieceColor);
            case PieceType.ROOK -> new RookMove(board, myPosition, this.pieceColor);
            case PieceType.PAWN -> new PawnMove(board, myPosition, this.pieceColor);
            };
        return moves.generateMoves();
    }

    public String toString() {
        return getClass().getName() + "[pieceColor =" + pieceColor + ",type = " + type + "]";
    }
}
