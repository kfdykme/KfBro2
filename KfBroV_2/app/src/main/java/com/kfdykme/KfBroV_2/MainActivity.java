package com.kfdykme.KfBroV_2;

import android.app.*;
import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.webkit.*;
import android.widget.*;
import com.kfdykme.utils.*;
import java.text.*;
import android.util.*;
import android.provider.*;
import android.widget.RelativeLayout.*;
import android.media.*;

public class MainActivity extends Activity 
{
	
	public Boolean isOpenAddressEdit;
	public Boolean isFristGoBack;
	public Button goBack_Button;
	public Button goForward_Button;
	public Button l_Button;
	public Button toHistory_Button;
	public Button toBookmark_Button;
	public Button toExit_Button;
	public Button addToBookmark_Button;
	public Button home_Button;
	public Button saveHomeUrl_Button;
	public Button settings_Button;
	public Button cancelEditHomeUrl_Button; 
	public ContentValues bookContentValue;
	public Dialog homeEdit_AlertDialog;
	public Dialog menu_AlertDialog;
	public Dialog settings_AlertDialog;
	public EditText homeUrl_EditText;
	public EditText address_EditText;
	public LinearLayout address_EditText_LinearLayout;
	public String lastLoadedUrl;
	public String homeUrl;
	public SQLiteDatabase booDatabase ;
	public SQLiteDatabase lastLoadedDatabase;
	public View menu_AlertDialog_View;
	public View settings_AlertDialog_View;
	public View homeEdit_AlertDialog_View;
	public WebView webview;
	public Window menu_AlertDialog_Window;
	public Window settings_AlertDialog_Window;
	public Window homeEdit_AlertDialog_Window;
	public WindowManager.LayoutParams menu_AlertDialog_Window_Params ;
	public WindowManager.LayoutParams settings_AlertDialog_Window_Params;
	public WindowManager.LayoutParams homeEdit_AlertDialog_Window_Params;
	
	
	public void addToBM(){
		booDatabase= openOrCreateDatabase("bookmark.db",MODE_PRIVATE,null);
		Cursor checkBM = booDatabase.query("bootb",null,"_id>?",new String[]{"0"},null,null,null);
		ContentValues cVal = new ContentValues();
		
		cVal.put("webtitle",webview.getTitle());

		cVal.put("weburl",webview.getUrl());


		if (checkBM != null){
			String[] checkBM_columns = checkBM.getColumnNames();
			while(checkBM.moveToNext()){
				for (String c:checkBM_columns){
					if (webview.getUrl().toString().contains(checkBM.getString(checkBM.getColumnIndex(c)))){
						
						Toast.makeText(getApplicationContext(),"It has been added to bookmark",Toast.LENGTH_SHORT).show();				
						cVal.clear();
						return;
					}
				}
			}
		}


		booDatabase.insert("bootb",null,cVal);
		Toast.makeText(MainActivity.this,"Added to bookmark successfully!",Toast.LENGTH_SHORT).show();
		cVal.clear();

		booDatabase.close();
	}
	

	
	public void closeAddressEdit(){
		LinearLayout.LayoutParams closeEditParams = (LinearLayout.LayoutParams) address_EditText_LinearLayout.getLayoutParams();
		closeEditParams.width = Constant.ADDRESS_EDIT_CLOSE;
		address_EditText.setAlpha(0f);
		address_EditText_LinearLayout.setLayoutParams(closeEditParams);
		isOpenAddressEdit = false;
		address_EditText.setText(webview.getUrl().toString());
		isFristGoBack = true;
	}
	
