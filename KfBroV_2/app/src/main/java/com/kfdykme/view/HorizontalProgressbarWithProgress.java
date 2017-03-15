package com.kfdykme.view;
import android.widget.*;
import android.content.*;
import android.util.*;
import java.lang.reflect.*;
import android.graphics.*;
import android.content.res.*;
import com.kfdykme.KfBroV_2.*;

public class HorizontalProgressbarWithProgress extends ProgressBar{
	
	private static final int DEFAULT_TEXT_SIZE = 10; //sp
	private static final int DEFAULT_TEXT_COLOR =0xfffc00d1 ;
	private static final int DEFAULT_COLOR_UNREACH = 0xffD3D4D1;
	private static final int DEFAULT_HEIGHT_UNREACH = 2;//dp
	private static final int DEFAULT_COLOR_REACH = DEFAULT_TEXT_COLOR;
	private static final int DEFAULT_HEIGHT_REACH = 2 ;//dp
	private static final int DEFAULT_TEXT_OFFSET = 10; //dp
	
	private int mTextSize = sp2xp(DEFAULT_TEXT_SIZE);
	private int mTextColor = DEFAULT_TEXT_COLOR;
	private int mTextOffset = dp2xp(DEFAULT_TEXT_OFFSET);
	private int mUnreachHeight = dp2xp(DEFAULT_HEIGHT_UNREACH);
	private int mUnreachColor = DEFAULT_COLOR_UNREACH;
	private int mReachHeight = dp2xp(DEFAULT_HEIGHT_REACH);
	private int mReachColor = DEFAULT_COLOR_REACH;
	
	private Paint mpiant = new Paint();
	
	private int mRealWidth;
	
	public HorizontalProgressbarWithProgress(Context context){
		this(context, null);
	}
	
	
	public HorizontalProgressbarWithProgress(Context context, AttributeSet attrs){
		this(context, attrs,0);
	}
	
	public HorizontalProgressbarWithProgress(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		obtainStyledAttrs(attrs);
	}

	private void obtainStyledAttrs(AttributeSet attrs)
	{
		TypedArray ta = getContext().obtainStyledAttributes(attrs,
			R.styleable.HorizontalProgressbarWithProgress);
			
		mTextSize = (int) ta.getDimension(R.styleable.HorizontalProgressbarWithProgress_progress_text_size,mTextSize);
		
		mTextColor = ta.getColor(R.styleable.HorizontalProgressbarWithProgress_progress_text_color,mTextColor);
		
		mTextOffset = (int) ta.getDimension(R.styleable.HorizontalProgressbarWithProgress_progress_text_offset,mTextOffset);
		
		mUnreachColor = ta.getColor(R.styleable.HorizontalProgressbarWithProgress_progress_unreach_color,mUnreachColor);
		
		mUnreachHeight = (int) ta.getDimension(R.styleable.HorizontalProgressbarWithProgress_progress_unreach_height,mUnreachHeight);
		
		mReachColor = ta.getColor(R.styleable.HorizontalProgressbarWithProgress_progress_reach_color,mReachColor);
		
		mReachHeight = (int) ta.getDimension(R.styleable.HorizontalProgressbarWithProgress_progress_reach_height,mReachHeight);
		
		ta.recycle();
		
		mpiant.setTextSize(mTextSize);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		//int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthVal = MeasureSpec.getSize(widthMeasureSpec);
		int height = measureHeight(heightMeasureSpec);
		setMeasuredDimension(widthVal,height);
		
		mRealWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
		//super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	private int measureHeight(int heightMeasureSpec)
	{
		int result = 0;
		int mode = MeasureSpec.getMode(heightMeasureSpec);
		int size = MeasureSpec.getSize(heightMeasureSpec);
		
		if ( mode == MeasureSpec.EXACTLY){
			result = size;
		} else {
			int textHeight = (int)( mpiant.descent() - mpiant.ascent());
			result = getPaddingTop() +
				getPaddingBottom() +
				Math.max(Math.max(mUnreachHeight,mReachHeight),Math.abs(textHeight));
			
			if ( mode == MeasureSpec.AT_MOST){
				result = Math.min(result,size);
			}
		}
		
		
		return result;
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		canvas.save();
		canvas.translate(getPaddingLeft(),getHeight()/2);
		
		boolean noNeedUnReach = false;
		
		String text = getProgress() + "%";
		int textWidth = (int) mpiant.measureText(text);
		float radio =  getProgress() * 1.0f / getMax();
		float progressX = radio * mRealWidth;
		if ( progressX + textWidth > mRealWidth){
			progressX = mRealWidth - textWidth;
			noNeedUnReach = true;
		}
		
		float endX = progressX - mTextOffset/2;
		
		if (endX > 0){
			mpiant.setColor(mReachColor);
			mpiant.setStrokeWidth(mReachHeight);
			canvas.drawLine(0,0,endX,0,mpiant);
		}
		
		mpiant.setColor(mTextColor);
		int y = (int)(-(mpiant.descent() + mpiant.ascent())/2);
		canvas.drawText(text,progressX,y,mpiant);
		
		if (!noNeedUnReach){
			float start = progressX + mTextOffset/2 + textWidth;
			mpiant.setColor(mUnreachColor);
			mpiant.setStrokeWidth(mUnreachHeight);
			canvas.drawLine(start,0,mRealWidth,0,mpiant);
		}
		
		canvas.restore();
	}
	
	
	
	
	private int dp2xp(int dpVal){
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
		dpVal,getResources().getDisplayMetrics());
	}
	
	private int sp2xp(int spVal){
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
		spVal,getResources().getDisplayMetrics());
	}
	
}
