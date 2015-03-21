package com.temobi.sx.sdk.vshare.widget.comment;

import com.temobi.sx.sdk.vshare.R;
import com.temobi.sx.sdk.vshare.utils.Const;
import com.temobi.sx.sdk.vshare.utils.PicUtils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;

public class CommentsItem extends FrameLayout {
	
	private ImageView user_head;
	private TextView  content, comment_date;
	private String refId, commentId, userId;

	public String getRefId() {
		return refId;
	}

	public String getCommentId() {
		return commentId;
	}

	public String getUserId() {
		return userId;
	}

	public CommentsItem(Context context,String refId, String commentId, String userId) {
		super(context);
		this.refId = refId;
		this.commentId = commentId;
		this.userId = userId;
		inflate(context, R.layout.comments_item, this);
		user_head = (ImageView) findViewById(R.id.comment_user_head);
		content = (TextView) findViewById(R.id.comment_content);
		comment_date = (TextView) findViewById(R.id.comment_date);
		loadAvater();
	}
	
	public ImageView getHeadImageView(){
		return user_head;
	}
	
	public TextView getCommentContentTextView(){
		return content;
	}
	
	public TextView getCommentDateTextView(){
		return comment_date;
	}
	
	public void removeSelf(){
		this.setVisibility(GONE);
	}

	protected void loadAvater(){
		PicUtils.load_image(getContext(), Const.AVATAR_URL_PREFIX + userId, user_head, R.drawable.default_head);
	}
	
}
