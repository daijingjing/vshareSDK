package com.temobi.sx.sdk.vshare.recorder;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.temobi.sx.sdk.vshare.R;
import com.temobi.sx.sdk.vshare.player.PreviewActivityBase;
import com.temobi.sx.sdk.vshare.player.VideoPlayer;
import com.temobi.sx.sdk.vshare.player.VideoPlayerBase;
import com.temobi.sx.sdk.vshare.widget.InputDialog;

public class PreviewActivity extends PreviewActivityBase {

	public PreviewActivity() {
		super();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
		final EditText edtTitle = getTitleEidtText();
		edtTitle.setImeActionLabel("上传", 0xF12);
		edtTitle.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == 0xF12) {
					findViewById(getPublishButtonId()).performClick();
					return true;
				}
				return false;
			}
		});
		
		edtTitle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				pause();
				new InputDialog(PreviewActivity.this){
					@Override
					protected void onPermission() {
						edtTitle.setText(getText());
					}
				}
				.setText(edtTitle.getText().toString())
				.setHint(edtTitle.getHint().toString())
				.setTitle("标题")
				.setSingleLine(false)
				.setMaxLines(3)
				.setEms(50)
				.show();
			}
		});
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
		VideoPlayer p = new VideoPlayer(this, arg0, true);
		p.setLoop(true);
		return p;
	}

	@Override
	protected void onRequestLocation() {
	}

	@Override
	protected EditText getTitleEidtText() {
		return (EditText) findViewById(R.id.title);
	}

}
