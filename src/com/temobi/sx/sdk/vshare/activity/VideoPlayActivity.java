package com.temobi.sx.sdk.vshare.activity;

import com.temobi.sx.sdk.vshare.R;
import com.temobi.sx.sdk.vshare.player.VideoPlayer;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

public class VideoPlayActivity extends Activity {

	String videoId = null;
	VideoPlayer player;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_videoplay);

		videoId = getIntent().getStringExtra("VideoId");


		findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				VideoPlayActivity.this.onBackPressed();
			}
		});
		
		createVideoPlayer();

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		videoId = intent.getStringExtra("VideoId");
		createVideoPlayer();
	}

	private void createVideoPlayer() {
		ViewGroup parent = (ViewGroup) findViewById(R.id.parent);
		parent.removeAllViews();

		player = new VideoPlayer(this, videoId, true);
		player.setLoop(true);
		parent.addView(player, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

	@Override
	protected void onDestroy() {
		player.release();
		super.onDestroy();
	}
}
