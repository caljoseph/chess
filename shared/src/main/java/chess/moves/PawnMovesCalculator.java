package chess.moves;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class PawnMovesCalculator implements PieceMovesCalculator {
    HashSet<ChessMove> moves = new HashSet<ChessMove>();
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        //check three spaces in front
        ArrayList<ChessPosition> moveOptions = new ArrayList<ChessPosition>();
        if (board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE) {
            moveOptions.add(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()));
            moveOptions.add(new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn()));
            moveOptions.add(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1));
            moveOptions.add(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1));
        } else {
            moveOptions.add(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()));
            moveOptions.add(new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn()));
            moveOptions.add(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1));
            moveOptions.add(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1));
        }

        for (ChessPosition newPosition : moveOptions) {
            if(validateMove(board, myPosition, newPosition) != null) {
                if (newPosition.getRow() == 1 || newPosition.getRow() == 8){
                    moves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, newPosition, ChessPiece.PieceType.BISHOP));
                } else {
                    moves.add(new ChessMove(myPosition, newPosition, null));
                }
            }
        }
        return moves;
    }

    ChessPosition validateMove(ChessBoard board, ChessPosition startPosition, ChessPosition endPosition) {
        //need to check if end position is on the board first
        if (endPosition.getColumn() < 1 || endPosition.getColumn() > 8
            || endPosition.getRow() < 1 || endPosition.getRow() > 8) {
            return null;
        }
        //initial moves condition for both teams
        if (board.getPiece(startPosition).getTeamColor() == ChessGame.TeamColor.WHITE){
            if (startPosition.getRow() == 2 && endPosition.getRow() == 4) {
                ChessPosition intermediatePosition = new ChessPosition(3, endPosition.getColumn());
                if (board.getPiece(intermediatePosition) == null && board.getPiece(endPosition) == null) {
                    return endPosition;
                }
                return null;
            }
        } else if (board.getPiece(startPosition).getTeamColor() == ChessGame.TeamColor.BLACK){
            if (startPosition.getRow() == 7 && endPosition.getRow() == 5) {
                ChessPosition intermediatePosition = new ChessPosition(6, endPosition.getColumn());
                if (board.getPiece(intermediatePosition) == null && board.getPiece(endPosition) == null) {
                    return endPosition;
                }
                return null;
            }
        }
        if (Math.abs(startPosition.getRow() - endPosition.getRow()) == 2) {
            return null;
        }
        if (startPosition.getColumn() == endPosition.getColumn()
                && board.getPiece(endPosition) == null) {
            // empty space ahead
            return endPosition;
        }
        if (board.getPiece(endPosition) != null){
            if (startPosition.getColumn() != endPosition.getColumn()
                    && !board.getPiece(endPosition).getTeamColor().equals(board.getPiece(startPosition).getTeamColor())) {
                // diagonal enemy
                return endPosition;
            }
        }
        return null;
    }
}

