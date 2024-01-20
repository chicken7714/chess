package chess;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] squares = new ChessPiece[8][8];
    public ChessBoard() {
        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7 ; j++) {
                squares[i][j] = null;
            }
        }
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow()-1][position.getColumn()-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow()-1][position.getColumn()-1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        throw new RuntimeException("Not implemented");
    }

    public String toString() {
        StringBuilder totalStringBuilder = new StringBuilder();

        for (int i = 1; i <= 8; i++) {
            StringBuilder rowStringBuilder = new StringBuilder();
            rowStringBuilder.append("Row ").append(i).append(": ");
            for (int j = 1; j <= 8; j++) {
                rowStringBuilder.append(squares[i-1][j-1]);
                if (j <= 7) {
                    rowStringBuilder.append(", ");
                }
            }
            totalStringBuilder.append(rowStringBuilder.toString()).append(System.lineSeparator());
        }
        return totalStringBuilder.toString();
    }
}
