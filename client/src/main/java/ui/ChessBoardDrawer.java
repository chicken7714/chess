package ui;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChessBoardDrawer {
    private final String hgfRow = String.format("    h%sg%sf%se%sd%sc%sb%sa \n", EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.EMPTY);

    private final String abcRow = String.format("    a%sb%sc%sd%se%sf%sg%sh \n", EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.EMPTY);

    public String generateChessBoard(ChessGame game, ChessGame.TeamColor teamColor) {
        String[][] boardIcons = new String[8][8];
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                boardIcons[i - 1][j - 1] = getPieceIcon(game.getBoard().getPiece(new ChessPosition(i, j)));
            }
        }

        if (teamColor == null || teamColor == ChessGame.TeamColor.WHITE) {
            return generateWhiteBoard(boardIcons);
        } else {
            return generateBlackBoard(boardIcons);
        }
    }

    private String generateWhiteBoard(String[][] boardIcons) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(abcRow);
        stringBuilder.append("   ------------------------------- \n");
        for (int i = 0; i < boardIcons.length; i++) {
            // Append the row number
            stringBuilder.append((8 - i) + " |");

            // Iterate over each element in the row
            for (int j = 0; j < boardIcons[i].length; j++) {
                stringBuilder.append(boardIcons[7-i][j]);

                // Append separator except for the last element in the row
                if (j < boardIcons[i].length - 1) {
                    stringBuilder.append("|");
                }
            }
            // Append the row number at the end of the row
            stringBuilder.append("| " + (8 - i) + "\n");
        }
        stringBuilder.append("   ------------------------------- \n");
        stringBuilder.append(abcRow);
        return stringBuilder.toString();
    }

    private String generateBlackBoard(String[][] boardIcons) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(hgfRow);
        stringBuilder.append("   ------------------------------- \n");
        for (int i = 0; i < boardIcons.length; i++) {
            // Append the row number
            stringBuilder.append((i + 1) + " |");

            // Iterate over each element in the row
            for (int j = 7; j >= 0; j--) {
                stringBuilder.append(boardIcons[i][j]);

                // Append separator except for the last element in the row
                if (j > 0) {
                    stringBuilder.append("|");
                }
            }
            // Append the row number at the end of the row
            stringBuilder.append("| " + (i + 1) + "\n");
        }
        stringBuilder.append("   ------------------------------- \n");
        stringBuilder.append(hgfRow);
        return stringBuilder.toString();
    }

    private String getPieceIcon(ChessPiece piece) {
        Map<ChessPiece.PieceType, String> whiteIcons = new HashMap<>();
        whiteIcons.put(ChessPiece.PieceType.PAWN, EscapeSequences.BLACK_PAWN);
        whiteIcons.put(ChessPiece.PieceType.ROOK, EscapeSequences.BLACK_ROOK);
        whiteIcons.put(ChessPiece.PieceType.KNIGHT, EscapeSequences.BLACK_KNIGHT);
        whiteIcons.put(ChessPiece.PieceType.BISHOP, EscapeSequences.BLACK_BISHOP);
        whiteIcons.put(ChessPiece.PieceType.QUEEN, EscapeSequences.BLACK_QUEEN);
        whiteIcons.put(ChessPiece.PieceType.KING, EscapeSequences.BLACK_KING);

        Map<ChessPiece.PieceType, String> blackIcons = new HashMap<>();
        blackIcons.put(ChessPiece.PieceType.PAWN, EscapeSequences.WHITE_PAWN);
        blackIcons.put(ChessPiece.PieceType.ROOK, EscapeSequences.WHITE_ROOK);
        blackIcons.put(ChessPiece.PieceType.KNIGHT, EscapeSequences.WHITE_KNIGHT);
        blackIcons.put(ChessPiece.PieceType.BISHOP, EscapeSequences.WHITE_BISHOP);
        blackIcons.put(ChessPiece.PieceType.QUEEN, EscapeSequences.WHITE_QUEEN);
        blackIcons.put(ChessPiece.PieceType.KING, EscapeSequences.WHITE_KING);

        if (piece == null) {
            return EscapeSequences.EMPTY;
        } else if (piece.getTeamColor().equals(ChessGame.TeamColor.BLACK)) {
            return blackIcons.get(piece.getPieceType());
        } else {
            return whiteIcons.get(piece.getPieceType());
        }
    }
}
