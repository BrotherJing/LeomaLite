package brotherjing.com.leomalite.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import brotherjing.com.leomalite.cache.LeomaBitmapCache;
import brotherjing.com.leomalite.R;
import brotherjing.com.leomalite.model.DoNavigationInfo;
import brotherjing.com.leomalite.model.PrepareNavigationInfo;
import brotherjing.com.leomalite.util.Logger;

/**
 * Created by jingyanga on 2016/7/25.
 */
public class LeomaNavigator {

    private LeomaActivity activity;
    private FrameLayout fragmentContainer;

    private List<LeomaFragment> fragmentStack;
    private List<LeomaFragment> fragmentPool;
    private int currentIndex;
    private int initCapacity;

    private PrepareNavigationInfo currentPrepareNavigationInfo;
    private LeomaFragment currentLoadingFragment;

    public LeomaNavigator(LeomaActivity activity, FrameLayout fragmentContainer, int initCapacity){
        this.activity = activity;
        this.fragmentContainer = fragmentContainer;
        this.initCapacity = initCapacity;

        fragmentStack = new ArrayList<>();
        fragmentPool = new ArrayList<>();
        for(int i=0;i<initCapacity;++i){
            fragmentStack.add(LeomaFragment.newInstance(activity));
            fragmentPool.add(LeomaFragment.newInstance(activity));
        }
        currentIndex = 0;
    }

    public LeomaFragment currentFragment(){
        return fragmentStack.get(currentIndex);
    }

    public void performShowBottom(){
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.add(fragmentContainer.getId(), fragmentStack.get(0));
        transaction.disallowAddToBackStack();
        transaction.commit();
    }

    public void performPop(boolean animated){
        Logger.i("perform pop");
        if(currentIndex<=0)return;
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        if(animated||true){
            transaction.setCustomAnimations(R.anim.frame_anim_from_left,R.anim.frame_anim_to_right);
        }
        transaction.show(fragmentStack.get(currentIndex-1));
        transaction.hide(fragmentStack.get(currentIndex));
        transaction.disallowAddToBackStack();
        transaction.commit();

        currentIndex--;
        if(fragmentStack.size()>initCapacity){
            for(int i=currentIndex;i<fragmentStack.size();++i){
                pushBackPool(fragmentStack.remove(currentIndex+1));
            }
        }
    }

    public void performPush(boolean animated){
        if(currentIndex>=fragmentStack.size()-1)return;
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        if(animated||true){
            transaction.setCustomAnimations(R.anim.frame_anim_from_right,R.anim.frame_anim_to_left);
        }
        if(currentLoadingFragment.isAdded())
            transaction.show(fragmentStack.get(currentIndex+1));
        else
            transaction.add(fragmentContainer.getId(),currentLoadingFragment);
        transaction.hide(fragmentStack.get(currentIndex));
        transaction.disallowAddToBackStack();
        transaction.commit();

        currentIndex++;
    }

    public void performTab(){
        if(currentLoadingFragment==null)return;
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.frame_anime_stay, R.anim.frame_anime_stay);
        if(currentLoadingFragment.isAdded())
            transaction.show(currentLoadingFragment);
        else
            transaction.add(fragmentContainer.getId(),currentLoadingFragment);
        transaction.hide(currentFragment());
        transaction.disallowAddToBackStack();
        transaction.commit();

