package chess.moveCalculators;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.HashSet;

public class QueenMove extends MoveCalculator {
    public QueenMove(ChessBoard board, ChessPosition position, ChessGame.TeamColor pieceColor) {
        super(board, position, pieceColor);
    }

    public HashSet<ChessMove> generateMoves() {
        moves = new RookMove(board, position, pieceColor).generateMoves();
        moves.addAll(new BishopMove(board, position, pieceColor).generateMoves());

        return moves;
    }
}
