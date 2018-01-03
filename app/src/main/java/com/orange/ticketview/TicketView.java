package com.orange.ticketview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * created by czh on 2018-01-02
 */

public class TicketView extends View{

    private int mWidth;
    private int mHeight;

    private int mBallColor;
    private int mBallRadius;

    private int mLineColor;
    private int mLineHeight;
    private int mDashGap;
    private int mDashWidth;
    private LineType mLineType;

    private int mDefaultColor= Color.parseColor("#c4c4c4");

    public enum LineType {
        POINT,
        RECTANGLE
    }

    private static final LineType[] sLineTypeArray = {
            LineType.POINT,
            LineType.RECTANGLE
    };

    public TicketView(Context context) {
        this(context,null);
    }

    public TicketView(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public TicketView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.TicketView);
        mBallColor=typedArray.getColor(R.styleable.TicketView_ballColor,mDefaultColor);
        mBallRadius =typedArray.getDimensionPixelSize(R.styleable.TicketView_ballRadius,20);

        mLineColor=typedArray.getColor(R.styleable.TicketView_lineColor,mDefaultColor);
        mDashGap=typedArray.getDimensionPixelOffset(R.styleable.TicketView_dashGap,18);
        mDashWidth=typedArray.getDimensionPixelOffset(R.styleable.TicketView_dashWidth,3);
        mLineHeight=typedArray.getDimensionPixelOffset(R.styleable.TicketView_lineHeight,3);
        final int index =typedArray.getInt(R.styleable.TicketView_lineType,0);
        if (index>=0){
            mLineType=sLineTypeArray[index];
        }else {
            mLineType= LineType.POINT;
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawLCircle(canvas);
        drawDashline(canvas);
        drawRCircle(canvas);
    }

    private void drawLCircle(Canvas canvas){
        Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(mBallColor);
        paint.setStyle(Paint.Style.FILL);
        RectF rectF=new RectF(-mBallRadius,0,mBallRadius,mBallRadius*2);
        canvas.drawArc(rectF,-90,180,true,paint);
    }

    private void drawDashline(Canvas canvas){
        Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(mLineColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(mLineHeight);

        if (LineType.POINT ==mLineType){
            //圆点虚线
            Path path = new Path();
            path.addCircle(0, 0, mDashWidth, Path.Direction.CW);
            paint.setPathEffect(new PathDashPathEffect(path, mDashGap, 0, PathDashPathEffect.Style.ROTATE));
        }else if (LineType.RECTANGLE ==mLineType){
            //矩形虚线
            paint.setPathEffect(new DashPathEffect(new float[] {mDashWidth, mDashGap}, 0));
        }

        Path linePath=new Path();
        linePath.moveTo(mBallRadius*2,mBallRadius);
        linePath.lineTo(mWidth-mBallRadius*2,mBallRadius);
        canvas.drawPath(linePath,paint);
    }

    private void drawRCircle(Canvas canvas){
        Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(mBallColor);
        paint.setStyle(Paint.Style.FILL);
        RectF rectF=new RectF(mWidth-mBallRadius,0,mWidth+mBallRadius,mBallRadius*2);
        canvas.drawArc(rectF,-270,180,true,paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        mWidth=widthSize;

        mHeight=mBallRadius*2;
//        if (heightMode!=MeasureSpec.EXACTLY){
//            heightSize=mBallRadius*2;
//        }
        setMeasuredDimension(widthSize,mHeight);
    }
}
