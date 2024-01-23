package chess.moves;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

public class BishopMovesCalculator implements PieceMovesCalculator {
    HashSet<ChessMove> moves = new HashSet<ChessMove>();
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        // Implement logic for moves
        addDiagonals(board, myPosition, "NE");
        addDiagonals(board, myPosition, "NW");
        addDiagonals(board, myPosition, "SE");
        addDiagonals(board, myPosition, "SW");
        return moves;
    }
    void addDiagonals(ChessBoard board, ChessPosition startPosition, String direction) {
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
            case "NE" -> getNextPositionNE(currentPosition);
            case "NW" -> getNextPositionNW(currentPosition);
            case "SE" -> getNextPositionSE(currentPosition);
            case "SW" -> getNextPositionSW(currentPosition);
            default -> null;
        };
    }

    ChessPosition getNextPositionNE(ChessPosition currentPosition) {
        if(currentPosition.getRow() == 8 || currentPosition.getColumn() == 8) {
            return null;
        }
        return new ChessPosition(currentPosition.getRow() + 1, currentPosition.getColumn() + 1);
    }
    ChessPosition getNextPositionNW(ChessPosition currentPosition) {
        if(currentPosition.getColumn() == 1 || currentPosition.getRow() == 8) {
            return null;
        }
        return new ChessPosition(currentPosition.getRow() + 1, currentPosition.getColumn() - 1);
    }
    ChessPosition getNextPositionSE(ChessPosition currentPosition) {
        if(currentPosition.getColumn() == 8 || currentPosition.getRow() == 1) {
            return null;
        }
        return new ChessPosition(currentPosition.getRow() - 1, currentPosition.getColumn() + 1);
    }
    ChessPosition getNextPositionSW(ChessPosition currentPosition) {
        if(currentPosition.getRow() == 1 || currentPosition.getColumn() == 1) {
            return null;
        }
        return new ChessPosition(currentPosition.getRow() - 1, currentPosition.getColumn() - 1);
    }
}


