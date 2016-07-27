package brotherjing.com.leomalite.view;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Map;

import brotherjing.com.leomalite.R;
import brotherjing.com.leomalite.util.Logger;

/**
 * Created by jingyanga on 2016/7/25.
 */
public class LeomaFragment extends Fragment{

    private LeomaWebView webView;
    private LeomaActivity activity;

    //private WeakReference<Bitmap> drawingCache;
    private Bitmap drawingCache;
    private FrameLayout drawingCacheLayout;

    public static LeomaFragment newInstance(LeomaActivity activity){
        return new LeomaFragment(activity);
    }

    private LeomaFragment(LeomaActivity activity){
        this.activity = activity;
        init();
    }

    private void init(){
        webView = activity.createWebView();
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        webView.setLayoutParams(params);

        drawingCacheLayout = new FrameLayout(activity){
            @Override
            protected void onDraw(Canvas canvas) {
                Logger.i("on draw");
                if(drawingCache!=null){
                    canvas.drawBitmap(drawingCache,0,0,null);
                    Logger.i("cache drawn");
                }
            }
            @Override
            public void draw(Canvas canvas) {
                super.draw(canvas);
                /*if(drawingCache.get()!=null) {
                    canvas.drawBitmap(drawingCache.get(), 0, 0, null);
                    Logger.i("cache drawn");
                }*/
                if(drawingCache!=null){
                    canvas.drawBitmap(drawingCache,0,0,null);
                    Logger.i("cache drawn");
                }
            }
        };
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /*if(webView!=null&&webView.getParent()!=null&&container!=null){
            container.removeView(webView);
        }
        return webView;*/
        FrameLayout mainView = (FrameLayout)inflater.inflate(R.layout.leoma_fragment,container,false);
        mainView.addView(webView);
        return mainView;
    }

    public void showDrawingCache(Bitmap drawingCache){
        //this.drawingCache = new WeakReference<>(drawingCache);
        this.drawingCache = drawingCache;
        if(drawingCacheLayout.getParent()==null){
            if ((getView()) != null) {
                ((FrameLayout)getView()).addView(drawingCacheLayout,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }
        }
        //((FrameLayout)getView()).postInvalidate();
        //drawingCacheLayout.postInvalidate();
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
        //drawingCache.clear();
    }

    public LeomaWebView getWebView() {
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
                webView.setLayerType(View.LAYER_TYPE_HARDWARE,null);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        return animation;
    }
}
