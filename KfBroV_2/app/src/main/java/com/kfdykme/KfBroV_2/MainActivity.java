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
import android.net.*;
import android.print.*;
import com.kfdykme.utils.KfWebSettings;
import android.widget.CompoundButton.*;
import android.text.*;
import android.view.animation.*;
import android.animation.*;
import java.util.*;
import android.view.WindowManager.*;

public class MainActivity extends Activity 
{
	private static final int NORMAL_HEIGHT = 50;
	private static final int BORDER_HEIGHT = 400;
	public Boolean isOpenAddressEdit;
	public Boolean isFristGoBack;
	public Button goBack_Button;
	public Button goForward_Button;
	public Button l_Button;
	public Button toHistory_Button;
	public Button toBookmark_Button;
	public Button toExit_Button;
	public Button addToBookmark_Button;
	public Button addNewWebView_Button;
	public Button home_Button;
	public Button saveHomeUrl_Button;
	public Button cancelEditHomeUrl_Button; 
	public Button deleteAddressEditTExt_Button;
	public Button Web_Button;
	public ContentValues bookContentValue;
	public Dialog homeEdit_AlertDialog;
	public Dialog menu_AlertDialog;
	public Dialog settings_AlertDialog;
	public EditText homeUrl_EditText;
	public EditText address_EditText;
	
	//webview's max number
	private int webInt = 0;
	//current displaying webview's id 
	private int webId = 0;
	
	public LinearLayout address_EditText_LinearLayout;
	public ScrollView menu_ScrollView;
	
	//last loaded website's url's string array 
	public String[] lastLoadedUrl = new String [200];
	
	public String homeUrl;
	public SQLiteDatabase booDatabase ;
	public SQLiteDatabase lastLoadedDatabase;
	public TextView address_TextView;
	public TextView[] webwindowsText;
	public View menu_AlertDialog_View;
	
	public View homeEdit_AlertDialog_View;
	
	//current dispalying webview
	public WebView current_webview;
	
	//all webviews are in this array
	public WebView[] allWebView = new WebView[200];
	
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
		
		cVal.put("webtitle",current_webview.getTitle());

		cVal.put("weburl",current_webview.getUrl());


