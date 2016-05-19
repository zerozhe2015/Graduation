package com.example.graduation_design.ui;

import com.example.graduation_design.R;
import com.example.graduation_design.db.UserNumberHelper;
import com.example.graduation_design.dialog.LoadingDialog;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {
	private EditText et_zhanghao;
	private EditText et_mima;
	private Button bt_login;
	private TextView tv_zhuce;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// requestWindow..这行代码是为了不让它显示android默认的actionbar（title）
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 这行代码就是关联到该activity对应的ui的xml文件
		setContentView(R.layout.activity_login);

		initView();

		initData();
	}

	private void initData() {
		bt_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				LoadingDialog loadingDialog = new LoadingDialog(LoginActivity.this);
				loadingDialog.show();
				String zhanghao = et_zhanghao.getText().toString();
				String mima = et_mima.getText().toString();

				if (zhanghao.length() == 0) {
					Toast.makeText(getApplicationContext(), "账户密码信息不能为空", Toast.LENGTH_SHORT).show();
				} else {
					if (mima.length() == 0) {
						Toast.makeText(getApplicationContext(), "账户密码信息不能为空", Toast.LENGTH_SHORT).show();
					} else {
						// 创建SQLiteOpenHelper对象
						UserNumberHelper userNumberHelper = new UserNumberHelper(LoginActivity.this);
						// 通过内容提供者对象调用其读写数据库的方法
						SQLiteDatabase db = userNumberHelper.getWritableDatabase();
						// 调用数据库的查询数据方法
						Cursor cursor = db.query("user", new String[] { "zhanghao", "mima" }, "zhanghao=? and mima=?",
								new String[] { zhanghao, mima }, null, null, null);
						// 根据循环每个数据集查询整个数据库中的所有数据
						boolean isExit = false;
						while (cursor.moveToNext()) {

							isExit = true;

						}
						// 为了优化性能 数据操作后要及时关闭数据库
						db.close();

						if (!isExit) {
							Toast.makeText(getApplicationContext(), "账户密码信息有误", Toast.LENGTH_SHORT).show();
						} else {
							String notice = "用户" + zhanghao + "已经登录";
							loadingDialog.dismiss();
							// 用Intent对象跳转页面（从MainActivity跳转到HistoryActivity界面）
							Intent intent = new Intent(LoginActivity.this, MainActivity.class);
							// 开始新的activity
							startActivity(intent);

							finish();
							overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
							Toast.makeText(getApplicationContext(), notice, Toast.LENGTH_SHORT).show();
						}
					}
				}
			}
		});

		tv_zhuce.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 用Intent对象跳转页面（从MainActivity跳转到HistoryActivity界面）
				Intent intent = new Intent(LoginActivity.this, ZhuceActivity.class);
				// 开始新的activity
				startActivity(intent);
				overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
			}
		});

	}

	private void initView() {
		TextView tv_title = (TextView) findViewById(R.id.tv_title);

		Typeface face = Typeface.createFromAsset(getAssets(), "zero2.ttf");
		tv_title.setTypeface(face);

		et_zhanghao = (EditText) findViewById(R.id.et_zhanghao);
		et_mima = (EditText) findViewById(R.id.et_mima);
		bt_login = (Button) findViewById(R.id.bt_login);
		tv_zhuce = (TextView) findViewById(R.id.tv_zhuce);

	}
}
