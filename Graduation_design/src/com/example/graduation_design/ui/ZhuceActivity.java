package com.example.graduation_design.ui;

import com.example.graduation_design.R;
import com.example.graduation_design.db.UserNumberHelper;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ZhuceActivity extends Activity {
	private EditText et_zhanghao;
	private EditText et_mima;
	private Button bt_zhuce;
	private TextView tv_zhuce;
	private EditText et_mima2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// requestWindow..这行代码是为了不让它显示android默认的actionbar（title）
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 这行代码就是关联到该activity对应的ui的xml文件
		setContentView(R.layout.activity_zhuce);

		initView();

		initData();
	}

	private void initData() {
		bt_zhuce.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String zhanghao = et_zhanghao.getText().toString();
				String mima = et_mima.getText().toString();
				String mima2 = et_mima2.getText().toString();

				if (zhanghao.length() == 0) {
					Toast.makeText(getApplicationContext(), "账户信息不能为空", Toast.LENGTH_SHORT).show();
				} else {
					if (mima != null && mima2 != null) {
						if(mima.equals(mima2)){
							// 创建SQLiteOpenHelper对象
							UserNumberHelper userNumberHelper = new UserNumberHelper(getApplicationContext());
							// 通过内容提供者对象调用其读写数据库的方法
							SQLiteDatabase db = userNumberHelper.getWritableDatabase();
							// 创建contentValues的对象
							ContentValues contentValues = new ContentValues();
							// 给contentValues赋值
							contentValues.put("zhanghao", zhanghao);
							contentValues.put("mima", mima);
							// 调用数据库的添加数据方法
							db.insert("user", null, contentValues);
							// 为了优化性能 数据操作后要及时关闭数据库
							db.close();
							Toast.makeText(getApplicationContext(), "您已注册成功", Toast.LENGTH_SHORT).show();
							finish();
							
						}else{
							Toast.makeText(getApplicationContext(), "两次输入密码不一致耶", Toast.LENGTH_SHORT).show();
							et_mima.setText("");
							et_mima2.setText("");
						}
					}else{
						Toast.makeText(getApplicationContext(), "密码信息不能为空", Toast.LENGTH_SHORT).show();
					}
				}

			}
		});

		tv_zhuce.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}

		});

	}

	private void initView() {
		et_zhanghao = (EditText) findViewById(R.id.et_zhanghao);
		et_mima = (EditText) findViewById(R.id.et_mima);
		et_mima2 = (EditText) findViewById(R.id.et_mima2);
		bt_zhuce = (Button) findViewById(R.id.bt_zhuce);
		tv_zhuce = (TextView) findViewById(R.id.tv_zhuce);

	}
}