	public void creteHistory(String url){
		
		SQLiteDatabase hisDatabase =  openOrCreateDatabase("history.db",MODE_PRIVATE,null);
		ContentValues cVal = new ContentValues();
		hisDatabase.execSQL("create table if not exists histb(_id integer primary key autoincrement,webtitle text not null,weburl text not null,loadingtime text not null)");
		
		if(url != lastLoadedUrl){
		cVal.put("webtitle",webview.getTitle().toString());
		cVal.put("weburl",webview.getUrl().toString());
		cVal.put("loadingtime",getTime());
		hisDatabase.insert("histb",null,cVal);
		lastLoadedUrl = webview.getUrl().toString();
		Log.i("info","creat");
		}
		
		cVal.clear();

		hisDatabase.close();

		
	}
	
	public void createLastLoadedUrl(){
		lastLoadedDatabase = openOrCreateDatabase(Constant.LAST_LOADED_DATABASE_NAME,MODE_PRIVATE,null);
		lastLoadedDatabase.execSQL("create table if not exists " + Constant.LAST_LOADED_TABLE_NAME+"("+Constant.LAST_LOADED_ID+" integer primary key autoincrement," + Constant.LAST_LOADED_URL +" text not null )");
		ContentValues lastLoadedContentValues = new ContentValues();
		lastLoadedDatabase.delete(Constant.LAST_LOADED_TABLE_NAME,Constant.LAST_LOADED_ID +" > ?",new String[]{"0"});
		if (webview.getUrl() != null){
			lastLoadedContentValues.put(Constant.LAST_LOADED_URL,webview.getUrl().toString());
			Log.i("lastLoaded",webview.getUrl().toString());
		} else {
			lastLoadedContentValues.put(Constant.LAST_LOADED_URL,Constant.LAST_LOADED_URL_NULL);
			Log.i("lastLoaded","null");
		}
		lastLoadedDatabase.insert(Constant.LAST_LOADED_TABLE_NAME,null,lastLoadedContentValues);
		lastLoadedDatabase.close();
		lastLoadedContentValues.clear();
	}
	
	public void doVisitWebsite(String URL){
		URL = URL.toLowerCase();
		if(URL.contains("http://")){}else if(URL.contains("www.")){
			URL = "http://" + URL;
		}else{
			URL = "http://www."+URL;
		}
		webview.loadUrl(URL);
	}
	
	public void findView(){
		address_EditText = (EditText) findViewById(R.id.addressEditText);
		address_EditText_LinearLayout = (LinearLayout) findViewById(R.id.addressEditText_LinerLayout);
		l_Button = (Button) findViewById(R.id.L_Button);
		webview = (WebView) findViewById(R.id.webView);
		findViewInMenuLayout();
		findViewInHomeUrlEditLayout();
	}
	
	public void findViewInHomeUrlEditLayout(){
		homeUrl_EditText = (EditText) homeEdit_AlertDialog_View.findViewById(R.id.edithomeurl_EditText);
		saveHomeUrl_Button = (Button) homeEdit_AlertDialog_View.findViewById(R.id.saveHomeurl_Button);
		
		cancelEditHomeUrl_Button = (Button) homeEdit_AlertDialog_View.findViewById(R.id.cancelEditHomeurl_Button);
		
		
	}
	
	public void findViewInMenuLayout(){
		addToBookmark_Button = (Button)menu_AlertDialog_View.findViewById(R.id.addToBookmark_dialogButton);
		goBack_Button = (Button) menu_AlertDialog_View.findViewById(R.id.goBack_dialogButton);
		goForward_Button = (Button)menu_AlertDialog_View.findViewById(R.id.goForward_dialogButton);
		home_Button = (Button) menu_AlertDialog_View.findViewById(R.id.home_dialogButton);
		settings_Button = (Button) menu_AlertDialog_View.findViewById(R.id.toSetting_dialogButton);
		toBookmark_Button = (Button)menu_AlertDialog_View.findViewById(R.id.toBookmark_dialogButton);
		toExit_Button = (Button)menu_AlertDialog_View.findViewById(R.id.toExit_dialogButton);
		toHistory_Button = (Button)menu_AlertDialog_View.findViewById(R.id.toHistory_dialogButton);
		
	}
	
	public String getTime(){

		SimpleDateFormat sDFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");

		String tData = sDFormat.format(new java.util.Date());

		return tData;
	}
	



