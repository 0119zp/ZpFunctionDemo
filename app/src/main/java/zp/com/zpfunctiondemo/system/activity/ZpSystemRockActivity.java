package zp.com.zpfunctiondemo.system.activity;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;

import zp.com.zpfunctiondemo.R;
import zp.com.zpfunctiondemo.arouter.ArouterPath;


/**
 * 手机摇一摇
 * 重力感应监听
 * Created by Administrator on 2018/1/6 0006.
 */
@Route(path = ArouterPath.SYSTEM_ROCK_PAGE, name = "手机摇一摇")
public class ZpSystemRockActivity extends Activity {

    private SensorManager mSensorManager;

    // 重力感应监听 摇一摇
    private SensorEventListener sensorEventListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {
            // 传感器信息改变时执行该方法
            float[] values = event.values;
            // x轴方向的重力加速度，向右为正
            float x = values[0];
            // y轴方向的重力加速度，向前为正
            float y = values[1];
            // z轴方向的重力加速度，向上为正
            float z = values[2];
            Log.e("zpan", "=======ss===== " + String.format("%s-%s-%s", Math.abs(x), Math.abs(y), Math.abs(z)));
            int mediumValue = 10;
            if (Math.abs(x) > mediumValue
                    || Math.abs(y) > mediumValue
                    || Math.abs(z) > mediumValue) {
                Toast.makeText(ZpSystemRockActivity.this, "我是你摇出来的\n开心吗\t~-~", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_rock);

        initRock();
    }

    private void initRock() {
        // 摇一摇
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 注册监听器
        if (mSensorManager != null) {
            // 第一个参数是Listener，第二个参数是所得传感器类型，第三个参数值获取传感器信息的频率
            mSensorManager.registerListener(sensorEventListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 反注册重力感应监听
        mSensorManager.unregisterListener(sensorEventListener);
    }
}
