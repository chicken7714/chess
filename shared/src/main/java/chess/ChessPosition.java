package chess;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {
    private final int row;
    private final int col;
    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return this.row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return this.col;
    }

    public String toString() {
        return getClass().getName() + "[row =" + row + ",col = " + col + "]";
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (o == this) {
            return true;
        }

        if (this.getClass() != o.getClass()) {
            return false;
        }

        ChessPosition other = (ChessPosition) o;

        return ((row == other.row) && (col == other.col));
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31*result + row;
        result = 31*result + col;
        return result;
    }
}
