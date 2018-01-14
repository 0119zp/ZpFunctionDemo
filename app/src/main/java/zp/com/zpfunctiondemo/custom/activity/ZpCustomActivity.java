package zp.com.zpfunctiondemo.custom.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;

import zp.com.zpfunctiondemo.R;
import zp.com.zpfunctiondemo.arouter.ArouterPath;

/**
 * Created by Administrator on 2018/1/14 0014.
 */
@Route(path = ArouterPath.CUSTOM_PAGE, name = "自定义功能页面")
public class ZpCustomActivity extends Activity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpage_tab);
    }

    /**
     * viewpage tab
     */
    public void setTab(View view) {
        ARouter.getInstance().build(ArouterPath.CUSTOM_VIEWPAGE_PAGE).navigation();
    }


}
