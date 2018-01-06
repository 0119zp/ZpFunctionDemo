package zp.com.zpfunctiondemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;

import zp.com.zpfunctiondemo.arouter.ArouterPath;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 系统功能
     */
    public void setSystem(View view) {
        ARouter.getInstance().build(ArouterPath.SYSTEM_PAGE).navigation();
    }


}
