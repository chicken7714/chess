package chess;

import java.util.Collection;
import java.util.HashSet;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor teamTurn;
    private ChessBoard board;
    public ChessGame() {
        setTeamTurn(TeamColor.WHITE);
        this.board = new ChessBoard();
        this.board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    /**
     * Switches which teams turn it is
     */
    private void switchTeamTurn() {
        if (this.teamTurn == TeamColor.WHITE) {
            this.teamTurn = TeamColor.BLACK;
        } else {
            this.teamTurn = TeamColor.WHITE;
        }
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        HashSet<ChessMove> validMoves = new HashSet<>();
        if (board.getPiece(startPosition) == null) {
            return null;
        } else {
            //This block of code checks whether the move will lead to check
            ChessPiece movingPiece = board.getPiece(startPosition);
            Collection<ChessMove> potentialMoves = movingPiece.pieceMoves(board, startPosition);
            for (ChessMove potentialMove : potentialMoves) {
                ChessGame simulation = new ChessGame();
                simulation.setBoard(new ChessBoard(board));
                simulation.board.addPiece(potentialMove.getEndPosition(), movingPiece);
                simulation.board.removePiece(potentialMove.getStartPosition());
                if (!simulation.isInCheck(movingPiece.getTeamColor())) {
                    validMoves.add(potentialMove);
                }
            }
        }
        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece movingPiece = board.getPiece(move.getStartPosition());
        if (movingPiece.getTeamColor() != this.teamTurn) {
            throw new InvalidMoveException("Other team's turn");
        }

        //Checking to see if proposed move is actually valid
        Collection<ChessMove> potentialMoves = validMoves(move.getStartPosition());
        boolean validMove = false;
        for (ChessMove element : potentialMoves) {
            if (move.equals(element)) {
                validMove = true;
                break;
            }
        }
        if (!validMove) {
            throw new InvalidMoveException("Invalid Move");
        }

        //Actually moving pieces on the board, swapping in promotion piece if necessary
        if (move.getPromotionPiece() == null) {
            board.addPiece(move.getEndPosition(), movingPiece);
        } else {
            ChessPiece promotionPiece = new ChessPiece(teamTurn, move.getPromotionPiece());
            board.addPiece(move.getEndPosition(), promotionPiece);
        }
        board.removePiece(move.getStartPosition());
        switchTeamTurn();
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        boolean isInCheck = false;
        ChessPiece teamKing = new ChessPiece(teamColor, ChessPiece.PieceType.KING);

        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
               ChessPiece pieceAtTile = board.getPiece(new ChessPosition(i, j));
               if (pieceAtTile == null || pieceAtTile.getTeamColor() == teamColor) {
                   continue;
               }
               Collection<ChessMove> potentialMoves = pieceAtTile.pieceMoves(board, new ChessPosition(i, j));
               for (ChessMove move : potentialMoves) {
                   if (board.getPiece(move.getEndPosition()) == null) {
                       continue;
                   }
                   if (board.getPiece(move.getEndPosition()).equals(teamKing)) {
                       isInCheck = true;
                       break;
                   }
               }
            }
        }
        return isInCheck;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            return false;
        }
        return teamHasNoValidMoves(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            return false;
        }
        return teamHasNoValidMoves(teamColor);
    }

    /**
     * Determines whether the team can make any moves or not
     *
     * @param teamColor which team to check
     * @return True if there is no valid moves the team can make
     */
    private boolean teamHasNoValidMoves(TeamColor teamColor) {
        boolean noValidMoves = true;

        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                if (board.getPiece(new ChessPosition(i, j)) == null) {
                    continue;
                }
                if (board.getPiece(new ChessPosition(i, j)).getTeamColor() == teamColor) {
                    Collection<ChessMove> potentialMoves = validMoves(new ChessPosition(i, j));
                    if (!potentialMoves.isEmpty()) {
                        noValidMoves = false;
                        break;
                    }
                }
            }
        }
        return noValidMoves;
    }
    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }
}
