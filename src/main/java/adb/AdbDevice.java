package adb;

import com.android.ddmlib.*;
import common.Constant;
import tools.GetLogReceiver;
import tools.LogTools;

import javax.usb.UsbDevice;
import javax.usb.UsbException;
import javax.usb.UsbInterface;
import javax.usb.UsbInterfaceDescriptor;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jiaxin on 2019/5/13.
 */
public class AdbDevice {

    private LogTools logTools = new LogTools();
    /** Constant for ADB class. */
    private static final byte ADB_CLASS = (byte) 0xff;
    /** Constant for ADB sub class. */
    private static final byte ADB_SUBCLASS = 0x42;
    /** Constant for ADB protocol. */
    private static final byte ADB_PROTOCOL = 1;
    /** PropertyCahe KEY for serialNumber */
    public static final String SERIAL_NUMBER = "sn";
    /** PropertyCahe KEY for screenSize 获取的是 widthxheight的字符串 */
    public static final String SCREEN_SIZE = "SCREEN_SIZE";
    public static final String MANUFACTURER = "MANUFACTURER";
    public static final String MODEL = "MODEL";
    private static final int adbRetry = 5 ;

    /** The claimed USB ADB interface. */
    private final UsbInterface iface;

    /** The in endpoint address. */
    private final byte inEndpoint;

    /** The out endpoint address. */
    private final byte outEndpoint;

    /** Reference From UsbDevice */
    private final UsbDevice usbDevice;

    /**  */
    Map<String, String> propertyCahe = new HashMap<>();

    /** Device Type */
    public enum Type {
        USBDevice,
        Other
    }

    private final Type type;


    /** IDevice */
    private  IDevice iDevice;

    private  ExecutorService executor;

    private static GetLogReceiver getLogReceiver = new GetLogReceiver();

    private boolean isKilled =false;

    private String iii = "";
    /**
     * Constructs a new ADB interface.
     *
     * @param iface
     *            The USB interface. Must not be null.
     * @param inEndpoint
     *            The in endpoint address.
     * @param outEndpoint
     *            THe out endpoint address.
     */
    public AdbDevice(UsbDevice usbDevice, UsbInterface iface, byte inEndpoint, byte outEndpoint)
    {
        if (iface == null)
            throw new IllegalArgumentException("iface must be set");
        this.type = Type.USBDevice;
        this.usbDevice = usbDevice;
        this.iface = iface;
        this.inEndpoint = inEndpoint;
        this.outEndpoint = outEndpoint;
        executor = Executors.newFixedThreadPool(1);
        getLogReceiver = new GetLogReceiver();
    }

    public AdbDevice(IDevice iDevice,String iii) {
        this.type = Type.Other;
        this.usbDevice = null;
        this.iface = null;
        this.inEndpoint = 0;
        this.outEndpoint = 0;
        this.iii = iii;
        setIDevice(iDevice);
        executor = Executors.newFixedThreadPool(1);
    }

    /**
     * Checks if the specified vendor is an ADB device vendor.
     *
     * @param vendorId
     *            The vendor ID to check.
     * @return True if ADB device vendor, false if not.
     */
    public static boolean isAdbVendor(short vendorId) {
        for (short adbVendorId: Vendors.VENDOR_IDS)
            if (adbVendorId == vendorId) return true;
        return false;
    }

    /**
     * Checks if the specified USB interface is an ADB interface.
     *
     * @param iface
     *            The interface to check.
     * @return True if interface is an ADB interface, false if not.
     */
    public static boolean isAdbInterface(UsbInterface iface) {
        UsbInterfaceDescriptor desc = iface.getUsbInterfaceDescriptor();
        return desc.bInterfaceClass() == ADB_CLASS &&
                desc.bInterfaceSubClass() == ADB_SUBCLASS &&
                desc.bInterfaceProtocol() == ADB_PROTOCOL;
    }

