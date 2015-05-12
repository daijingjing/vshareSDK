package com.temobi.sx.sdk.vshare.widget.comment;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.android.volley.RequestQueue;
import com.temobi.sx.sdk.vshare.R;
import com.temobi.sx.sdk.vshare.model.Comment;
import com.temobi.sx.sdk.vshare.net.CommentRemoveRequest;
import com.temobi.sx.sdk.vshare.net.CommentReportRequest;
import com.temobi.sx.sdk.vshare.net.CommentRequest;
import com.temobi.sx.sdk.vshare.utils.DateUtils;
import com.temobi.sx.sdk.vshare.utils.PrefUtils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class CommentView extends FrameLayout {
	
	private final static String TAG = CommentView.class.getSimpleName();

	private CommentRequest commentRequest;
	private CommentRemoveRequest commentRemoveRequest;
	private CommentReportRequest commentReportRequest;

	private ImageView moreButton;// 点击加载更多
	private View loading; // 正在加载的滚动条
	private LinearLayout commentContainer;
	private CommentItemView selectedItem;
	
	private String refId;
	
	public CommentView(Context context, RequestQueue requestQueue) {
		super(context);
		inflate(context, R.layout.comments, this);

		init();
		
		commentRequest = new CommentRequest(getContext(), requestQueue, PrefUtils.getUserKey(getContext()), null) {

			@Override
			protected void onReady() {
				refreshList(getComments());
				onLoadFinished();
			}

			@Override
			protected void onError(String message) {
				onLoadError();
			}
			
		};

		commentRemoveRequest = new CommentRemoveRequest(context, requestQueue, PrefUtils.getUserKey(getContext()), null) {

			@Override
			protected void onReady() {
				onDeleteFinished();
			}

			@Override
			protected void onError(String message) {
				onDeleteError();
			}
		};
		
		commentReportRequest = new CommentReportRequest(context, requestQueue, PrefUtils.getUserKey(getContext()), "") {
			
			@Override
			protected void onReady() {
				Toast.makeText(getContext(), "已成功举报！", Toast.LENGTH_SHORT).show();
				if(this.isRemoved()){
					load(refId);
				}
			}
			
			@Override
			protected void onError(String arg0) {
				Toast.makeText(getContext(), "举报失败，请检查网络设置！", Toast.LENGTH_SHORT).show();
			}
		}; 
	}

	protected void onLoadFinished() {
		loading.setVisibility(View.GONE);
	}

	protected void onDeleteFinished() {
		loading.setVisibility(View.GONE);
	}

	protected void onLoadError() {
		loading.setVisibility(View.GONE);
		//Toast.makeText(getContext(), "加载数据失败，请检查网络设置！", Toast.LENGTH_SHORT).show();
	}
	
	protected void onDeleteError() {
		loading.setVisibility(View.GONE);
		Toast.makeText(getContext(), "删除失败，请检查网络设置！", Toast.LENGTH_SHORT).show();
	}

	private void init() {
		commentContainer = (LinearLayout) findViewById(R.id.comment_container);

		moreButton = (ImageView) findViewById(R.id.more);
		moreButton.setVisibility(View.GONE);

		loading = findViewById(R.id.loading);
		loading.setVisibility(View.GONE);

		moreButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				loadMore();
			}
		});
	}

	public void load(String refId) {
		this.refId = refId;
		commentRequest.cancelRequest();

		onLoading();
		commentRequest.setRefId(refId);
		commentRequest.load();
	}

	public void loadMore() {
		onLoading();
		commentRequest.loadMore();
	}

	private void onLoading() {
		loading.setVisibility(VISIBLE);
	}

	private void removeItem(CommentItemView v) {
		commentRemoveRequest.setCommentId(v.getCommentId());
		commentRemoveRequest.sync();
		commentContainer.removeView(v);
	}

	private void refreshList(List<Comment> data) {

		commentContainer.removeAllViews();
		for (int i = 0; i < data.size(); i++) {
			Comment comment = data.get(i);
			CommentItemView commentItemView = new CommentItemView(getContext(), comment.RefId, comment.Id, comment.UserId);

			comment.UserName = comment.UserName.replaceAll("[\\r|\\n|\\t]", "");
			comment.Content = comment.Content.replaceAll("[\\r|\\n|\\t]", "");
			int nameLength = StringUtils.length(comment.UserName);
			
			commentItemView.getCommentContentTextView().setText(nameLength > 0 ? comment.UserName + ":" + comment.Content : comment.Content);
			
			commentItemView.getCommentDateTextView().setText(DateUtils.toBeauty(comment.Publish));
			commentItemView.getCommentContentTextView().setOnLongClickListener(onCommentClickListener);
			commentItemView.getHeadImageView().setOnClickListener(new UserAvatarClickListener(comment.UserId));
			
			commentContainer.addView(commentItemView);
		}

		if (commentRequest.hasMore()) {
			moreButton.setVisibility(VISIBLE);
		} else {
			moreButton.setVisibility(View.GONE);
		}
	}
	
	private class UserAvatarClickListener implements View.OnClickListener{
		final String userId;
		public UserAvatarClickListener(String userId){
			this.userId = userId;
		}
		@Override
		public void onClick(View arg0) {
			CommentView.this.onClickUserAvatar(userId);
		}
	}
	
	protected void onClickUserAvatar(String userId) {
	}
	
	private View.OnLongClickListener onCommentClickListener = new View.OnLongClickListener() {
		@Override
		public boolean onLongClick(View v) {
			selectedItem = (CommentItemView) v.getParent().getParent();
			if (selectedItem.getUserId().equals(PrefUtils.getUserId(getContext()))) {
				new AlertDialog.Builder(getContext()).setItems(new String[] { "删除评论"},
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								switch (which) {
								case 0:
									removeItem(selectedItem);
									break;
									
								default:
									break;
								}
							}
						}).show();
				
				return true;
			}else{
				new AlertDialog.Builder(getContext()).setItems(new String[] { "举报" },
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							switch (which) {
							case 0:
								doReportComment(selectedItem);
								break;
								
							default:
								break;
							}
						}
					}).show();
				return true;
			}
			
//			return false;
		}
	};
	
	private void doReportComment(final CommentItemView item){
		new AlertDialog.Builder(getContext(), AlertDialog.THEME_HOLO_LIGHT).setTitle("举报")
		.setMessage("确定要举报该内容吗？").setNegativeButton("点错了", null)
		.setPositiveButton("绝对要举报", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if (StringUtils.isNotEmpty(item.getCommentId())) {
					commentReportRequest.setCommentId(item.getCommentId());
					commentReportRequest.sync();
//					if(commentReportRequest.isRemoved()){
//						removeItem(item);
//					}
				} else {
					Toast.makeText(getContext(), "网络不给力，操作失败，请检查您的网络设置", Toast.LENGTH_LONG).show();
				}
			}
		}).show();
	}
}
