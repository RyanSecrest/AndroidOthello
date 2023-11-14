package com.example.numbergridview;

import android.widget.TextView;

public class OthelloGame {
    public static final int EMPTY = 0;
    public static final int BLACK = 1;
    public static final int WHITE = 2;

    private int[][] mBoard;
    private int mCurrentPlayer;

    public OthelloGame() {
        mBoard = new int[8][8];
        startNewGame();
    }

    public void startNewGame() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                mBoard[i][j] = EMPTY;
            }
        }

        // Set initial positions
        mBoard[3][3] = WHITE;
        mBoard[4][4] = WHITE;
        mBoard[3][4] = BLACK;
        mBoard[4][3] = BLACK;

        mCurrentPlayer = BLACK;
    }

    public int getCell(int x, int y) {
        return mBoard[x][y];
    }

    public boolean makeMove(int x, int y) {
        NumberGridView mNumberGridView = MainActivity.getNumberGridView();
        TextView mTextViewWinner = MainActivity.getTextViewWinner();

        if (isValidMove(x, y, mCurrentPlayer)) {
            placePiece(x, y, mCurrentPlayer);
            mCurrentPlayer = (mCurrentPlayer == BLACK) ? WHITE : BLACK;
            if (isGameOver()) {
                OthelloGame game = mNumberGridView.getGame();
                int blackScore = game.getScore(OthelloGame.BLACK);
                int whiteScore = game.getScore(OthelloGame.WHITE);
                String winner = (blackScore > whiteScore) ? "Black" : "White";
                String text = "Winner: " + winner;
                mTextViewWinner.setText(text);
            }
            return true;
        }
        return false;
    }


    public int getCurrentPlayer() {
        return mCurrentPlayer;
    }

    private void placePiece(int x, int y, int player) {
        mBoard[x][y] = player;

        // Flip opponent's pieces
        flipPieces(x, y, player);
    }

    private boolean isValidMove(int x, int y, int player) {
        // Check if the cell is empty
        if (mBoard[x][y] != EMPTY) {
            return false;
        }

        // Check if there's at least one valid direction to flip opponent's pieces
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (isValidDirection(x, y, player, dx, dy)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isValidDirection(int x, int y, int player, int dx, int dy) {
        if (dx == 0 && dy == 0) {
            return false;
        }

        int opponent = (player == BLACK) ? WHITE : BLACK;
        int newX = x + dx;
        int newY = y + dy;

        // Check if the first piece in the direction is the opponent's piece
        if (!isOnBoard(newX, newY) || mBoard[newX][newY] != opponent) {
            return false;
        }

        newX += dx;
        newY += dy;

        while (isOnBoard(newX, newY)) {
            if (mBoard[newX][newY] == EMPTY) {
                return false;
            }
            if (mBoard[newX][newY] == player) {
                return true;
            }

            newX += dx;
            newY += dy;
        }

        return false;
    }

    private void flipPieces(int x, int y, int player) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (isValidDirection(x, y, player, dx, dy)) {
                    flipDirection(x, y, player, dx, dy);
                }
            }
        }
    }

    private void flipDirection(int x, int y, int player, int dx, int dy) {
        int opponent = (player == BLACK) ? WHITE : BLACK;
        int newX = x + dx;
        int newY = y + dy;

        while (isOnBoard(newX, newY) && mBoard[newX][newY] == opponent) {
            mBoard[newX][newY] = player;
            newX += dx;
            newY += dy;
        }
    }

    private boolean isOnBoard(int x, int y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }

    public int getScore(int player) {
        int score = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (mBoard[i][j] == player) {
                    score++;
                }
            }
        }
        return score;
    }

    public boolean hasValidMoves(int player) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (isValidMove(i, j, player)) {
                    return true;
                }
            }
        }
        return false;
    }


    public boolean isGameOver() {
        if (getScore(BLACK) + getScore(WHITE) == 64) { // check if board is full
            return true;
        } else if (!hasValidMoves(BLACK) && !hasValidMoves(WHITE)) { // check if both players have no valid moves left
            return true;
        } else {
            return false;
        }
    }
}
