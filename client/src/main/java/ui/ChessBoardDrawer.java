package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ChessBoardDrawer {
    private final String hgfRow = String.format("    h%sg%sf%se%sd%sc%sb%sa \n", EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.EMPTY);

    private final String abcRow = String.format("    a%sb%sc%sd%se%sf%sg%sh \n", EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY, EscapeSequences.EMPTY,
            EscapeSequences.EMPTY);

    public String generateChessBoard(ChessGame game, ChessGame.TeamColor teamColor) {
        String [][] boardIcons = initializeBoardIcons(game);

        if (teamColor == null || teamColor == ChessGame.TeamColor.WHITE) {
            return generateWhiteBoard(boardIcons);
        } else {
            return generateBlackBoard(boardIcons);
        }
    }

    public String highlightBoard(ChessGame game, ChessPosition startPos, Collection<ChessMove> validMoves, ChessGame.TeamColor teamColor) {
        String[][] boardIcons = initializeBoardIcons(game);

        if (teamColor == null || teamColor == ChessGame.TeamColor.WHITE) {
            return highlightWhiteBoard(boardIcons, startPos, validMoves);
        } else {
            return highlightBlackBoard(boardIcons, startPos, validMoves);
        }
    }

    private String[][] initializeBoardIcons(ChessGame game) {
        String[][] boardIcons = new String[8][8];
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                boardIcons[i - 1][j - 1] = getPieceIcon(game.getBoard().getPiece(new ChessPosition(i, j)));
            }
        }
        return boardIcons;
    }

    private String generateWhiteBoard(String[][] boardIcons) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n");
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
        stringBuilder.append("\n");
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

    private String highlightWhiteBoard(String[][] boardIcons, ChessPosition startPos, Collection<ChessMove> validMoves) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(EscapeSequences.SET_BG_COLOR_BLACK);
        stringBuilder.append("\n");
        stringBuilder.append(abcRow);
        stringBuilder.append("   ------------------------------- \n");
        for (int i = 0; i < boardIcons.length; i++) {
            // Append the row number
            stringBuilder.append((8 - i) + " |");
            // Iterate over each element in the row
            for (int j = 0; j < boardIcons[i].length; j++) {
                boolean outputted = false;
                if ((8 - i) == startPos.getRow() && (j + 1) == startPos.getColumn()) {
                    stringBuilder.append(EscapeSequences.SET_BG_COLOR_RED);
                    stringBuilder.append(boardIcons[7 - i][j]);
                    stringBuilder.append(EscapeSequences.SET_BG_COLOR_BLACK);
                } else {
                    if (validMoves != null) {
                        for (ChessMove validMove : validMoves) {
                            if ((8 - i) == validMove.getEndPosition().getRow() && (j + 1) == validMove.getEndPosition().getColumn()) {
                                stringBuilder.append(EscapeSequences.SET_BG_COLOR_DARK_GREEN);
                                stringBuilder.append(boardIcons[7 - i][j]);
                                stringBuilder.append(EscapeSequences.SET_BG_COLOR_BLACK);
                                outputted = true;
                            }
                        }
                    }
                    if (!outputted) {
                        stringBuilder.append(boardIcons[7 - i][j]);
                    }
                }
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
        stringBuilder.append(EscapeSequences.RESET_BG_COLOR);
        return stringBuilder.toString();
    }

    private String highlightBlackBoard(String[][] boardIcons, ChessPosition startPos, Collection<ChessMove> validMoves) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n");
        stringBuilder.append(EscapeSequences.SET_BG_COLOR_BLACK);
        stringBuilder.append(hgfRow);
        stringBuilder.append("   ------------------------------- \n");
        for (int i = 0; i < boardIcons.length; i++) {
            // Append the row number
            stringBuilder.append((i + 1) + " |");

            // Iterate over each element in the row
            for (int j = 7; j >= 0; j--) {
                boolean outputted = false;
                if ((i + 1) == startPos.getRow() && (j + 1) == startPos.getColumn()) {
                    stringBuilder.append(EscapeSequences.SET_BG_COLOR_RED);
                    stringBuilder.append(boardIcons[i][j]);
                    stringBuilder.append(EscapeSequences.SET_BG_COLOR_BLACK);
                } else {
                    if (validMoves != null) {
                        for (ChessMove validMove : validMoves) {
                            if ((i + 1) == validMove.getEndPosition().getRow() && (j + 1) == validMove.getEndPosition().getColumn()) {
                                stringBuilder.append(EscapeSequences.SET_BG_COLOR_DARK_GREEN);
                                stringBuilder.append(boardIcons[i][j]);
                                stringBuilder.append(EscapeSequences.SET_BG_COLOR_BLACK);
                                outputted = true;
                            }
                        }
                    }
                    if (!outputted) {
                        stringBuilder.append(boardIcons[i][j]);
                    }
                }
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
}
