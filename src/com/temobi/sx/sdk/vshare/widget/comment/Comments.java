package com.temobi.sx.sdk.vshare.widget.comment;

import java.util.List;

import com.android.volley.RequestQueue;
import com.temobi.sx.sdk.vshare.R;
import com.temobi.sx.sdk.vshare.model.Comment;
import com.temobi.sx.sdk.vshare.net.CommentRemoveRequest;
import com.temobi.sx.sdk.vshare.net.CommentRequest;
import com.temobi.sx.sdk.vshare.utils.DateUtils;
import com.temobi.sx.sdk.vshare.utils.PrefUtils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Comments extends FrameLayout {
	
	private final static String TAG = Comments.class.getSimpleName();

	private CommentRequest commentRequest;
	private CommentRemoveRequest commentRemoveRequest;

	private ImageView moreButton;// 点击加载更多
	private View loading; // 正在加载的滚动条
	private LinearLayout commentContainer;
	private CommentsItem selectedItem;

	public Comments(Context context, RequestQueue requestQueue) {
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

	}

	protected void onLoadFinished() {
		loading.setVisibility(View.GONE);
	}

	protected void onDeleteFinished() {
		loading.setVisibility(View.GONE);
	}

	protected void onLoadError() {
		loading.setVisibility(View.GONE);
		Toast.makeText(getContext(), "加载数据失败，请检查网络设置！", Toast.LENGTH_SHORT).show();
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

	private void removeItem(CommentsItem v) {
		commentRemoveRequest.setCommentId(v.getCommentId());
		commentRemoveRequest.sync();
		commentContainer.removeView(v);
	}

	private void refreshList(List<Comment> data) {

		commentContainer.removeAllViews();
		for (int i = 0; i < data.size(); i++) {
			Comment comment = data.get(i);
			CommentsItem commentItemView = new CommentsItem(getContext(), comment.RefId, comment.Id, comment.UserId);

			commentItemView.getCommentContentTextView().setText(
					Html.fromHtml("<font color=\"#6a7a8a\">" + comment.UserName
							+ "</font>:<font color=\"#5a5a5a\">" + comment.Content + "</font>"));
			
			commentItemView.getCommentDateTextView().setText(DateUtils.toBeauty(comment.Publish));
			commentItemView.getCommentContentTextView().setOnClickListener(onCommentClickListener);
			
			commentContainer.addView(commentItemView);
		}

		if (commentRequest.hasMore()) {
			moreButton.setVisibility(VISIBLE);
		} else {
			moreButton.setVisibility(View.GONE);
		}
	}
	
	private View.OnClickListener onCommentClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			selectedItem = (CommentsItem) v.getParent().getParent();
			if (selectedItem.getUserId().equals(PrefUtils.getUserId(getContext()))) {
				new AlertDialog.Builder(getContext()).setItems(new String[] { "删除评论" },
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
			} else {
				Toast.makeText(getContext(), "亲！只能删除自己的评论", Toast.LENGTH_SHORT).show();
			}
		}
	};
}
