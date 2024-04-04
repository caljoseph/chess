package chess;

import java.util.HashMap;
import java.util.Map;
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
    public String prettyString() {
        Map<Integer, Character> mapping = new HashMap<>();
        mapping.put(1, 'a');
        mapping.put(2, 'b');
        mapping.put(3, 'c');
        mapping.put(4, 'd');
        mapping.put(5, 'e');
        mapping.put(6, 'f');
        mapping.put(7, 'g');
        mapping.put(8, 'h');

        return String.valueOf(row) + mapping.get(col);
    }
    @Override
    public int hashCode() {
        return Objects.hash(getRow(), col);
    }



}
