package chess;

import java.util.Objects;

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
    public int getRow() {
        return row;
    }
    public int getColumn() {
        return col;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChessPosition that)) return false;
        return getRow() == that.getRow() && col == that.col;
    }
    @Override
    public String toString() {
        return "ChessPosition{" +
                "row=" + row +
                ", col=" + col +
                '}';
    }
    @Override
    public int hashCode() {
        return Objects.hash(getRow(), col);
    }



}
