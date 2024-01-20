package chess.moveCalculators;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.ChessBoard;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public abstract class MoveCalculator {
    protected ChessBoard board;
    protected ChessPosition position;
    protected  ChessGame.TeamColor pieceColor;
    protected HashSet<ChessMove> moves;

    public MoveCalculator(ChessBoard board, ChessPosition position, ChessGame.TeamColor pieceColor) {
        this.board = board;
        this.position = position;
        this.pieceColor = pieceColor;
        this.moves = new HashSet<>();
    }

    public abstract HashSet<ChessMove> generateMoves();

    public boolean validMoveCheck(int row, int col) {
        ChessPosition potentialLanding = new ChessPosition(row, col);
        if (row > 8 || col > 8) {
            return false;
        }
        if (row < 1 || col < 1) {
            return false;
        }
        if (board.getPiece(potentialLanding) != null) {
            return board.getPiece(potentialLanding).getTeamColor() != pieceColor;
        }
        return true;
    }
}
