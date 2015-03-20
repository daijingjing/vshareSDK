package com.temobi.sx.sdk.vshare.widget;

import com.android.volley.RequestQueue;
import com.temobi.sx.sdk.vshare.R;
import com.temobi.sx.sdk.vshare.utils.PrefUtils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

public class TopicVideoListView extends VideoListViewBase {

	public TopicVideoListView(Context context, RequestQueue requestQueue) {
		super(context, requestQueue);
	}

	@Override
	protected View createItemView(String itemId) {
		View itemView = View.inflate(getContext(), R.layout.topic_video_item, null);
		
		ViewGroup extView = (ViewGroup)itemView.findViewById(R.id.loved_and_comment);

		// 点赞
		VideoSupportView videoSupportView = new VideoSupportView(getContext(), this.requestQueue);
		videoSupportView.load(itemId, PrefUtils.getUserId(getContext()));
		extView.addView(videoSupportView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

		return itemView;
	}

	@Override
	protected ImageView findVideoPoster(View arg0) {
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
}
