package main;

import pieces.*;

public class CheckScanner {

    private final Board board;

    public CheckScanner(Board board) {
        this.board = board;
    }

    public boolean isKingChecked(Move move, boolean forPaint) {
        Piece king = board.findKing(move.piece.isWhite());
        assert king != null;

        int kingCol = king.getCol();
        int kingRow = king.getRow();

        if (board.getSelectedPiece() != null && board.getSelectedPiece() instanceof King || move.piece instanceof King) {
            kingCol = move.newCol;
            kingRow = move.newRow;
        }

        return hitByRook(move.newCol, move.newRow, king, kingCol, kingRow, 0, 1, forPaint) || //up
                hitByRook(move.newCol, move.newRow, king, kingCol, kingRow, 1, 0, forPaint) || //right
                hitByRook(move.newCol, move.newRow, king, kingCol, kingRow, 0, -1, forPaint) || //down
                hitByRook(move.newCol, move.newRow, king, kingCol, kingRow, -1, 0, forPaint) || //left

                hitByBishop(move.newCol, move.newRow, king, kingCol, kingRow, -1, -1, forPaint) || //up left
                hitByBishop(move.newCol, move.newRow, king, kingCol, kingRow, 1, -1, forPaint) || //up right
                hitByBishop(move.newCol, move.newRow, king, kingCol, kingRow, 1, 1, forPaint) || //down right
                hitByBishop(move.newCol, move.newRow, king, kingCol, kingRow, -1, 1, forPaint) || //down left

                hitByKnight(move.newCol, move.newRow, king, kingCol, kingRow) ||
                hitByKing(king, kingCol, kingRow) ||
                hitByPawn(move.newCol, move.newRow, king, kingCol, kingRow);
    }

    private boolean hitByRook(int col, int row, Piece king, int kingCol, int kingRow, int colVal, int rowVal, boolean forPaint) {
        for (int i = 1; i < 8; i++) {
            if (kingCol + (i * colVal) == col && kingRow + (i * rowVal) == row) {
                break;
            }
            Piece piece = board.getPiece(kingCol + (i * colVal), kingRow + (i * rowVal));
            if (forPaint) {
                if (piece != null && piece != king) {
                    if (!board.sameTeam(piece, king) && (piece instanceof Rook || piece instanceof Queen)) {
                        return true;
                    }
                    break;
                }
            } else {
                if (piece != null && piece != board.getSelectedPiece() && piece != king) {
                    if (!board.sameTeam(piece, king) && (piece instanceof Rook || piece instanceof Queen)) {
                        return true;
                    }
                    break;
                }

            }
        }
        return false;
    }

    private boolean hitByBishop(int col, int row, Piece king, int kingCol, int kingRow, int colVal, int rowVal, boolean forPaint) {
        for (int i = 1; i < 8; i++) {
            if (kingCol - (i * colVal) == col && kingRow - (i * rowVal) == row) {
                break;
            }
            Piece piece = board.getPiece(kingCol - (i * colVal), kingRow - (i * rowVal));
            if (forPaint) {
                if (piece != null && piece != king) {
                    if (!board.sameTeam(piece, king) && (piece instanceof Bishop || piece instanceof Queen)) {
                        return true;
                    }
                    break;
                }
            } else {
                if (piece != null && piece != board.getSelectedPiece() && piece != king) {
                    if (!board.sameTeam(piece, king) && (piece instanceof Bishop || piece instanceof Queen)) {
                        return true;
                    }
                    break;
                }
            }
        }
        return false;
    }

    private boolean hitByKnight(int col, int row, Piece king, int kingCol, int kingRow) {
        return checkKnight(board.getPiece(kingCol + 1, kingRow + 2), king, col, row) ||
                checkKnight(board.getPiece(kingCol + 2, kingRow + 1), king, col, row) ||
                checkKnight(board.getPiece(kingCol + 2, kingRow - 1), king, col, row) ||
                checkKnight(board.getPiece(kingCol + 1, kingRow - 2), king, col, row) ||
                checkKnight(board.getPiece(kingCol - 1, kingRow - 2), king, col, row) ||
                checkKnight(board.getPiece(kingCol - 2, kingRow - 1), king, col, row) ||
                checkKnight(board.getPiece(kingCol - 2, kingRow + 1), king, col, row) ||
                checkKnight(board.getPiece(kingCol - 1, kingRow + 2), king, col, row);
    }

    private boolean checkKnight(Piece p, Piece k, int col, int row) {
        return p != null && !board.sameTeam(p, k) && p instanceof Knight && !(p.getCol() == col && p.getRow() == row);
    }

    private boolean hitByKing(Piece king, int kingCol, int kingRow) {
        return checkKing(board.getPiece(kingCol + 1, kingRow), king) ||
                checkKing(board.getPiece(kingCol + 1, kingRow + 1), king) ||
                checkKing(board.getPiece(kingCol, kingRow + 1), king) ||
                checkKing(board.getPiece(kingCol - 1, kingRow + 1), king) ||
                checkKing(board.getPiece(kingCol - 1, kingRow), king) ||
                checkKing(board.getPiece(kingCol - 1, kingRow - 1), king) ||
                checkKing(board.getPiece(kingCol, kingRow - 1), king) ||
                checkKing(board.getPiece(kingCol + 1, kingRow - 1), king);
    }

    private boolean checkKing(Piece p, Piece k) {
        return p != null && !board.sameTeam(p, k) && p instanceof King;
    }

    private boolean hitByPawn(int col, int row, Piece king, int kingCol, int kingRow) {
        int colorIndex = king.isWhite() ? -1 : 1;

        return checkPawn(board.getPiece(kingCol + 1, kingRow + colorIndex), king, col, row) ||
                checkPawn(board.getPiece(kingCol - 1, kingRow + colorIndex), king, col, row);
    }

    private boolean checkPawn(Piece p, Piece k, int col, int row) {
        return p != null && !board.sameTeam(p, k) && p instanceof Pawn && !(p.getCol() == col && p.getRow() == row);
    }
}
