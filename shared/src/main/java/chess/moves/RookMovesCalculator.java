package chess.moves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class RookMovesCalculator implements PieceMovesCalculator{
    HashSet<ChessMove> moves = new HashSet<ChessMove>();
    public enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT,
    }
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        addOrthogonals(board, myPosition, RookMovesCalculator.Direction.UP);
        addOrthogonals(board, myPosition, RookMovesCalculator.Direction.DOWN);
        addOrthogonals(board, myPosition, RookMovesCalculator.Direction.LEFT);
        addOrthogonals(board, myPosition, RookMovesCalculator.Direction.RIGHT);

        return moves;
    }

    void addOrthogonals(ChessBoard board, ChessPosition startPos, RookMovesCalculator.Direction direction){
        ChessPosition nextPos = getNextPosition(startPos, direction);
        while (nextPos != null){
            ChessPiece nextSquare = board.getPiece(nextPos);
            //what is this square?
            if (nextSquare == null) {
                //blank space
                moves.add(new ChessMove(startPos, nextPos, null));
                nextPos = getNextPosition(nextPos, direction);
            }else {
                if (nextSquare.getTeamColor() != board.getPiece(startPos).getTeamColor()){
                    //if enemy
                    moves.add(new ChessMove(startPos, nextPos, null));
                    break;
                } else {
                    break;
                }
            }
        }
    };

    ChessPosition getNextPosition (ChessPosition startPos, RookMovesCalculator.Direction direction) {
        int row = startPos.getRow();
        int col = startPos.getColumn();
        return switch (direction) {
            case UP -> validatePos(new ChessPosition(row + 1, col));
            case DOWN -> validatePos(new ChessPosition(row - 1, col));
            case LEFT -> validatePos(new ChessPosition(row, col + 1));
            case RIGHT -> validatePos(new ChessPosition(row, col - 1));
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
