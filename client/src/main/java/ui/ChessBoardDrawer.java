package ui;

import chess.ChessGame;

import java.util.Collections;

public class ChessBoardDrawer {
    private final String columnNameRow = String.format("    h%sg%sf%se%sd%sc%sb%sa \n", EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.EMPTY);

    private final String whitePawnRow = String.format("2 |%s|%s|%s|%s|%s|%s|%s|%s| 2\n", EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN,
            EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN,
            EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN);

    private final String blackPawnRow = String.format("7 |%s|%s|%s|%s|%s|%s|%s|%s| 7\n", EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN,
            EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN,
            EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN);

    private final String abcRow = String.format("    a%sb%sc%sd%se%sf%sg%sh \n", EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.EMPTY);

    public String generateChessBoard(ChessGame game) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(columnNameRow);
        stringBuilder.append("   ------------------------------- \n");
        stringBuilder.append(String.format("1 |%s|%s|%s|%s|%s|%s|%s|%s| 1\n", EscapeSequences.WHITE_ROOK, EscapeSequences.WHITE_KNIGHT,
                EscapeSequences.WHITE_BISHOP, EscapeSequences.WHITE_KING, EscapeSequences.WHITE_QUEEN,
                EscapeSequences.WHITE_BISHOP, EscapeSequences.WHITE_KNIGHT, EscapeSequences.WHITE_ROOK));
        stringBuilder.append(whitePawnRow);
        stringBuilder.append(String.format("3 |%s| 3\n", EscapeSequences.REPEATED_EMPTY));
        stringBuilder.append(String.format("4 |%s| 4\n", EscapeSequences.REPEATED_EMPTY));
        stringBuilder.append(String.format("5 |%s| 5\n", EscapeSequences.REPEATED_EMPTY));
        stringBuilder.append(String.format("6 |%s| 6\n", EscapeSequences.REPEATED_EMPTY));
        stringBuilder.append(blackPawnRow);
        stringBuilder.append(String.format("8 |%s|%s|%s|%s|%s|%s|%s|%s| 8\n", EscapeSequences.BLACK_ROOK, EscapeSequences.BLACK_KNIGHT,
                EscapeSequences.BLACK_BISHOP, EscapeSequences.BLACK_KING, EscapeSequences.BLACK_QUEEN,
                EscapeSequences.BLACK_BISHOP, EscapeSequences.BLACK_KNIGHT, EscapeSequences.BLACK_ROOK));
        stringBuilder.append("   -------------------------------\n");
        stringBuilder.append(columnNameRow);
        stringBuilder.append("\n");
        stringBuilder.append("--------------------------------------");
        stringBuilder.append("\n\n");
        stringBuilder.append(abcRow);
        stringBuilder.append("   ------------------------------- \n");
        stringBuilder.append(String.format("8 |%s|%s|%s|%s|%s|%s|%s|%s| 8\n", EscapeSequences.BLACK_ROOK, EscapeSequences.BLACK_KNIGHT,
                EscapeSequences.BLACK_BISHOP, EscapeSequences.BLACK_QUEEN, EscapeSequences.BLACK_KING,
                EscapeSequences.BLACK_BISHOP, EscapeSequences.BLACK_KNIGHT, EscapeSequences.BLACK_ROOK));
        stringBuilder.append(blackPawnRow);
        stringBuilder.append(String.format("6 |%s| 6\n", EscapeSequences.REPEATED_EMPTY));
        stringBuilder.append(String.format("5 |%s| 5\n", EscapeSequences.REPEATED_EMPTY));
        stringBuilder.append(String.format("4 |%s| 4\n", EscapeSequences.REPEATED_EMPTY));
        stringBuilder.append(String.format("3 |%s| 3\n", EscapeSequences.REPEATED_EMPTY));
        stringBuilder.append(whitePawnRow);
        stringBuilder.append(String.format("1 |%s|%s|%s|%s|%s|%s|%s|%s| 1\n", EscapeSequences.WHITE_ROOK, EscapeSequences.WHITE_KNIGHT,
                EscapeSequences.WHITE_BISHOP, EscapeSequences.WHITE_QUEEN, EscapeSequences.WHITE_KING,
                EscapeSequences.WHITE_BISHOP, EscapeSequences.WHITE_KNIGHT, EscapeSequences.WHITE_ROOK));
        stringBuilder.append("   ------------------------------- \n");
        stringBuilder.append(abcRow);

        return stringBuilder.toString();
    }

}
