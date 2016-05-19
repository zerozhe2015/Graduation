package com.example.graduation_design.ui;

import com.example.graduation_design.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;

public class SplashActivity extends Activity {

	// 进入下一界面状态码
	protected static final int ENTER_HOME = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);

		// 延时三秒中后去发送状态码为ENTER_HOME消息,进入后一个界面
		mHandler.sendEmptyMessageDelayed(ENTER_HOME, 1500);
	}

	// 创建Handler对象
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ENTER_HOME:
				// 进入应用程序主界面,开启一个新的activity
				// 用Intent对象跳转页面（从MainActivity跳转到HistoryActivity界面）
				Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
				// 开始新的activity
				startActivity(intent);
				finish();
				break;

			}
		}
	};
}
