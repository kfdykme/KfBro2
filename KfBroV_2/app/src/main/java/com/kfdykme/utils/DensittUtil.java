package com.kfdykme.utils;

import android.content.Context;
import android.util.*;

public class DensittUtil
{
	public static int px2dp(Context context, float pxValue){
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	
	public static int dp2px(int dpVal,Context context){
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dpVal,context.getResources().getDisplayMetrics());
	}
}
