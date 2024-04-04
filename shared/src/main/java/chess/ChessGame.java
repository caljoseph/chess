package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor turnColor;
    private ChessBoard board;
    private boolean isOver;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChessGame chessGame)) return false;
        return turnColor == chessGame.turnColor && Objects.equals(getBoard(), chessGame.getBoard());
    }

    @Override
    public int hashCode() {
        return Objects.hash(turnColor, getBoard());
    }

    public boolean isOver() {
        return isOver;
    }

    public void setOver(boolean over) {
        isOver = over;
    }

    public ChessGame() {
        isOver = false;
        turnColor = TeamColor.WHITE;
        board = new ChessBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turnColor;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        turnColor =  team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        var validMoves = new HashSet<ChessMove>();

        ChessPiece currentPiece = board.getPiece(startPosition);
        if (currentPiece == null) return validMoves;

        var rawMoves = currentPiece.pieceMoves(board, startPosition);
        for (ChessMove move: rawMoves) {
            if (validateMove(currentPiece, move) != null) {
                validMoves.add(move);
            }
        }
        return validMoves;
    }

    private ChessMove validateMove(ChessPiece currentPiece, ChessMove move) {
        //must check if this move is gonna put my team into check
        //create a new hypothetical board (copy) and execute this move

        ChessBoard testBoard = new ChessBoard(board);
        makeTestMove(testBoard, move);

        //then loop through whole other team and see if any of the endPosition
        //of entries in their pieceMoves arrays contain my kings coordinates

        ChessPosition myKingPos = testBoard.getKingPosition(currentPiece.getTeamColor());
        var enemies = testBoard.getTeam(otherTeam(currentPiece.getTeamColor()));

        // get a list of pieceMoves for each one
        for (ChessPosition enemyPos : enemies) {

            ChessPiece curEnemy = testBoard.getPiece(enemyPos);
            var curEnemyMoves = curEnemy.pieceMoves(testBoard, enemyPos);
            //access the endPosition of each entry, compare to my king's coordinates
            for (ChessMove enemyMove : curEnemyMoves) {
                if (enemyMove.getEndPosition().equals(myKingPos)) {
                    return null;
                }
            }
        }
        return move;
    }


    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (isInCheckmate(TeamColor.WHITE)) {
            throw new InvalidMoveException("White is in checkmate. The game is over.");
        } else if (isInCheckmate(TeamColor.BLACK)) {
            throw new InvalidMoveException("Black is in checkmate. The game is over.");
        } else if (isInStalemate(TeamColor.WHITE) && isInStalemate(TeamColor.BLACK)) {
            throw new InvalidMoveException("Stalemate. The game is over.");
        } else if (getTeamTurn() == null) {
            throw new InvalidMoveException("The game is over due to resign.");
        }
        ChessPosition startPos = move.getStartPosition();
        ChessPosition endPos = move.getEndPosition();
        ChessPiece currentPiece = board.getPiece(startPos);
        ChessPiece target = board.getPiece(endPos);

        if (currentPiece.getTeamColor() != turnColor) {
            throw new InvalidMoveException("Incorrect Team's Turn");
        }

        var validMoves = validMoves(startPos);
        if (validMoves.contains(move)){
            if (move.getPromotionPiece() != null) {
                board.addPiece(endPos, new ChessPiece(currentPiece.getTeamColor(), move.getPromotionPiece()));
                board.addPiece(startPos, null);
                updateTurnColor();
            } else {
                board.addPiece(endPos, currentPiece);
                board.addPiece(startPos, null);
                updateTurnColor();
            }
        } else {
            throw new InvalidMoveException("Invalid Move");
        }
    }
    private void makeTestMove(ChessBoard testBoard, ChessMove move) {
        ChessPosition startPos = move.getStartPosition();
        ChessPosition endPos = move.getEndPosition();
        ChessPiece currentPiece = testBoard.getPiece(startPos);

        testBoard.addPiece(endPos, currentPiece);
        testBoard.addPiece(startPos, null);
    }

    private void updateTurnColor() {
        if (turnColor == TeamColor.WHITE) {
            turnColor = TeamColor.BLACK;
        } else {
            turnColor = TeamColor.WHITE;
        }
    }

    public TeamColor otherTeam(TeamColor team) {
        if (team == TeamColor.WHITE) {
            return TeamColor.BLACK;
        } else {
            return TeamColor.WHITE;
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition myKingPos = board.getKingPosition(teamColor);
        var enemies = board.getTeam(otherTeam(teamColor));

        // get a list of pieceMoves for each one
        for (ChessPosition enemyPos : enemies) {
            ChessPiece curEnemy = board.getPiece(enemyPos);
            var curEnemyMoves = curEnemy.pieceMoves(board, enemyPos);
            //access the endPosition of each entry, compare to my king's coordinates
            for (ChessMove enemyMove : curEnemyMoves) {
                if (enemyMove.getEndPosition().equals(myKingPos)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (isInCheck(teamColor) && (turnColor == teamColor)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {

        var myTeam = board.getTeam(teamColor);

        // get a list of pieceMoves for each one
        for (ChessPosition piece : myTeam) {
            var curMoves = validMoves(piece);
            if(!curMoves.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = new ChessBoard(board);
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