	public void homeEditDialogInistial(){
		homeEdit_AlertDialog_View = View.inflate(MainActivity.this,R.layout.edithomeurl_layout,null);
		homeEdit_AlertDialog = new Dialog(MainActivity.this,R.style.AppTheme); 
		homeEdit_AlertDialog.setCanceledOnTouchOutside(true);
        homeEdit_AlertDialog_Window = homeEdit_AlertDialog.getWindow();
		homeEdit_AlertDialog_Window.requestFeature(Window.FEATURE_NO_TITLE);
		homeEdit_AlertDialog.setContentView(homeEdit_AlertDialog_View);
		homeEdit_AlertDialog_Window_Params = homeEdit_AlertDialog_Window.getAttributes();
		homeEdit_AlertDialog_Window.setGravity(Gravity.RIGHT);
		homeEdit_AlertDialog_Window_Params.width = DensittUtil.dp2px(250,MainActivity.this);
		homeEdit_AlertDialog_Window_Params.height = DensittUtil.dp2px(162,MainActivity.this);
		homeEdit_AlertDialog_Window_Params.alpha = 0.69f;
		homeEdit_AlertDialog_Window_Params.x= 100;
		homeEdit_AlertDialog_Window_Params.y = 120;
		homeEdit_AlertDialog_Window.setAttributes(homeEdit_AlertDialog_Window_Params);
	}

	public void inistial(){
		
		isFristGoBack = true;
		isOpenAddressEdit = false;
		menuInitial();
		homeEditDialogInistial();
		settingInitial();
		homeUrl = Constant.FIRST_HOME_URL;
		
		findView();
		setListener();
		webInistial();
		
		booDatabase =  openOrCreateDatabase("bookmark.db",MODE_PRIVATE,null);

		bookContentValue = new ContentValues();

		booDatabase.execSQL("create table if not exists bootb(_id integer primary key autoincrement,webtitle text not null,weburl text not null)");
		
		
	}
	
	public class kfbroOnClickListener implements OnClickListener
	{

		@Override
		public void onClick(View view)
		{

			switch(view.getId()){
				case R.id.addToBookmark_dialogButton:
					addToBM();
					break;
				case R.id.goBack_dialogButton:
					if (webview.canGoBack()){
						webview.goBack();
					}else if (isFristGoBack){
						isFristGoBack = false;
						address_EditText.setText("again to leave");
					} else {
						finish();
					}
					break;
				case R.id.goForward_dialogButton:
					if(webview.canGoForward()){
						webview.goForward();
					}
					break;
				case R.id.home_dialogButton:
					doVisitWebsite(homeUrl);
					break;
				case R.id.L_Button:
					if (isOpenAddressEdit){
						closeAddressEdit();
					}
					else{
						openAddressEdit();
					}
					break;
				case R.id.toExit_dialogButton:
					menu_AlertDialog.dismiss();
					System.exit(0);
					break;
				case R.id.toBookmark_dialogButton:
					Intent inteb = new Intent(MainActivity.this,BookmarkActivity.class);
					startActivityForResult(inteb,2);
					break;
				case R.id.toHistory_dialogButton:
					Intent inte = new Intent(MainActivity.this,HistoryActivity.class);
					startActivityForResult(inte,1);
					break;
				case R.id.addressEditText:
					break;
				case R.id.saveHomeurl_Button:
					saveHomeUrl();
					break;
				case R.id.toSetting_dialogButton:
					settings_AlertDialog.show();
					break;
				case R.id.cancelEditHomeurl_Button:
					homeEdit_AlertDialog.dismiss();
					break;
				default:
					closeAddressEdit();
					break;
			}
		}
	}
	
	
	public class kfbroOnLongClickListener implements OnLongClickListener
	{

