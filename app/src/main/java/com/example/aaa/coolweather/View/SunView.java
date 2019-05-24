package com.example.aaa.coolweather.View;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

import com.example.aaa.coolweather.R;
import com.example.aaa.coolweather.util.Utility;

/**
 * Created by aaa on 2019/5/13.
 */

public class SunView extends View {
    private int mWidth; //屏幕宽度
    //当太阳在最高点时不会有部分消失
    private int marginTop = 30;//离顶部的高度
    private int mCircleColor;  //圆弧颜色
    private int mFontColor;  //字体颜色
    private int mRadius;  //圆的半径

    private float mCurrentAngle; //当前旋转的角度
    private float mTotalMinute; //总时间(日落时间减去日出时间的总分钟数)
    private float mNeedMinute; //当前时间减去日出时间后的总分钟数
    private float mPercentage; //根据所给的时间算出来的百分占比
    private float positionX, positionY; //太阳图片的x、y坐标
    private float mFontSize;  //字体颜色

    private String mStartTime; //开始时间(日出时间)
    private String mEndTime; //结束时间（日落时间）
    private String mCurrentTime; //当前时间

    private Paint mPaint; //画笔
    private RectF mRectF; //半圆弧所在的矩形
    private Bitmap mSunIcon; //太阳图片
    private WindowManager wm;
    public SunView(Context context){
        this(context,null);
    }
    public SunView(Context context, @Nullable AttributeSet attrs){
        this(context,attrs,0);
    }
    public SunView(Context context, @Nullable AttributeSet attrs, int defstyleAttr){
        super(context,attrs,defstyleAttr);
        initView(context,attrs);
    }
    /**
     * 初始化view，加载进属性
     */
    public void initView(Context context, @Nullable AttributeSet attrs){
        TypedArray ta=context.obtainStyledAttributes(attrs, R.styleable.SunView);
        mCircleColor=ta.getColor(R.styleable.SunView_sun_circle_color,getResources().getColor(R.color.colorAccent));
        mFontColor=ta.getColor(R.styleable.SunView_sun_font_color, Color.BLACK);
        mFontSize= Utility.dp2px(getContext(),ta.getDimension(R.styleable.SunView_sun_font_size,12));
        mRadius=Utility.dp2px(getContext(),ta.getInteger(R.styleable.SunView_sun_circle_radius,150));
        ta.recycle();
        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);

