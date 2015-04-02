package com.temobi.sx.sdk.vshare.widget;

import org.apache.commons.lang.StringUtils;

import com.android.volley.RequestQueue;
import com.temobi.sx.sdk.vshare.R;
import com.temobi.sx.sdk.vshare.net.CommentPostRequest;
import com.temobi.sx.sdk.vshare.utils.PrefUtils;
import com.temobi.sx.sdk.vshare.widget.comment.CommentPost;
import com.temobi.sx.sdk.vshare.widget.comment.CommentView;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class VideoListView extends VideoListViewBase {
	
	final static int ID_SUPPORT = 0x8801;
	final static int ID_COMMENT = 0x8802;

	public VideoListView(Context context, RequestQueue requestQueue) {
		super(context, requestQueue);
	}

	@Override
	protected View createItemView(String videoId) {
		View itemView = View.inflate(getContext(), R.layout.video_item, null);
		
		ViewGroup extView = (ViewGroup)itemView.findViewById(R.id.loved_and_comment);
		
		// 点赞
		VideoSupportView videoSupportView = new VideoSupportView(getContext(), this.requestQueue) {
			@Override
			protected void onClickUserAvatar(String userId) {
				VideoListView.this.onClickSupportUserAvatar(userId);
			}
		};
		videoSupportView.setId(ID_SUPPORT);
		extView.addView(videoSupportView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		
		CommentView commentView = new CommentView(getContext(), this.requestQueue) {
			@Override
			protected void onClickUserAvatar(String userId) {
				VideoListView.this.onClickCommentUserAvatar(userId);
			}
		};
		
		commentView.setId(ID_COMMENT);
		extView.addView(commentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

		itemView.findViewById(R.id.comment).setTag(videoId);
		itemView.findViewById(R.id.comment).setOnClickListener(new PostCommentClickListener(videoId, commentView));
		
		return itemView;
	}
	
	class PostCommentClickListener implements View.OnClickListener {
		String refId;
		CommentView commentView;
		
		public PostCommentClickListener(String refId, CommentView commentView) {
			this.refId = refId;
			this.commentView = commentView;
		}
		
		@Override
		public void onClick(View arg0) {
			refId = (String)arg0.getTag();
			new CommentPost(getContext()){
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
	protected ImageView getVideoPoster(View itemView) {
		return (ImageView)itemView.findViewById(R.id.poster);
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
	protected TextView getCreateDate(View itemView) {
		return (TextView)itemView.findViewById(R.id.hh_time);
	}

	@Override
	protected TextView getLocation(View itemView) {
		return (TextView)itemView.findViewById(R.id.position);
	}

	@Override
	protected int getDefaultAvatarDrawable() {
		return R.drawable.default_head;
	}

	@Override
	protected ImageView getUserAvatar(View itemView) {
		return (ImageView)itemView.findViewById(R.id.user_avatar);
	}
	
	@Override
	protected void loadAvatar(String userId, String mobile, ImageView avatar) {
		super.loadAvatar(userId, mobile, avatar);
		
		if (avatar instanceof CircleImageView) {
			boolean mine = StringUtils.equals(userId, PrefUtils.getUserId(getContext()));
			((CircleImageView)avatar).setInsideBorderColor(Color.parseColor(mine ? "#99D00000" : "#9900D000"));
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

	@Override
	protected void loadItemData(View itemView, String videoId) {
		CommentView commentView = (CommentView)itemView.findViewById(ID_COMMENT);
		VideoSupportView videoSupportView = (VideoSupportView)itemView.findViewById(ID_SUPPORT);
		
		videoSupportView.load(videoId, PrefUtils.getUserId(getContext()));
		commentView.load(videoId);
		
		itemView.findViewById(R.id.comment).setTag(videoId);
	}

	@Override
	protected View createFooterLoadingView() {
		return inflate(getContext(), R.layout.footer_loading, null);
	}

	@Override
	protected View getReportButton(View itemView) {
		return itemView.findViewById(R.id.report);
	}

	@Override
	protected TextView getPlayTotal(View itemView) {
		return (TextView)itemView.findViewById(R.id.play_total);
	}

	protected void onClickSupportUserAvatar(String userId) {
	}
	protected void onClickCommentUserAvatar(String userId) {
	}
}
