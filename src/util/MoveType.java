package util;

import main.Board;

import java.util.ArrayList;

public enum MoveType {
    HORIZONTAL(new ArrayList<>() {{
        // actually it's Board.BOARD_ROWS - 1, but just in case
        for (int i = 1; i < Board.BOARD_ROWS; i++) {
            add(new CoordinateRC(0, -i));
            add(new CoordinateRC(0, i));
        }
    }}),
    VERTICAL(new ArrayList<>() {{
        for (int i = 1; i < Board.BOARD_COLS; i++) {
            add(new CoordinateRC(-i, 0));
            add(new CoordinateRC(i, 0));
        }
    }}),
    CROSS(new ArrayList<>() {{
        addAll(MoveType.HORIZONTAL.getRelativeCoordinates());
        addAll(MoveType.VERTICAL.getRelativeCoordinates());
    }}),
    DIAGONAL(new ArrayList<>() {{
        // assuming BOARD_COLS == BOARD_ROWS
        for (int i = 1; i < Board.BOARD_COLS; i++) {
            add(new CoordinateRC(-i, -i));
            add(new CoordinateRC(-i, i));
            add(new CoordinateRC(i, -i));
            add(new CoordinateRC(i, i));
        }
    }}),
    KING(new ArrayList<>() {{
        add(new CoordinateRC(-1, -1));
        add(new CoordinateRC(-1, 0));
        add(new CoordinateRC(-1, 1));
        add(new CoordinateRC(0, -1));
//      add(new CoordinateRC(0, 0));
        add(new CoordinateRC(0, 1));
        add(new CoordinateRC(1, -1));
        add(new CoordinateRC(1, 0));
        add(new CoordinateRC(1, 1));
    }}),
    KNIGHT(new ArrayList<>() {{
        add(new CoordinateRC(-1, -2));
        add(new CoordinateRC(-2, -1));
        add(new CoordinateRC(-2, 1));
        add(new CoordinateRC(-1, 2));
        add(new CoordinateRC(1, 2));
        add(new CoordinateRC(2, 1));
        add(new CoordinateRC(2, -1));
        add(new CoordinateRC(1, -2));
    }}),

    WHITE_PAWN_ATTACK(new ArrayList<>() {{
        add(new CoordinateRC(-1, -1));
        add(new CoordinateRC(-1, 1));
    }}),

    BLACK_PAWN_ATTACK(new ArrayList<>() {{
        add(new CoordinateRC(1, -1));
        add(new CoordinateRC(1, 1));
    }}),

    WHITE_PAWN_FIRST_MOVE(new ArrayList<>() {{
        add(new CoordinateRC(-1, 0));
        add(new CoordinateRC(-2, 0));
    }}),

    BLACK_PAWN_FIRST_MOVE(new ArrayList<>() {{
        add(new CoordinateRC(1, 0));
        add(new CoordinateRC(2, 0));
    }}),

    WHITE_PAWN_NORMAL_MOVE(new ArrayList<>() {{
        add(new CoordinateRC(-1, 0));
    }}),

    BLACK_PAWN_NORMAL_MOVE(new ArrayList<>() {{
        add(new CoordinateRC(1, 0));
    }});

    private final ArrayList<Coordinate> relativeCoordinates;

    MoveType(ArrayList<Coordinate> relativeCoordinates) {
        this.relativeCoordinates = relativeCoordinates;
    }

    MoveType() {
        this.relativeCoordinates = null;
    }

    public ArrayList<Coordinate> getRelativeCoordinates() {
        return relativeCoordinates;
    }

}
