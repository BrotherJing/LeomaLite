package brotherjing.com.leomalite;

import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import brotherjing.com.leomalite.model.DoNavigationInfo;
import brotherjing.com.leomalite.model.PrepareNavigationInfo;
import brotherjing.com.leomalite.util.Logger;
import brotherjing.com.leomalite.view.LeomaActivity;
import brotherjing.com.leomalite.view.LeomaFragment;

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
        if(true||animated){
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
        if(true||animated){
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
        transaction.hide(currentFragment());
        if(currentLoadingFragment.isAdded())
            transaction.show(currentLoadingFragment);
        else
            transaction.add(fragmentContainer.getId(),currentLoadingFragment);
        transaction.disallowAddToBackStack();
        transaction.commit();

        pushBackPool(fragmentStack.remove(currentIndex));
        fragmentStack.add(currentIndex,currentLoadingFragment);
    }

    public void prepareNavigation(PrepareNavigationInfo prepareNavigationInfo){
        if(currentPrepareNavigationInfo !=null)return;
        currentPrepareNavigationInfo = prepareNavigationInfo;

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
        }
        else if(prepareNavigationInfo.getNavigateType() == PrepareNavigationInfo.NAVI_RELOAD && TextUtils.isEmpty(prepareNavigationInfo.getUrl())){
            currentLoadingFragment.getWebView().reload();
        }else{
            currentLoadingFragment.initWebView(prepareNavigationInfo.getUrl(), prepareNavigationInfo.getData());
        }

    }

    public void doNavigation(DoNavigationInfo doNavigationInfo){
        //long sleepTime = doNavigationInfo.getSleepTime();

        if(currentPrepareNavigationInfo==null){
            currentFragment().getWebView().setJSBackMethod(doNavigationInfo.getJSBackMethod());
            if(!TextUtils.isEmpty(doNavigationInfo.getMiddlePage())&&currentIndex<fragmentStack.size()-1){
                fragmentStack.get(currentIndex+1).getWebView().loadMiddlePage(doNavigationInfo.getMiddlePage());
            }
            return;
        }

        currentLoadingFragment.getWebView().setJSBackMethod(doNavigationInfo.getJSBackMethod());
        if(currentPrepareNavigationInfo.getNavigateType()==PrepareNavigationInfo.NAVI_PUSH){
            performPush(currentPrepareNavigationInfo.isAnimated());
        }else if(currentPrepareNavigationInfo.getNavigateType()==PrepareNavigationInfo.NAVI_TAB){
            performTab();
        }
        finishNavigation(0,true);
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
}
