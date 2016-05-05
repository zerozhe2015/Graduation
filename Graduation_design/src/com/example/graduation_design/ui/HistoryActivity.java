package com.example.graduation_design.ui;

import java.util.ArrayList;

import com.example.graduation_design.R;
import com.example.graduation_design.R.id;
import com.example.graduation_design.R.layout;
import com.example.graduation_design.adapter.GuanliAdapter;
import com.example.graduation_design.bean.Alldata;
import com.example.graduation_design.db.UserNumberHelper;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.LayoutParams;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class HistoryActivity extends ActionBarActivity {

	private ArrayList<Alldata> list = new ArrayList<Alldata>();
	private ImageView mRightButton;
	private TextView mTvActionTitle;
	private LayoutInflater mInflater;
	private ActionBar mActionBar;
	private TextView tv_time;
	private ListView lv_listview;
	private LinearLayout ll_nodata;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);

		initActionBar();

		init();

		initData();
	}

	private void initData() {
		list.clear();
		
		UserNumberHelper userNumberHelper = new UserNumberHelper(HistoryActivity.this);
		SQLiteDatabase db = userNumberHelper.getWritableDatabase();
		Cursor cursor = db.query("alldata",
				new String[] { "pm", "comprehensive", "temperature", "humidity", "wind1", "wind2", "time" }, null, null,
				null, null, null);
		while (cursor.moveToNext()) {
			String pm = cursor.getString(0);
			String comprehensive = cursor.getString(1);
			String temperature = cursor.getString(2);
			String humidity = cursor.getString(3);
			String wind1 = cursor.getString(4);
			String wind2 = cursor.getString(5);
			String time = cursor.getString(6);

			Alldata alldata = new Alldata();
			alldata.setPm(pm);
			alldata.setComprehensive(comprehensive);
			alldata.setTemperature(temperature);
			alldata.setHumidity(humidity);
			alldata.setWind1(wind1);
			alldata.setWind2(wind2);
			alldata.setTime(time);
			list.add(alldata);
		}
		db.close();

		if (list.size() == 0) {
			lv_listview.setVisibility(View.GONE);
			ll_nodata.setVisibility(View.VISIBLE);
		}
		
		GuanliAdapter guanliAdapter = new GuanliAdapter(list, HistoryActivity.this);
		lv_listview.setAdapter(guanliAdapter);
	}

	/**
	 * 初始化View控件
	 */
	private void init() {
		tv_time = (TextView) findViewById(R.id.tv_time);
		lv_listview = (ListView) findViewById(R.id.lv_listview);
		ll_nodata = (LinearLayout) findViewById(R.id.ll_nodata);
	}

	/**
	 * 初始化actionbar
	 */
	private void initActionBar() {
		mActionBar = getSupportActionBar();
		if (isHasBackButton()) {
			mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
			View view = View.inflate(getApplicationContext(), R.layout.actionbar_custom_backtitle5, null);
			View back = view.findViewById(R.id.btn_back);
			mRightButton = (ImageView) view.findViewById(R.id.iv_share);
			mRightButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
				}
			});
			back.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					onBackPressed();
				}
			});

			mTvActionTitle = (TextView) view.findViewById(R.id.tv_actionbar_title);

			String titleRes = "历史记录";
			if (titleRes != null) {
				mTvActionTitle.setText(titleRes);
			}
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			mActionBar.setCustomView(view, params);
			// View spinner =
			// mActionBar.getCustomView().findViewById(R.id.spinner);
			// if (haveSpinner()) {
			// spinner.setVisibility(View.VISIBLE);
			// } else {
			// spinner.setVisibility(View.GONE);
			// }
		} else {
			mActionBar.setTitle("历史记录");
			// 显示HOME建
			mActionBar.setDisplayHomeAsUpEnabled(true);
			// 按钮可点击
			mActionBar.setHomeButtonEnabled(true);

			// mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		}
	}

	private boolean isHasBackButton() {
		return true;
	}
}
