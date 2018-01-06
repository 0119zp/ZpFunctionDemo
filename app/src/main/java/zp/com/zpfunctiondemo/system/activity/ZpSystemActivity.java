package zp.com.zpfunctiondemo.system.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;

import zp.com.zpfunctiondemo.R;
import zp.com.zpfunctiondemo.arouter.ArouterPath;

/**
 * Created by Administrator on 2018/1/6 0006.
 */
@Route(path = ArouterPath.SYSTEM_PAGE, name = "系统功能页面")
public class ZpSystemActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zp_system);
    }

    /**
     * 手机摇一摇
     */
    public void setRock(View view) {
        ARouter.getInstance().build(ArouterPath.SYSTEM_ROCK_PAGE).navigation();
    }

}
