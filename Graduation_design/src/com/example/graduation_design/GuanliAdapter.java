package com.example.graduation_design;

import java.util.ArrayList;

import com.example.graduation_design.SwipeLayout.OnSwipeListener;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GuanliAdapter extends BasicAdapter<Alldata> {

	private Context context;
	// private final UMSocialService mController =
	// UMServiceFactory.getUMSocialService("com.umeng.share");

	public GuanliAdapter(ArrayList<Alldata> list, Context context) {
		super(list);
		this.context = context;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.activity_item_detail3, null);
		}
		AlldataHolder holder = AlldataHolder.getHolder(convertView);
		final Alldata alldata = list.get(position);
		
		holder.tv_all_value.setText(alldata.getComprehensive());
		holder.tv_pm.setText("PM："+alldata.getPm());
		holder.tv_humidity.setText("湿度："+alldata.getHumidity()+"%");
		holder.tv_temperature.setText("温度："+alldata.getTemperature());
		holder.tv_date_month.setText(alldata.getTime().substring(5, 7)+"月");
		holder.tv_date_day.setText(alldata.getTime().substring(8, 10));
		

		holder.msl.getFrontView().setOnClickListener(new OnClickListener() {

		

			private String pm;
			private String comprehensive;
			private String temperature;
			private String humidity;
			private String wind1;
			private String wind2;
			private String time;

			@Override
			public void onClick(View v) {
				for (Alldata alldata : list) {

					pm = list.get(position).getPm();
					comprehensive = list.get(position).getComprehensive();
					temperature = list.get(position).getTemperature();
					humidity = list.get(position).getHumidity();
					wind1 = list.get(position).getWind1();
					wind2 = list.get(position).getWind2();
					time = list.get(position).getTime();
				}

				Intent intent = new Intent(context, DetailActivity.class);
				intent.putExtra("pm", pm);
				intent.putExtra("comprehensive", comprehensive);
				intent.putExtra("temperature", temperature);
				intent.putExtra("humidity", humidity);
				intent.putExtra("wind1", wind1);
				intent.putExtra("wind2", wind2);
				intent.putExtra("time", time);

				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

				context.startActivity(intent);
			}
		});

		holder.tv_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				UserNumberHelper helper = new UserNumberHelper(context);
				SQLiteDatabase db = helper.getWritableDatabase();
				db.delete("alldata", "time = ?", new String[] { alldata.getTime() });
				list.remove(position);

				GuanliAdapter.this.notifyDataSetChanged();

			}
		});

		return convertView;
	}

	static class AlldataHolder {
		TextView tv_date_day, tv_date_month, tv_all_value, tv_delete, tv_temperature, tv_humidity, tv_pm;
		MySwipeLayout msl;
		LinearLayout ll_item;

		public AlldataHolder(View convertView) {
			msl = (MySwipeLayout) convertView.findViewById(R.id.msl);
			tv_date_day = (TextView) convertView.findViewById(R.id.tv_date_day);
			tv_date_month = (TextView) convertView.findViewById(R.id.tv_date_month);
			tv_all_value = (TextView) convertView.findViewById(R.id.tv_all_value);
			tv_delete = (TextView) convertView.findViewById(R.id.tv_delete);

			tv_temperature = (TextView) convertView.findViewById(R.id.tv_temperature);
			tv_humidity = (TextView) convertView.findViewById(R.id.tv_humidity);
			tv_pm = (TextView) convertView.findViewById(R.id.tv_pm);

			ll_item = (LinearLayout) convertView.findViewById(R.id.ll_item);
		}

		public static AlldataHolder getHolder(View convertView) {
			AlldataHolder holder = (AlldataHolder) convertView.getTag();
			if (holder == null) {
				holder = new AlldataHolder(convertView);
				convertView.setTag(holder);
			}
			return holder;
		}

	}

	private ArrayList<SwipeLayout> openedItems = new ArrayList<SwipeLayout>();

	class MySwipeListener implements OnSwipeListener {

		@Override
		public void onStartOpen(SwipeLayout layout) {
			closeAllItem();

		}

		@Override
		public void onStartClose(SwipeLayout layout) {

		}

		@Override
		public void onOpen(SwipeLayout layout) {
			openedItems.add(layout);

		}

		@Override
		public void onClose(SwipeLayout layout) {
			openedItems.remove(layout);
		}
	}

	public void closeAllItem() {
		for (int i = 0; i < openedItems.size(); i++) {
			openedItems.get(i).close();
		}
		openedItems.clear();
	}

}
