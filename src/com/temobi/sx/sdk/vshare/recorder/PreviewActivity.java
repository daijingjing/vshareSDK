package com.temobi.sx.sdk.vshare.recorder;

import com.temobi.sx.sdk.vshare.R;
import com.temobi.sx.sdk.vshare.player.PreviewActivityBase;
import com.temobi.sx.sdk.vshare.player.VideoPlayer;
import com.temobi.sx.sdk.vshare.player.VideoPlayerBase;

public class PreviewActivity extends PreviewActivityBase {

	public PreviewActivity() {
		super();
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_preview;
	}

	@Override
	protected int getPublishButtonId() {
		return R.id.preview_next;
	}

	@Override
	protected int getCancelButtonId() {
		return R.id.preview_cancel;
	}

	@Override
	protected int getPreviewContainerId() {
		return R.id.preview_video_parent;
	}

	@Override
	protected VideoPlayerBase createLocalPlayer(String arg0, String arg1) {
		VideoPlayer p =  new VideoPlayer(this, arg0, true);
		p.setLoop(true);
		return p;
	}

	@Override
	protected void onRequestLocation() {
	}
	
}