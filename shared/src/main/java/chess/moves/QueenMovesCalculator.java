package chess.moves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class QueenMovesCalculator implements PieceMovesCalculator{
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> bishopMoves = new BishopMovesCalculator().pieceMoves(board,myPosition);
        Collection<ChessMove> rookMoves = new RookMovesCalculator().pieceMoves(board,myPosition);
        bishopMoves.addAll(rookMoves);
        return bishopMoves;
    }
}