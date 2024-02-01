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
    public ChessMove validateMove (ChessBoard board, ChessPosition startPos, ChessPosition endPos) {
        //check if target is on the board -> if not return null
        if (!onBoard(endPos)) {
            return null;
        }
        ChessPiece targetSquare = board.getPiece(endPos);
        //rule out double move unless on starting line
        ChessGame.TeamColor myTeam = board.getPiece(startPos).getTeamColor();
        int row = startPos.getRow();
        int col = startPos.getColumn();
        int endRow = endPos.getRow();
        int endCol = endPos.getColumn();
        ChessPiece intermediatePiece;

        if (myTeam == ChessGame.TeamColor.WHITE) {
            if (row != 2 && (Math.abs(row - endRow ) > 1)) {
                return null;
            } else {
                if (board.getPiece(new ChessPosition(3, col)) != null) {return null;}
            }
        } else {
            if (row != 7 && (Math.abs(row - endRow ) > 1)) {
                return null;
            } else {
                if (board.getPiece(new ChessPosition(6, col)) != null) {return null;}
            }
        }
        if(targetSquare != null) {
            if (col != endCol){
                //diagonal
                if (targetSquare.getTeamColor() != myTeam) {
                    return new ChessMove(startPos, endPos, null);
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            if (col != endCol){
                return null;
            } else {
                return new ChessMove(startPos, endPos, null);
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