        //这是一个合适的图片
        Bitmap requirebitmap=BitmapFactory.decodeResource(getResources(),R.mipmap.sun);
        //我们要缩放到跟这个图片相同大小
        mSunIcon= zoomImage(BitmapFactory.decodeResource(getResources(),R.mipmap.sunrise),
                requirebitmap.getWidth()*2,requirebitmap.getHeight()*2);

    }
    /***
     * 图片的缩放方法
     *
     * @param bgimage
     *            ：源图片资源
     * @param newWidth
     *            ：缩放后宽度
     * @param newHeight
     *            ：缩放后高度
     * @return
     */
    public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
                                   double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }
    @Override
    protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec){
        //wm=(WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
        mWidth= MeasureSpec.getSize(widthMeasureSpec);
        positionX=mWidth/2-mRadius-mSunIcon.getWidth()/2;//太阳图片的初始x坐标
        positionY=marginTop+mRadius-mSunIcon.getHeight()/2;
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
    }
    @Override
    protected void onLayout(boolean changed,int left,int top,int right,int bottom){
        //绘制view的位置
        super.onLayout(changed,mWidth/2-mRadius,marginTop,mWidth/2+mRadius,mRadius * 2 + marginTop);
    }
    @Override
    protected void onDraw(Canvas canvas){
        //画半圆
        drawSemicircle(canvas);
        canvas.save();
        //绘制太阳的位置
        drawSunPosition(canvas);
        //绘制文字的位置
        drawFont(canvas);
        super.onDraw(canvas);
    }

    /**
     * 绘制半圆
     * @param canvas
     */
    public  void drawSemicircle(Canvas canvas){
        //左上右下
        mRectF=new RectF(mWidth/2-mRadius,marginTop,mWidth/2+mRadius,mRadius*2+marginTop);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setDither(true);//防止抖动
        mPaint.setColor(mCircleColor);

        canvas.drawArc(mRectF,180,180,true,mPaint);
    }

    /**
     * 绘制太阳位置
     * @param canvas
     */
    public void drawSunPosition(Canvas canvas){
        canvas.drawBitmap(mSunIcon,positionX,positionY,mPaint);
    }

    /**
     * 绘制左右下角位置
     * @param canvas
     */
    public void drawFont(Canvas canvas){

        mPaint.setColor(mFontColor);
        mPaint.setTextSize(mFontSize);
        String startTime = TextUtils.isEmpty(mStartTime)?"":mStartTime;
        String endTime = TextUtils.isEmpty(mEndTime)?"":mEndTime;
        String sunrise="日出时间:"+startTime;
        String sunset="日落时间:"+endTime;
        //测量文字宽度的方法也可以使用
        //float width = paint.measureText(string);
        canvas.drawText(sunrise,mWidth/2-mRadius-mPaint.measureText(sunrise)/2,mRadius+50+marginTop,mPaint);
        //canvas.drawText(sunrise,mWidth/2-mRadius-getTextWidth(mPaint,sunrise)/2,mRadius+50+marginTop+40,mPaint);
        canvas.drawText(sunset,mWidth/2+mRadius-mPaint.measureText(sunset)/2,mRadius+50+marginTop,mPaint);
    }
    /**
     * 新的计算文字宽度方法
     *
     */
    public static int newgetTextWidth(Paint paint, String str){
        Rect bounds = new Rect();
        paint.getTextBounds(str, 0, str.length(), bounds);
        int height = bounds.height();
        int width = bounds.width();
        return width;
    }
    /**
     * 精确计算文字宽度
     *
     * @param paint 画笔
     * @param str   字符串文本
     * @return
     */
    public static int getTextWidth(Paint paint, String str)
    {
        int iRet = 0;
        if (str != null && str.length() > 0)
        {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++)
            {
                iRet += (int) Math.ceil(widths[j]);
            }
        }
        return iRet;
    }
    /**
     * 设置时间
     */
    public void setTimes(String startTime, String endTime, String currentTime)
    {
        mStartTime = startTime;
        mEndTime = endTime;
        mCurrentTime = currentTime;

        mTotalMinute = calculateTime(mStartTime, mEndTime);//计算总时间，单位：分钟
        mNeedMinute = calculateTime(mStartTime, mCurrentTime);//计算当前所给的时间 单位：分钟
        mPercentage = formatTime(mTotalMinute, mNeedMinute);//当前时间的总分钟数占日出日落总分钟数的百分比

        mCurrentAngle =  (180 * mPercentage);

        setAnimation(0, mCurrentAngle, 5000);

    }

    private boolean checkTime(String startTime, String endTime){


        if(TextUtils.isEmpty(startTime) || TextUtils.isEmpty(endTime)
                || !startTime.contains(":") || !endTime.contains(":"))
        {
            return false;
        }
        String startTimes[]=startTime.split(":");
        String endTimes[]=endTime.split(":");
        float startHours= Float.parseFloat(startTimes[0]);
        float endHours= Float.parseFloat(endTimes[0]);
        float starttimes= Float.parseFloat(startTimes[1]);
        float endtimes= Float.parseFloat(endTimes[1]);
        if (startHours>endHours)
            return  false;
        if (startHours < 0 || endHours < 0
                || startHours > 23 || endHours > 23
                || starttimes < 0 || endtimes < 0
                || starttimes > 60 || endtimes > 60)
        {
            return false;
        }
        //如果所给的时间(hour)小于日出时间（hour）或者大于日落时间（hour）
        //比如checkTime(mStartTime, mCurrentTime),mCurrentTime比日落时间还长
        //那么就要返回false了。在calculateTime()中就返回0，也就是needtime=0。
        if ((startHours < Float.parseFloat(mStartTime.split(":")[0]))
                || (endHours > Float.parseFloat(mEndTime.split(":")[0])))
        {
            return false;
        }

        //如果所给时间与日出时间：hour相等，minute小于日出时间
        if ((startHours == Float.parseFloat(mStartTime.split(":")[0]))
                && (starttimes < Float.parseFloat(mStartTime.split(":")[1])))
        {
            return false;
        }

        //如果所给时间与日落时间：hour相等，minute大于日落时间
        if ((startHours == Float.parseFloat(mEndTime.split(":")[0]))
                && (endtimes > Float.parseFloat(mEndTime.split(":")[1])))
        {
            return false;
        }
        return true;

    }
    /**
     * 根据日出和日落时间计算出一天总共的时间:单位为分钟
     *
     * @param startTime 日出时间
     * @param endTime   日落时间
     * @return
     */
    private float calculateTime(String startTime, String endTime)
    {

        if (checkTime(startTime, endTime))
        {
            String startTimes[] = startTime.split(":");
            String endTimes[] = endTime.split(":");

            float startHour = Float.parseFloat(startTimes[0]);
            float startMinute = Float.parseFloat(startTimes[1]);

            float endHour = Float.parseFloat(endTimes[0]);
            float endMinute = Float.parseFloat(endTimes[1]);

            float needTime = (endHour - startHour - 1) * 60 + (60 - startMinute) + endMinute;
            return needTime;
        }
        return 0;
    }

    /**
     * 根据具体的时间、日出日落的时间差值 计算出所给时间的百分占比
     *
     * @param totalTime 日出日落的总时间差
     * @param needTime  当前时间与日出时间差
     * @return
     */
    private float formatTime(float totalTime, float needTime)
    {
        if (totalTime == 0)
            return 0;
        if (needTime==0)
            return 1;
        float percent=needTime/totalTime;
        return percent;
    }

    /**
     * 设置动画
     * @param startAngle
     * @param currentAngle
     * @param duration
     */
    private void setAnimation(float startAngle,float currentAngle,int duration){
        ValueAnimator sunAnimator= ValueAnimator.ofFloat(startAngle,currentAngle);
        sunAnimator.setDuration(duration);
        sunAnimator.setTarget(currentAngle);
        sunAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurrentAngle=(float)animation.getAnimatedValue();
                invalidateView();
            }
        });
        //千万别忘了这个
        sunAnimator.start();
    }
    private void invalidateView(){
        //绘制太阳的x坐标与y坐标
        //记得要减去太阳图标的大小。（让太阳始终在半圆上）
        positionX=mWidth/2-(float)(mRadius*Math.cos((mCurrentAngle)*Math.PI/180))-mSunIcon.getWidth()/2;
        positionY=marginTop+mRadius-(float)(mRadius*Math.sin((mCurrentAngle)*Math.PI/180))-mSunIcon.getHeight()/2;
        invalidate();
    }
}
