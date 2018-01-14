package zp.com.zpfunctiondemo.custom.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;

import zp.com.zpfunctiondemo.arouter.ArouterPath;

/**
 * Created by Administrator on 2018/1/14 0014.
 */
@Route(path = ArouterPath.CUSTOM_PAGE, name = "自定义功能页面")
public class ZpCustomActivity extends Activity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


}
