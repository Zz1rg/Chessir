package util;

import java.util.ArrayList;

public enum MoveType {
    HORIZONTAL(new ArrayList<Coordinate>() {{
        add(new CoordinateRC(0, -1));
        add(new CoordinateRC(0, 1));
    }}),
    VERTICAL(new ArrayList<Coordinate>() {{
        add(new CoordinateRC(-1, 0));
        add(new CoordinateRC(1, 0));
    }}),
    DIAGONAL(new ArrayList<Coordinate>() {{
        add(new CoordinateRC(-1, -1));
        add(new CoordinateRC(-1, 1));
        add(new CoordinateRC(1, -1));
        add(new CoordinateRC(1, 1));
    }}),
    KING(new ArrayList<Coordinate>() {{
        addAll(MoveType.HORIZONTAL.getRelativeCoordinates());
        addAll(MoveType.VERTICAL.getRelativeCoordinates());
        addAll(MoveType.DIAGONAL.getRelativeCoordinates());
    }}),
    KNIGHT(new ArrayList<Coordinate>() {{
        add(new CoordinateRC(-1, -2));
        add(new CoordinateRC(-2, -1));
        add(new CoordinateRC(-2, 1));
        add(new CoordinateRC(-1, 2));
        add(new CoordinateRC(1, 2));
        add(new CoordinateRC(2, 1));
        add(new CoordinateRC(2, -1));
        add(new CoordinateRC(1, -2));
    }}),

    WHITE_PAWN_ATTACK(new ArrayList<Coordinate>() {{
        add(new CoordinateRC(-1, -1));
        add(new CoordinateRC(-1, 1));
    }}),

    BLACK_PAWN_ATTACK(new ArrayList<Coordinate>() {{
        add(new CoordinateRC(1, -1));
        add(new CoordinateRC(1, 1));
    }}),

    WHITE_PAWN_FIRST_MOVE(new ArrayList<Coordinate>() {{
        add(new CoordinateRC(-1, 0));
        add(new CoordinateRC(-2, 0));
    }}),

    BLACK_PAWN_FIRST_MOVE(new ArrayList<Coordinate>() {{
        add(new CoordinateRC(1, 0));
        add(new CoordinateRC(2, 0));
    }}),

    WHITE_PAWN_NORMAL_MOVE(new ArrayList<Coordinate>() {{
        add(new CoordinateRC(-1, 0));
    }}),

    BLACK_PAWN_NORMAL_MOVE(new ArrayList<Coordinate>() {{
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