        pushBackPool(fragmentStack.remove(currentIndex));
        fragmentStack.add(currentIndex,currentLoadingFragment);
    }

    public void prepareNavigation(PrepareNavigationInfo prepareNavigationInfo){
        if(currentPrepareNavigationInfo !=null)return;
        currentPrepareNavigationInfo = prepareNavigationInfo;

        LeomaBitmapCache.storeDrawingCache(currentFragment().getWebView().getInitURL(),
                currentFragment().getWebView());

        //decide current loading fragment
        switch (prepareNavigationInfo.getNavigateType()){
            case PrepareNavigationInfo.NAVI_TAB:
                currentLoadingFragment = fetchFromPool();
                break;
            case PrepareNavigationInfo.NAVI_PUSH:
                if(currentIndex==fragmentStack.size()-1){
                    fragmentStack.add(fetchFromPool());
                }
                currentLoadingFragment = fragmentStack.get(currentIndex+1);
                break;
            case PrepareNavigationInfo.NAVI_POP:
                if(currentIndex<=0){
                    currentPrepareNavigationInfo = null;
                    return;
                }
                currentLoadingFragment = fragmentStack.get(currentIndex-1);
                break;
            case PrepareNavigationInfo.NAVI_RELOAD:
                currentLoadingFragment = currentFragment();
                break;
            default:break;
        }

        if(prepareNavigationInfo.getNavigateType()==PrepareNavigationInfo.NAVI_POP){
            performPop(prepareNavigationInfo.isAnimated());
            finishNavigation(0, true);
        } else if(prepareNavigationInfo.getNavigateType() == PrepareNavigationInfo.NAVI_RELOAD && TextUtils.isEmpty(prepareNavigationInfo.getUrl())){
            currentLoadingFragment.getWebView().reload();
        }else{
            currentLoadingFragment.getWebView().initWithURL(prepareNavigationInfo.getUrl(), prepareNavigationInfo.getData());
        }
    }

    public void doFastNavigation(){
        if(currentPrepareNavigationInfo!=null&&currentPrepareNavigationInfo.getNavigateType()==PrepareNavigationInfo.NAVI_POP){
            return;
        }
        if(LeomaBitmapCache.getDrawingCache(currentLoadingFragment.getWebView().getInitURL())!=null){
            Logger.i("drawing cache hit: "+currentLoadingFragment.getWebView().getInitURL());
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Logger.i(currentLoadingFragment.getWebView().getInitURL());
                    currentLoadingFragment.showDrawingCache(LeomaBitmapCache.getDrawingCache(currentLoadingFragment.getWebView().getInitURL()));
                    doNavigation();
                    Logger.pin("actually do navigation");
                }
            },100);

        }else {
            currentLoadingFragment.getWebView().setOnLeomaWebViewFinishListener(new LeomaWebView.OnLeomaWebViewLoadFinishListener() {
                @Override
                public void onFinished(LeomaWebView webView) {
                    doNavigation();
                    Logger.pin("actually do navigation");
                }
            });
        }
    }

    public void completeNavigationInfo(DoNavigationInfo doNavigationInfo){
        if(currentPrepareNavigationInfo==null){
            currentFragment().getWebView().setJSBackMethod(doNavigationInfo.getJSBackMethod());
            if(!TextUtils.isEmpty(doNavigationInfo.getMiddlePage())&&currentIndex<fragmentStack.size()-1){
                fragmentStack.get(currentIndex+1).getWebView().loadMiddlePage(doNavigationInfo.getMiddlePage());
            }
            return;
        }

        currentLoadingFragment.getWebView().setJSBackMethod(doNavigationInfo.getJSBackMethod());
    }

    public void doNavigation(){
        if(currentPrepareNavigationInfo==null)return;
        if(currentPrepareNavigationInfo.getNavigateType()==PrepareNavigationInfo.NAVI_PUSH){
            performPush(currentPrepareNavigationInfo.isAnimated());
        }else if(currentPrepareNavigationInfo.getNavigateType()==PrepareNavigationInfo.NAVI_TAB){
            performTab();
        }
        finishNavigation(0,true);
    }

    public boolean handleBackPressed(){
        if(currentFragment().getWebView().handleBackPressed()){
            return true;
        }
        if(currentIndex>0){
            Logger.i("navigator handle back press");
            performPop(true);
            return true;
        }
        return false;
    }

    private static final int MIN_START_SLIDE_X = 40;
    private static final int MIN_FINISH_SLIDE_X = 360;

    private int startSlideX;
    private LeomaFragment slideTo,slideFrom;
    private boolean isSliding = false;

    public boolean handleTouchEvent(final MotionEvent ev){
        if(currentIndex==0)return false;
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                startSlideX = (int)ev.getX();
                return false;
            case MotionEvent.ACTION_MOVE:
                if(startSlideX>MIN_START_SLIDE_X||ev.getX()-startSlideX<ViewConfiguration.get(activity).getScaledTouchSlop()){
                    return false;
                }
                if(!isSliding) {
                    isSliding = true;
                    slideTo = fragmentStack.get(currentIndex - 1);
                    slideFrom = currentFragment();
                    FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                    transaction.show(slideTo).show(slideFrom).commit();
                    if (slideFrom.getView() != null)
                        slideFrom.getView().setLayerType(View.LAYER_TYPE_HARDWARE, null);
                }
                else {
                    if (slideFrom.getView() != null) {
                        slideFrom.getView().setTranslationX(ev.getX() - startSlideX);
                    }
                }
                return true;
            case MotionEvent.ACTION_UP:
                if(!isSliding)return false;
                isSliding = false;
                ValueAnimator animator = ValueAnimator.ofFloat(1f,0f);
                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        if(slideFrom.getView()!=null)
                            slideFrom.getView().setLayerType(View.LAYER_TYPE_NONE,null);
                        if(ev.getX()-startSlideX>MIN_FINISH_SLIDE_X){
                            if(slideFrom.getView()!=null)
                                slideFrom.getView().setTranslationX(slideFrom.getView().getWidth());
                            FragmentTransaction transaction1 = activity.getSupportFragmentManager().beginTransaction();
                            transaction1.show(slideTo);
                            transaction1.hide(slideFrom);
                            transaction1.disallowAddToBackStack();
                            transaction1.commit();
                            currentIndex--;
                            if(fragmentStack.size()>initCapacity){
                                for(int i=currentIndex;i<fragmentStack.size();++i){
                                    pushBackPool(fragmentStack.remove(currentIndex+1));
                                }
                            }
                            slideTo.getWebView().executeJS("fw.Native.Page_CrossNotes(14)");
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    slideFrom.getView().setTranslationX(0);
                                }
                            },50);
                        }else{
                            slideFrom.getView().setTranslationX(0);
                            FragmentTransaction transaction1 = activity.getSupportFragmentManager().beginTransaction();
                            transaction1.hide(slideTo);
                            transaction1.disallowAddToBackStack();
                            transaction1.commit();
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
                if(ev.getX()-startSlideX>MIN_FINISH_SLIDE_X){
                    animator.setFloatValues(slideFrom.getView().getTranslationX(),slideFrom.getView().getWidth());
                    animator.setDuration((long)(300*(slideFrom.getView().getWidth()-slideFrom.getView().getTranslationX())/slideFrom.getView().getWidth()));
                }else{
                    animator.setFloatValues(slideFrom.getView().getTranslationX(),0f);
                    animator.setDuration((long)(300*slideFrom.getView().getTranslationX()/slideFrom.getView().getWidth()));
                }
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        float currentValue = (Float) valueAnimator.getAnimatedValue();
                        slideFrom.getView().setTranslationX(currentValue);
                    }
                });
                animator.start();
                return true;
        }
        return false;
    }

    private void finishNavigation(long delay, final boolean executeCallback){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(executeCallback){
                    currentLoadingFragment.getWebView().executeJS(
                            currentPrepareNavigationInfo.getCrossPageCallbackMethod()+"("+
                            currentPrepareNavigationInfo.getCrossPageCallbackParams()+")");
                    currentPrepareNavigationInfo.setCrossPageCallbackMethod(null);
                    currentPrepareNavigationInfo.setCrossPageCallbackParams(null);
                }
                currentLoadingFragment = null;
                currentPrepareNavigationInfo = null;
            }
        }, delay);
    }

    private LeomaFragment fetchFromPool(){
        if(!fragmentPool.isEmpty()){
            return fragmentPool.remove(0);
        }else{
            return LeomaFragment.newInstance(activity);
        }
    }

    private void pushBackPool(LeomaFragment fragment){
        fragmentPool.add(fragment);
    }

    public int indexOf(LeomaFragment leomaFragment){
        return fragmentStack.indexOf(leomaFragment);
    }
}
