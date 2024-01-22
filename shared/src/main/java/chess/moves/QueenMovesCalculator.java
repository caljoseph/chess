package chess.moves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class QueenMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> bishopMoves = (HashSet<ChessMove>) new BishopMovesCalculator().pieceMoves(board, myPosition);
        HashSet<ChessMove> rookMoves = (HashSet<ChessMove>) new RookMovesCalculator().pieceMoves(board, myPosition);
        bishopMoves.addAll(rookMoves);
        return bishopMoves;
    }
}
