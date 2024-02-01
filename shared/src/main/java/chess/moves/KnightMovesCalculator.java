package chess.moves;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

public class KnightMovesCalculator implements PieceMovesCalculator{
    HashSet<ChessPosition> possiblePositions = new HashSet<ChessPosition>();
    HashSet<ChessMove> moves = new HashSet<ChessMove>();
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        addPossiblePositions(myPosition);

        for (ChessPosition endPos: possiblePositions){
            if (validateMove(board, myPosition, endPos) != null) {
                moves.add(validateMove(board, myPosition, endPos));
            }
        }

        return moves;
    }
    public ChessMove validateMove (ChessBoard board, ChessPosition startPos, ChessPosition endPos) {
        //check if target is on the board -> if not return null
        if (!onBoard(endPos)) {
            return null;
        }
        ChessPiece targetSquare = board.getPiece(endPos);
        //if empty square we go for it
        if (targetSquare == null) {
            return new ChessMove(startPos, endPos, null);
        }
        ChessGame.TeamColor myTeam = board.getPiece(startPos).getTeamColor();
        //if enemy we go for it
        if (targetSquare.getTeamColor() != myTeam){
            return new ChessMove(startPos, endPos, null);
        } else {
            return null;
        }
    }
    public boolean onBoard(ChessPosition position) {
        if (1 > position.getRow() || 1 > position.getColumn()
                || 8 < position.getRow() || 8 < position.getColumn()) {
            return false;
        } else {
            return true;
        }
    }

    void addPossiblePositions (ChessPosition startPos){
        int startRow = startPos.getRow();
        int startCol = startPos.getColumn();

        possiblePositions.add(new ChessPosition(startRow + 2, startCol + 1));
        possiblePositions.add(new ChessPosition(startRow + 2, startCol - 1));
        possiblePositions.add(new ChessPosition(startRow - 2, startCol + 1));
        possiblePositions.add(new ChessPosition(startRow - 2, startCol - 1));
        possiblePositions.add(new ChessPosition(startRow + 1, startCol + 2));
        possiblePositions.add(new ChessPosition(startRow + 1, startCol - 2));
        possiblePositions.add(new ChessPosition(startRow - 1, startCol + 2));
        possiblePositions.add(new ChessPosition(startRow - 1, startCol - 2));
    }
}