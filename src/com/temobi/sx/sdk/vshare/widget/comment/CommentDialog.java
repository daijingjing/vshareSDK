package com.temobi.sx.sdk.vshare.widget.comment;

import com.temobi.sx.sdk.vshare.R;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class CommentDialog extends Dialog implements TextWatcher {

	private Button send_btn;
	private Button cancel_btn;
	private EditText content;

	private Context context;

	public CommentDialog(Context context) {
		super(context, R.style.common_dlg_style);
		this.context = context;
		setContentView(R.layout.comment_post);
		send_btn = (Button) findViewById(R.id.send_btn);
		send_btn.setEnabled(false);
		send_btn.setTextColor(context.getResources().getColor(R.color.commment_dialog_send_btn_disable_color));
		send_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onCommit(content.getText().toString());
			}
		});
		cancel_btn = (Button) findViewById(R.id.cancel_btn);
		cancel_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onCancel();
			}
		});
		content = (EditText) findViewById(R.id.content);
		content.addTextChangedListener(this);

		setCanceledOnTouchOutside(true);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		onCancel();
	}

	protected void onCancel() {
		this.dismiss();
	}

	protected void onCommit(String text) {
		this.dismiss();
	}

	public EditText getContent() {
		return content;
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (s.length() <= 0) {
			send_btn.setEnabled(false);
			send_btn.setTextColor(context.getResources().getColor(R.color.commment_dialog_send_btn_disable_color));
		} else {
			send_btn.setEnabled(true);
			send_btn.setTextColor(context.getResources().getColor(R.color.commment_dialog_send_btn_enable_color));
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
	}

}
