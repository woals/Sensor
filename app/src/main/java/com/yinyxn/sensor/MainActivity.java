package com.yinyxn.sensor;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    TextView textView2;
    Button button;
    Button button2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) this.findViewById(R.id.textView);
        textView2 = (TextView) this.findViewById(R.id.textView2);
        button = (Button) findViewById(R.id.button_orientation);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),OrientationSensorActivity.class));
            }
        });
        button2 = (Button) findViewById(R.id.button_sound);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SoundActivity.class));
            }
        });
        textView.setMovementMethod(ScrollingMovementMethod.getInstance());

        // 从系统服务中获得传感器管理器
        SensorManager sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // 从传感器管理器中获得全部的传感器列表
        List<Sensor> allSensors = sm.getSensorList(Sensor.TYPE_ALL);

        // 显示有多少个传感器
        textView.setText("该手机有" + allSensors.size() + "个传感器，他们分别是：\n");

        // 显示每个传感器的具体信息
        for (Sensor sensor : allSensors) {
            String tempString = "\n" + "  设备名称：" + sensor.getName() + "\n" + "  设备版本" + sensor.getVersion() + "\n" + "  供应商："
                    + sensor.getVendor() + "\n";
            switch (sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    textView.setText(textView.getText().toString() + sensor.getType() + " 加速度传感器accelerometer" + tempString);
                    break;
                case Sensor.TYPE_GYROSCOPE:
                    textView.setText(textView.getText().toString() + sensor.getType() + " 陀螺仪传感器gyroscope" + tempString);
                    break;
                case Sensor.TYPE_LIGHT:
                    textView.setText(textView.getText().toString() + sensor.getType() + " 环境光线传感器light" + tempString);
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    textView.setText(textView.getText().toString() + sensor.getType() + " 电磁场传感器magnetic field" + tempString);
                    break;
                case Sensor.TYPE_ORIENTATION:
                    textView.setText(textView.getText().toString() + sensor.getType() + " 方向传感器orientation" + tempString);
                    break;
                case Sensor.TYPE_PRESSURE:
                    textView.setText(textView.getText().toString() + sensor.getType() + " 压力传感器pressure" + tempString);
                    break;
                case Sensor.TYPE_PROXIMITY:
                    textView.setText(textView.getText().toString() + sensor.getType() + " 距离传感器proximity" + tempString);
                    break;
                case Sensor.TYPE_TEMPERATURE:
                    textView.setText(textView.getText().toString() + sensor.getType() + " 温度传感器temperature" + tempString);
                    break;
                default:
                    textView.setText(textView.getText().toString() + sensor.getType() + " 未知传感器" + tempString);
                    break;
            }
        }
    }
}
