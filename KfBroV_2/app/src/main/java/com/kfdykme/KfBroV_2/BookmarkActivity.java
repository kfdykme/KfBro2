package com.kfdykme.KfBroV_2;

import android.app.*;
import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;


public class BookmarkActivity extends Activity implements OnClickListener
{
///////////////////////////////////////////////
	private Button bBack;

	private Button bClear;

	private LinearLayout booLinearLayout;	

	SQLiteDatabase booDatabase;
////////////////////////////////////////////////
	@Override
	public void onClick(View v)
	{

		switch(v.getId()){
			case R.id.boo_back_button:
				finish();
				break;
			case R.id.boo_clear_button:
				booDatabase.delete("bootb","_id>?",new String[]{"0"});
				booLinearLayout.removeAllViewsInLayout();
				break;

		}
		// TODO: Implement this method
	}


/////////////////////////////////////////////////
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE) ;
        setContentView(R.layout.bookmark);

		intisi();
	}

	///////////////////////////////////////////////////

	private void intisi(){
		findView();

		setOnClick();

		displayBM();
		booDatabase = openOrCreateDatabase("bookmark.db",MODE_PRIVATE,null);


	}
	///////////////////////////////////////////////////

	private void findView(){
		bBack = (Button) findViewById(R.id.boo_back_button);

		bClear = (Button ) findViewById(R.id.boo_clear_button);

		booLinearLayout = (LinearLayout) findViewById(R.id.bookmarkLinearLayout);
	}
	///////////////////////////////////////////////////

	private void setOnClick(){

		bBack.setOnClickListener(this);

		bClear.setOnClickListener(this);
	}

	///////////////////////////////////////////////////

	private void displayBM(){

		booDatabase = openOrCreateDatabase("bookmark.db",MODE_PRIVATE,null);

		Cursor booCur = booDatabase.query("bootb",null,"_id>?",new String[]{"0"},null,null,"_id DESC");

		if (booCur != null){
			String[] booCur_columns = booCur.getColumnNames();

			while(booCur.moveToNext()){
				for(String c:booCur_columns){
					switch(c){
						case "webtitle":
							TextView boo_title = new TextView(this);
							boo_title.setText(booCur.getString(booCur.getColumnIndex(c)));
							//
							boo_title.setTextColor(Color.parseColor("#f0cabd"));
							boo_title.setTextSize(20);
							boo_title.setLeft(10);
							boo_title.setPadding(9,10,0,0);
							boo_title.setTop(9);
							boo_title.setSingleLine(true);
							//
							booLinearLayout.addView(boo_title);
							boo_title= null;

							break;
						case "weburl":

							TextView boo_url = new TextView(this);
							boo_url.setText(booCur.getString(booCur.getColumnIndex(c)));
							//
							boo_url.setSingleLine(true);
							boo_url.setTextColor(Color.parseColor("#f0cabd"));
							boo_url.setShadowLayer(20,0,0,Color.parseColor("#f0f0f0"));
							boo_url.setPadding(27,0,0,0);
							//
							boo_url.setOnClickListener(new OnClickListener(){


									@Override
									public void onClick(View v){
										Intent inte = getIntent();
										inte.putExtra("URL",((TextView)v).getText().toString());
										setResult(RESULT_OK,inte);
										finish();
									}
								});
							//
							booLinearLayout.addView(boo_url);

							boo_url= null;
							break;

					}
				}
			}

		}

		booCur.close();
		booDatabase.close();
	}
	///////////////////////////////////////////////////
	///////////////////////////////////////////////////
	///////////////////////////////////////////////////
	///////////////////////////////////////////////////
	///////////////////////////////////////////////////
	///////////////////////////////////////////////////
	///////////////////////////////////////////////////
	///////////////////////////////////////////////////

}
