package chess.moves;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

public class KingMovesCalculator implements PieceMovesCalculator{
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
    public ChessMove validateMove(ChessBoard board, ChessPosition startPos, ChessPosition endPos) {
        // Check if target is on the board, if not return null
        if (!onBoard(endPos)) {
            return null;
        }

        ChessPiece targetSquare = board.getPiece(endPos);

        // If the square is empty or has an enemy piece, make the move
        if (isSquareEmptyOrEnemy(targetSquare, board.getPiece(startPos))) {
            return new ChessMove(startPos, endPos, null);
        } else {
            return null;
        }
    }

    private boolean isSquareEmptyOrEnemy(ChessPiece targetSquare, ChessPiece startPiece) {
        ChessGame.TeamColor myTeam = startPiece.getTeamColor();

        // If the square is empty or has an enemy piece, return true; otherwise, return false
        return targetSquare == null || targetSquare.getTeamColor() != myTeam;
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

        possiblePositions.add(new ChessPosition(startRow + 1, startCol));
        possiblePositions.add(new ChessPosition(startRow - 1, startCol));
        possiblePositions.add(new ChessPosition(startRow, startCol + 1));
        possiblePositions.add(new ChessPosition(startRow, startCol - 1));
        possiblePositions.add(new ChessPosition(startRow + 1, startCol + 1));
        possiblePositions.add(new ChessPosition(startRow + 1, startCol - 1));
        possiblePositions.add(new ChessPosition(startRow - 1, startCol + 1));
        possiblePositions.add(new ChessPosition(startRow - 1, startCol - 1));
    }
}