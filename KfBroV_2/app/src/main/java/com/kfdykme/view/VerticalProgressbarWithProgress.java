package com.kfdykme.view;
import android.widget.*;
import android.content.*;
import java.util.jar.*;
import android.util.*;
import android.graphics.*;
import android.content.res.*;
import com.kfdykme.KfBroV_2.*;
public class VerticalProgressbarWithProgress extends ProgressBar
{
	private static final int DEFAULT_REACH_WIDTH = 10;//dp
	private static final int DEFAULT_REACH_COLOR = 0xe4f8e389;
	private static final int DEFAULT_UNREACH_WIDTH = 10;//dp
	private static final int DEFAULT_UNREACH_COLOR = 0xe44f4f4f;
	private static final int DEFAULT_OFFSET = 10;//dp
	
	private int mReachWidth = dp2xp(DEFAULT_REACH_WIDTH);
	private int mReachColor = DEFAULT_REACH_COLOR;
	private int mUnreachWidth = dp2xp(DEFAULT_UNREACH_WIDTH);
	private int mUnreachColor = DEFAULT_UNREACH_COLOR;
	private int mOffset = dp2xp(DEFAULT_OFFSET);
	
	Paint mpaint = new Paint();
	
	private int mRealHeight;
	
	public VerticalProgressbarWithProgress (Context context){
		this(context,null);
	}
	
	public VerticalProgressbarWithProgress(Context context, AttributeSet attr){
		this(context,attr,0);
	}
	
	public VerticalProgressbarWithProgress(Context context, AttributeSet attr, int defStyle){
		super(context,attr,defStyle);
		obtainStyledAttrs(attr);
	}

	private void obtainStyledAttrs(AttributeSet attr)
	{
		TypedArray ta = getContext().obtainStyledAttributes(attr,R.styleable.VerticalProgressbarWithProgress);
		
		mOffset = (int) ta.getDimension(R.styleable.VerticalProgressbarWithProgress_progress_text_offset,mOffset);
		
		mReachWidth = (int) ta.getDimension(R.styleable.VerticalProgressbarWithProgress_progress_reach_width,mReachWidth);
		
		mReachColor = ta.getColor(R.styleable.VerticalProgressbarWithProgress_progress_reach_color,mReachColor);
		
		mUnreachWidth  = (int) ta.getDimension(R.styleable.VerticalProgressbarWithProgress_progress_unreach_width,mUnreachWidth);
		
		mUnreachColor = ta.getColor(R.styleable.VerticalProgressbarWithProgress_progress_unreach_color,mUnreachColor);
		
		ta.recycle();
		
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		int width = measureWidth(widthMeasureSpec);
		
		int height = MeasureSpec.getSize(heightMeasureSpec);
		setMeasuredDimension(width,height);
		mRealHeight = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
	}

	private int measureWidth(int widthMeasureSpec)
	{
		int result = 0;
		int size = MeasureSpec.getSize(widthMeasureSpec);
		int mode = MeasureSpec.getMode(widthMeasureSpec);
		if ( mode == MeasureSpec.EXACTLY){
			result = size;
		} else {
			result = getPaddingLeft()
			+ getPaddingRight() 
			+ Math.max(mUnreachWidth,mReachWidth);
			
			if ( mode == MeasureSpec.AT_MOST){
				result = Math.min(size,result);
			}
		}
		
		return result;
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		canvas.save();
		
		canvas.translate(getWidth()/2,getPaddingTop());
		
		boolean noNeedUnReach = false;
		
		float radio = getProgress() * 1.0f / getMax();
		float progressY = radio * mRealHeight;
		
		if ( progressY + mOffset> mRealHeight){
			progressY = mRealHeight - mOffset;
			noNeedUnReach = true;
		}
		
		float endY = progressY;
		
		if (endY > 0){
			mpaint.setColor(mReachColor);
			mpaint.setStrokeWidth(mReachWidth);
			canvas.drawLine(0,0,0,endY,mpaint);
		}
		
		
		if (!noNeedUnReach){
			float start = progressY+mOffset;
			mpaint.setColor(mUnreachColor);
			mpaint.setStrokeWidth(mUnreachWidth);
			canvas.drawLine(0,start,0,mRealHeight,mpaint);
		
		}
		canvas.restore();
	}
	
	
	
	
	
	
	
	private int dp2xp(int dpVal){
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dpVal,getResources().getDisplayMetrics());
	}
}