		@Override
		public boolean onLongClick(View view)
		{
			switch(view.getId()){
				case R.id.addressEditText:
					address_EditText.setText("");
					break;
				case R.id.home_dialogButton:
					menu_AlertDialog.dismiss();
					homeEdit_AlertDialog.show();
					break;
				case R.id.L_Button:
					homeUrl_EditText.setText(homeUrl);
					
					menu_AlertDialog.show();
					break;
			}
			
			// TODO: Implement this method
			return false;
		}
	}
	
	public class kfbroOnKeyListener implements OnKeyListener
	{

		@Override
		public boolean onKey(View ViewStub, int keyCode, KeyEvent event)
		{
			switch(keyCode){
				case event.KEYCODE_ENTER:
					doVisitWebsite(address_EditText.getText().toString());
					break;
				case event.KEYCODE_BACK:
					if (isFristGoBack){
						isFristGoBack = false;
						Toast.makeText(MainActivity.this,"again to leave",Toast.LENGTH_SHORT);
					} else {
						finish();
					}
					break;
			}
			// TODO: Implement this method
			return false;
		}
	}
	
	public void loadHomeUrl(){
		SQLiteDatabase homeUrlDatabase = openOrCreateDatabase(Constant.HOME_DATABASE_NAME,MODE_PRIVATE,null);
		homeUrlDatabase.execSQL("create table if not exists " + Constant.HOME_TABLE_NAME+"("+Constant.HOME_ID+" integer primary key autoincrement," + Constant.HOME_WEB_URL +" text not null )");

		Cursor homeUrlCursor = homeUrlDatabase.query(Constant.HOME_TABLE_NAME,null,Constant.HOME_ID+ " > ?",new String[]{"0"},null,null,null);

		if (homeUrlCursor != null){
			String[] cColumns =  homeUrlCursor.getColumnNames();
			while (homeUrlCursor.moveToNext()){
				for(String cColumn:cColumns){
					switch(cColumn){
						case Constant.HOME_WEB_URL:
							homeUrl = homeUrlCursor.getString(homeUrlCursor.getColumnIndex(Constant.HOME_WEB_URL));
							break;
					}

				}
			}
		} else {
			homeUrl = Constant.FIRST_HOME_URL;
		}
	}
	
	public void loadLastUrl(){
		lastLoadedDatabase = openOrCreateDatabase(Constant.LAST_LOADED_DATABASE_NAME,MODE_PRIVATE,null);
		lastLoadedDatabase.execSQL("create table if not exists " + Constant.LAST_LOADED_TABLE_NAME+"("+Constant.LAST_LOADED_ID+" integer primary key autoincrement," + Constant.LAST_LOADED_URL +" text not null )");

		
		Cursor lastLoadedCursor = lastLoadedDatabase.query(Constant.LAST_LOADED_TABLE_NAME,null,Constant.LAST_LOADED_ID + " > ?",new String[]{"0"},null,null,null);

		if (lastLoadedCursor != null){
			String[] cColumns =  lastLoadedCursor.getColumnNames();
		while (lastLoadedCursor.moveToNext()){
			for(String cColumn:cColumns){
				switch(cColumn){
					case Constant.LAST_LOADED_URL:
						lastLoadedUrl = lastLoadedCursor.getString(lastLoadedCursor.getColumnIndex(Constant.LAST_LOADED_URL));
						break;
						}

			}
		}
		}
	}
	
	public void menuInitial(){
		menu_AlertDialog_View = View.inflate(MainActivity.this,R.layout.menu_dialog,null);
		menu_AlertDialog = new Dialog(MainActivity.this,R.style.AppTheme); 
		menu_AlertDialog.setCanceledOnTouchOutside(true);
		
		menu_AlertDialog_Window = menu_AlertDialog.getWindow();
		menu_AlertDialog_Window.requestFeature(Window.FEATURE_NO_TITLE);
		menu_AlertDialog.setContentView(menu_AlertDialog_View);
		
		menu_AlertDialog_Window_Params = menu_AlertDialog_Window.getAttributes();
		menu_AlertDialog_Window.setGravity(Gravity.RIGHT);
		menu_AlertDialog_Window_Params.width = DensittUtil.dp2px(180,MainActivity.this);
		menu_AlertDialog_Window_Params.height = DensittUtil.dp2px(420,MainActivity.this);
		menu_AlertDialog_Window_Params.alpha = 0.69f;
		menu_AlertDialog_Window_Params.x= 60;
		menu_AlertDialog_Window_Params.y = 120;
		menu_AlertDialog_Window.setAttributes(menu_AlertDialog_Window_Params);
		}
	
