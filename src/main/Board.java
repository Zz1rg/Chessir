package main;

import controller.GameController;
import gui.Stopwatch;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import pieces.*;
import util.Gamemode;
import util.Team;

import java.net.URL;
import java.util.ArrayList;

public class Board extends GridPane {

    public final int tileSize = 75;
    public final int cols = 8;
    public final int rows = 8;

    private boolean kingChecked = false;

    private final Stopwatch whiteStopwatch, blackStopwatch;
    private final GameController gameController;

    public ArrayList<Piece> pieceList = new ArrayList<>();

    public ArrayList<Move> moveHistory = new ArrayList<>();

    public Piece selectedPiece;

    // TODO: migrate game logic into GameController class ?
    //  (e.g. checkScanner, enPassantTile, makeMove, isValidMove, sameTeam, getPiece, findKing, capture, getTileNum, paint)

    public CheckScanner checkScanner = new CheckScanner(this);

    public int enPassantTile = -1;

    public BorderPane root;

    public Gamemode gamemode;

    public Board(BorderPane root, Gamemode gamemode) {
        this.root = root;
        this.gamemode = gamemode;
        this.setPrefHeight(rows * tileSize);
        this.setPrefWidth(cols * tileSize);
        this.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(5))));

        this.whiteStopwatch = new Stopwatch(Team.WHITE, gamemode.getSeconds(), gamemode.getIncrementSecs());
        this.blackStopwatch = new Stopwatch(Team.BLACK, gamemode.getSeconds(), gamemode.getIncrementSecs());
        // gameController must go after stopwatch
        this.gameController = new GameController(this);

        //set numbers of col and row
        for (int i = 0; i < cols; i++) {
            this.getColumnConstraints().add(new ColumnConstraints(tileSize));
        }
        for (int i = 0; i < rows; i++) {
            this.getRowConstraints().add(new RowConstraints(tileSize));
        }

        this.setOnMousePressed(e -> {
            int col = (int) (e.getX() / tileSize);
            int row = (int) (e.getY() / tileSize);
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
        if (piece.isWhite != gameController.isWhiteTurn()) {
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

        /*pieceList.add(new Queen(this, 1, 2, false));
        pieceList.add(new Queen(this, 0, 1, false));
        pieceList.add(new King(this, 7, 7, false));
        pieceList.add(new King(this, 7, 0, true));*/
    }

    public Piece getPiece(int col, int row) {
        for (Piece piece : pieceList) {
            if (piece.col == col && piece.row == row) {
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

        if (move.piece instanceof Pawn) {
            movePawn(move);
        } else if (move.piece instanceof King) {
            moveKing(move);
        } else {
            move.piece.col = move.newCol;
            move.piece.row = move.newRow;
            move.piece.xPos = move.newCol * tileSize;
            move.piece.yPos = move.newRow * tileSize;

            if (move.piece.isFirstMove()) {
                move.piece.firstMoved();
            }

            capture(move.capturedPiece);
            gameController.swapTurn();
        }
        selectedPiece = null;
        moveHistory.add(move);
        gameController.checkForMate(gameController.isWhiteTurn(), root);
        gameController.updateMoveHistory();
        AudioClip sound;
        URL resource;
        if (move.capturedPiece == null) {
            resource = ClassLoader.getSystemResource("move-self.mp3");
        } else {
            resource = ClassLoader.getSystemResource("capture.mp3");
        }
        sound = new AudioClip(resource.toString());
        sound.play();
    }

    private void moveKing(Move move) {
        //castling
        if (Math.abs(move.newCol - move.piece.col) == 2) {
            Piece rook;
            if (move.piece.col < move.newCol) {
                rook = getPiece(7, move.piece.row);
                rook.setCol(5);
            } else {
                rook = getPiece(0, move.piece.row);
                rook.setCol(3);
            }
            rook.setFirstMove(false);
            rook.setxPos(rook.getCol() * tileSize);
        }

        move.piece.col = move.newCol;
        move.piece.row = move.newRow;
        move.piece.xPos = move.newCol * tileSize;
        move.piece.yPos = move.newRow * tileSize;

        if (move.piece.isFirstMove) {
            move.piece.firstMoved();
        }

        capture(move.capturedPiece);
        gameController.swapTurn();
    }

    public void movePawn(Move move) {
        //enPassant
        int colorIndex = move.piece.isWhite ? 1 : -1;
        int promotionRow = move.piece.isWhite ? 0 : 7;

        if (getTileNum(move.newCol, move.newRow) == enPassantTile) {
            move.capturedPiece = getPiece(move.newCol, move.newRow + colorIndex);
        }
        if (Math.abs(move.newRow - move.piece.row) == 2) {
            enPassantTile = getTileNum(move.newCol, move.newRow + colorIndex);
        } else {
            enPassantTile = -1;
        }

        //Promotion
        if (move.newRow == promotionRow) {
            promotePawn(move);
        }

        move.piece.col = move.newCol;
        move.piece.row = move.newRow;
        move.piece.xPos = move.newCol * tileSize;
        move.piece.yPos = move.newRow * tileSize;

        if (move.piece.isFirstMove) {
            move.piece.firstMoved();
        }

        capture(move.capturedPiece);
        gameController.swapTurn();
    }

    public void promotePawn(Move move) {
        pieceList.add(new Queen(this, move.newCol, move.newRow, move.piece.isWhite));
        capture(move.piece);
    }

    public void capture(Piece piece) {
        pieceList.remove(piece);
    }

    public boolean isValidMove(Move move) {
        if (sameTeam(move.piece, move.capturedPiece)) {
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
        if (checkScanner.isKingChecked(move, false)) {
            return false;
        }

        return true;
    }

    public boolean sameTeam(Piece piece1, Piece piece2) {
        if (piece1 == null || piece2 == null) {
            return false;
        }
        return piece1.isWhite == piece2.isWhite;
    }

    public int getTileNum(int col, int row) {
        return row * rows + col;
    }

    public Piece findKing(boolean isWhite) {
        for (Piece piece : pieceList) {
            if (piece instanceof King && piece.isWhite == isWhite) {
                return piece;
            }
        }
        return null;
    }

    public void paint() {
        //paint board
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                //black tile
                Pane blackPane = new Pane();
                blackPane.setBackground(new Background(new BackgroundFill(Color.color(234.0 / 255, 191.0 / 255, 153.0 / 255), null, null)));
                blackPane.setPrefHeight(tileSize);
                blackPane.setPrefWidth(tileSize);
                //white tile
                Pane whitePane = new Pane();
                whitePane.setBackground(new Background(new BackgroundFill(Color.color(178.0 / 255, 110.0 / 255, 55.0 / 255), null, null)));
                whitePane.setPrefHeight(tileSize);
                whitePane.setPrefWidth(tileSize);
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
            bluePane.setPrefHeight(tileSize);
            bluePane.setPrefWidth(tileSize);
            this.add(bluePane, selectedPiece.getCol(), selectedPiece.getRow());
            //paint movable squares
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (isValidMove(new Move(this, selectedPiece, c, r))) {
                        //available move tile
                        Pane greenPane = new Pane();
                        greenPane.setBackground(new Background(new BackgroundFill(Color.color(0, 255.0 / 255, 0, 50.0 / 100), null, null)));
                        greenPane.setPrefHeight(tileSize);
                        greenPane.setPrefWidth(tileSize);
                        this.add(greenPane, c, r);
                    }
                }
            }
        }

        Piece king = findKing(gameController.isWhiteTurn());
        //paint checked king
        if (checkScanner.isKingChecked(new Move(this, king, king.col, king.row), true) && !this.isKingChecked()) {
            //System.out.println("King is checked");
            //checked king tile
            setKingChecked(true);
            Pane redPane = new Pane();
            redPane.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
            redPane.setPrefHeight(tileSize);
            redPane.setPrefWidth(tileSize);
            this.add(redPane, king.col, king.row);
            AudioClip sound = new AudioClip(ClassLoader.getSystemResource("notify.mp3").toString());
            sound.play();
        } else if (checkScanner.isKingChecked(new Move(this, king, king.col, king.row), true) && this.isKingChecked()) {
            //System.out.println("King is still checked");
            Pane redPane = new Pane();
            redPane.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
            redPane.setPrefHeight(tileSize);
            redPane.setPrefWidth(tileSize);
            this.add(redPane, king.col, king.row);
        } else {
            //System.out.println("King is not checked");
            setKingChecked(false);
        }

        //paint pieces
        for (Piece piece : pieceList) {
            this.add(piece, piece.xPos / tileSize, piece.yPos / tileSize);
        }
    }

    public GameController getGameController() {
        return gameController;
    }

    public Board getBoard() {
        return this;
    }

    public boolean isKingChecked() {
        return kingChecked;
    }

    public void setKingChecked(boolean kingChecked) {
        this.kingChecked = kingChecked;
    }

    public int getCols() {
        return cols;
    }

    public int getRows() {
        return rows;
    }

    public ArrayList<Piece> getPieceList() {
        return pieceList;
    }

    public ArrayList<Move> getMoveHistory() {
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
}