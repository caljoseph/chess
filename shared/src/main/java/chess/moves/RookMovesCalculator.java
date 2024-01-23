package chess.moves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class RookMovesCalculator implements PieceMovesCalculator {
    HashSet<ChessMove> moves = new HashSet<ChessMove>();
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        // Implement logic for moves
        addOrthogonals(board, myPosition, "up");
        addOrthogonals(board, myPosition, "left");
        addOrthogonals(board, myPosition, "right");
        addOrthogonals(board, myPosition, "down");
        return moves;
    }
    void addOrthogonals(ChessBoard board, ChessPosition startPosition, String direction) {
        ChessPosition currentPosition = startPosition;
        while (0 < currentPosition.getRow() && currentPosition.getRow() < 8
                && 0 < currentPosition.getColumn() && currentPosition.getColumn() < 8 ) {
            if (getNextPosition(currentPosition, direction) == null){
                //if we are going to fall off of the board, try another direction
                break;
            }
            if (board.getPiece(getNextPosition(currentPosition, direction)) == null){
                currentPosition = getNextPosition(currentPosition, direction);
                ChessMove currentMove = new ChessMove(startPosition, currentPosition, null);
                moves.add(currentMove);
                //adds empty squares
            }
            else if(!board.getPiece(getNextPosition(currentPosition, direction)).getTeamColor().equals(board.getPiece(startPosition).getTeamColor())) {
                currentPosition = getNextPosition(currentPosition, direction);
                ChessMove currentMove = new ChessMove(startPosition, currentPosition, null);
                moves.add(currentMove);
                break;
                //stops at capture
            }
            else {
                break;
                //stops and does not permit move on encounter with teammate
            }

        }
    }
    ChessPosition getNextPosition(ChessPosition currentPosition, String direction) {
        return switch (direction) {
            case "up" -> getNextPositionUp(currentPosition);
            case "left" -> getNextPositionLeft(currentPosition);
            case "right" -> getNextPositionRight(currentPosition);
            case "down" -> getNextPositionDown(currentPosition);
            default -> null;
        };
    }

    ChessPosition getNextPositionUp(ChessPosition currentPosition) {
        if(currentPosition.getRow() == 8) {
            return null;
        }
        return new ChessPosition(currentPosition.getRow() + 1, currentPosition.getColumn());
    }
    ChessPosition getNextPositionLeft(ChessPosition currentPosition) {
        if(currentPosition.getColumn() == 1) {
            return null;
        }
        return new ChessPosition(currentPosition.getRow(), currentPosition.getColumn() - 1);
    }
    ChessPosition getNextPositionRight(ChessPosition currentPosition) {
        if(currentPosition.getColumn() == 8) {
            return null;
        }
        return new ChessPosition(currentPosition.getRow(), currentPosition.getColumn() + 1);
    }
    ChessPosition getNextPositionDown(ChessPosition currentPosition) {
        if(currentPosition.getRow() == 1) {
            return null;
        }
        return new ChessPosition(currentPosition.getRow() - 1, currentPosition.getColumn());
    }
}


