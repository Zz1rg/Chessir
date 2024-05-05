package main;

import controller.GameController;
import gui.Stopwatch;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import pieces.*;
import util.Gamemode;
import util.MoveRecord;
import util.Team;

import java.net.URL;
import java.util.ArrayList;

public class Board extends GridPane {

    public static final int TILE_SIZE = 75;
    public static final int BOARD_COLS = 8;
    public static final int BOARD_ROWS = 8;

    private boolean kingChecked = false;

    private final Stopwatch whiteStopwatch, blackStopwatch;
    private final GameController gameController;

    private final ArrayList<Piece> pieceList = new ArrayList<>();

    private final ArrayList<MoveRecord> moveHistory = new ArrayList<>();

    private Piece selectedPiece;

    public final CheckScanner checkScanner = new CheckScanner(this);

    private int enPassantTile = -1;

    private final BorderPane root;

    private final Gamemode gamemode;

    public Board(BorderPane root, Gamemode gamemode) {
        this.root = root;
        this.gamemode = gamemode;
        this.setPrefHeight(BOARD_ROWS * TILE_SIZE);
        this.setMaxHeight(BOARD_ROWS * TILE_SIZE);
        this.setPrefWidth(BOARD_COLS * TILE_SIZE);
        this.setMaxWidth(BOARD_COLS * TILE_SIZE);
        this.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(5))));

        this.whiteStopwatch = new Stopwatch(Team.WHITE, gamemode.getSeconds(), gamemode.getIncrementSecs());
        this.blackStopwatch = new Stopwatch(Team.BLACK, gamemode.getSeconds(), gamemode.getIncrementSecs());
        // gameController must go after stopwatch
        this.gameController = new GameController(this);

        //set numbers of col and row
        for (int i = 0; i < BOARD_COLS; i++) {
            this.getColumnConstraints().add(new ColumnConstraints(TILE_SIZE));
        }
        for (int i = 0; i < BOARD_ROWS; i++) {
            this.getRowConstraints().add(new RowConstraints(TILE_SIZE));
        }

        this.setOnMousePressed(e -> {
            int col = (int) (e.getX() / TILE_SIZE);
            int row = (int) (e.getY() / TILE_SIZE);
            Piece clickedPiece = getPiece(col, row);

            if (selectedPiece == null) {
                selectPiece(clickedPiece);
            } else {
                if (clickedPiece == selectedPiece) {
                    selectedPiece = null;
                } else {
                    if (sameTeam(clickedPiece, selectedPiece)) {
                        selectPiece(clickedPiece);
                    } else {
                        Move move = new Move(this, selectedPiece, col, row);
                        makeMove(move);
                    }
                }
            }

            getChildren().clear();
            paint();
        });

        initBoard();
    }

    public void initBoard() {
        addPieces();
        paint();
    }

    // This function is only used in onMousePressed
    private void selectPiece(Piece piece) {
        if (piece == null) {
            return;
        }
        if (piece.isWhite() != gameController.isWhiteTurn()) {
            return;
        }
        selectedPiece = piece;
    }

    public void addPieces() {
        for (int i = 0; i <= 7; i++) {
            pieceList.add(new Pawn(this, i, 1, false));
        }

        for (int i = 0; i <= 7; i++) {
            pieceList.add(new Pawn(this, i, 6, true));
        }

        pieceList.add(new Knight(this, 1, 0, false));
        pieceList.add(new Knight(this, 6, 0, false));
        pieceList.add(new Knight(this, 1, 7, true));
        pieceList.add(new Knight(this, 6, 7, true));
        pieceList.add(new King(this, 4, 0, false));
        pieceList.add(new King(this, 4, 7, true));
        pieceList.add(new Rook(this, 0, 0, false));
        pieceList.add(new Rook(this, 7, 0, false));
        pieceList.add(new Rook(this, 0, 7, true));
        pieceList.add(new Rook(this, 7, 7, true));
        pieceList.add(new Bishop(this, 2, 0, false));
        pieceList.add(new Bishop(this, 5, 0, false));
        pieceList.add(new Bishop(this, 2, 7, true));
        pieceList.add(new Bishop(this, 5, 7, true));
        pieceList.add(new Queen(this, 3, 0, false));
        pieceList.add(new Queen(this, 3, 7, true));
    }

    public Piece getPiece(int col, int row) {
        for (Piece piece : pieceList) {
            if (piece.getCol() == col && piece.getRow() == row) {
                return piece;
            }
        }
        return null;
    }

    public void makeMove(Move move) {
        if (!isValidMove(move)) {
            selectedPiece = null;
            return;
        }

        // add move record to move history before states change
        if (move.piece instanceof King && Math.abs(move.newCol - move.piece.getCol()) == 2) {
            moveHistory.add(new MoveRecord(move, getEnPassantTile(), move.piece.isFirstMove(), true));
        } else {
            moveHistory.add(new MoveRecord(move, getEnPassantTile(), move.piece.isFirstMove(), false));
        }


        if (move.piece instanceof Pawn) {
            movePawn(move);
        } else if (move.piece instanceof King) {
            moveKing(move);
        } else {
            move.piece.setCol(move.newCol);
            move.piece.setRow(move.newRow);
            move.piece.setxPos(move.newCol * TILE_SIZE);
            move.piece.setyPos(move.newRow * TILE_SIZE);

            if (move.piece.isFirstMove()) {
                move.piece.firstMoved();
            }

            capture(move.getCapturedPiece());
            gameController.swapTurn();
        }
        selectedPiece = null;
        gameController.checkForMate(gameController.isWhiteTurn());
        gameController.updateMoveHistory();
        AudioClip sound;
        URL resource;
        if (move.getCapturedPiece() == null) {
            resource = ClassLoader.getSystemResource("move-self.mp3");
        } else {
            resource = ClassLoader.getSystemResource("capture.mp3");
        }
        sound = new AudioClip(resource.toString());
        sound.play();
    }

    private void moveKing(Move move) {
        //castling
        if (Math.abs(move.newCol - move.piece.getCol()) == 2) {
            Piece rook;
            if (move.piece.getCol() < move.newCol) {
                rook = getPiece(7, move.piece.getRow());
                rook.setCol(5);
            } else {
                rook = getPiece(0, move.piece.getRow());
                rook.setCol(3);
            }
            rook.setFirstMove(false);
            rook.setxPos(rook.getCol() * TILE_SIZE);
        }

        move.piece.setCol(move.newCol);
        move.piece.setRow(move.newRow);
        move.piece.setxPos(move.newCol * TILE_SIZE);
        move.piece.setyPos(move.newRow * TILE_SIZE);

        if (move.piece.isFirstMove()) {
            move.piece.firstMoved();
        }

        capture(move.getCapturedPiece());
        gameController.swapTurn();
    }

    public void movePawn(Move move) {
        //enPassant
        int colorIndex = move.piece.isWhite() ? 1 : -1;
        int promotionRow = move.piece.isWhite() ? 0 : 7;

        if (getTileNum(move.newCol, move.newRow) == enPassantTile) {
            move.setCapturedPiece(getPiece(move.newCol, move.newRow + colorIndex));
        }
        if (Math.abs(move.newRow - move.piece.getRow()) == 2) {
            enPassantTile = getTileNum(move.newCol, move.newRow + colorIndex);
        } else {
            enPassantTile = -1;
        }

        //Promotion
        if (move.newRow == promotionRow) {
            promotePawn(move);
        }

        move.piece.setCol(move.newCol);
        move.piece.setRow(move.newRow);
        move.piece.setxPos(move.newCol * TILE_SIZE);
        move.piece.setyPos(move.newRow * TILE_SIZE);

        if (move.piece.isFirstMove()) {
            move.piece.firstMoved();
        }

        capture(move.getCapturedPiece());
        gameController.swapTurn();
    }

    public void promotePawn(Move move) {
        pieceList.add(new Queen(this, move.newCol, move.newRow, move.piece.isWhite()));
        capture(move.piece);
    }

    public void capture(Piece piece) {
        pieceList.remove(piece);
    }

    public boolean isValidMove(Move move) {
        if (sameTeam(move.piece, move.getCapturedPiece())) {
            return false;
        }
        if (!move.piece.isValidMovement(move.newCol, move.newRow)) {
            return false;
        }
        if (move.piece.moveCollidesWithPiece(move.newCol, move.newRow)) {
            return false;
        }
        if (move.newCol < 0 || move.newCol > 7 || move.newRow < 0 || move.newRow > 7) {
            return false;
        }
        return !checkScanner.isKingChecked(move, false);
    }

    public boolean sameTeam(Piece piece1, Piece piece2) {
        if (piece1 == null || piece2 == null) {
            return false;
        }
        return piece1.isWhite() == piece2.isWhite();
    }

    public int getTileNum(int col, int row) {
        return row * BOARD_ROWS + col;
    }

    public Piece findKing(boolean isWhite) {
        for (Piece piece : pieceList) {
            if (piece instanceof King && piece.isWhite() == isWhite) {
                return piece;
            }
        }
        return null;
    }

    public void paint() {
        //paint board
        for (int r = 0; r < BOARD_ROWS; r++) {
            for (int c = 0; c < BOARD_COLS; c++) {
                //black tile
                Pane blackPane = new Pane();
                blackPane.setBackground(new Background(new BackgroundFill(Color.color(234.0 / 255, 191.0 / 255, 153.0 / 255), null, null)));
                blackPane.setPrefHeight(TILE_SIZE);
                blackPane.setPrefWidth(TILE_SIZE);
                //white tile
                Pane whitePane = new Pane();
                whitePane.setBackground(new Background(new BackgroundFill(Color.color(178.0 / 255, 110.0 / 255, 55.0 / 255), null, null)));
                whitePane.setPrefHeight(TILE_SIZE);
                whitePane.setPrefWidth(TILE_SIZE);
                if ((c + r) % 2 == 0) {
                    this.add(blackPane, c, r);
                } else {
                    this.add(whitePane, c, r);
                }
            }
        }

        if (selectedPiece != null) {
            // paint selected piece
            Pane bluePane = new Pane();
            bluePane.setBackground(new Background(new BackgroundFill(Color.color(80.0 / 255, 64.0 / 255, 1, 0.8), null, null)));
            bluePane.setPrefHeight(TILE_SIZE);
            bluePane.setPrefWidth(TILE_SIZE);
            this.add(bluePane, selectedPiece.getCol(), selectedPiece.getRow());
            //paint movable squares
            for (int r = 0; r < BOARD_ROWS; r++) {
                for (int c = 0; c < BOARD_COLS; c++) {
                    if (isValidMove(new Move(this, selectedPiece, c, r))) {
                        //available move tile
                        Pane greenPane = new Pane();
                        greenPane.setBackground(new Background(new BackgroundFill(Color.color(0, 255.0 / 255, 0, 50.0 / 100), null, null)));
                        greenPane.setPrefHeight(TILE_SIZE);
                        greenPane.setPrefWidth(TILE_SIZE);
                        this.add(greenPane, c, r);
                    }
                }
            }
        }

        Piece king = findKing(gameController.isWhiteTurn());
        //paint checked king
        if (checkScanner.isKingChecked(new Move(this, king, king.getCol(), king.getRow()), true) && !this.isKingChecked()) {
            //System.out.println("King is checked");
            //checked king tile
            setKingChecked(true);
            Pane redPane = new Pane();
            redPane.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
            redPane.setPrefHeight(TILE_SIZE);
            redPane.setPrefWidth(TILE_SIZE);
            this.add(redPane, king.getCol(), king.getRow());
            AudioClip sound = new AudioClip(ClassLoader.getSystemResource("notify.mp3").toString());
            sound.play();
        } else if (checkScanner.isKingChecked(new Move(this, king, king.getCol(), king.getRow()), true) && this.isKingChecked()) {
            //System.out.println("King is still checked");
            Pane redPane = new Pane();
            redPane.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
            redPane.setPrefHeight(TILE_SIZE);
            redPane.setPrefWidth(TILE_SIZE);
            this.add(redPane, king.getCol(), king.getRow());
        } else {
            //System.out.println("King is not checked");
            setKingChecked(false);
        }

        //paint pieces
        for (Piece piece : pieceList) {
            this.add(piece, piece.getxPos() / TILE_SIZE, piece.getyPos() / TILE_SIZE);
        }
    }

    public GameController getGameController() {
        return gameController;
    }

    public boolean isKingChecked() {
        return kingChecked;
    }

    public void setKingChecked(boolean kingChecked) {
        this.kingChecked = kingChecked;
    }

    public ArrayList<Piece> getPieceList() {
        return pieceList;
    }

    public ArrayList<MoveRecord> getMoveHistory() {
        return moveHistory;
    }

    public BorderPane getRoot() {
        return root;
    }

    public Stopwatch getBlackStopwatch() {
        return blackStopwatch;
    }

    public Stopwatch getWhiteStopwatch() {
        return whiteStopwatch;
    }

    public Gamemode getGamemode() {
        return gamemode;
    }

    public int getEnPassantTile() {
        return enPassantTile;
    }

    public Piece getSelectedPiece() {
        return selectedPiece;
    }

    public void setEnPassantTile(int enPassantTile) {
        if (enPassantTile < 0 || 7 < enPassantTile) {
            enPassantTile = -1;
        }
        this.enPassantTile = enPassantTile;
    }
}