package com.example.graduation_design.widget;


import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * 侧拉删除
 * @author smart_lll
 *
 */
public class MySwipeLayout extends FrameLayout {

	private ViewDragHelper mDragHelper;
	private View mBackView;
	private View mFrontView;
	private float mDownX;
	
	private Status status = Status.Close;
	private OnSwipeChangeListener onSwipeChangeListener;
	private GestureDetectorCompat mGestureDetector;
	
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public OnSwipeChangeListener getOnSwipeChangeListener() {
		return onSwipeChangeListener;
	}

	public void setOnSwipeChangeListener(OnSwipeChangeListener onSwipeChangeListener) {
		this.onSwipeChangeListener = onSwipeChangeListener;
	}
	public static enum Status {
		Close, Open, Draging
	}
	public interface OnSwipeChangeListener {
		void onOpen(MySwipeLayout layout);
		void onClose(MySwipeLayout layout);
		void onDraging(MySwipeLayout layout);
		
		// 准备开启
		void onStartOpen(MySwipeLayout layout);
		// 准备关闭
		void onStartClose(MySwipeLayout layout);
	}
	
	public MySwipeLayout(Context context) {
		this(context, null);
	}

	public MySwipeLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MySwipeLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		mDragHelper = ViewDragHelper.create(this, callback);
		mGestureDetector = new GestureDetectorCompat(context, mOnGestureListener);
		
	}
	
	private SimpleOnGestureListener mOnGestureListener = new GestureDetector.SimpleOnGestureListener() {

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			// 当横向移动距离大于等于纵向时，返回true
			return Math.abs(distanceX) >= Math.abs(distanceY);
		}
	};
	ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {

		@Override
		public boolean tryCaptureView(View child, int pointerId) {
			return true;
		}
		
		public int getViewHorizontalDragRange(View child) {
			return mRange;
		};
		
		public int clampViewPositionHorizontal(View child, int left, int dx) {
			
			// 限定拖拽范围
			if(child == mFrontView){
				if(left < -mRange){
					return -mRange;
				}else if (left > 0) {
					return 0;
				}
			}else if (child == mBackView) {
				if(left < mWidth - mRange){
					return mWidth - mRange;
				} else if (left > mWidth) {
					return mWidth;
				}
			}
			
			return left;
		}

		// 位置已经发生了改变
		@Override
		public void onViewPositionChanged(View changedView, int left, int top,
				int dx, int dy) {
			if(changedView == mFrontView){
				// 让变化量生效
				mBackView.offsetLeftAndRight(dx);
			} else if (changedView == mBackView) {
				mFrontView.offsetLeftAndRight(dx);
			}
			
			dispatchDragEvent();
			
			invalidate();
		}

		@Override
		public void onViewReleased(View releasedChild, float xvel, float yvel) {
			super.onViewReleased(releasedChild, xvel, yvel);
			
			// xvel 横向速度, 向左-, 向右+
			
			// 释放动画
			if(xvel == 0 && mFrontView.getLeft() < -mRange * 0.5f){
				open();
			}else if (xvel < 0) {
				open();
			}else {
				close();
			}
			
			
		};
		
	};
	private int mHeight;
	private int mWidth;
	private int mRange;
	
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);

		// 进行控件的摆放, 根据当前打开状态
		layoutContent(false);
	};
	
	/**
	 * 更新状态, 执行回调
	 */
	protected void dispatchDragEvent() {
		
		Status preStatus = status;
		
		// 更新状态
		status = updateStatus();
		
		if(onSwipeChangeListener == null){
			return;
		}
		
		onSwipeChangeListener.onDraging(this);
		
		if(preStatus != status){
			// 状态发生了改变
			if(status == Status.Close){
				onSwipeChangeListener.onClose(this);
			} else if (status == Status.Open) {
				onSwipeChangeListener.onOpen(this);
			} else if (status == Status.Draging) {
				if(preStatus == Status.Close){
					// 要开启
					onSwipeChangeListener.onStartOpen(this);
				}else if (preStatus == Status.Open) {
					// 要关闭
					onSwipeChangeListener.onStartClose(this);
				}
			}
			
		}
		
	}

	private Status updateStatus() {
		int left = mFrontView.getLeft();
		if(left == 0){
			return Status.Close;
		}else if (left == -mRange) {
			return Status.Open;
		}
		
		return Status.Draging;
	}

	@Override
	public void computeScroll() {
		super.computeScroll();
		// 维持动画的继续
		if(mDragHelper.continueSettling(true)){
			ViewCompat.postInvalidateOnAnimation(this);
		}
		
	}
	
	public void close() {
		close(true);
	}
	public void close(boolean isSmooth){
		if(isSmooth){
			// 触发平滑动画
			if(mDragHelper.smoothSlideViewTo(mFrontView, 0, 0)){
				ViewCompat.postInvalidateOnAnimation(this);
			}
		}else {
			layoutContent(false);
		}
	}

	public void open() {
		open(true);
	}
	public void open(boolean isSmooth) {
		if(isSmooth ){
			// 触发平滑动画
			if(mDragHelper.smoothSlideViewTo(mFrontView, -mRange, 0)){
				ViewCompat.postInvalidateOnAnimation(this);
			}
		}else{
			layoutContent(true);
		}
	}
	

	/**
	 * 根据当前的开启状态摆放view
	 * @param isOpen
	 */
	private void layoutContent(boolean isOpen) {
		
		// 计算得到前View的位置矩形
		Rect frontRect = computeFrontRect(isOpen);
		mFrontView.layout(frontRect.left, frontRect.top, frontRect.right, frontRect.bottom);
		
		// 通过前视图的矩形获取后视图的矩形
		Rect backRect = computeBackRectViaFront(frontRect);
		mBackView.layout(backRect.left, backRect.top, backRect.right, backRect.bottom);
		
		// 将某个View前置
		bringChildToFront(mFrontView);
	}

	/**
	 * 通过前视图的矩形获取后视图的矩形
	 * @param frontRect
	 * @return
	 */
	private Rect computeBackRectViaFront(Rect frontRect) {
		int left = frontRect.right;
		return new Rect(left, 0, left + mRange, 0 + mHeight);
	}

	/**
	  * 计算得到前View的位置矩形
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

	public boolean onInterceptTouchEvent(android.view.MotionEvent ev) {
		return  mDragHelper.shouldInterceptTouchEvent(ev) & mGestureDetector.onTouchEvent(ev);// mDragHelper.shouldInterceptTouchEvent(ev);
	};
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		/*//int left = mFrontView.getLeft();
		float startX = 0;
		float newX = 0;
		float endX = 0;
		
		try {
			mDragHelper.processTouchEvent(event);
		} catch (Exception e) {
			e.printStackTrace();
		}
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startX = event.getX();
			endX = startX;
			L.i("LLL", "ACTION_DOWN:"+startX);
			if((event.getAction()==MotionEvent.ACTION_DOWN))
			return true;
			break;
		case MotionEvent.ACTION_MOVE:
			newX = event.getX();
			endX = newX;
			L.i("LLL", "ACTION_MOVE:"+newX);
			break;
		case MotionEvent.ACTION_UP:
			endX = event.getX();
			if((event.getAction()==MotionEvent.ACTION_UP)&&startX-endX>20){
				L.i("LLL", "Result:"+true);
				return true;
			}
			L.i("LLL", "ACTION_UP:"+endX);
			break;
		}
		if((event.getAction()==MotionEvent.ACTION_UP)&&startX-endX>20){
			L.i("LLL", "Result:"+true);
			return true;
		}else{
			L.i("LLL", "Result:"+false);
			return false;
		}*/
		switch (MotionEventCompat.getActionMasked(event)) {
		case MotionEvent.ACTION_DOWN:
			mDownX = event.getRawX();
			break;
		case MotionEvent.ACTION_MOVE:
            
			float deltaX = event.getRawX() - mDownX;
			if(deltaX > mDragHelper.getTouchSlop()){
				// 请求父级View不拦截touch事件
				requestDisallowInterceptTouchEvent(true);
			}
			break;
		case MotionEvent.ACTION_UP:
			mDownX = 0;
		default :
			break;
	}
	
		try {
			mDragHelper.processTouchEvent(event);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		mHeight = mFrontView.getMeasuredHeight();
		mWidth = mFrontView.getMeasuredWidth();
		
		// 通过backview的宽度得到拖拽范围
		mRange = mBackView.getMeasuredWidth();
	}
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		mBackView = getChildAt(0);
		mFrontView = getChildAt(1);
		
		
	}
	/**
	 * 得到item中的前视图
	 * @return
	 */
	public View getFrontView() {
		return mFrontView;
	}
}
