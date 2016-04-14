package com.yinyxn.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

public class SoundActivity extends AppCompatActivity {

    //录音和停止按钮
    private Button recordButton;
    private Button stopButton;

    //检测摇动相关变量
    private long initTime = 0;
    private long lastTime = 0;
    private long curTime = 0;
    private long duration = 0;

    private float last_x = 0.0f;
    private float last_y = 0.0f;
    private float last_z = 0.0f;

    private float shake = 0.0f;
    private float totalShake = 0.0f;

    //媒体录音器对象
    private MediaRecorder mr;

    //是否正在录音
    private boolean isRecoding = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound);

        // UI组件
        recordButton = (Button) this.findViewById(R.id.Button01);
        stopButton = (Button) this.findViewById(R.id.Button02);
        final TextView tx1 = (TextView) this.findViewById(R.id.TextView01);

        // 录音按钮点击事件
        recordButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //如果没有在录音，那么点击按钮可以开始录音
                if (!isRecoding) {
                    startRecord();
                }
            }
        });

        // 停止按钮点击事件
        stopButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                initShake();
                //如果正在录音，那么可以停止录音
                if (mr != null) {
                    mr.stop();
                    mr.release();
                    mr = null;
                    recordButton.setText("录音");
                    Toast.makeText(getApplicationContext(), "录音完毕", Toast.LENGTH_LONG).show();
                    isRecoding = false;

                }
            }
        });

        // 获取传感器管理器
        SensorManager sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        // 获取加速度传感器
        Sensor acceleromererSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // 定义传感器事件监听器
        SensorEventListener acceleromererListener = new SensorEventListener() {
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                //什么也不干
            }

            //传感器数据变动事件
            @Override
            public void onSensorChanged(SensorEvent event) {

                //如果没有开始录音的话可以监听是否有摇动事件，如果有摇动事件可以开始录音
                if (!isRecoding) {
                    //获取加速度传感器的三个参数
                    float x = event.values[SensorManager.DATA_X];
                    float y = event.values[SensorManager.DATA_Y];
                    float z = event.values[SensorManager.DATA_Z];
                    Log.d("tag",x+"\n"+y+"\n"+z+"\n");
                    // 水平x=0,y=0,z=10;水平反向z=-10;
                    // 上倾 y=10，下倾y=-10
                    // 左倾 x=10，右倾x=-10

                    //获取当前时刻的毫秒数
                    curTime = System.currentTimeMillis();
                    Log.d("time", "curTime"+String.valueOf(curTime));

                    //100毫秒检测一次
                    if ((curTime - lastTime) > 100) {

                        duration = (curTime - lastTime);

                        // 看是不是刚开始晃动
                        if (last_x == 0.0f && last_y == 0.0f && last_z == 0.0f) {
                            //last_x、last_y、last_z同时为0时，表示刚刚开始记录
                            initTime = System.currentTimeMillis();
                        } else {
                            // 单次晃动幅度
                            shake = (Math.abs(x - last_x) + Math.abs(y - last_y) + Math.abs(z - last_z)) / duration * 100;
                            Log.d("shake", "shake"+String.valueOf(shake));
                        }

                        //把每次的晃动幅度相加，得到总体晃动幅度
                        totalShake += shake;
                        Log.d("totalShake", "totalShake"+String.valueOf(totalShake));

                        // 判断是否为摇动，这是我自己写的标准，不准确，只是用来做教学示例，别误会了^_^
                        if (totalShake > 10 && totalShake / (curTime - initTime) * 1000 > 10) {
                            startRecord();
                            initShake();
                        }

                        tx1.setText("总体晃动幅度=" + totalShake + "\n平均晃动幅度=" + totalShake / (curTime - initTime) * 1000);
                    }

                    last_x = x;
                    last_y = y;
                    last_z = z;
                    lastTime = curTime;
                }
            }

        };

        //在传感器管理器中注册监听器
        sm.registerListener(acceleromererListener, acceleromererSensor, SensorManager.SENSOR_DELAY_NORMAL);

    }

    // 开始录音
    public void startRecord() {
        //把正在录音的标志设为真
        isRecoding = true;
        //存放文件
        File file = new File("/sdcard/" + "YY"
                + new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".amr");

        Toast.makeText(getApplicationContext(), "正在录音，录音文件在" + file.getAbsolutePath(), Toast.LENGTH_LONG).show();

        // 创建录音对象
        mr = new MediaRecorder();

        // 从麦克风源进行录音
        mr.setAudioSource(MediaRecorder.AudioSource.MIC);

        // 设置输出格式
        mr.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);

        // 设置编码格式
        mr.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        // 设置输出文件
        mr.setOutputFile(file.getAbsolutePath());

        try {
            // 创建文件
            file.createNewFile();
            // 准备录制
            mr.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 开始录制
        mr.start();
        recordButton.setText("录音中……");
    }

    //摇动初始化
    public void initShake() {
        lastTime = 0;
        duration = 0;
        curTime = 0;
        initTime = 0;
        last_x = 0.0f;
        last_y = 0.0f;
        last_z = 0.0f;
        shake = 0.0f;
        totalShake = 0.0f;
    }
}
