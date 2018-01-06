package zp.com.zpfunctiondemo;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;

/**
 * Created by Administrator on 2018/1/6 0006.
 */

public class ZpApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ARouter.openLog();      // 打印日志
        ARouter.openDebug();    // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        ARouter.init(this);    // 尽可能早，推荐在Application中初始化
    }

}
