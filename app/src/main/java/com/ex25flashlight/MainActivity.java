package com.ex25flashlight;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    DeviceControl deviceControl;
    boolean is = true;
    ImageView imgFlashlight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgFlashlight = (ImageView) findViewById(R.id.img_flashlight);
//        imgFlashlight.setBackgroundResource(R.drawable.close);
//        imgFlashlight.setImageDrawable(getResources().getDrawable(R.drawable.guan));
        try {
            deviceControl = new DeviceControl(DeviceControl.MAIN_GPIO);

        } catch (IOException e) {
            e.printStackTrace();
        }
        imgFlashlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (is) {
                    is = false;
                    imgFlashlight.setBackgroundResource(R.drawable.open);
//                    imgFlashlight.setImageDrawable(getResources().getDrawable(R.drawable.kai));
                    deviceControl.PowerOnDevice("67");
                    deviceControl.PowerOnDevice("93");
                } else {
                    is = true;
                    imgFlashlight.setBackgroundResource(R.drawable.close);
//                    imgFlashlight.setImageDrawable(getResources().getDrawable(R.drawable.guan));
                    deviceControl.PowerOffDevice("67");
                    deviceControl.PowerOffDevice("93");
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getgpio();
        if (ispower){
            imgFlashlight.setBackgroundResource(R.drawable.open);
            is=false;
        }else {
            is = true;
            imgFlashlight.setBackgroundResource(R.drawable.close);
        }
    }

    private String[] poewro = {"67", "93"};
    private boolean ispower;
    public void getgpio() {
        List lists = new ArrayList();
        String line = null;
        try {
            InputStream inputStream = new FileInputStream("sys/class/misc/mtgpio/pin");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            for (int i = 1; i < 170; i++) {
                if ((line = reader.readLine()) != null) {
                    lists.add(line);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 1; i < lists.size(); i++) {
            String list = lists.get(i).toString();
            String gpio = list.substring(0, list.indexOf(":"));
            String upordown = list.substring(7, 8);
            for (int j = 0; j < poewro.length; j++) {
                if (poewro[j].equals(gpio.trim())) {//gpio去空格
                    if (upordown.equals("1")) {//上电
                        ispower = true;
                    } else if (upordown.equals("0")) {//下电
                        ispower = false;
                    }
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        deviceControl.PowerOffDevice("67");
        deviceControl.PowerOffDevice("93");
    }
}
