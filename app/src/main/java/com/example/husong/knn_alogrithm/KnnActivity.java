package com.example.husong.knn_alogrithm;

import android.app.Activity;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class KnnActivity extends Activity implements SensorEventListener {

    private Button startButton;
    private TextView noticeText;
    //定义三个分类器：
    private knn_Cluster cluster_List[] =new knn_Cluster[3];
    //用于装待检测节点的容器
    private ArrayList<knn_Node>  node_List = new ArrayList<knn_Node>();

    private SensorManager sensorManager;
    private Sensor sensor;
    private float AccelX = 0;
    private float AccelY = 0;
    private float AccelZ = 0;
    private int n = 0;

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = (Button)findViewById(R.id.button);
        noticeText = (TextView)findViewById(R.id.noticeTextView);

        //1、得到sensorManager对象
        sensorManager=(SensorManager)this.getSystemService(SENSOR_SERVICE);
        //2、得到sensor
        sensor=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noticeText.setText("正在检测，请稍等");

                //从ContentProvider 中取不同状态的点装入不同的分类器

                //以下是run 的 点
                ArrayList<knn_Node>  run_List = new ArrayList<knn_Node>();
                //构造URI从ContentProvider中取得数据；
                Uri run_uri = Uri.parse("content://com.husong.example.myProvider/run");
                Cursor run_cursor = KnnActivity.this.getContentResolver().query(run_uri,null,null,null,null);
                if( (run_cursor!=null) && run_cursor.getCount() > 0){
                    while(run_cursor.moveToNext()){
                        String idstr = run_cursor.getString(0);
                        int index = Integer.parseInt(idstr);
                        double accx = Double.parseDouble(run_cursor.getString(1));
                        double accy = Double.parseDouble(run_cursor.getString(2));
                        double accz = Double.parseDouble(run_cursor.getString(3));
                        double dimension[] = {accx,accy,accz};
                        knn_Node run_node = new knn_Node( index,dimension);
                        run_List.add(run_node);
                    }
                }
                cluster_List[0] = new knn_Cluster("run", run_List);

                //以下是 walk 的点
                ArrayList<knn_Node>  walk_List = new ArrayList<knn_Node>();
                //构造URI从ContentProvider中取得数据；
                Uri walk_uri = Uri.parse("content://com.husong.example.myProvider/walk_List");
                Cursor walk_cursor = KnnActivity.this.getContentResolver().query(walk_uri,null,null,null,null);
                if( (walk_cursor!=null) && walk_cursor.getCount() > 0){
                    while(walk_cursor.moveToNext()){
                        String idstr = walk_cursor.getString(0);
                        int index = Integer.parseInt(idstr);
                        double accx = Double.parseDouble(walk_cursor.getString(1));
                        double accy = Double.parseDouble(walk_cursor.getString(2));
                        double accz = Double.parseDouble(walk_cursor.getString(3));
                        double dimension[] = {accx,accy,accz};
                        knn_Node walk_node = new knn_Node(index,dimension);
                        run_List.add(walk_node);
                    }
                }
                cluster_List[1] = new knn_Cluster("walk", walk_List);
                //以下是 sit 的点
                ArrayList<knn_Node>  sit_List = new ArrayList<knn_Node>();
                //构造URI从ContentProvider中取得数据；
                Uri sit_uri = Uri.parse("content://com.husong.example.myProvider/sit_List");
                Cursor sit_cursor = KnnActivity.this.getContentResolver().query(sit_uri,null,null,null,null);
                if( (sit_cursor!=null) && sit_cursor.getCount() > 0){
                    while(sit_cursor.moveToNext()){
                        String idstr = sit_cursor.getString(0);
                        int index = Integer.parseInt(idstr);
                        double accx = Double.parseDouble(sit_cursor.getString(1));
                        double accy = Double.parseDouble(sit_cursor.getString(2));
                        double accz = Double.parseDouble(sit_cursor.getString(3));
                        double dimension[] = {accx,accy,accz};
                        knn_Node sit_node = new knn_Node(index,dimension);
                        sit_List.add(sit_node);
                    }
                }
                cluster_List[2] = new knn_Cluster("walk", sit_List);
            }
        });
    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        // TODO Auto-generated method stub
        if (noticeText.getText().equals("正在检测，请稍等") && (node_List.size() < 200)) {
            AccelX = event.values[0];
            AccelY = event.values[1];
            AccelZ = event.values[2];
            n++;
            double dimension[] = {AccelX, AccelY, AccelZ};
            knn_Node knn_Node = new knn_Node(n, dimension);
            node_List.add(knn_Node);
        }else if(!(node_List.size()<200)) {
            sensorManager.unregisterListener(this);
            noticeText.setText("正在分析人体状态");
            if (noticeText.getText().equals("正在分析人体状态")) {
                Classify_Algorithm classify = new Classify_Algorithm(cluster_List, node_List);
                classify.calEuclideanDistanceSum(); //调用最短距离计算函数

                int runs = 0, sits = 0, walks = 0;
                for (int j = 0; j < node_List.size(); j++) {
                    if ((node_List.get(j).getClassification()).equals("run")) {
                        runs++;
                    } else if ((node_List.get(j).getClassification()).equals("walk")) {
                        walks++;
                    } else {
                        sits++;
                    }
                }
                if (runs <= walks && sits <= walks) {
                    noticeText.setText("您在走路！");
                } else if (walks <= runs && sits <= runs) {
                    noticeText.setText("您在跑步！");
                } else if (walks <= sits && runs <= sits) {
                    noticeText.setText("您坐着！");
                } else {
                    noticeText.setText("无法判断您的运动方式！");
                }
            }
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
