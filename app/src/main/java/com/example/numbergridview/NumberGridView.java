package com.example.numbergridview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class NumberGridView extends View {
    private final int mBoardSize = 8;
    private float mCellSize;
    private final Paint mGridPaint = new Paint();
    private final Paint mBlackPaint = new Paint();
    private final Paint mWhitePaint = new Paint();
    private final Rect mRect = new Rect();
    private OthelloGame mOthelloGame;

    private UpdateUiListener mUpdateUiListener;

    public interface UpdateUiListener {
        void updateUi();
    }

    public void setUpdateUiListener(UpdateUiListener listener) {
        mUpdateUiListener = listener;
    }

    public NumberGridView(Context context) {
        super(context);
        initialize();
    }

    public NumberGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mGridPaint.setColor(Color.BLACK);
        mGridPaint.setStrokeWidth(5);
        mBlackPaint.setColor(Color.BLACK);
        mWhitePaint.setColor(Color.WHITE);

        OthelloGame othelloGame = new OthelloGame();
        mOthelloGame = othelloGame;
    }

    private void initialize() {
        mGridPaint.setColor(Color.BLACK);
        mGridPaint.setStrokeWidth(5);
        mBlackPaint.setColor(Color.BLACK);
        mWhitePaint.setColor(Color.WHITE);

        mOthelloGame = new OthelloGame();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int size = Math.min(width, height);
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCellSize = Math.min(w, h) / (float) mBoardSize;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.GREEN);
        drawGrid(canvas);
        drawCells(canvas);
    }

    private void drawGrid(Canvas canvas) {
        for (int i = 0; i <= mBoardSize; i++) {
            canvas.drawLine(i * mCellSize, 0, i * mCellSize, mBoardSize * mCellSize, mGridPaint);
            canvas.drawLine(0, i * mCellSize, mBoardSize * mCellSize, i * mCellSize, mGridPaint);
        }
    }

    private void drawCells(Canvas canvas) {
        for (int i = 0; i < mBoardSize; i++) {
            for (int j = 0; j < mBoardSize; j++) {
                int cell = mOthelloGame.getCell(i, j);
                if (cell != OthelloGame.EMPTY) {
                    Paint paint = (cell == OthelloGame.BLACK) ? mBlackPaint : mWhitePaint;
                    int margin = 8;
                    mRect.set((int) (i * mCellSize) + margin, (int) (j * mCellSize) + margin,
                            (int) ((i + 1) * mCellSize) - margin, (int) ((j + 1) * mCellSize) - margin);
                    canvas.drawOval(mRect.left, mRect.top, mRect.right, mRect.bottom, paint);
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int x = (int) (event.getX() / mCellSize);
            int y = (int) (event.getY() / mCellSize);

            if (mOthelloGame.makeMove(x, y)) {
                invalidate();
                if (mUpdateUiListener != null) {
                    mUpdateUiListener.updateUi();
                }
            }
            return true;
        }
        return false;
    }

    public OthelloGame getGame() {
        return mOthelloGame;
    }
}
