package cn.libery.switchhost;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.BounceInterpolator;


/**
 * @author shizhiqiang on 2018/5/11.
 */
public class DragFloatButton extends FloatingActionButton {

    private int screenWidth;
    private int screenHeight;
    private int screenWidthHalf;
    private int statusHeight;
    private String mTitle = "切换HOST";
    private Paint mPaint;
    private Rect mBounds;

    public DragFloatButton(Context context) {
        super(context);
        init();
    }

    public DragFloatButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DragFloatButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        screenWidth = PixelUtil.getWith();
        screenWidthHalf = screenWidth / 2;
        screenHeight = PixelUtil.getHeight();
        statusHeight = PixelUtil.dp2px(24);
        initPaint();
    }

    private float lastX;
    private float lastY;

    private boolean isDrag;


    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float rawX = event.getRawX();
        float rawY = event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isDrag = false;
                getParent().requestDisallowInterceptTouchEvent(true);
                lastX = rawX;
                lastY = rawY;
                break;
            case MotionEvent.ACTION_MOVE:
                //计算手指移动了多少
                float dx = rawX - lastX;
                float dy = rawY - lastY;
                isDrag = dx != 0 || dy != 0;

                float x = getX() + dx;
                float y = getY() + dy;
                //检测是否到达边缘 左上右下
                x = x < 0 ? 0 : x > screenWidth - getWidth() ? screenWidth - getWidth() : x;
                y = y < statusHeight ? statusHeight : y + getHeight() > screenHeight ? screenHeight - getHeight() : y;
                setX(x);
                setY(y);
                lastX = rawX;
                lastY = rawY;
                break;
            case MotionEvent.ACTION_UP:
                if (isDrag) {
                    //恢复按压效果
                    setPressed(false);
                    if (rawX > screenWidthHalf) {
                        animate().setInterpolator(new BounceInterpolator())
                                .setDuration(200)
                                .xBy(screenWidth - getWidth() - getX())
                                .start();
                    } else {
                        ObjectAnimator oa = ObjectAnimator.ofFloat(this, "x", getX(), 0);
                        oa.setInterpolator(new BounceInterpolator());
                        oa.setDuration(200);
                        oa.start();
                    }
                }
                break;
            default:
        }
        //如果是拖拽则消耗事件，否则正常传递即可。
        return isDrag || super.onTouchEvent(event);
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(27);
        mBounds = new Rect();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setMinimumWidth(PixelUtil.dp2px(58));
        mPaint.getTextBounds(mTitle, 0, mTitle.length(), mBounds);
        Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
        int baseline = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        canvas.drawText(mTitle, (getMeasuredWidth() / 2 - mBounds.width() / 2), baseline, mPaint);
    }

}
