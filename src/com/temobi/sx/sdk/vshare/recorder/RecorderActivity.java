package com.temobi.sx.sdk.vshare.recorder;

import android.annotation.TargetApi;
import android.os.Build;

import com.temobi.sx.sdk.vshare.R;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class RecorderActivity extends RecorderActivityBase {

	public RecorderActivity() {
		super();
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_recorder;
	}

	@Override
	protected int getButtonLayoutId() {
		return R.layout.recorder_button;
	}

	@Override
	protected int getRecordingId() {
		return R.id.recorder_recording;
	}

	@Override
	protected int getRecordButtonId() {
		return R.id.recorder_push_record;
	}

	@Override
	protected int getGuideAreaId() {
		return R.id.recorder_guide;
	}

	@Override
	protected int getGuideText1Id() {
		return R.drawable.video_text01;
	}

	@Override
	protected int getGuideText2Id() {
		return R.drawable.video_text02;
	}

	@Override
	protected int getGuideText3Id() {
		return R.drawable.video_text03;
	}

	@Override
	protected int getGuideText4Id() {
		return R.drawable.video_text04;
	}

	@Override
	protected int getCancelButtonId() {
		return R.id.recorder_cancel;
	}

	@Override
	protected int getPreviewButtonId() {
		return R.id.recorder_preview;
	}

	@Override
	protected int getTimerId() {
		return R.id.recorder_timer;
	}

	@Override
	protected int getFlashButtonId() {
		return R.id.recorder_flashlight;
	}

	@Override
	protected int getCameraButtonId() {
		return R.id.recorder_camera;
	}

	@Override
	protected int getSurfaceParentId() {
		return R.id.recorder_surface_parent;
	}

	@Override
	protected Class getPreviewActivityClass() {
		return PreviewActivity.class;
	}
	
}