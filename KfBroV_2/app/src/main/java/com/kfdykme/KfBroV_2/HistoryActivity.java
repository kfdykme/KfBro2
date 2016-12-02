package com.kfdykme.KfBroV_2;
import android.os.*;
import android.app.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.content.*;
import java.util.*;
import java.text.*;
import android.database.sqlite.*;
import android.database.Cursor;
import android.util.*;
import android.text.*;
import android.graphics.*;
import com.kfdykme.utils.*;


public class HistoryActivity extends Activity implements OnClickListener
{
	private Button bBTMian;

	public Button clear_History_Button;
	
	private LinearLayout hisLayout;

	//private Button his_item;

	private SQLiteDatabase hisDatabase;
	

public void clearHistory(){
	hisDatabase =  openOrCreateDatabase(Constant.HISTORY_DATABASE_NAME,MODE_PRIVATE,null);
	hisDatabase.delete(Constant.HISTORY_TABLE_NAME,Constant.HISTORY_ID+" > ?",new String[]{"0"});
	hisLayout.removeAllViews();
	hisDatabase.close();
}
private void displayHis(){
	hisDatabase =  openOrCreateDatabase(Constant.HISTORY_DATABASE_NAME,MODE_PRIVATE,null);
		hisDatabase.execSQL("create table if not exists "+Constant.HISTORY_TABLE_NAME+"("+Constant.HISTORY_ID+" integer primary key autoincrement,"+Constant.HISTORY_WEB_TITLE+" text not null,"+Constant.HISTORY_WEB_URL+" text not null,"+Constant.HISTORY_WEB_LOADINGTIME+" text not null)");
		

	Cursor hisCur = hisDatabase.query(Constant.HISTORY_TABLE_NAME,null,Constant.HISTORY_ID+">?",new String[]{"0"},null,null,"_id DESC");

		if (hisCur !=null){
			String[] cColumns =  hisCur.getColumnNames();

			while(hisCur.moveToNext()){
				for(String cColumn:cColumns){
					switch(cColumn){
						case Constant.HISTORY_ID:

							break;
					case Constant.HISTORY_WEB_TITLE:
							TextView his_title = new TextView(this);
							his_title.setText(hisCur.getString(hisCur.getColumnIndex(cColumn)));
							his_title.setTextColor(Color.parseColor("#f0cabd"));
							his_title.setTextSize(20);
							his_title.setLeft(10);
							his_title.setPadding(9,10,0,0);
							his_title.setTop(9);
							his_title.setSingleLine(true);
							hisLayout.addView(his_title);
							his_title = null;
						Log.i("info",hisCur.getString(hisCur.getColumnIndex(cColumn)));
						
							break;
					case Constant.HISTORY_WEB_URL:

							TextView his_url = new TextView(this);
							his_url.setText(hisCur.getString(hisCur.getColumnIndex(cColumn)));
							his_url.setSingleLine(true);
							his_url.setTextColor(Color.parseColor("#f0cabd"));
							his_url.setShadowLayer(20,0,0,Color.parseColor("#f0f0f0"));
							his_url.setPadding(27,0,0,0);
							his_url.setOnClickListener(new OnClickListener(){


								
									@Override
									public void onClick(View v){
										Intent inte = getIntent();
										inte.putExtra("URL",((TextView)v).getText().toString());
										setResult(RESULT_OK,inte);
										finish();
									}
								});
							hisLayout.addView(his_url);
						Log.i("info",hisCur.getString(hisCur.getColumnIndex(cColumn)));
						
							break;
					case Constant.HISTORY_WEB_LOADINGTIME:
							TextView his_lt = new TextView(this);
							his_lt.setText(hisCur.getString(hisCur.getColumnIndex(cColumn)));
							his_lt.setLeft(130);
							his_lt.setPadding(460,0,0,0);
							his_lt.setTextSize(13);
							his_lt.setSingleLine(true);
							his_lt.setTextColor(Color.parseColor("#f0cabd"));

							hisLayout.addView(his_lt);
						Log.i("info",hisCur.getString(hisCur.getColumnIndex(cColumn)));
						
							break;
					}
				Log.i("info","creat");
				
				}
			}
		}

	}

	private void findView()
	{
		bBTMian = (Button) findViewById(R.id.goBackButton);

		clear_History_Button = (Button) findViewById(R.id.clearButton);

		hisLayout = (LinearLayout)findViewById(R.id.hisListLayout);

	}
	
///////////////////////////////////////////////////////////

	private void inistial(){
		findView();

		bBTMian.setOnClickListener(this);
		clear_History_Button.setOnClickListener(this);

		displayHis();

	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.history);

		inistial();

	}

	@Override
	public void onClick(View v)
	{
		switch(v.getId()){
			case R.id.goBackButton:
				finish();
				break;
			case R.id.clearButton:
				clearHistory();
				break;
		}

	}

	/////////////////////////////////////////////////////////


	////////////////////////////////////////////////

}
		