		if (checkBM != null){
			String[] checkBM_columns = checkBM.getColumnNames();
			while(checkBM.moveToNext()){
				for (String c:checkBM_columns){
					if (current_webview.getUrl().toString().contains(checkBM.getString(checkBM.getColumnIndex(c)))){
						
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
		
		isOpenAddressEdit = false;
		Animation loadAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.scale_to_small); 
		address_EditText_LinearLayout.startAnimation(loadAnimation);
		address_EditText_LinearLayout.setVisibility(View.INVISIBLE);
		address_EditText.setText(current_webview.getUrl().toString());
		address_TextView.setText(current_webview.getTitle().toString());
		isFristGoBack = true;
		
	}
	
	public void creteHistory(String url){
		
		SQLiteDatabase hisDatabase =  openOrCreateDatabase(Constant.HISTORY_DATABASE_NAME,MODE_PRIVATE,null);
		ContentValues cVal = new ContentValues();
		hisDatabase.execSQL("create table if not exists "+Constant.HISTORY_TABLE_NAME+"("+Constant.HISTORY_ID+" integer primary key autoincrement,"+Constant.HISTORY_WEB_TITLE+" text not null,"+Constant.HISTORY_WEB_URL+" text not null,"+Constant.HISTORY_WEB_LOADINGTIME+" text not null)");
		
		cVal.clear();
		cVal.put(Constant.HISTORY_WEB_URL,url);
		cVal.put(Constant.HISTORY_WEB_LOADINGTIME,getTime());
		cVal.put(Constant.HISTORY_WEB_TITLE,current_webview.getTitle().toString());
		hisDatabase.insert(Constant.HISTORY_TABLE_NAME,null,cVal);
		for (int i = 0 ; i < webInt;i++){}
		lastLoadedUrl[i] = current_webview.getUrl().toString();
		
		hisDatabase.close();
		lastLoadedUrl = url;
		Log.i("info","creat");
		
		
	}
	
	public void createLastLoadedUrl(){
		lastLoadedDatabase = openOrCreateDatabase(Constant.LAST_LOADED_DATABASE_NAME,MODE_PRIVATE,null);
		lastLoadedDatabase.execSQL("create tabe if not exists " + Constant.LAST_LOADED_TABLE_NAME+"("+Constant.LAST_LOADED_ID+" integer primary key autoincrement," + Constant.LAST_LOADED_URL +" text not null )");
		ContentValues lastLoadedContentValues = new ContentValues();
		lastLoadedDatabase.delete(Constant.LAST_LOADED_TABLE_NAME,Constant.LAST_LOADED_ID +" > ?",new String[]{"0"});
		if (current_webview.getUrl() != null){
			lastLoadedContentValues.put(Constant.LAST_LOADED_URL,current_webview.getUrl().toString());
			Log.i("lastLoaded",current_webview.getUrl().toString());
		} else {
			lastLoadedContentValues.put(Constant.LAST_LOADED_URL,Constant.LAST_LOADED_URL_NULL);
			Log.i("lastLoaded","null");
		}
		lastLoadedDatabase.insert(Constant.LAST_LOADED_TABLE_NAME,null,lastLoadedContentValues);
		lastLoadedDatabase.close();
		lastLoadedContentValues.clear();
	}
	
	public void doVisitWebsite(String URL){
		if(URL != null&&!URL.isEmpty()){
		URL = URL.toLowerCase();
		if(URL.contains("http://")||URL.contains("https://")){}else if(URL.contains("www.")){
			URL = "http://" + URL;
		}else{
			URL = "http://www."+URL;
		}
		//Toast.makeText(MainActivity.this,current_webview.getClass().toString(),Toast.LENGTH_LONG).show();
		current_webview.loadUrl(URL);
		}
	}
	
	public void findView(){
		
		address_EditText = (EditText) findViewById(R.id.addressEditText);
		address_EditText_LinearLayout = (LinearLayout) findViewById(R.id.addressEditText_LinerLayout);
		address_TextView = (TextView) findViewById(R.id.titleTextView);
		l_Button = (Button) findViewById(R.id.L_Button);
		deleteAddressEditTExt_Button = (Button) findViewById(R.id.delete_address_edittext_button);
		Web_Button = (Button) findViewById(R.id.Web_Button);
		
		menu_ScrollView = (ScrollView) findViewById(R.id.menuScrollView);
	
		
		findViewInMenuLayout();
		findViewInHomeUrlEditLayout();
	}
	
	public void findViewInHomeUrlEditLayout(){
		homeUrl_EditText = (EditText) homeEdit_AlertDialog_View.findViewById(R.id.edithomeurl_EditText);
		saveHomeUrl_Button = (Button) homeEdit_AlertDialog_View.findViewById(R.id.saveHomeurl_Button);
		
		cancelEditHomeUrl_Button = (Button) homeEdit_AlertDialog_View.findViewById(R.id.cancelEditHomeurl_Button);
		
		
	}
	
	
	public void findViewInMenuLayout(){
		addNewWebView_Button = (Button)findViewById(R.id.addNewWebView_dialogButton);
		addToBookmark_Button = (Button)findViewById(R.id.addToBookmark_dialogButton);
		goBack_Button = (Button) findViewById(R.id.goBack_dialogButton);
		goForward_Button = (Button)findViewById(R.id.goForward_dialogButton);
		home_Button = (Button) findViewById(R.id.home_dialogButton);
		toBookmark_Button = (Button)findViewById(R.id.toBookmark_dialogButton);
		toExit_Button = (Button)findViewById(R.id.toExit_dialogButton);
		toHistory_Button = (Button)findViewById(R.id.toHistory_dialogButton);
		
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
		homeUrl = Constant.FIRST_HOME_URL;
		
		findView();
		setListener();
		webInistial();
		
		booDatabase =  openOrCreateDatabase("bookmark.db",MODE_PRIVATE,null);

		bookContentValue = new ContentValues();

		booDatabase.execSQL("create table if not exists bootb(_id integer primary key autoincrement,webtitle text not null,weburl text not null)");
		
		loadLastUrl();
		loadHomeUrl();
		if (lastLoadedUrl == Constant.LAST_LOADED_URL_NULL){
			Log.i("llurl","llurl == null");
			doVisitWebsite(homeUrl);
		} else {
			Log.i("llurl","llurl != null");
			doVisitWebsite(lastLoadedUrl);
		}
		
		menu_ScrollView.setOnScrollChangeListener(new OnScrollChangeListener(){

				@Override
				public void onScrollChange(View v, int p2, int p3, int p4, int p5)
				{
					RelativeLayout l1 = (RelativeLayout) findViewById(R.id.mainLinearLayout);
					ViewGroup.LayoutParams a = (ViewGroup.LayoutParams) l1.getLayoutParams();
					
					if ( a.height == DensittUtil.dp2px(NORMAL_HEIGHT,MainActivity.this))
					a.height *= (BORDER_HEIGHT/NORMAL_HEIGHT);
					
					l1.setLayoutParams(a);
					
					// I try to use objectanimator to change the scrollview's height and failue
					//ObjectAnimator.ofFloat(p1,"scaleY",1f,3f).setDuration(1000).start();
					//Toast.makeText(MainActivity.this,"scrollView2Big",Toast.LENGTH_SHORT).show();
					
				}
				
			
		});
		
	}

	
	
	public class kfbroOnClickListener implements OnClickListener
	{

		@Override
		public void onClick(View view)
		{

			//Toast.makeText(MainActivity.this,"click",Toast.LENGTH_SHORT).show();
			RelativeLayout l1 = (RelativeLayout) findViewById(R.id.mainLinearLayout);
			ViewGroup.LayoutParams a = (ViewGroup.LayoutParams) l1.getLayoutParams();
			if ( a.height == DensittUtil.dp2px(BORDER_HEIGHT,MainActivity.this)){
				a.height /= (BORDER_HEIGHT/NORMAL_HEIGHT);
			}
			l1.setLayoutParams(a);
			
			switch(view.getId()){
				case R.id.addNewWebView_dialogButton:
					creatNewWebViewAsCurrentWeb();
							break;
				case R.id.addToBookmark_dialogButton:
					addToBM();
					break;
				case R.id.goBack_dialogButton:
					if (current_webview.canGoBack())
						current_webview.goBack();
					break;
				case R.id.goForward_dialogButton:
					if(current_webview.canGoForward()){
						current_webview.goForward();
					}
					break;
				case R.id.home_dialogButton:
					current_webview.loadUrl(homeUrl);
					break;
				case R.id.newWebview_dialogButton:
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
					finish();
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
				case R.id.delete_address_edittext_button:
					address_EditText.setText("");
					break;
					
				default:
					closeAddressEdit();
					
					
					
					break;
			}
		}

		private void creatNewWebViewAsCurrentWeb()
		{
			//Toast.makeText(MainActivity.this,"new web page",Toast.LENGTH_SHORT).show();
			//make last webview gone
			current_webview.setVisibility(View.GONE);
			
			//
			current_webview = allWebView[webId = ++webInt];
			
			current_webview.setVisibility(View.VISIBLE);
			current_webview.loadUrl(homeUrl);
			
			// TODO: Implement this method
		}
	}
	
	
	public class kfbroOnLongClickListener implements OnLongClickListener
	{

		@Override
		public boolean onLongClick(View view)
		{
			switch(view.getId()){
				case R.id.addressEditText:
					//address_EditText.setText("");
					break;
				case R.id.home_dialogButton:
					menu_AlertDialog.dismiss();
					homeUrl_EditText.setText(homeUrl);
					homeEdit_AlertDialog.show();
					break;
				case R.id.L_Button:
				//	menu_AlertDialog.show();
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
						lastLoadedUrl  = lastLoadedCursor.getString(lastLoadedCursor.getColumnIndex(Constant.LAST_LOADED_URL));
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
		menu_AlertDialog_Window_Params.height = DensittUtil.dp2px(300,MainActivity.this);
		//menu_AlertDialog_Window_Params.alpha = 0.69f;
		menu_AlertDialog_Window_Params.x= 60;
		menu_AlertDialog_Window_Params.y = 120;
		menu_AlertDialog_Window_Params.setTitle("menu");
		menu_AlertDialog_Window.setAttributes(menu_AlertDialog_Window_Params);
		}
	
	public void openWebWindowsChoose(View v){
		View wwc = View.inflate(MainActivity.this,R.layout.webviewgroup,null);
		LinearLayout l = (LinearLayout) wwc.findViewById(R.id.webviewgroupLinearLayout);
		Dialog wwcd = new Dialog(MainActivity.this,R.style.AppTheme);
		wwcd.setCanceledOnTouchOutside(true);
		Window ww = wwcd.getWindow();
		ww.requestFeature(Window.FEATURE_NO_TITLE);
		wwcd.setContentView(wwc);
		
		WindowManager.LayoutParams wwp = ww.getAttributes();
		webwindowsText = new TextView[200];
		for (int i = 0 ; i < webInt+1 ; i++){
			webwindowsText[i] = new TextView(this);
			String title = allWebView[i].getTitle().toString();
			Log.i("webtitle",title);
			if (title != "" || !title.isEmpty())
			webwindowsText[i].setText(" " +title);
			webwindowsText[i].setSingleLine(true);
			webwindowsText[i].setWidth(l.getLayoutParams().width);
			webwindowsText[i].setTextColor(Color.parseColor("#EAC566"));
			webwindowsText[i].setTextSize(TypedValue.COMPLEX_UNIT_SP,22);
			webwindowsText[i].setId(50+i);
			webwindowsText[i].setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v)
					{
						current_webview.setVisibility(View.GONE);
						current_webview = allWebView[v.getId() - 50];
						current_webview.setVisibility(View.VISIBLE); 
						
						//((Dialog)(v.getParent())).dismiss();
					}
				});
			//t[i].setBackgroundColor(Color.parseColor("#4a5a5a"));
			//t[i].setTextAppearance(MainActivity.this,R.style.webWindowsText);
			//wwcd.addContentView(t[i],null);
			l.addView(webwindowsText[i]);
		}
		wwp.height = DensittUtil.dp2px((webInt + 1) * 50,MainActivity.this);
		ww.setGravity(Gravity.CENTER_VERTICAL);
		wwcd.show();
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(requestCode == 1&&resultCode == RESULT_OK){
			doVisitWebsite(data.getStringExtra("URL"));
		}else if (requestCode == 2 && resultCode == RESULT_OK){
			doVisitWebsite(data.getStringExtra("URL"));
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
	public boolean onKeyDown(int keyCode, KeyEvent event){


		switch(keyCode){
			case event.KEYCODE_ENTER:
				doVisitWebsite(address_EditText.getText().toString());
				break;
			case event.KEYCODE_BACK:
				if (current_webview.canGoBack()){
					current_webview.goBack();
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
	protected void onStop()
	{
		createLastLoadedUrl();
		// TODO: Implement this method
		super.onStop();
	}


		
	public void openAddressEdit(){
		address_EditText_LinearLayout.setVisibility(View.VISIBLE);
		Animation loadAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.scale_to_big); 
		address_EditText_LinearLayout.startAnimation(loadAnimation);
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
		addNewWebView_Button.setOnClickListener(new kfbroOnClickListener());		
		addToBookmark_Button.setOnClickListener(new kfbroOnClickListener());
		
		cancelEditHomeUrl_Button.setOnClickListener(new kfbroOnClickListener());
		saveHomeUrl_Button.setOnClickListener(new kfbroOnClickListener());
		l_Button.setOnClickListener(new kfbroOnClickListener());
		goBack_Button.setOnClickListener(new kfbroOnClickListener());
		goForward_Button.setOnClickListener(new kfbroOnClickListener());
		home_Button.setOnClickListener(new kfbroOnClickListener());
		toExit_Button.setOnClickListener(new kfbroOnClickListener());
		toBookmark_Button.setOnClickListener(new kfbroOnClickListener());
		toHistory_Button.setOnClickListener(new kfbroOnClickListener());
		deleteAddressEditTExt_Button.setOnClickListener(new kfbroOnClickListener());
		
		
		address_EditText.setOnKeyListener(new kfbroOnKeyListener());
		
		address_EditText.setOnLongClickListener(new kfbroOnLongClickListener());
		
		l_Button.setOnLongClickListener(new kfbroOnLongClickListener());
		home_Button.setOnLongClickListener(new kfbroOnLongClickListener());
		
		Switch newWebSwitch = (Switch) findViewById(R.id.newWebSwitch);
		
		newWebSwitch.setOnClickListener(new kfbroOnClickListener());
		
	}
	
	public void webInistial(){
		LinearLayout f = (LinearLayout) findViewById(R.id.mainWebLayout);

		Toast.makeText(MainActivity.this,"new web window",Toast.LENGTH_SHORT).show();
		
		
		for ( int i = 0 ; i < 20 ; i++) {
		allWebView[i] = new WebView(MainActivity.this);
		//allWebView[i].set
			//float hi = f.getLayoutParams().height;
			//float wi = f.getLayoutParams().width;
			
		f.addView(allWebView[i],f.getLayoutParams().width,f.getLayoutParams().height);
		allWebView[i].setVisibility(View.GONE);
		
		WebSettings WS = allWebView[i].getSettings();
		
		WS.setJavaScriptEnabled(KfWebSettings.SETTINGS_JAVASCRIPTENABLED);
		WS.setJavaScriptCanOpenWindowsAutomatically(KfWebSettings.SETTINGS_JAVASCRIPTCANOPENWINDOWSAUTOMATICALLY);
		WS.setAppCacheEnabled(KfWebSettings.SETTINGS_APPCACHEENABLED);
		WS.setAppCacheMaxSize(20480);
		WS.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		WS.setBuiltInZoomControls(KfWebSettings.SETTINGS_BUILDINZOOMCONTROLS);
		WS.setSavePassword(KfWebSettings.SETTINGS_SAVAFORMDATA);
		WS.setSaveFormData(KfWebSettings.SETTINGS_SAVAPASSWORD);
		allWebView[i].setWebChromeClient(webChrome);
		allWebView[i].setWebViewClient(webViewClient);
		}
		
	    current_webview = allWebView[webInt];
		current_webview.setVisibility(View.VISIBLE);
		
		doVisitWebsite(homeUrl);
	}
	
	public WebChromeClient webChrome = new WebChromeClient(){

		@Override
		public void onProgressChanged(WebView view, int newProgress)
		{
			
			
			
			
			
			ProgressBar p= (ProgressBar)findViewById(R.id.address_ProgressBar);
			p.setProgress(newProgress);
			if(newProgress == 100){
				p.setVisibility(View.GONE);
				
				//Log.i("progress","newProgress == 100");
			}		
			super.onProgressChanged(view, newProgress);
		}
	};
	
	public WebViewClient webViewClient = new WebViewClient(){

			
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT); 
			
			WebView.HitTestResult hitTestResult = view.getHitTestResult();
			//hitTestResult==null解决重定向问题

			if (!TextUtils.isEmpty(url) && hitTestResult == null) {

				view.loadUrl(url);

				return true;

			}
			return super.shouldOverrideUrlLoading(view, url);
		}

	//最后在回退的时候：添加如下：
	//if(view.canGoBack()){
	//	view.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
	//	mView.goBack();
	//}
		

		@Override
		public void onPageFinished(WebView view, String url)
		{
			closeAddressEdit();
			creteHistory(current_webview.getUrl().toString());
			l_Button.setText("L");
			
			super.onPageFinished(view, url);
			
		}

		
	
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon)
		{
			ProgressBar p= (ProgressBar)findViewById(R.id.address_ProgressBar);
			p.setVisibility(View.VISIBLE);
			l_Button.setText(" ");
			address_TextView.setText("Loading");
			super.onPageStarted(view, url, favicon);
			
			
		}
		
		
	
	};
	
	}
