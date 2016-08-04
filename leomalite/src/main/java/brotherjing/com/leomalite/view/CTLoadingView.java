package brotherjing.com.leomalite.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import brotherjing.com.leomalite.R;
import brotherjing.com.leomalite.util.ConvertUtils;
import brotherjing.com.leomalite.util.Logger;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class CTLoadingView {
	private ViewGroup superView;
	private RelativeLayout holder, loadingView;
	private Context mContext;
	private int View_Size = 63;
	private int Circle_Size = 49;
	private int Middle_Width = 28;
	private int Middle_Height = 34;
	private ImageView circle;
	private Handler handler;
	private Runnable timeOutTask;
	private boolean enabled = true;
	private ViewGroup.LayoutParams holderParams;
	private static final long DEFAULT_TIMEOUT_MILL = 10 * 1000;

	public CTLoadingView(ViewGroup father, Context ctx) {
		this.mContext = ctx;
		this.superView = father;
		this.handler = new Handler();
		View_Size = (int) ConvertUtils.dipToPx(mContext, View_Size);
		Circle_Size = (int) ConvertUtils.dipToPx(mContext, Circle_Size);
		Middle_Width = (int) ConvertUtils.dipToPx(mContext, Middle_Width);
		Middle_Height = (int) ConvertUtils.dipToPx(mContext, Middle_Height);
		intUI();
	}

	private void intUI() {
		this.holder = new RelativeLayout(mContext);
		holderParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);

		this.loadingView = new RelativeLayout(mContext);
		LayoutParams params = new LayoutParams(View_Size, View_Size);
		params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
		this.loadingView.setBackgroundResource(R.drawable.ct_loading_shape);

		ImageView middle = new ImageView(mContext);
		middle.setImageResource(R.drawable.loading_middle);
		LayoutParams middleP = new LayoutParams(Middle_Width, Middle_Height);
		middleP.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
		this.loadingView.addView(middle, middleP);

		circle = new ImageView(mContext);
		circle.setImageResource(R.drawable.loading_circle);
		LayoutParams circleP = new LayoutParams(Circle_Size, Circle_Size);
		circleP.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
		this.loadingView.addView(circle, circleP);

		this.holder.addView(this.loadingView, params);

		this.holder.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return !enabled;
			}
		});

		this.timeOutTask = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (holder.getParent() != null) {
					updateLoading(false, holder.isEnabled(), 0);
				}
			}
		};
	}

	ObjectAnimator animator;

	private void initAnime() {
		disInitAnime();
		//circle.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.frame_loading_rotate));
		//animator = ObjectAnimator.ofFloat(circle,View.TRANSLATION_X,-20,20);
		animator = ObjectAnimator.ofFloat(circle, View.ROTATION,0,360);
		animator.setRepeatMode(ValueAnimator.RESTART);
		animator.setRepeatCount(ValueAnimator.INFINITE);
		animator.setInterpolator(new LinearInterpolator());
		animator.setDuration(1000);
		this.loadingView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		animator.start();
	}

	private void disInitAnime() {
		//circle.clearAnimation();
		if(animator!=null&&animator.isRunning()){
			animator.cancel();
		}
		this.loadingView.setLayerType(View.LAYER_TYPE_NONE, null);
		if(this.loadingView.isHardwareAccelerated()){
			Logger.i("window is hardware accelerate");
		}else{
			Logger.i("window is not hardware accelerate");
		}
	}

	public void updateLoading(boolean show, final boolean enabled, long timeout) {
		if (show) {
			this.enabled = enabled;
			if (this.holder.getParent() != null)
				this.holder.bringToFront();
			else
				this.superView.addView(this.holder, holderParams);
			initAnime();
			handler.removeCallbacks(this.timeOutTask);
			handler.postDelayed(this.timeOutTask, timeout);
		} else {
			this.superView.removeView(this.holder);
			handler.removeCallbacks(this.timeOutTask);
			disInitAnime();

		}
	}

	public void updateLoading(boolean show, final boolean enable) {
		updateLoading(show, enable, DEFAULT_TIMEOUT_MILL);
	}
}
