package chess.moves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class KnightMovesCalculator implements PieceMovesCalculator {
    HashSet<ChessMove> moves = new HashSet<ChessMove>();
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessPosition> moveOptions = new ArrayList<ChessPosition>();
        moveOptions.add(new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() + 1));
        moveOptions.add(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 2));
        moveOptions.add(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 2));
        moveOptions.add(new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() + 1));
        moveOptions.add(new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() - 1));
        moveOptions.add(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 2));
        moveOptions.add(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 2));
        moveOptions.add(new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() - 1));

        for (ChessPosition newPosition : moveOptions) {
            if(validateMove(board, myPosition, newPosition) != null) {
                moves.add(validateMove(board, myPosition, newPosition));
            }
        }
        return moves;
    }
    ChessMove validateMove(ChessBoard board, ChessPosition myPosition, ChessPosition newPosition) {
        if(newPosition.getRow() < 1 || newPosition.getRow() > 8
                || newPosition.getColumn() < 1 || newPosition.getColumn() > 8 ) {
            return null;
        }// Check if the possible move is taking us off the board
        ChessPiece knight = board.getPiece(myPosition);
        ChessPiece piece = board.getPiece(newPosition);
        if(piece == null || !piece.getTeamColor().equals(knight.getTeamColor())){
            return new ChessMove(myPosition, newPosition, null);
        } else return null;
    }
}