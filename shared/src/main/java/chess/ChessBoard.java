package chess;

import java.util.Arrays;

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
                ChessPiece current_piece = squares[row][col];
                result.append('|');
                if (current_piece != null) {
                    if (current_piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                        switch (current_piece.getPieceType()) {
                            case ChessPiece.PieceType.BISHOP -> result.append('b');
                            case ChessPiece.PieceType.KING -> result.append('k');
                            case ChessPiece.PieceType.KNIGHT -> result.append('n');
                            case ChessPiece.PieceType.PAWN -> result.append('p');
                            case ChessPiece.PieceType.QUEEN -> result.append('q');
                            case ChessPiece.PieceType.ROOK -> result.append('r');
                        }
                    }
                    else {
                        switch (current_piece.getPieceType()) {
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

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow() - 1][position.getColumn() - 1] = piece;
    }



//    public void removePiece(ChessPosition position, ChessPiece piece) {
////        not sure how confident on this guy I am, what would I do wiht piece?
//        squares[position.getRow() - 1][position.getColumn() - 1] = null;
//    }

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
    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                squares[row][col] = null;
            }
        }
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