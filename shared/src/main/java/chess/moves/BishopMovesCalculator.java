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

    void addDiagonals(ChessBoard board, ChessPosition startPos, Direction direction){
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
