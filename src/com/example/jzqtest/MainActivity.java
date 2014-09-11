package com.example.jzqtest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
	private Button btn_get;
	private Button btn_post;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btn_get = (Button) findViewById(R.id.btn_get);
		btn_post = (Button) findViewById(R.id.btn_post);
		btn_get.setOnClickListener(listener);
		btn_post.setOnClickListener(listener);
	}

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_post:
				Runnable postRun = new Runnable() {
					@Override
					public void run() {
						Log.i("TestHttpActivity", "ok");
						DefaultHttpClient client = new DefaultHttpClient();
						/** NameValuePair是传送给服务器的请求参数 param.get("name") **/
						List<NameValuePair> list = new ArrayList<NameValuePair>();
						NameValuePair pair1 = new BasicNameValuePair("name", "name0001");
						NameValuePair pair2 = new BasicNameValuePair("age", "age0001");
						list.add(pair1);
						list.add(pair2);
						UrlEncodedFormEntity entity = null;
						try {
							/** 设置编码 **/
							entity = new UrlEncodedFormEntity(list, "UTF-8");
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						/** 新建一个post请求 **/
						HttpPost post = new HttpPost("http://106.186.22.172:12341");
						post.setEntity(entity);
						HttpResponse response = null;
						String strResult = "";
						try {
							/** 客服端向服务器发送请求 **/
							response = client.execute(post);
						} catch (ClientProtocolException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						/** 请求发送成功，并得到响应 **/
						if (response.getStatusLine().getStatusCode() == 200) {
							try {
								/** 读取服务器返回过来的json字符串数据 **/
								strResult = EntityUtils.toString(response.getEntity());
							} catch (IllegalStateException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							Log.i("TestHttpActivity",  strResult);

						} else
							Log.i("TestHttpActivity",  "POST提交失败");
					}
				};
				new Thread(postRun).start();
				break;

			case R.id.btn_get:
				Runnable getRun = new Runnable() {
					
					@Override
					public void run() {
						DefaultHttpClient client1 = new DefaultHttpClient();
						/** NameValuePair是传送给服务器的请求参数 param.get("name") **/

						UrlEncodedFormEntity entity1 = null;

						/** 新建一个get请求 **/
						HttpGet get = new HttpGet("http://106.186.22.172:12341?name=qiqi");
						HttpResponse response1 = null;
						String strResult1 = "";
						try {
							/** 客服端向服务器发送请求 **/
							response1 = client1.execute(get);
						} catch (ClientProtocolException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				/** 请求发送成功，并得到响应 **/
				if (response1.getStatusLine().getStatusCode() == 200) {
					try {
						/** 读取服务器返回过来的json字符串数据 **/
						strResult1 = EntityUtils
								.toString(response1.getEntity());
					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					JSONObject jsonObject1 = null;
					try {
						/** 把json字符串转换成json对象 **/
						jsonObject1 = getJSON(strResult1);
					} catch (JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					String names = "";
					Log.d("qiqi", strResult1);
					try {
						/**
						 * jsonObject.getString("code") 取出code 比如这里返回的json 字符串为
						 * [code:0,msg:"ok",data:[list:{"name":1},{"name":2}]]
						 * **/

						/** 得到data这个key **/
						String data = jsonObject1.getString("students");
						/** 把data下的数据转换成json对象 **/
						JSONArray jarr1 = new JSONArray(data);
						/** 判断data对象下的list是否存在 **/
//						if (jDat1.get("list") != null) {
//							/** 把list转换成jsonArray对象 **/
//							JSONArray jarr1 = jDat1.getJSONArray("list");
							/** 循环list对象 **/
							for (int i = 0; i < jarr1.length(); i++) {

								/** **/
								JSONObject jsono = (JSONObject) jarr1.get(i);

								/** 取出list下的name的值 **/
								names = jsono.getString("name");
								Log.d("qiqi", names);
//							}
						}
//						Toast.makeText(
//								MainActivity.this,
//								"get请求： code:" + jsonObject1.getString("code")
//										+ "name:" + names, Toast.LENGTH_SHORT)
//								.show();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else
//					Toast.makeText(MainActivity.this, "get提交失败",
//							Toast.LENGTH_SHORT).show();
					Log.d("qiqi", "get提交失败");
					}
				};
				new Thread(getRun).start();
				break;
			}

		}
	};

	public JSONObject getJSON(String sb) throws JSONException {
		return new JSONObject(sb);
	}

}