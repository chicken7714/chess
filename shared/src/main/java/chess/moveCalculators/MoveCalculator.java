package chess.moveCalculators;

import chess.ChessMove;
import chess.ChessPosition;
import chess.ChessBoard;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MoveCalculator {
    private ArrayList<ChessMove> moves;
    private ChessBoard board;
    private ChessPosition position;

    public MoveCalculator(ChessBoard board, ChessPosition position){
        this.board = board;
        this.position = position;
        this.moves = new ArrayList<>();
    }

    public void generateRules() {

    }

    public ArrayList<ChessMove> getMoves() {
        return this.moves;
    }
}
