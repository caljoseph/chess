package chess.moves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class BishopMovesCalculator implements PieceMovesCalculator{
    HashSet<ChessMove> moves = new HashSet<ChessMove>();
    public enum Direction {
        NE,
        NW,
        SE,
        SW
    }
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        addDiagonals(board, myPosition, Direction.NE);
        addDiagonals(board, myPosition, Direction.NW);
        addDiagonals(board, myPosition, Direction.SE);
        addDiagonals(board, myPosition, Direction.SW);

        return moves;
    }

    void addDiagonals(ChessBoard board, ChessPosition startPos, Direction direction) {
        ChessPosition nextPos = getNextPosition(startPos, direction);

        while (nextPos != null) {
            ChessPiece nextSquare = board.getPiece(nextPos);

            if (nextSquare == null) {
                // Blank space, add move and continue
                moves.add(new ChessMove(startPos, nextPos, null));
                nextPos = getNextPosition(nextPos, direction);
            } else {
                if (isEnemyPiece(nextSquare, board.getPiece(startPos))) {
                    // Enemy piece, add move and break
                    moves.add(new ChessMove(startPos, nextPos, null));
                    break;
                } else {
                    // Same team piece, break
                    break;
                }
            }
        }
    }

    private boolean isEnemyPiece(ChessPiece piece1, ChessPiece piece2) {
        return piece1.getTeamColor() != piece2.getTeamColor();
    }

    ChessPosition getNextPosition (ChessPosition startPos, Direction direction) {
        int row = startPos.getRow();
        int col = startPos.getColumn();
        return switch (direction) {
            case NE -> validatePos(new ChessPosition(row + 1, col + 1));
            case NW -> validatePos(new ChessPosition(row + 1, col - 1));
            case SE -> validatePos(new ChessPosition(row - 1, col + 1));
            case SW -> validatePos(new ChessPosition(row - 1, col - 1));
        };
    }
    public ChessPosition validatePos(ChessPosition position) {
        if (1 > position.getRow() || 1 > position.getColumn()
                || 8 < position.getRow() || 8 < position.getColumn()) {
            return null;
        } else {
            return position;
        }
    }
}
