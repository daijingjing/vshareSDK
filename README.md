#SDK使用说明

1、开发环境Eclipse
2、服务端接口通过HTTP+JSON的方式交互
3、SDK调用前需要初始化，详细的初始化代码可以参考DEMO下的MainActivity
4、主要功能：视频的录制、播放、截图展现


#由于设计用户体系，需要进行用户集成登录，用户集成验证的接口如下：

业务系统接口地址：http://d.c365.com/thirdparty/validate
    调用方式：HTTP+JSON (POST)
    参数：source_id [int] 渠道编号(暂定1000)
    参数：mobile [string] 用户手机号
    返回值：validcode [string] 随机验证码

    
#业务系统接口测试代码：
curl -H "Content-Type: application/json" -d "{\"mobile\":\"18636636365\", \"source_id\":1000}" "http://d.c365.com/thirdparty/validate"



#SDK初始化过程：

1、SDK会验证当前会话是否有效，如果有效，则可以调用录制操作
	// 初始化SDK
	sdkInstance = new SDK(MainActivity.this, appId, appPassword, channelId) {
		@Override
		protected void onInvalidAppId() {
			Toast.makeText(MainActivity.this, "APPID无效", Toast.LENGTH_LONG).show();
		}
		@Override
		protected void onInvalidSession() {
			// 通过第三方验证接口获取验证码
			Toast.makeText(MainActivity.this, "会话无效，请登录", Toast.LENGTH_LONG).show();
			findViewById(R.id.login).setEnabled(true);
		}
		@Override
		protected void onSucceeded(String userId) {
			MainActivity.this.onLoginSucceed(userId);
		}
		@Override
		protected void onFaild() {
			Toast.makeText(MainActivity.this, "启动失败！", Toast.LENGTH_LONG).show();
		}
	};

2、如果SDK无效，会触发 onInvalidSession 方法
3、通过用户集成验证的接口，获取手机号对应的验证码，调用SDK.login方法完成SDK的初始化
	curl -H "Content-Type: application/json" -d "{\"mobile\":\"18636636365\", \"source_id\":1000}" "http://d.c365.com/thirdparty/validate"
	sdkInstance.login(edt_mobile.getText().toString(), edt_code.getText().toString());
		
4、调用录制功能，可以传递视频录制的位置信息，以及主题ID，用于区分视频存放区域（分类用）
	Intent intent = new Intent(MainActivity.this, com.temobi.sx.sdk.vshare.recorder.RecorderActivity.class);
	intent.putExtra("locAddr", "山西省太原市");
	intent.putExtra("locLon", locLon); // double 类型
	intent.putExtra("locLat", locLat); // double 类型
	//intent.putExtra("TopicId", "test"); //主题ID
	
	startActivityForResult(intent, ACTIVITY_CODE_TO_RECORDER);

5、录制完成后 RecorderActivity 会返回视频的ID信息等，通过视频ID信息调用视频播放控件进行视频的播放；
	final String videoId = data.getStringExtra("videoId");
	final String posterUrl = data.getStringExtra("posterURL");
	final String shareUrl = data.getStringExtra("shareURL"); 
	
	VideoPlayer v = new VideoPlayer(MainActivity.this, videoId, true);

6、视频ID用于业务展现，数据保存在调用方。

