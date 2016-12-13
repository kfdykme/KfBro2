package com.kfdykme.utils;
import android.widget.*;
import android.provider.*;
import android.view.*;
import android.view.View.*;
import com.kfdykme.KfBroV_2.MainActivity;
import android.database.sqlite.*;
public class KfWebSettings
{
	//SQLiteDatabase KfWebSettingsDatabase = openOrCreateDatabase(
	
	static public View settings_AlertDialog_View;
	static public Switch JavaScriptEnabled_Switch;
	static public Switch JavaScriptCanOpenWindowsAutomatically_Switch;
	static public Switch AppCacheEnabled_Switch;
	static public Switch AppCacheMaxSize_Switch;
	static public Switch BuildInZoomControls_Switch;
	static public Switch SavaFormData_Switch;
	static public Switch SavaPassword_Switch;
	public static boolean SETTINGS_JAVASCRIPTENABLED = true;
	public static boolean SETTINGS_JAVASCRIPTCANOPENWINDOWSAUTOMATICALLY = true;
	public static boolean SETTINGS_APPCACHEENABLED = true;
	public static boolean SETTINGS_BUILDINZOOMCONTROLS = true;
	public static boolean SETTINGS_SAVAFORMDATA = true;
	public static boolean SETTINGS_SAVAPASSWORD = true;
	
}