    /**
     * 获取SerialNumber，由于使用频繁，故特增加此接口
     * @return
     */
    public String getSerialNumber() {
        String serialNUmber = findPropertyCahe(SERIAL_NUMBER);
        if (serialNUmber == null) {
            if (iDevice != null) {
                serialNUmber = iDevice.getSerialNumber();
            } else if (usbDevice != null) {
                try {
                    serialNUmber = usbDevice.getSerialNumberString();
                } catch (UsbException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            propertyCahe.put(SERIAL_NUMBER, serialNUmber);
        }
        return serialNUmber;
    }

    public UsbDevice getUsbDevice() {
        return usbDevice;
    }

    public IDevice getIDevice() {
        return iDevice;
    }

    /**
     * 设置IDevice，同时会获取常用信息放到缓存中
     * @param iDevice
     */
    public void setIDevice(IDevice iDevice) {
        this.iDevice = iDevice;
        fillPropertyCahe();
    }

    public String getProperty(String name) {
        String s = findPropertyCahe(name);
        if (s==null || "".equals(s)) {
            s = iDevice.getProperty(name);
        }
        return s;
    }

    /** 获取常用信息加入到缓存 */
    private void fillPropertyCahe() {

        // serialNumber
        propertyCahe.put(SERIAL_NUMBER, iDevice.getSerialNumber());

        // abi & sdk
        String abi = iDevice.getProperty(Constant.PROP_ABI);
        propertyCahe.put(Constant.PROP_ABI, abi);
        String sdk = iDevice.getProperty(Constant.PROP_SDK);
        propertyCahe.put(Constant.PROP_SDK, sdk);
        String manufacturer = iDevice.getProperty(Constant.PROP_MANU);
        propertyCahe.put(Constant.PROP_MANU,manufacturer);
        String model = iDevice.getProperty(Constant.PROP_MODEL);
        propertyCahe.put(Constant.PROP_MODEL,model);
        String shellCmd ="dumpsys window";
        String str = executeShellCommand(iDevice, shellCmd);
        if (str != null && !str.isEmpty()) {
            Pattern pattern =  Pattern.compile("init=(\\d+x\\d+)");
            Matcher m = pattern.matcher(str);
            if (m.find()) {
                propertyCahe.put(SCREEN_SIZE, m.group(1));
            }
        }
    }

    /**
     * 在缓存中查找属性值
     * @param key
     */
    public String findPropertyCahe(String key) {
        return propertyCahe.get(key);
    }


    public void adbTap (String x,String y){

        String cmd = "input tap "+x+" "+y;
        executeShellCommand(iDevice,cmd);
    }

    public void adbInput(String text){

        String cmd = "input text "+text;
        executeShellCommand(iDevice,cmd);
    }

    public void adbswipe(String sx,String sy,String ex,String ey,int type){
        if(type==1){
            //正常滑动
            String cmd = "input swipe "+sx+" "+sy+" "+ex+" "+ey;
            executeShellCommand(iDevice,cmd);
        }
        if(type==2){
            //长按屏幕
            String cmd = "input swipe "+sx+" "+sy+" "+ex+" "+ey+" 800";
            executeShellCommand(iDevice,cmd);
        }

    }

    public void adbkill(String pid){
        String cmd = "kill -9 "+pid;
        executeShellCommand(iDevice,cmd);
    }

    public boolean adbinstall(String apkfilepath){

        try {
            iDevice.installPackage(apkfilepath,true);
            return true;
        } catch (InstallException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void adbTapkeyevent(String eventnumber){
        String cmd = "input keyevent "+eventnumber;
        executeShellCommand(iDevice,cmd);
    }

    public  void  adblogcat(){
        executor.submit(new Runnable() {
            @Override
            public void run() {
                while (true){
                    String cmd = "logcat -v time";
                    try {
                        iDevice.executeShellCommand(cmd,getLogReceiver);
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    } catch (AdbCommandRejectedException e) {
                        e.printStackTrace();
                    } catch (ShellCommandUnresponsiveException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // 每间隔1秒重试一次
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (isKilled) {
                        break;
                    }
                }
            }
        });
    }

    public GetLogReceiver getGetLogReceiver(){
        return getLogReceiver;
    }

    private  String executeShellCommand(IDevice device, String command) {
        CollectingOutputReceiver output = new CollectingOutputReceiver();
        try {
            device.executeShellCommand(command, output, 0);
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (AdbCommandRejectedException e) {
                //每隔3秒重试指定次数ADB连接
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            executeShellCommand(device,command);
            logTools.printLog("ADB未授权","error");
            e.printStackTrace();
        } catch (ShellCommandUnresponsiveException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output.getOutput();
    }
}
