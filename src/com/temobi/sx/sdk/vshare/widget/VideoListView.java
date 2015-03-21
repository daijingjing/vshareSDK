package com.temobi.sx.sdk.vshare.widget;

import org.apache.commons.lang.StringUtils;

import com.android.volley.RequestQueue;
import com.temobi.sx.sdk.vshare.R;
import com.temobi.sx.sdk.vshare.net.CommentPostRequest;
import com.temobi.sx.sdk.vshare.utils.Const;
import com.temobi.sx.sdk.vshare.utils.PicUtils;
import com.temobi.sx.sdk.vshare.utils.PrefUtils;
import com.temobi.sx.sdk.vshare.widget.comment.CommentDialog;
import com.temobi.sx.sdk.vshare.widget.comment.Comments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class VideoListView extends VideoListViewBase {

	public VideoListView(Context context, RequestQueue requestQueue) {
		super(context, requestQueue);
	}

	@Override
	protected View createItemView(String itemId) {
		View itemView = View.inflate(getContext(), R.layout.topic_video_item, null);
		
		ViewGroup extView = (ViewGroup)itemView.findViewById(R.id.loved_and_comment);
		
		// 点赞
		VideoSupportView videoSupportView = new VideoSupportView(getContext(), this.requestQueue);
		extView.addView(videoSupportView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		videoSupportView.load(itemId, PrefUtils.getUserId(getContext()));

		Comments commentView = new Comments(getContext(), this.requestQueue);
		extView.addView(commentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		commentView.load(itemId);
		
		itemView.findViewById(R.id.comment).setOnClickListener(new PostCommentClickListener(itemId, commentView));
		return itemView;
	}
	
	class PostCommentClickListener implements View.OnClickListener {
		String refId;
		Comments commentView;
		
		public PostCommentClickListener(String refId, Comments commentView) {
			this.refId = refId;
			this.commentView = commentView;
		}
		
		@Override
		public void onClick(View arg0) {
			new CommentDialog(getContext()){
				protected void onCommit(String text) {
					new CommentPostRequest(getContext(), requestQueue, PrefUtils.getUserKey(getContext()), refId, null, text) {
						
						@Override
						protected void onReady() {
							dismiss();
							commentView.load(refId);
						}
						
						@Override
						protected void onError(String message) {
							Toast.makeText(getContext(), "评论失败，请重试", Toast.LENGTH_SHORT).show();										
						}
					}.sync();
				};
			}.show(); 
		}
	};

	@Override
	protected ImageView getVideoPoster(View arg0) {
		return (ImageView)arg0.findViewById(R.id.poster);
	}

	@Override
	protected int getDefaultPosterDrawable() {
		return R.drawable.pic_normal;
	}

	@Override
	protected void onPlayVideo(String videoId) {
		Toast.makeText(getContext(), "播放视频：" + videoId, Toast.LENGTH_SHORT).show();
	}

	@Override
	protected TextView getCreateDate(View arg0) {
		return (TextView)arg0.findViewById(R.id.hh_time);
	}

	@Override
	protected TextView getLocation(View arg0) {
		return (TextView)arg0.findViewById(R.id.position);
	}

	@Override
	protected int getDefaultAvatarDrawable() {
		return R.drawable.default_head;
	}

	@Override
	protected ImageView getUserAvatar(View arg0) {
		return (ImageView)arg0.findViewById(R.id.user_avatar);
	}
	
	@Override
	protected void loadAvatar(String userId, String mobile, ImageView avatar) {
		super.loadAvatar(userId, mobile, avatar);
		
		boolean mine = StringUtils.equals(userId, PrefUtils.getUserId(getContext()));
		if (mine && avatar instanceof CircleImageView) {
			((CircleImageView)avatar).setInsideBorderColor(Color.parseColor("#99D00000"));
		}
	}

	@Override
	protected View getDeleteButton(View arg0) {
		return arg0.findViewById(R.id.delete);
	}

	@Override
	protected View getShareButton(View arg0) {
		return arg0.findViewById(R.id.share);
	}

	@Override
	protected void onShareVideo(String videoId, String posterUrl, String shortUrl) {
	}
}
