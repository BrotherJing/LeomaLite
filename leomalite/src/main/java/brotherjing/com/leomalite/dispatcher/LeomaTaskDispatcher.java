package brotherjing.com.leomalite.dispatcher;

import android.os.Handler;
import android.os.HandlerThread;

import java.util.ArrayList;

/**
 * Created by jingyanga on 2016/7/26.
 */
public class LeomaTaskDispatcher {

    private static final int POOL_SIZE = 2;
    private static final String LOOPER_NAME = "async_looper";

    private static final ArrayList<Handler> handlers;
    private static int currentIndex = 0;

    static {
        handlers = new ArrayList<>();
        for(int i=0;i<POOL_SIZE;++i){
            HandlerThread handlerThread = new HandlerThread(LOOPER_NAME+"_"+i);
            handlerThread.start();
            handlers.add(new Handler(handlerThread.getLooper()));
        }
    }

    public static void runBackground(final Runnable runnable){
        handlers.get(currentIndex).post(runnable);
        currentIndex = (currentIndex+1)%POOL_SIZE;
    }

}
