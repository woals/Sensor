package com.yinyxn.sensor;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class OrientationSensorActivity extends AppCompatActivity {

    TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orientation_sensor);
        textView2 = (TextView) findViewById(R.id.textView3);

        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensor = sm.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            textView2.append("设备名称:"+sensor.getName() + "\n");//设备名称
            textView2.append("功率:"+sensor.getPower() + "\n");//功率
            textView2.append("设备供应商:"+sensor.getVendor() + "\n");//设备供应商
            textView2.append("设备版本号:"+sensor.getVersion() + "\n");//设备版本号
            textView2.append("传感器类型:"+sensor.getType() + "\n");//传感器类型
            textView2.append("最大取值范围:"+sensor.getMaximumRange() + "\n");//最大取值范围
            textView2.append("精度:"+sensor.getResolution() + "\n");//精度

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                textView2.append(sensor.getReportingMode() + "\n");
            }
            textView2.append(sensor.getFifoMaxEventCount() + "\n");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                textView2.append(sensor.getMaxDelay() + "\n");
                textView2.append(sensor.getMinDelay() + "\n");
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                textView2.append(sensor.getStringType() + "\n");
            }
        }
    }
}
