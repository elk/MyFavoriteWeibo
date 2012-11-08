package com.example.myfavoriteweibo;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.StatusesAPI;
import com.weibo.sdk.android.net.RequestListener;

public class PostActivity extends Activity implements OnClickListener,
		RequestListener {

	private EditText postEdit;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post);

		Button btnPost = (Button) findViewById(R.id.btnPost);
		postEdit = (EditText) findViewById(R.id.postEdit);
		btnPost.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_post, menu);
		return true;
	}

	@Override
	public void onComplete(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onError(WeiboException arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onIOException(IOException arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		StatusesAPI api = new StatusesAPI(LoginActivity.accessToken);
		String content = postEdit.getText().toString();
		if (TextUtils.isEmpty(content)) {
			Toast.makeText(this, "请输入内容!", Toast.LENGTH_LONG).show();
			return;
		}

		api.update(content, "90", "90", this);
		Toast.makeText(this, "发送成功!", Toast.LENGTH_LONG).show();
		this.postEdit.setText("");

	}

}
