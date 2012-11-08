package com.example.myfavoriteweibo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.example.myfavoriteweibo.keep.AccessTokenKeeper;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.sso.SsoHandler;
import com.weibo.sdk.android.util.Utility;

public class LoginActivity extends Activity {

	private Weibo mWeibo;
	//private static final String CONSUMER_KEY = "966056985";// 替换为开发者的appkey，例如"1646212860";
	private static final String CONSUMER_KEY =  "1037908465";
	private static final String REDIRECT_URL = "http://www.sina.com";
	private Button btnLogin;
	public static Oauth2AccessToken accessToken;
	public static final String TAG = "myfavoriteweibo";

	private SsoHandler mSsoHandler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		mWeibo = Weibo.getInstance(CONSUMER_KEY, REDIRECT_URL);

		btnLogin = (Button) findViewById(R.id.btnLogin);// 触发sso的按钮

		try {
			Class sso = Class.forName("com.weibo.sdk.android.sso.SsoHandler");
		} catch (ClassNotFoundException e) {
			Log.i(TAG, "com.weibo.sdk.android.sso.SsoHandler not found");

		}
		btnLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/**
				 * 下面两个注释掉的代码，仅当sdk支持sso时有效，
				 */
				mSsoHandler = new SsoHandler(LoginActivity.this, mWeibo);
				mSsoHandler.authorize(new AuthDialogListener());
			}

		});
		
		LoginActivity.accessToken=AccessTokenKeeper.readAccessToken(this);
        if(LoginActivity.accessToken.isSessionValid()){
        	Weibo.isWifi=Utility.isWifi(this);
            try {
                Class sso=Class.forName("com.weibo.sdk.android.api.WeiboAPI");//如果支持weiboapi的话，显示api功能演示入口按钮
            } catch (ClassNotFoundException e) {
                Log.i(TAG, "com.weibo.sdk.android.api.WeiboAPI not found");
            }
            startPostActivity();
            
        }

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_login, menu);
		return true;
	}

	class AuthDialogListener implements WeiboAuthListener {

		@Override
		public void onComplete(Bundle values) {
			Log.i(TAG, "in onComplete");
			String token = values.getString("access_token");
			String expires_in = values.getString("expires_in");
			LoginActivity.accessToken = new Oauth2AccessToken(token, expires_in);
			if (LoginActivity.accessToken.isSessionValid()) {
				Toast.makeText(LoginActivity.this, "认证成功", Toast.LENGTH_SHORT)
						.show();
			}
			AccessTokenKeeper.keepAccessToken(LoginActivity.this, accessToken);
			startPostActivity();
		}

		@Override
		public void onError(WeiboDialogError e) {
			Toast.makeText(getApplicationContext(),
					"Auth error : " + e.getMessage(), Toast.LENGTH_LONG).show();
		}

		@Override
		public void onCancel() {
			Toast.makeText(getApplicationContext(), "Auth cancel",
					Toast.LENGTH_LONG).show();
		}

		@Override
		public void onWeiboException(WeiboException e) {
			Toast.makeText(getApplicationContext(),
					"Auth exception : " + e.getMessage(), Toast.LENGTH_LONG)
					.show();
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.i(TAG, "in onActivityResult");
		if (mSsoHandler != null) {
			mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}
	
	private void startPostActivity() {
		Intent intent = new Intent(LoginActivity.this, PostActivity.class);
        startActivity(intent);
	}
}