	public void settingInitial(){
		settings_AlertDialog_View = View.inflate(MainActivity.this,R.layout.settings_dialog,null);
		settings_AlertDialog = new Dialog(MainActivity.this,R.style.AppTheme);
		settings_AlertDialog.setCanceledOnTouchOutside(true);
		
		
		settings_AlertDialog_Window = settings_AlertDialog.getWindow();
		settings_AlertDialog_Window.requestFeature(Window.FEATURE_NO_TITLE);
		settings_AlertDialog.setContentView(settings_AlertDialog_View);
		
		settings_AlertDialog_Window_Params = settings_AlertDialog_Window.getAttributes();
		settings_AlertDialog_Window.setGravity(Gravity.RIGHT);
		settings_AlertDialog_Window_Params.width = DensittUtil.dp2px(300,MainActivity.this);
		settings_AlertDialog_Window_Params.height = DensittUtil.dp2px(300,MainActivity.this);
		settings_AlertDialog_Window_Params.x = DensittUtil.dp2px(30,MainActivity.this);
		settings_AlertDialog_Window_Params.y = DensittUtil.dp2px(30,MainActivity.this);
		settings_AlertDialog_Window.setAttributes(settings_AlertDialog_Window_Params);
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(requestCode == 1&&resultCode == RESULT_OK){

			webview.loadUrl(data.getStringExtra("URL"));

			address_EditText.setText(data.getStringExtra("URL"));
		}else if (requestCode == 2 && resultCode == RESULT_OK){

			webview.loadUrl(data.getStringExtra("URL"));

			address_EditText.setText(data.getStringExtra("URL"));
		}

		// TODO: Implement this method
		super.onActivityResult(requestCode, resultCode, data);
	};

	
		
	
	@Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
		inistial();

    }

	@Override
	protected void onDestroy()
	{
		// TODO: Implement this method
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){


		switch(keyCode){
			case event.KEYCODE_ENTER:
				doVisitWebsite(address_EditText.getText().toString());
				break;
			case event.KEYCODE_BACK:
				if (webview.canGoBack()){
					webview.goBack();
				}else if (isFristGoBack){
					isFristGoBack = false;
					address_EditText.setText("again to leave");
					Toast.makeText(getApplicationContext(),"again to leave",Toast.LENGTH_SHORT).show();
				} else {
					finish();
				}
				break;
		}

		return true;
	}
	
	@Override
	protected void onPause()
	{
		// TODO: Implement this method
		super.onPause();
	}
	
	@Override
	protected void onRestart()
	{
		// TODO: Implement this method
		super.onRestart();
	}

	@Override
	protected void onResume()
	{
		// TODO: Implement this method
		super.onResume();
	}
	
	@Override
	protected void onStop()
	{
		createLastLoadedUrl();
		// TODO: Implement this method
		super.onStop();
	}


		
	public void openAddressEdit(){
		LinearLayout.LayoutParams openEditParams = (LinearLayout.LayoutParams) address_EditText_LinearLayout.getLayoutParams();
		openEditParams.width = Constant.ADDRESS_EDIT_OPEN;
		address_EditText.setAlpha(1f);
		address_EditText_LinearLayout.setLayoutParams(openEditParams);
		isOpenAddressEdit = true;
		
	}
	
	
	public void saveHomeUrl(){
		SQLiteDatabase homeUrlDatabase = openOrCreateDatabase(Constant.HOME_DATABASE_NAME,MODE_PRIVATE,null);
		ContentValues homeUrlContentValues = new ContentValues();
		homeUrlDatabase.execSQL("create table if not exists "+ Constant.HOME_TABLE_NAME+"("+Constant.HOME_ID +" integer primary key autoincrement," + Constant.HOME_WEB_URL+ " text not null)");
		homeUrlDatabase.delete(Constant.HOME_TABLE_NAME,Constant.HOME_ID + " > ?",new String[]{"0"});
		homeUrlContentValues.put(Constant.HOME_WEB_URL,homeUrl_EditText.getText().toString());
		homeUrlDatabase.insert(Constant.HOME_TABLE_NAME,null,homeUrlContentValues);
		homeUrlContentValues.clear();
		homeUrlDatabase.close();
		homeUrl = homeUrl_EditText.getText().toString();
		Toast.makeText(MainActivity.this, homeUrl+"has saved as home url.",Toast.LENGTH_LONG).show();
		Log.i("info","saveHomeUrl()");
	}
	
	
	public void setListener(){
		
		addToBookmark_Button.setOnClickListener(new kfbroOnClickListener());
		cancelEditHomeUrl_Button.setOnClickListener(new kfbroOnClickListener());
		saveHomeUrl_Button.setOnClickListener(new kfbroOnClickListener());
		l_Button.setOnClickListener(new kfbroOnClickListener());
		goBack_Button.setOnClickListener(new kfbroOnClickListener());
		goForward_Button.setOnClickListener(new kfbroOnClickListener());
		home_Button.setOnClickListener(new kfbroOnClickListener());
		settings_Button.setOnClickListener(new kfbroOnClickListener());
		toExit_Button.setOnClickListener(new kfbroOnClickListener());
		toBookmark_Button.setOnClickListener(new kfbroOnClickListener());
		toHistory_Button.setOnClickListener(new kfbroOnClickListener());
		
		address_EditText.setOnKeyListener(new kfbroOnKeyListener());
		
		address_EditText.setOnLongClickListener(new kfbroOnLongClickListener());
		
		l_Button.setOnLongClickListener(new kfbroOnLongClickListener());
		home_Button.setOnLongClickListener(new kfbroOnLongClickListener());
		
	}
	
	public void webInistial(){
		WebSettings WS = webview.getSettings();
		
		WS.setJavaScriptEnabled(true);
		WS.setJavaScriptCanOpenWindowsAutomatically(true);
		WS.setAppCacheEnabled(true);
		WS.setAppCacheMaxSize(20480);
		WS.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		WS.setBuiltInZoomControls(true);
		WS.setSavePassword(true);
		WS.setSaveFormData(true);
		
		webview.setWebChromeClient(webChrome);
		webview.setWebViewClient(webViewClient);
		loadLastUrl();
		loadHomeUrl();
		if (lastLoadedUrl == Constant.LAST_LOADED_URL_NULL){
			Log.i("info","llurl == null");
			webview.loadUrl(homeUrl);
		} else {
			Log.i("info","llurl != null");
			webview.loadUrl(lastLoadedUrl);
			}
		//webview.loadUrl(lastLoadedUrl);
		
	}
	
	public WebChromeClient webChrome = new WebChromeClient(){

		@Override
		public void onProgressChanged(WebView view, int newProgress)
		{
			if(newProgress == 100){
				creteHistory(address_EditText.getText().toString());
				
			}	
			
			// TODO: Implement this method
			super.onProgressChanged(view, newProgress);
		}
	};
	
	public WebViewClient webViewClient = new WebViewClient(){
		@Override
		public boolean shouldOverrideUrlLoading(WebView view,String url){
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon)
		{
			address_EditText.setText(url);
			// TODO: Implement this method
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onPageFinished(WebView view, String url)
		{
			closeAddressEdit();
			
			// TODO: Implement this method
			super.onPageFinished(view, url);
		}		
		
	};
	
	}
