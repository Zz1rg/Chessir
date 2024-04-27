package util;

import main.Move;

public record MoveRecord(Move move, int enPassantTile, boolean isPieceFirstMove) {
}
