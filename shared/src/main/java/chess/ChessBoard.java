package chess;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] squares = new ChessPiece[8][8];

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChessBoard that)) return false;
        return Arrays.deepEquals(squares, that.squares);
    }
    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int row = 7; row >= 0; row--) {
            for (int col = 0; col < 8; col++) {
                ChessPiece currentPiece = squares[row][col];
                result.append('|');
                if (currentPiece != null) {
                    if (currentPiece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                        switch (currentPiece.getPieceType()) {
                            case ChessPiece.PieceType.BISHOP -> result.append('b');
                            case ChessPiece.PieceType.KING -> result.append('k');
                            case ChessPiece.PieceType.KNIGHT -> result.append('n');
                            case ChessPiece.PieceType.PAWN -> result.append('p');
                            case ChessPiece.PieceType.QUEEN -> result.append('q');
                            case ChessPiece.PieceType.ROOK -> result.append('r');
                        }
                    }
                    else {
                        switch (currentPiece.getPieceType()) {
                            case ChessPiece.PieceType.BISHOP -> result.append('B');
                            case ChessPiece.PieceType.KING -> result.append('K');
                            case ChessPiece.PieceType.KNIGHT -> result.append('N');
                            case ChessPiece.PieceType.PAWN -> result.append('P');
                            case ChessPiece.PieceType.QUEEN -> result.append('Q');
                            case ChessPiece.PieceType.ROOK -> result.append('R');
                        }
                    }
                } else {
                    result.append(' ');
                }
            }
            if (row > 0) {
                result.append("|\n");
            } else {
                result.append('|');  // Last row, no newline after the last '|'
            }
        }
        return result.toString();
    }
    public ChessBoard(){
     }
    public ChessBoard(ChessBoard o){
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPosition currPosition = new ChessPosition(row + 1, col + 1);
                if (o.getPiece(currPosition) != null){
                    ChessPiece currPiece = o.getPiece(currPosition);
                    squares[row][col] = new ChessPiece(currPiece);
                }

            }
        }
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow() - 1][position.getColumn() - 1] = piece;
    }


    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow() - 1][position.getColumn() - 1];
    }

    public ChessPosition getKingPosition(ChessGame.TeamColor team) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (squares[row][col] != null) {
                    if (squares[row][col].getPieceType() == ChessPiece.PieceType.KING
                            && squares[row][col].getTeamColor().equals(team)) {
                        return new ChessPosition(row + 1, col + 1);
                    }
                }
            }
        }
        return null;
    }

    public Collection<ChessPosition> getTeam(ChessGame.TeamColor team) {
        HashSet<ChessPosition> enemyTeam = new HashSet<>();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (squares[row][col] != null) {
                    if (squares[row][col].getTeamColor().equals(team)){
                        enemyTeam.add(new ChessPosition(row + 1, col + 1));
                    }
                }
            }
        }
        return enemyTeam;
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        squares = new ChessPiece[8][8];
        //Set white
        squares[0][7] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        squares[0][6] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        squares[0][5] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        squares[0][4] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        squares[0][3] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        squares[0][2] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        squares[0][1] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        squares[0][0] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        for (int i = 0; i < 8; i++) {
            squares[1][i] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        }
        //Set black
        squares[7][7] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        squares[7][6] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        squares[7][5] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        squares[7][4] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        squares[7][3] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        squares[7][2] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        squares[7][1] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        squares[7][0] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        for (int i = 0; i < 8; i++) {
            squares[6][i] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        }
    }
}