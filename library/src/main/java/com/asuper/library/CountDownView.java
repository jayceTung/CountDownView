package com.asuper.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Super on 2016/10/18.
 */
public class CountDownView extends View {
    private static final String TAG = "CountDownView";

    /**
     * default params
     */
    private static final int BACKGROUND_COLOR = 0x50555555;
    private static final float BORDER_WIDTH = 15f;
    private static final int BORDER_COLOR = 0xFF6ADBFE;
    private static final String TXT = "跳过广告";
    private static final float TEXT_SIZE = 50f;
    private static final int TEXT_COLOR = 0xFFFFFFFF;

    private Context mContext;
    private Paint mCirclePaint;
    private Paint mBorderPaint ;
    private TextPaint mTextPaint;
    private StaticLayout mStaticLayout;
    private float progress = 0;

    private int backgroundColor;
    private float borderWidth;
    private int borderColor;
    private String mText;
    private float mTextSize;
    private int mTextColor;

    public CountDownView(Context context) {
        this(context, null);
    }

    public CountDownView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountDownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.CountDownView);
        init(typedArray);
    }

    /**
     * init params
     * @param typedArray
     */
    private void init(TypedArray typedArray) {
        backgroundColor = typedArray.getColor(R.styleable.CountDownView_background_color, BACKGROUND_COLOR);
        borderWidth = typedArray.getDimension(R.styleable.CountDownView_border_width, BORDER_WIDTH);
        borderColor = typedArray.getColor(R.styleable.CountDownView_border_color, BORDER_COLOR);
        mText = typedArray.getString(R.styleable.CountDownView_text);
        if (TextUtils.isEmpty(mText)) {
            mText = TXT;
        }
        mTextSize = typedArray.getDimension(R.styleable.CountDownView_text_size, TEXT_SIZE);
        mTextColor = typedArray.getColor(R.styleable.CountDownView_text_color, TEXT_COLOR);

        typedArray.recycle();

        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setDither(true);
        mCirclePaint.setColor(backgroundColor);
        mCirclePaint.setStyle(Paint.Style.FILL);

        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setDither(true);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        mBorderPaint = new Paint();
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setDither(true);
        mBorderPaint.setColor(borderColor);
        mBorderPaint.setStrokeWidth(borderWidth);
        mBorderPaint.setStyle(Paint.Style.STROKE);

        int textWidth = (int) mTextPaint.measureText(mText.substring(0, (mText.length() + 1) / 2));
        mStaticLayout = new StaticLayout(mText, mTextPaint, textWidth, Layout.Alignment.ALIGN_NORMAL, 1F, 0, false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode != MeasureSpec.EXACTLY) {
            width = mStaticLayout.getWidth();
        }

        if (heightMode != MeasureSpec.EXACTLY) {
            height = mStaticLayout.getHeight();
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        int min = Math.min(width, height);

        canvas.drawCircle(width / 2, height / 2, min, mCirclePaint);

        //画边框
        RectF rectF;
        if (width > height) {
            rectF = new RectF(width / 2 - min / 2 + borderWidth / 2, 0 + borderWidth / 2, width / 2 + min / 2 - borderWidth / 2, height - borderWidth / 2);
        } else {
            rectF = new RectF(borderWidth / 2, height / 2 - min / 2 + borderWidth / 2, width - borderWidth / 2, height / 2 - borderWidth / 2 + min / 2);
        }
        canvas.drawArc(rectF, -90, progress, false, mBorderPaint);
        //画居中的文字
//       canvas.drawText("稍等片刻", width / 2, height / 2 - textPaint.descent() + textPaint.getTextSize() / 2, textPaint);
        canvas.translate(width / 2, height / 2 - mStaticLayout.getHeight() / 2);
        mStaticLayout.draw(canvas);
    }
}
