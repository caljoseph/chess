package chess.moves;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

public class PawnMovesCalculator implements PieceMovesCalculator{
    HashSet<ChessPosition> possiblePositions = new HashSet<ChessPosition>();
    HashSet<ChessMove> moves = new HashSet<ChessMove>();
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessGame.TeamColor teamColor = board.getPiece(myPosition).getTeamColor();
        addPossiblePositions(myPosition, teamColor);

        for (ChessPosition endPos: possiblePositions){
            if (validateMove(board, myPosition, endPos) != null) {
                if (endPos.getRow() == 1 || endPos.getRow() == 8){
                    moves.add(new ChessMove (validateMove(board, myPosition, endPos), ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove (validateMove(board, myPosition, endPos), ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove (validateMove(board, myPosition, endPos), ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove (validateMove(board, myPosition, endPos), ChessPiece.PieceType.ROOK));
                } else {
                    moves.add(validateMove(board, myPosition, endPos));
                }
            }
        }
        return moves;
    }
    public ChessMove validateMove(ChessBoard board, ChessPosition startPos, ChessPosition endPos){
        if(!onBoard(endPos)){
            return null;
        }
        //if I am not on the starting line and I will move more tan 1 row reject this move
        ChessGame.TeamColor myTeam = board.getPiece(startPos).getTeamColor();
        int myRow = startPos.getRow();
        int myCol = startPos.getColumn();
        int endRow = endPos.getRow();
        ChessPiece intermediatePiece = null;
        if (myTeam == ChessGame.TeamColor.WHITE){
            if (myRow != 2  && (Math.abs(myRow - endRow) > 1)){
                return null;
            } else {
                intermediatePiece = board.getPiece(new ChessPosition((myRow + 1), myCol));
            }
        } else{
            if (myRow != 7  && (Math.abs(myRow - endRow) > 1)){
                return null;
            } else {
                intermediatePiece = board.getPiece(new ChessPosition((myRow - 1), myCol));
            }
        }

        ChessPiece endPiece = board.getPiece(endPos);
        boolean diagonalMove = (startPos.getColumn() != endPos.getColumn());
        if (endPiece != null) {
            //when there is a piece blocking
            boolean isEnemy = (board.getPiece(startPos).getTeamColor() != board.getPiece(endPos).getTeamColor());
            if (diagonalMove && isEnemy){
                return new ChessMove(startPos, endPos, null);
            }else{
                return null;
            }
        } else {
            //when there is a free space
            if (diagonalMove){
                return null;
            }else{
                if (intermediatePiece != null) {
                    return null;
                } else {
                    return new ChessMove(startPos, endPos, null);
                }
            }
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

    void addPossiblePositions (ChessPosition startPos, ChessGame.TeamColor teamColor){
        int startRow = startPos.getRow();
        int startCol = startPos.getColumn();

        if (teamColor == ChessGame.TeamColor.WHITE) {
            possiblePositions.add(new ChessPosition(startRow + 1, startCol));
            possiblePositions.add(new ChessPosition(startRow + 1, startCol + 1));
            possiblePositions.add(new ChessPosition(startRow + 1, startCol - 1));
            possiblePositions.add(new ChessPosition(startRow + 2, startCol));
        } else {
            possiblePositions.add(new ChessPosition(startRow - 1, startCol));
            possiblePositions.add(new ChessPosition(startRow - 1, startCol + 1));
            possiblePositions.add(new ChessPosition(startRow - 1, startCol - 1));
            possiblePositions.add(new ChessPosition(startRow - 2, startCol));
        }

    }
}