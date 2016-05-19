package com.example.graduation_design.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.example.graduation_design.R;
import com.example.graduation_design.R.color;
import com.example.graduation_design.R.id;
import com.example.graduation_design.R.layout;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.LayoutParams;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.appcompat.R.integer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * @author zero
 *
 *         详情界面，包括详情数据和当时的波形图
 */
public class DetailActivity extends ActionBarActivity {

	private ImageView mRightButton;
	private TextView mTvActionTitle;
	private LayoutInflater mInflater;
	private ActionBar mActionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wave_display);
		// getIntent方法获取从上个界面传过来的数据
		String pm = getIntent().getStringExtra("pm");
		String comprehensive = getIntent().getStringExtra("comprehensive");
		String temperature = getIntent().getStringExtra("temperature");
		String humidity = getIntent().getStringExtra("humidity");
		String wind1 = getIntent().getStringExtra("wind1");
		String wind2 = getIntent().getStringExtra("wind2");
		String time = getIntent().getStringExtra("time");
		// 将风级的string字符串转换成int型
		int wind11 = Integer.valueOf(wind1).intValue();
		int wind22 = Integer.valueOf(wind2).intValue();

		// 初始化actionbar
		initActionBar();

		// 初始化view控件
		
		TextView tv_pingjia = (TextView) findViewById(R.id.tv_pingjia);
		TextView tv_comprehensive = (TextView) findViewById(R.id.tv_comprehensive);
		TextView tv_time = (TextView) findViewById(R.id.tv_time);
		TextView tv_temperature = (TextView) findViewById(R.id.tv_temperature);
		TextView tv_humidity = (TextView) findViewById(R.id.tv_humidity);
		TextView tv_wind = (TextView) findViewById(R.id.tv_wind);
		TextView tv_pm = (TextView) findViewById(R.id.tv_pm);

		// view控件的动态赋值
		int comprehensive1 = Integer.valueOf(comprehensive).intValue();
		if (comprehensive1 > 79 && comprehensive1 < 101) {
			tv_comprehensive.setText("综合指数：" + comprehensive + "  优");
			tv_pingjia.setText("当前环境的指数很不错，处于优秀等级，很安全，适于生存。");
		} else if (comprehensive1 >= 65 && comprehensive1 <= 80) {
			tv_comprehensive.setText("综合指数：" + comprehensive + "  良");
			tv_pingjia.setText("当前环境的指数很正常，处于良好等级，比较安全，可以放心。");
		} else if (comprehensive1 >= 50 && comprehensive1 <= 64) {
			tv_comprehensive.setText("综合指数：" + comprehensive + "  合格");
			tv_pingjia.setText("当前环境的指数刚及格，处于合格等级，目前安全，需要注意。");
		} else {
			tv_comprehensive.setText("综合指数：" + comprehensive + "  差");
			tv_pingjia.setText("当前环境的指数很低，处于危险等级，请灰常注意哦。");
		}

		tv_time.setText(time);
		tv_temperature.setText("温度：" + temperature);
		tv_humidity.setText("湿度：" + humidity);
		if (wind11 > wind22) {
			tv_wind.setText("风向：" + wind2 + "-" + wind1 + "级");
		} else if (wind11 < wind22) {
			tv_wind.setText("风向：" + wind1 + "-" + wind2 + "级");
		} else {
			tv_wind.setText("风向：" + wind1 + "级");
		}

		tv_pm.setText("PM：" + pm);

		// 下面就是波形图，hellochart这个jar包就是第三方框架

		LineChartView chart = (LineChartView) findViewById(R.id.chart);

		List<PointValue> values = new ArrayList<PointValue>();
		List<AxisValue> mAxisValues = new ArrayList<AxisValue>();
		// values.add(new PointValue(0, 2));
		// values.add(new PointValue(1, 4));
		// values.add(new PointValue(2, 3));
		// values.add(new PointValue(3, 4));
		// In most cased you can call data model methods in builder-pattern-like
		// manner.
		// Line line = new
		// Line(values).setColor(DetailActivity.this.getResources().getColor(R.color.newblue))
		// .setCubic(true);
		// List<Line> lines = new ArrayList<Line>();
		// lines.add(line);
		// LineChartData data = new LineChartData();
		// data.setLines(lines);
		// LineChartView chart = new LineChartView(DetailActivity.this);
		// chart.setLineChartData(data);

		for (int i = 0; i < 10; i++) {
			values.add(new PointValue(i, new Random().nextInt(10)));
			mAxisValues.add(new AxisValue(i).setValue(i)); // 为每个对应的i设置相应的label(显示在X轴)
		}
		Line line = new Line(values).setColor(DetailActivity.this.getResources().getColor(R.color.blue)).setCubic(true);
		List<Line> lines = new ArrayList<Line>();
		lines.add(line);
		LineChartData data = new LineChartData();
		data.setLines(lines);

		// 坐标轴
		Axis axisX = new Axis(); // X轴
		axisX.setHasTiltedLabels(true);
		axisX.setTextColor(DetailActivity.this.getResources().getColor(R.color.gray));
		axisX.setName("采集时间");
		axisX.setTextSize(9);
		axisX.setAutoGenerated(true);
		axisX.setMaxLabelChars(10);
		axisX.setValues(mAxisValues);
		data.setAxisXBottom(axisX);

		Axis axisY = new Axis(); // Y轴
		axisY.setMaxLabelChars(10); // 默认是3，只能看最后三个数字
		data.setAxisYLeft(axisY);

		// 设置行为属性，支持缩放、滑动以及平移
		chart.setInteractive(true);
		chart.setZoomType(ZoomType.HORIZONTAL);
		chart.setZoomType(ZoomType.VERTICAL);
		chart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
		chart.setLineChartData(data);
		chart.setVisibility(View.VISIBLE);
	}

	/**
	 * 初始化ActionBar
	 */
	private void initActionBar() {
		mActionBar = getSupportActionBar();
		if (isHasBackButton()) {
			mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
			View view = View.inflate(getApplicationContext(), R.layout.actionbar_custom_backtitle5, null);
			View back = view.findViewById(R.id.btn_back);
			mRightButton = (ImageView) view.findViewById(R.id.iv_share);
			mRightButton.setVisibility(View.INVISIBLE);
			back.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					onBackPressed();
				}
			});

			mTvActionTitle = (TextView) view.findViewById(R.id.tv_actionbar_title);

			String titleRes = "详情";
			if (titleRes != null) {
				mTvActionTitle.setText(titleRes);
			}
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			mActionBar.setCustomView(view, params);
		} else {
			mActionBar.setTitle("详情");
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
