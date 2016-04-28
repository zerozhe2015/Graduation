package com.example.graduation_design;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * 侧拉删除
 * @author poplar
 *
 */
public class SwipeLayout extends FrameLayout {

	private ViewDragHelper mHelper;
	private View mBackView;  // 后布局
	private View mFrontView; // 前布局
	private int mWidth; // 控件宽度
	private int mHeight;// 控件高度
	private int mRange; // 拖拽范围
	
	public static enum Status{
		Close, Open, Swiping
	}
	private Status status = Status.Close;
	
	public interface OnSwipeListener {
		
		// 打开
		void onOpen(SwipeLayout layout);
		
		// 关闭
		void onClose(SwipeLayout layout);
		
		// 将要打开
		void onStartOpen(SwipeLayout layout);
		
		// 将要关闭
		void onStartClose(SwipeLayout layout);
	}
	private OnSwipeListener onSwipeListener;
	
	
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public OnSwipeListener getOnSwipeListener() {
		return onSwipeListener;
	}

	public void setOnSwipeListener(OnSwipeListener onSwipeListener) {
		this.onSwipeListener = onSwipeListener;
	}

	public SwipeLayout(Context context) {
		this(context, null);
	}

	public SwipeLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SwipeLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		// 1. 创建ViewDragHelper, 初始化参数
		mHelper = ViewDragHelper.create(this, mCallback);
		
	}
	
	// 3. 重写事件回调
	ViewDragHelper.Callback mCallback = new ViewDragHelper.Callback() {
		
		// 返回值决定了是否可以拖拽
		@Override
		public boolean tryCaptureView(View child, int pointerId) {
			return true;
		}
		
		// 水平方向拖拽范围, > 0
		public int getViewHorizontalDragRange(View child) {
			return mRange;
		};
		
		// 位置发生之前, 修正将要移动到的位置. 
		// left 建议移动到的位置, 返回值决定了真正要移动到的位置
		public int clampViewPositionHorizontal(View child, int left, int dx) {
			
			if(child == mFrontView){
				if(left < -mRange){
					// 限定前布局左边界
					return -mRange;
				}else if (left > 0 ) {
					// 限定前布局右边界
					return 0;
				}
			}else if (child == mBackView) {
				if(left < mWidth - mRange){
					// 限定后布局左边界
					return mWidth - mRange;
				}else if (left > mWidth) {
					// 限定后布局右边界
					return mWidth;
					
				}
			}
			return left;
		}

		// 移动结束
		@Override
		public void onViewPositionChanged(View changedView, int left, int top,
				int dx, int dy) {
			
			if(changedView == mFrontView){
				// 将前布局刚刚发生的水平偏移量, 转交给后布局
				mBackView.offsetLeftAndRight(dx);
			}else if(changedView == mBackView){
				// 将后布局刚刚发生的水平偏移量, 转交给前布局
				mFrontView.offsetLeftAndRight(dx);
			}
			
			dispathDragEvent();
			
			invalidate();
			
		}

		// 当视图被释放, 做动画
		@Override
		public void onViewReleased(View releasedChild, float xvel, float yvel) {
			
			// 向右+, 向左-
			// open
			if(xvel == 0 && mFrontView.getLeft() < - mRange / 2.0f){
				open();
			} else if(xvel < 0){
				open();
			} else {
				close();
			}
			
		};
		
		
	};
	
	// 2. 转交 拦截判断, 触摸事件处理
	public boolean onInterceptTouchEvent(android.view.MotionEvent ev) {
		return mHelper.shouldInterceptTouchEvent(ev);
	};
	
	protected void dispathDragEvent() {
		
		// 更新获取最新状态
		Status lastStatus = status;
		status = updateStatus();
		
		// 状态更新监听, 状态发生变化执行
		if(lastStatus != status && onSwipeListener != null){
			if(status == Status.Open){
				onSwipeListener.onOpen(this);
			}else if (status == Status.Close) {
				onSwipeListener.onClose(this);
			}else if (status == Status.Swiping) {
				if(lastStatus == Status.Close){
					onSwipeListener.onStartOpen(this);
				}else if (lastStatus == Status.Open) {
					onSwipeListener.onStartClose(this);
				}
			}
		}
		
		
	}

	private Status updateStatus() {
		int left = mFrontView.getLeft();
		if(left == 0){
			return Status.Close;
		}else if (left == - mRange) {
			return Status.Open;
		}
		
		return Status.Swiping;
	}

	public void close() {
		close(true);
	}

	public void close(boolean isSmooth){
		int finalLeft = 0;
		if(isSmooth){
			// 1. 触发一个平滑动画
			if(mHelper.smoothSlideViewTo(mFrontView, finalLeft, 0)){
				// 动画还没结束, 需要重绘界面
				ViewCompat.postInvalidateOnAnimation(this);
			}
		}else {
			layoutContent(true);
		}
	}
	public void open() {
		open(true);
	}
	public void open(boolean isSmooth){
		int finalLeft = -mRange;
		if(isSmooth){
			// 1. 触发一个平滑动画
			if(mHelper.smoothSlideViewTo(mFrontView, finalLeft, 0)){
				// 动画还没结束, 需要重绘界面
				ViewCompat.postInvalidateOnAnimation(this);
			}
		}else {
			layoutContent(true);
		}
	}
	
	// 2. 维持动画的继续
	@Override
	public void computeScroll() {
		super.computeScroll();
		
		if(mHelper.continueSettling(true)){
			// true, 动画还没结束, 需要重绘界面
			ViewCompat.postInvalidateOnAnimation(this);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		try {
			mHelper.processTouchEvent(event);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return true; // 消费事件
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mBackView = getChildAt(0);
		mFrontView = getChildAt(1);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		mWidth = getMeasuredWidth();
		mHeight = getMeasuredHeight();
		mRange = mBackView.getMeasuredWidth();
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		// 摆放内容
		layoutContent(false);
		
	}

	private void layoutContent(boolean isOpen) {
		Rect frontRect = computeFrontRect(isOpen);
		mFrontView.layout(frontRect.left, frontRect.top, frontRect.right, frontRect.bottom);
		
		Rect backRect = computeBackRectViaFront(frontRect);
		mBackView.layout(backRect.left, backRect.top, backRect.right, backRect.bottom);
		
		// 将布局前置
		bringChildToFront(mFrontView);
	}

	/**
	 * 通过前布局, 计算后布局矩形
	 * @param frontRect
	 * @return
	 */
	private Rect computeBackRectViaFront(Rect frontRect) {
		int left = frontRect.right;
		return new Rect(left, 0, left + mRange, 0 + mHeight);
	}

	/**
	 * 计算前布局矩形
	 * @param isOpen
	 * @return
	 */
	private Rect computeFrontRect(boolean isOpen) {
		int left = 0;
		if(isOpen){
			left = -mRange;
		}
		return new Rect(left, 0, left + mWidth, 0 + mHeight);
	}

}
