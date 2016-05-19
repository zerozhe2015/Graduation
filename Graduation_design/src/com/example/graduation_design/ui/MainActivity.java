package com.example.graduation_design.ui;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

import com.example.graduation_design.R;
import com.example.graduation_design.db.UserNumberHelper;
import com.example.graduation_design.widget.RoundProgressBar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author zero
 * 
 *         项目的主界面，主要功能：实时监测当前环境下的环境状况，将监测的数据存储在数据库中，还有查询历史记录功能
 */
public class MainActivity extends Activity {

	private TextView tv_pm_value;
	private TextView tv_comprehensive_value;
	private TextView tv_temperature_value;
	private TextView tv_humidity_value;
	private TextView tv_wind_value;
	private ImageView iv_test1;
	private LinearLayout ll_history;
	private LinearLayout ll_part1;

	private Socket socket;
	private PrintStream output;

	/* 服务器地址 */
	private final String SERVER_HOST_IP = "192.168.1.2";

	/* 服务器端口 */
	private final int SERVER_HOST_PORT = 9400;

	private int progress = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindow..这行代码是为了不让它显示android默认的actionbar（title）
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 这行代码就是关联到该activity对应的ui的xml文件
		setContentView(R.layout.activity_main);

		// 初始化View控件
		init();

		// 获取实时数据接口，实时展示数据，并进行存储到数据库中
		initData();

	}

	/**
	 * 初始化socket通讯
	 */
	private void initClientSocket() {
		try {
			/* 连接服务器 */
			socket = new Socket(SERVER_HOST_IP, SERVER_HOST_PORT);

			/* 获取输出流 */
			output = new PrintStream(socket.getOutputStream(), true, "utf-8");

		} catch (UnknownHostException e) {
			handleException(e, "unknown host exception: " + e.toString());
		} catch (IOException e) {
			handleException(e, "io exception: " + e.toString());
		}
	}

	public void handleException(Exception e, String prefix) {
		e.printStackTrace();
		String result = prefix + e.toString();

		System.out.println("获取数据" + result);
	}

	public void closeSocket() {
		try {
			output.close();
			socket.close();
		} catch (IOException e) {
			handleException(e, "close exception: ");
		}
	}

	/**
	 * 获取实时数据接口，实时展示数据，并进行存储到数据库中
	 */
	private void initData() {
		// 测试按钮的点击事件
		iv_test1.setOnClickListener(new OnClickListener() {

			@SuppressLint("SimpleDateFormat")
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				// 清空进度数
				progress = 0;
				// 让综合指数隐藏
				ll_zero.setVisibility(View.GONE);
				// 让进度条显示
				mCircleBar.setVisibility(View.VISIBLE);

				// 随机1－100的pm的随机值
				final int pm = (int) (Math.random() * 100);
				// 随机1－100的综合指数的随机值
				final int comprehensive = (int) (Math.random() * 100);
				// 随机1－100的温度的随机值
				final int temperature = (int) (Math.random() * 100);
				// 随机1－100的湿度的随机值
				final int humidity = (int) (Math.random() * 100);
				// 随机1－10的风级的随机值
				final int wind1 = (int) (Math.random() * 10);
				// 随机1－10的风级的随机值
				final int wind2 = (int) (Math.random() * 10);
				
				/**
				 * 定义综合值区间80-100为健康绿色（优）；区间在65-80为深海蓝色（良）；区间在50- 65为枯叶黄（合格）；
				 * 区间在50以下为红色（差）
				 */

				new Thread(new Runnable() {

					@Override
					public void run() {
						//进度循环（从0%到100%）
						while (progress <= 100) {
							//进度值数据累加
							progress += 2;
							//进度百分比的变化
							mCircleBar.setProgress(progress);
							//当到达100%时，进行下面操作
							if (progress == 100) {
								//这时为了防止在子线程中做UI处理
								MainActivity.this.runOnUiThread(new Runnable() {

									@Override
									public void run() {
										//显示综合值
										ll_zero.setVisibility(View.VISIBLE);
										//隐藏进度条
										mCircleBar.setVisibility(View.GONE);

										if (comprehensive > 79 && comprehensive < 101) {
											// 对于整体背景的颜色修改
											ll_part1.setBackgroundResource(R.drawable.bg1);
											// 对于综合值的view控件动态赋值
											tv_comprehensive_value.setText(comprehensive + "");
											// 对于综合值的view控件动态改变其颜色，使其字体颜色和北京颜色一直
											tv_comprehensive_value.setTextColor(
													MainActivity.this.getResources().getColor(R.color.newgreen));
										} else if (comprehensive >= 65 && comprehensive <= 80) {
											// 对于整体背景的颜色修改
											ll_part1.setBackgroundResource(R.drawable.bg2);
											// 对于综合值的view控件动态赋值
											tv_comprehensive_value.setText(comprehensive + "");
											// 对于综合值的view控件动态改变其颜色，使其字体颜色和北京颜色一直
											tv_comprehensive_value.setTextColor(
													MainActivity.this.getResources().getColor(R.color.newblue));
										} else if (comprehensive >= 50 && comprehensive <= 64) {
											// 对于整体背景的颜色修改
											ll_part1.setBackgroundResource(R.drawable.bg3);
											// 对于综合值的view控件动态赋值
											tv_comprehensive_value.setText(comprehensive + "");
											// 对于综合值的view控件动态改变其颜色，使其字体颜色和北京颜色一直
											tv_comprehensive_value.setTextColor(
													MainActivity.this.getResources().getColor(R.color.newyellow));
										} else {
											// 对于整体背景的颜色修改
											ll_part1.setBackgroundResource(R.drawable.hong);
											// 对于综合值的view控件动态赋值
											tv_comprehensive_value.setText(comprehensive + "");
											// 对于综合值的view控件动态改变其颜色，使其字体颜色和北京颜色一直
											tv_comprehensive_value.setTextColor(
													MainActivity.this.getResources().getColor(R.color.newred));
										}

										// 对于pm的view控件动态赋值
										tv_pm_value.setText("PM:" + pm);

										// 对于温度值的view控件动态赋值
										tv_temperature_value.setText(temperature + "℃");

										// 对于湿度值的view控件动态赋值
										tv_humidity_value.setText(humidity + "%");

										// 对于风级区间值的view控件动态赋值
										if (wind1 > wind2) {
											tv_wind_value.setText(wind2 + "-" + wind1 + "级");
										} else if (wind1 < wind2) {
											tv_wind_value.setText(wind1 + "-" + wind2 + "级");
										} else {
											tv_wind_value.setText(wind1 + "级");
										}

									}
								});

								// 简单时间格式化对象的生成（年－月－日 时－分－秒）
								SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
								// 用sDateFormat对象调用格式化方法生成字符串date，date为当前本机下的时间值
								String date = sDateFormat.format(new java.util.Date());

								// 创建SQLiteOpenHelper对象
								UserNumberHelper userNumberHelper = new UserNumberHelper(getApplicationContext());
								// 通过内容提供者对象调用其读写数据库的方法
								SQLiteDatabase db = userNumberHelper.getWritableDatabase();
								// 创建contentValues的对象
								ContentValues contentValues = new ContentValues();
								// 给contentValues赋值
								contentValues.put("pm", pm + "");
								contentValues.put("comprehensive", comprehensive + "");
								contentValues.put("temperature", temperature + "");
								contentValues.put("humidity", humidity + "");
								contentValues.put("wind1", wind1 + "");
								contentValues.put("wind2", wind2 + "");
								contentValues.put("time", date);
								// 调用数据库的添加数据方法 long
								// android.database.sqlite.SQLiteDatabase.insert(String
								// table,
								// String nullColumnHack, ContentValues values)
								// table 就是数据库的表名 “alldata”
								// contentValues就是要插入的那条数据
								db.insert("alldata", null, contentValues);
								// 为了优化性能 数据操作后要及时关闭数据库
								db.close();

							}
							try {
								Thread.sleep(50);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}).start();

				// initClientSocket();
				// 点击检测拉取相应的数据

			}
		});
	}

	/**
	 * 这个是物理返回键,我在此做了一个点击两次退出离开应用的操作，点击两次后退出
	 */
	@Override
	public void onBackPressed() {
		exitBy2Click();
	}

	/**
	 *  * 双击退出函数  
	 */
	private static Boolean isExit = false;
	private RoundProgressBar mCircleBar;
	private LinearLayout ll_zero;

	private void exitBy2Click() {
		Timer tExit = null;
		if (isExit == false) {
			isExit = true; // 准备退出
			Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false; // 取消退出
				}
			}, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

		} else

		{
			finish();
			System.exit(0);
		}
	}

	/**
	 * 初始化View控件
	 */
	private void init() {
		// 下面都是初始化TextView控件
		tv_pm_value = (TextView) findViewById(R.id.tv_pm_value);
		tv_comprehensive_value = (TextView) findViewById(R.id.tv_comprehensive_value);
		tv_temperature_value = (TextView) findViewById(R.id.tv_temperature_value);
		tv_humidity_value = (TextView) findViewById(R.id.tv_humidity_value);
		tv_wind_value = (TextView) findViewById(R.id.tv_wind_value);
		// 下面都是初始化ImageView控件
		iv_test1 = (ImageView) findViewById(R.id.iv_test1);
		// 下面都是初始化LinearLayout控件
		ll_history = (LinearLayout) findViewById(R.id.ll_history);
		ll_part1 = (LinearLayout) findViewById(R.id.ll_part1);

		mCircleBar = (RoundProgressBar) findViewById(R.id.circleProgressbar);

		ll_zero = (LinearLayout) findViewById(R.id.ll_zero);

		// 历史记录的点击事件
		ll_history.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 用Intent对象跳转页面（从MainActivity跳转到HistoryActivity界面）
				Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
				// 开始新的activity
				startActivity(intent);
				overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
			}
		});
	}

}
