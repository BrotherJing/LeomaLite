package brotherjing.com.leomalite.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import java.lang.ref.WeakReference;

import brotherjing.com.leomalite.R;
import brotherjing.com.leomalite.util.Logger;

/**
 * Created by jingyanga on 2016/7/25.
 */
public class LeomaFragment extends Fragment{

    private LeomaWebView webView;
    private LeomaActivity activity;

    private WeakReference<Bitmap> drawingCache;
    private FrameLayout drawingCacheLayout;

    public static LeomaFragment newInstance(LeomaActivity activity){
        return new LeomaFragment(activity);
    }

    private LeomaFragment(LeomaActivity activity){
        this.activity = activity;
    }

    private void initWebView(){
        webView = activity.createWebView();
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        webView.setLayoutParams(params);
    }

    private void initDrawingCacheView(){

        drawingCacheLayout = new FrameLayout(activity){
            @Override
            public void draw(Canvas canvas) {
                super.draw(canvas);
                if(drawingCache.get()!=null){
                    canvas.drawBitmap(drawingCache.get(),0,0,null);
                    Logger.i("cache drawn");
                }
            }
        };
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(webView==null)initWebView();
        if(drawingCacheLayout==null)initDrawingCacheView();
        FrameLayout mainView = (FrameLayout)inflater.inflate(R.layout.leoma_fragment,container,false);
        mainView.addView(webView);
        return mainView;
    }

    public void showDrawingCache(Bitmap drawingCache){
        this.drawingCache = new WeakReference<>(drawingCache);
        if(drawingCacheLayout==null)initDrawingCacheView();
        if(drawingCacheLayout.getParent()==null){
            if ((getView()) != null) {
                ((FrameLayout)getView()).addView(drawingCacheLayout,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }
        }
        drawingCacheLayout.setBackgroundColor(0xffffff);
        webView.setOnLeomaWebViewFinishListener(new LeomaWebView.OnLeomaWebViewLoadFinishListener() {
            @Override
            public void onFinished(LeomaWebView webView) {
                hideDrawingCache();
            }
        });
    }

    public void hideDrawingCache(){
        if ((getView()) != null) {
            ((FrameLayout)getView()).removeView(drawingCacheLayout);
        }
        drawingCache.clear();
    }

    public LeomaWebView getWebView() {
        if(webView==null)initWebView();
        return webView;
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if(nextAnim==0)return super.onCreateAnimation(transit,enter,nextAnim);
        Animation animation = AnimationUtils.loadAnimation(activity,nextAnim);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                webView.setLayerType(View.LAYER_TYPE_HARDWARE,null);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                webView.setLayerType(View.LAYER_TYPE_NONE,null);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        return animation;
    }
}
