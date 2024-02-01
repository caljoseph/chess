package chess;

import chess.moves.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private PieceType pieceType;
    private ChessGame.TeamColor teamColor;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        pieceType = type;
        teamColor = pieceColor;
    }
    public ChessPiece(ChessPiece o) {
        if(o != null) {
            pieceType = o.pieceType;
            teamColor = o.teamColor;
        }
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return teamColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return pieceType;
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceType=" + pieceType +
                ", teamColor=" + teamColor +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChessPiece that)) return false;
        return getPieceType() == that.getPieceType() && getTeamColor() == that.getTeamColor();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPieceType(), getTeamColor());
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return switch (pieceType) {
            case BISHOP -> new BishopMovesCalculator().pieceMoves(board, myPosition);
            case KING -> new KingMovesCalculator().pieceMoves(board, myPosition);
            case KNIGHT -> new KnightMovesCalculator().pieceMoves(board, myPosition);
            case PAWN -> new PawnMovesCalculator().pieceMoves(board, myPosition);
            case QUEEN -> new QueenMovesCalculator().pieceMoves(board, myPosition);
            case ROOK -> new RookMovesCalculator().pieceMoves(board, myPosition);
            case null -> new HashSet<>();
        };
    }
}
