package chess.moves;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

public class BishopMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        //add diagonal moves in each direction until we hit an edge or another piece.
        addDiagonals(board, myPosition, 1, 1);
        addDiagonals(board, myPosition, 1, -1);
        addDiagonals(board, myPosition, -1, 1);
        addDiagonals(board, myPosition, -1, -1);
        return moves;
    }
    HashSet<ChessMove> moves = new HashSet<ChessMove>();
    void addDiagonals(ChessBoard board, ChessPosition startPosition, int rowDirection, int colDirection) {
        ChessPosition currentPosition = startPosition;
        while (1 < currentPosition.getRow() && currentPosition.getRow() < 8
                && 1 < currentPosition.getColumn() && currentPosition.getColumn() < 8 ) {
            currentPosition = getNextPosition(currentPosition, rowDirection, colDirection);
            if (board.getPiece(currentPosition) == null){
                ChessMove currentMove = new ChessMove(startPosition, currentPosition, null);
                moves.add(currentMove);
                //adds empty squares
            }
            else if(!board.getPiece(currentPosition).getTeamColor().equals(board.getPiece(startPosition).getTeamColor())) {
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
    ChessPosition getNextPosition(ChessPosition currentPosition, int rowDirection, int colDirection){
        return new ChessPosition(currentPosition.getRow() + rowDirection, currentPosition.getColumn() + colDirection);
    }
}


