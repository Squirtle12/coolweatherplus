package com.example.aaa.coolweather.View;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.example.aaa.coolweather.R;

/**
 * Created by aaa on 2019/4/18.
 */

public class CircleIndexView extends View {
    //（灰）外圈画笔
    private Paint outPaint = new Paint();
    //（绿）内圈画笔
    private Paint inPaint = new Paint();
    //文字画笔
    private Paint middleTextPaint = new Paint();

    //外切矩形
    private RectF mRectF;

    //圆初始弧度
    private float startAngle = 135;
    //圆结束弧度
    private float sweepAngle = 270;

    //图的中心
    int mCenter = 0;

    int mRadius = 0;
    //动画完成时间
    int duration;
    private int indexValue;
    private String middleText = "N/A";
    private String topText = "";
    private int circleWidth;
    private int circleHeight;
    private float inSweepAngle;

    private float numberTextSize;
    private float middleTextSize;
    private float topTextSize;

    private int outCircleColor;
    private int inCircleColor;
    private int numberColor;
    private int middleTextColor;
    private int topTextColor;
    //200dp对应px
    int dp200 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics());

    public int getOutCircleColor()
    {
        return outCircleColor;
    }

    public void setOutCircleColor(int outCircleColor)
    {
        this.outCircleColor = outCircleColor;
    }

    public int getInCircleColor()
    {
        return inCircleColor;
    }

    public void setInCircleColor(int inCircleColor)
    {
        this.inCircleColor = inCircleColor;
    }

    public int getNumberColor()
    {
        return numberColor;
    }

    public void setNumberColor(int numberColor)
    {
        this.numberColor = numberColor;
    }

    public int getMiddleTextColor()
    {
        return middleTextColor;
    }

    public void setMiddleTextColor(int middleTextColor)
    {
        this.middleTextColor = middleTextColor;
    }

    public float getTopTextSize() {
        return topTextSize;
    }

    public void setTopTextSize(float topTextSize) {
        this.topTextSize = topTextSize;
    }

    public int getTopTextColor() {
        return topTextColor;
    }

    public void setTopTextColor(int topTextColor) {
        this.topTextColor = topTextColor;
    }

    public String getTopText() {
        return topText;
    }

    public void setTopText(String topText) {
        this.topText = topText;
    }

    public float getNumberTextSize()
    {
        return numberTextSize;
    }

    public void setNumberTextSize(float numberTextSize)
    {
        this.numberTextSize = numberTextSize;
    }

    public float getMiddleTextSize()
    {
        return middleTextSize;
    }

    public void setMiddleTextSize(float middleTextSize)
    {
        this.middleTextSize = middleTextSize;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getMiddleText()
    {
        return middleText;
    }

    public void setMiddleText(String middleText)
    {
        this.middleText = middleText;
    }

    public int getIndexValue()
    {
        return indexValue;
    }

    public void setIndexValue(int indexValue)
    {
        this.indexValue = indexValue;
    }

    public int getCircleWidth()
    {
        return circleWidth;
    }

    public void setCircleWidth(int circleWidth)
    {
        this.circleWidth = circleWidth;
    }

    public int getCircleHeight()
    {
        return circleHeight;
    }

    public void setCircleHeight(int circleHeight)
    {
        this.circleHeight = circleHeight;
    }

    public float getInSweepAngle()
    {
        return inSweepAngle;
    }

    public void setInSweepAngle(float inSweepAngle)
    {
        this.inSweepAngle = inSweepAngle;
    }


    public CircleIndexView(Context context)
    {
        this(context, null);
    }

    public CircleIndexView(Context context, AttributeSet attrs)
    {
        this(context, attrs, -1);
    }

    public CircleIndexView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        initParams(context, attrs);
        initPaint();
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CircleIndexView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        initParams(context, attrs);
        initPaint();
    }
    /**
     * 初始化画笔
     */
    public void initPaint()
    {
        outPaint.setColor(getOutCircleColor());
        outPaint.setAntiAlias(true);
        outPaint.setStyle(Paint.Style.STROKE);
        outPaint.setStrokeCap(Paint.Cap.ROUND);
        outPaint.setStrokeWidth(12);

        inPaint.setColor(getInCircleColor());
        inPaint.setAntiAlias(true);
        inPaint.setStyle(Paint.Style.STROKE);
        inPaint.setStrokeCap(Paint.Cap.ROUND);
        inPaint.setStrokeWidth(12);

        middleTextPaint = new Paint();
        middleTextPaint.setTextAlign(Paint.Align.CENTER);
        middleTextPaint.setAntiAlias(true);
    }
    /**
     * 初始化View参数
     * 获取布局（xml）中的属性以及使用布局（xml）中的属性，
     * TypedArray帮我们实现了获取attrs中的id与解析id的功能
     * 因此我们可以根据这个id在TypedArray中获取想要的属性值。
     * @param context
     * @param attrs
     */
    private void initParams(Context context, AttributeSet attrs)
    {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CircleIndexView);
        middleText = ta.getString(R.styleable.CircleIndexView_middleText);
        topText = ta.getString(R.styleable.CircleIndexView_topText);

        numberTextSize = ta.getDimension(R.styleable.CircleIndexView_numberTextSize,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20, context.getResources().getDisplayMetrics()));
        middleTextSize = ta.getDimension(R.styleable.CircleIndexView_middleTextSize,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 15, context.getResources().getDisplayMetrics()));
        topTextSize = ta.getDimension(R.styleable.CircleIndexView_topTextSize,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 15, context.getResources().getDisplayMetrics()));
        outCircleColor = ta.getColor(R.styleable.CircleIndexView_outCircleColor, Color.LTGRAY);
        inCircleColor = ta.getColor(R.styleable.CircleIndexView_inCircleColor, Color.GREEN);
        numberColor = ta.getColor(R.styleable.CircleIndexView_numberColor, Color.GREEN);
        middleTextColor = ta.getColor(R.styleable.CircleIndexView_middleTextColor, Color.LTGRAY);
        topTextColor = ta.getColor(R.styleable.CircleIndexView_topTextColor, Color.LTGRAY);
        duration=ta.getInteger(R.styleable.CircleIndexView_duration,2000);
        ta.recycle();
    }

    /**
     * 设置了无论match_parent还是wrap_content都是200dp的默认值。
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        setMeasuredDimension(measureWidth(widthMeasureSpec),
                measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int widthMeasureSpec)
    {
        //将200dp转换为px
        //我的屏幕1dp对应3px
        int result=dp200;
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        switch (specMode)
        {
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
            case MeasureSpec.AT_MOST:
                break;
        }
        setCircleWidth(result);
        float xishu=(float) result/dp200;
        setMiddleTextSize(60*xishu);
        setTopTextSize(45*xishu);
        setNumberTextSize(100*xishu);
        return result;
    }
    private int measureHeight(int heightMeasureSpec)
    {
        //将200dp转换为px
        int result=dp200;
        int specMode = MeasureSpec.getMode(heightMeasureSpec);
        int specSize = MeasureSpec.getSize(heightMeasureSpec);
        switch (specMode)
        {
            case MeasureSpec.EXACTLY: {
                result = specSize;
            }
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
            case MeasureSpec.AT_MOST:
                break;
        }
        setCircleHeight(result);
        return result;
    }
    @Override
    protected void onDraw(Canvas canvas){
        //200dp:middleTextSize:60,middleNumberSize:100,bottomTextSize:45
        super.onDraw(canvas);
        mCenter = getCircleWidth()/2;
        //mRadius = getCircleWidth() / 2 - 50;
        mRadius = getCircleWidth()/2-50;
        //左，上，右，下
        mRectF =new RectF(mCenter-mRadius,mCenter-mRadius+getTopTextSize(),
                mCenter+mRadius,mCenter+mRadius+getTopTextSize());
        //外圆圈
        canvas.drawArc(mRectF,startAngle,sweepAngle,false,outPaint);
        //内圆圈
        canvas.drawArc(mRectF,startAngle,getInSweepAngle(),false,inPaint);

        //中心文字(etc. 良)
        middleTextPaint.setColor(getMiddleTextColor());
        middleTextPaint.setTextSize(getMiddleTextSize());
        Paint.FontMetrics fontMetrics=middleTextPaint.getFontMetrics();
        float baseline=(getCircleHeight()-getNumberTextSize()-getMiddleTextSize()-20-fontMetrics.bottom-fontMetrics.top)/2+getTopTextSize();
       //想让他在居中的位置（getCircleHeight（)/2-getNumberTextSize()/2-getMiddleTextSize()/2-20）
        canvas.drawText(getMiddleText(), getCircleWidth() / 2, baseline , middleTextPaint);

        //中心数字（etc）
        middleTextPaint.setColor(getNumberColor());
        middleTextPaint.setTextSize(getNumberTextSize());
        fontMetrics=middleTextPaint.getFontMetrics();
        baseline=(getCircleHeight()-fontMetrics.bottom-fontMetrics.top)/2+getTopTextSize();
        canvas.drawText(getIndexValue()+"",getCircleWidth()/2,
                baseline,middleTextPaint);


        //顶部文字(etc. 空气污染指数)
        middleTextPaint.setColor(getTopTextColor());
        middleTextPaint.setTextSize(getTopTextSize());
        fontMetrics=middleTextPaint.getFontMetrics();
        baseline=(getCircleHeight()-2*mRadius-fontMetrics.bottom-fontMetrics.top)/2;
        //canvas.drawText(getBottomText(), getCircleWidth() / 2, getCircleHeight() - 50, middleTextPaint);
        //让这个与圆的最顶端间隔20px
        canvas.drawText(getTopText(), getCircleWidth() / 2,baseline ,middleTextPaint);
}

    /**
     *
     * @param value 空气质量数值
     * @param middleText 空气质量（良）
     */
    public void updateIndex(int value,String middleText){

        setMiddleText(middleText);
        invalidate();
        //当前角度
        float inSweepAngle = sweepAngle * value / 500;
        //角度由0f到当前角度变化
        ValueAnimator angleAnim = ValueAnimator.ofFloat(0f, inSweepAngle);
        //动画持续时间
        angleAnim.setDuration(getDuration());
        //数值由0到value变化
        ValueAnimator valueAnim = ValueAnimator.ofInt(0,value);
        valueAnim.setDuration(getDuration());
        //注册监听器
        angleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            //当angleAnim变化回调/
            public void onAnimationUpdate(ValueAnimator valueAnimator)
            {
                float currentValue = (float) valueAnimator.getAnimatedValue();
                //将当前的角度值赋给inSweepAngle
                setInSweepAngle(currentValue);
                //通知view改变，调用这个函数后，会返回到onDraw();
                invalidate();
            }
        });
        valueAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator)
            {
                int currentValue = (int) valueAnimator.getAnimatedValue();
                setIndexValue(currentValue);
                invalidate();
            }
        });
        //让两个动画同时进行。
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.setStartDelay(150);
        animatorSet.playTogether(angleAnim, valueAnim);
        animatorSet.start();

    }
}
