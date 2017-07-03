
package ntu.csie.pinyin;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class CircleLayout extends ViewGroup {

    public static final float DEFAULT_ANGLE_OFFSET = -90f;
    public static final float DEFAULT_ANGLE_RANGE = 360f;

    protected final float mAngleOffset;
    protected final float mAngleRange;

    protected final int mMarginV;
    protected final int mMarginH;


    private RectF mBounds = new RectF();

    private Bitmap mDst;
    private Bitmap mSrc;


    private Bitmap mDrawingCache;


    public CircleLayout(Context context) {
        this(context, null);
    }


    public CircleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleLayout, 0, 0);
        mAngleOffset = a.getFloat(R.styleable.CircleLayout_angleOffset, DEFAULT_ANGLE_OFFSET);
        mAngleRange = a.getFloat(R.styleable.CircleLayout_angleRange, DEFAULT_ANGLE_RANGE);
        mMarginV = a.getDimensionPixelSize(R.styleable.CircleLayout_marginV, 0);
        mMarginH = a.getDimensionPixelSize(R.styleable.CircleLayout_marginH, 0);
        a.recycle();

        setLayerType(LAYER_TYPE_SOFTWARE, null);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int count = getChildCount();

        int maxHeight = 0;
        int maxWidth = 0;

        // Find rightmost and bottommost child
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
                maxWidth = Math.max(maxWidth, child.getMeasuredWidth());
                maxHeight = Math.max(maxHeight, child.getMeasuredHeight());
            }
        }

        // Check against our minimum height and width
        maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());

        int width = resolveSize(maxWidth, widthMeasureSpec);
        int height = resolveSize(maxHeight, heightMeasureSpec);

        setMeasuredDimension(width, height);

        if (mSrc != null && (mSrc.getWidth() != width || mSrc.getHeight() != height)) {
            mDst.recycle();
            mSrc.recycle();
            mDrawingCache.recycle();

            mDst = null;
            mSrc = null;
            mDrawingCache = null;
        }

        if (mSrc == null) {
            mSrc = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            mDst = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            mDrawingCache = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        }
    }

    private LayoutParams layoutParams(View child) {
        return (LayoutParams) child.getLayoutParams();
    }

    @Override
    @SuppressWarnings("deprecation")
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int children = getChildCount();

        float totalWeight = 0f;

        for (int i = 0; i < children; i++) {
            final View child = getChildAt(i);

            LayoutParams lp = layoutParams(child);

            totalWeight += lp.weight;
        }

        final int width = getWidth();
        final int height = getHeight();

        final float minDimen = width > height ? height : width;
        final float radiusV = (minDimen - mMarginV) / 2f;
        final float radiusH = (minDimen - mMarginH) / 2f;

        mBounds.set(width / 2 - minDimen / 2, height / 2 - minDimen / 2, width / 2 + minDimen / 2, height / 2 + minDimen / 2);

        float startAngle = mAngleOffset;

        for (int i = 0; i < children; i++) {
            final View child = getChildAt(i);

            final LayoutParams lp = layoutParams(child);

            final float angle = mAngleRange / totalWeight * lp.weight;

            final float centerAngle = startAngle + angle / 2f;
            final int x;
            final int y;

            if (children > 1) {
                double radian = Math.toRadians(centerAngle);
                x = (int) (radiusV * Math.cos(radian)) + (width / 2);
                y = (int) (radiusH * Math.sin(radian)) + (height / 2);
            } else {
                x = width / 2;
                y = height / 2;
            }

            final int halfChildWidth = child.getMeasuredWidth() / 2;
            final int halfChildHeight = child.getMeasuredHeight() / 2;

            final int left = lp.width != LayoutParams.FILL_PARENT ? x - halfChildWidth : 0;
            final int top = lp.height != LayoutParams.FILL_PARENT ? y - halfChildHeight : 0;
            final int right = lp.width != LayoutParams.FILL_PARENT ? x + halfChildWidth : width;
            final int bottom = lp.height != LayoutParams.FILL_PARENT ? y + halfChildHeight : height;

            child.layout(left, top, right, bottom);

            if (left != child.getLeft() || top != child.getTop()
                    || right != child.getRight() || bottom != child.getBottom()
                    || lp.startAngle != startAngle
                    || lp.endAngle != startAngle + angle) {
            }

            lp.startAngle = startAngle;

            startAngle += angle;

            lp.endAngle = startAngle;
        }

        invalidate();
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        LayoutParams lp = new LayoutParams(p.width, p.height);

        if (p instanceof LinearLayout.LayoutParams) {
            lp.weight = ((LinearLayout.LayoutParams) p).weight;
        }

        return lp;
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }


    public static class LayoutParams extends ViewGroup.LayoutParams {

        private float startAngle;
        private float endAngle;

        public float weight = 1f;

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(Context context, AttributeSet attrs) {
            super(context, attrs);
        }
    }

}