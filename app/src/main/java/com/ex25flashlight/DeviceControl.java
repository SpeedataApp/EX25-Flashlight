package com.ex25flashlight;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DeviceControl {
    private BufferedWriter CtrlFile;
    private String gpio = "";
    public static final String MAIN_GPIO = "sys/class/misc/mtgpio/pin";
    public static final String OUT_GPIO = "/sys/class/misc/aw9523/gpio";
    public DeviceControl(String path) throws IOException {
        File DeviceName = new File(path);
        CtrlFile = new BufferedWriter(new FileWriter(DeviceName, false));    //open file
    }

    public DeviceControl(String path, String gpio) throws IOException {
        this.gpio = gpio;
        File DeviceName = new File(path);
        CtrlFile = new BufferedWriter(new FileWriter(DeviceName, false));    //open file
    }

    public void PowerOnDevice(String gpio)        //poweron psam device
    {
        try {
            CtrlFile.write("-wmode" + gpio + " 0");   //将GPIO99设置为GPIO模式
            CtrlFile.flush();
            CtrlFile.write("-wdir" + gpio + " 1");        //将GPIO99设置为输出模式
            CtrlFile.flush();
            CtrlFile.write("-wdout" + gpio + " 1");   //上电IO口调整
            CtrlFile.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void PowerOffDevice(String gpio)     //poweroff psam device
    {
        try {

            CtrlFile.write("-wmode" + gpio + " 0");   //将GPIO99设置为GPIO模式
            CtrlFile.flush();
            CtrlFile.write("-wdir" + gpio + " 1");        //将GPIO99设置为输出模式
            CtrlFile.flush();
            CtrlFile.write("-wdout" + gpio + " 0");   //xia电IO口调整
            CtrlFile.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void PowerOnDeviceOut(String gpio)        //poweron psam device
    {
        try {
            CtrlFile.write(gpio + "on");   //上电IO口调整

            CtrlFile.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void PowerOffDeviceOut(String gpio)     //poweroff psam device
    {
        try {
            CtrlFile.write(gpio + "off");   //下电IO口调整
            CtrlFile.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void PsamResetDevice() throws IOException        //reset psam device
    {
        CtrlFile.write("-wdir44 1");
        CtrlFile.flush();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        CtrlFile.write("-wdout44 0");
        CtrlFile.flush();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        CtrlFile.write("-wdout44 1");
        CtrlFile.flush();
    }

    public void DeviceClose() throws IOException        //close file
    {
        CtrlFile.close();
    }
}