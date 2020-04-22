package service;

import adb.AdbDevice;
import adb.IAdbServerListener;
import com.android.ddmlib.*;
import common.Constant;
import dao.DevicesDao;
import dao.ParameterDao;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;
import tools.LogTools;

import javax.annotation.Resource;
import javax.usb.*;
import javax.usb.event.UsbServicesEvent;
import javax.usb.event.UsbServicesListener;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by jiaxin on 2019/5/9.
 * 通过USB监听和ADB的设备监听处理当前在线设备列表实现
 */
@Component("DeviceListenerService")
public class DeviceListenerService {

    private ExecutorService executor;
    private List<AdbDevice> adbDeviceList = new ArrayList<>();
    private AndroidDebugBridge adb = null;
    private List<IAdbServerListener> listeners = null;
    private String adbPath = null;
    private String adbPlatformTools = "platform-tools";
    private boolean success = false;
    private static boolean listenerres = false;
    private static int miniportoffset = 1;
    @Resource
    DevicesDao devicesDao;
    @Resource
    ParameterDao parameterDao;
    private  LogTools logTools = new LogTools();

    private void init() {
        logTools.printLog("开始初始化","info");
        AndroidDebugBridge.initIfNeeded(false);//如果之前没初始化过时候初始化ADB
        if(AndroidDebugBridge.getBridge()==null){//只有第一次启动服务才做下面操作，刷新页面不做
            //先把设备表所有设备状态置0下线
            HashMap args = new HashMap();
            args.put("status","0");
            devicesDao.updateDevicesStatus(args);
            listenerres = false;
            adb = AndroidDebugBridge.createBridge(getADBPath(), true);
            listeners = new ArrayList<>();
            if (adb != null) {
                if (waitForDeviceList()) {
                    success = true;
                }
            }
            //初始化清空之前数据库所有设备的MINICAP的端口和分辨率重新分配，以及所有设别的使用状态
            HashMap args1 = new HashMap();
            args1.put("minicapport","");
            args1.put("adbkitport","");
            args1.put("status","0");
            devicesDao.updateDevicesStatusPort(args1);
        }
    }

    private boolean waitForDeviceList() {
        int maxWaittingTime = 100;
        int interval = 10;
        if(adb!=null) {
            while (!adb.hasInitialDeviceList()) {
                try {
                    Thread.sleep(interval);
                } catch (InterruptedException e) {
                    break;
                }
                maxWaittingTime -= 1;
                if (maxWaittingTime == 0) {
                    disconnectAdb();
                    return false;
                }
            }
        }else{
            logTools.printLog("adb连接错误，无法获取adb对象！！！","error");
            return false;
        }
        return true;
    }

    void disconnectAdb() {
        if (adb != null) {
            AndroidDebugBridge.disconnectBridge();
            adb = null;
        }
        success = false;
    }

    private String getADBPath(){
        if (adbPath == null){
            adbPath = System.getenv("ANDROID_SDK_ROOT");
            if(adbPath != null){
                adbPath += File.separator + adbPlatformTools;
            }else {
                adbPath = "adb";
                return adbPath;
            }
        }
        //adbPath += File.separator + "adb";
        return adbPath;
    }

    public void listenUSB(){
        init();
        logTools.printLog("========start usb listen!=======","info");
        executor = Executors.newFixedThreadPool(100);
        usbListenerThread();
    }

    public void listenADB(){
    executor.submit(new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(new Constant().REFRESHADBINTRAVAL);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String iii = RandomStringUtils.randomAlphanumeric(10);
                refreshAdbDeviceList(iii);
            }
        }
    });
    }

    private void usbListenerThread() {
                logTools.printLog("========start usb listen thread!=======","info");
                UsbServices services = null;
                try {
                    //启动USB监听线程
                    services = UsbHostManager.getUsbServices();
                } catch (UsbException e) {
                    e.printStackTrace();
                }
                services.addUsbServicesListener(new myUsbListener());
                logTools.printLog("已开启USB设备监听...","info");
            }

    class myUsbListener implements UsbServicesListener {

        @Override
        public void usbDeviceAttached(UsbServicesEvent usbServicesEvent) {
            UsbDevice device = usbServicesEvent.getUsbDevice();
            if (!device.isUsbHub()) {
                onUsbDeviceConnected(device);
            }
        }
        @Override
        public void usbDeviceDetached(UsbServicesEvent usbServicesEvent) {
            UsbDevice device = usbServicesEvent.getUsbDevice();
            if (!device.isUsbHub()) {
                onUsbDeviceDisConnected(device);
            }
        }
    }

    private List<AdbDevice> checkAdbDevices(UsbDevice usbDevice) {

        List<AdbDevice> adbDevices = new ArrayList<>();

        UsbDeviceDescriptor deviceDesc = usbDevice.getUsbDeviceDescriptor();

        // Ignore devices from Non-ADB vendors
        // Check interfaces of device
        UsbConfiguration config = usbDevice.getActiveUsbConfiguration();
        for (UsbInterface iface: (List<UsbInterface>) config.getUsbInterfaces())
        {
            List<UsbEndpoint> endpoints = iface.getUsbEndpoints();

            // Ignore interface if it does not have two endpoints
            if (endpoints.size() != 2) continue;

            // Ignore interface if it does not match the ADB specs
            if (!AdbDevice.isAdbInterface(iface)) continue;

            UsbEndpointDescriptor ed1 =
                    endpoints.get(0).getUsbEndpointDescriptor();
            UsbEndpointDescriptor ed2 =
                    endpoints.get(1).getUsbEndpointDescriptor();

            // Ignore interface if endpoints are not bulk endpoints
            if (((ed1.bmAttributes() & UsbConst.ENDPOINT_TYPE_BULK) == 0) ||
                    ((ed2.bmAttributes() & UsbConst.ENDPOINT_TYPE_BULK) == 0))
                continue;

            // Determine which endpoint is in and which is out. If both
            // endpoints are in or out then ignore the interface
            byte a1 = ed1.bEndpointAddress();
            byte a2 = ed2.bEndpointAddress();
            byte in, out;
            if (((a1 & UsbConst.ENDPOINT_DIRECTION_IN) != 0) &&
                    ((a2 & UsbConst.ENDPOINT_DIRECTION_IN) == 0)) {
                in = a1;
                out = a2;
            } else if (((a2 & UsbConst.ENDPOINT_DIRECTION_IN) != 0) &&
                    ((a1 & UsbConst.ENDPOINT_DIRECTION_IN) == 0)) {
                out = a1;
                in = a2;
            } else {
                continue;
            }

            adbDevices.add(new AdbDevice(usbDevice, iface, in, out));
        }
        return adbDevices;
    }

    /**
     * USB设备连接时调用
     */
    private void onUsbDeviceConnected(UsbDevice usbDevice) {
        List<AdbDevice> devices = checkAdbDevices(usbDevice);
        devices.forEach(adbDevice -> onAdbDeviceConnected(adbDevice));
    }

    /**
     * USB设备断开时调用
     */
    private void onUsbDeviceDisConnected(UsbDevice usbDevice) {
        List<AdbDevice> devices = checkAdbDevices(usbDevice);
        devices.forEach(adbDevice -> onAdbDeviceDisConnected(adbDevice));
    }


    /**
     * 发现安卓设备时调用
     */
    private void onAdbDeviceConnected(AdbDevice adbDevice) {
        String iii = RandomStringUtils.randomAlphanumeric(15);
      //  refreshAdbDeviceList(iii);
    }

    /**
     * 与adb同步设备状态
     */
    private void refreshAdbDeviceList(String iii) {

            List<AdbDevice> tmpAdbDeviceList = new ArrayList<>(this.adbDeviceList);
            IDevice[] iDevices = getIDevices();
            List<Map<String, Object>> paramlist = parameterDao.getParamByName(Constant.MINICAPBASEPORT);
            int minicapBasePort = Integer.parseInt((String) paramlist.get(0).get("value"));
            List<Map<String, Object>> paramlist1 = parameterDao.getParamByName(Constant.ADBKITBASEPORT);
            int adbkitBasePort = Integer.parseInt((String) paramlist1.get(0).get("value"));
            // 添加新的adb设备


            for (IDevice iDevice : iDevices) {
                boolean exists = false;
                for (AdbDevice adbDev : tmpAdbDeviceList) {
                    if (adbDev.getIDevice().getSerialNumber().equals(iDevice.getSerialNumber())) {
                        exists = true;
                        break;
                    }
                }

                // 如果在已有列表不存在，添加到已有列表
                if (!exists) {
                    AdbDevice device = new AdbDevice(iDevice,iii);
                    logTools.printLog("Android设备连接：" +device.getSerialNumber(),"info");
                    if(device.getProperty("SCREEN_SIZE")==null){
                        //因为有的设备插入后需要ADB授权才可以用，所以在未授权情况下可能造成获取device属性null，因此需要重生成实例
                        device = new AdbDevice(iDevice,iii);
                    }
                    this.adbDeviceList.add(device);
                    AdbDevice finalDevice = device;
                    listeners.forEach(l -> l.onAdbDeviceConnected(finalDevice));
                    List<Map<String, Object>> devicelist = devicesDao.getDevicesByDeviceidModel(device.getSerialNumber(),device.getProperty(Constant.PROP_MODEL));
                    String minicapport = Integer.toString(minicapBasePort+miniportoffset);
                    String adbkitport = Integer.toString(adbkitBasePort+miniportoffset);
                    miniportoffset++;
                    if(devicelist.size()==0){//如果新手机就插入表否则不插入
                        HashMap args = new HashMap();
                        args.put("deviceid",device.getSerialNumber());
                        args.put("resolution",device.getProperty("SCREEN_SIZE"));
                        args.put("androidversion",device.getProperty(Constant.PROP_SDK));
                        args.put("cpu",device.getProperty(Constant.PROP_ABI));
                        args.put("manufacturer",device.getProperty(Constant.PROP_MANU));
                        args.put("model",device.getProperty(Constant.PROP_MODEL));
                        args.put("status","1");
                        args.put("userid","");
                        args.put("lastusetime","");
                        args.put("minicapport",minicapport);
                        args.put("adbkitport",adbkitport);
                        devicesDao.insertDevices(args);
                    }else{
                        //如果旧手机则更新一下手机的状态为1-在线和minicap端口
                        HashMap args = new HashMap();
                        args.put("deviceid",device.getSerialNumber());
                        args.put("model",device.getProperty(Constant.PROP_MODEL));
                        args.put("status","1");
                        args.put("minicapport",minicapport);
                        args.put("adbkitport",adbkitport);
                        devicesDao.updateDevicesStatusPortByDeviceidModel(args);
                    }
                }
            }
            listenerres=true;
            // 移除已断开的设备
            for (AdbDevice adbDev : tmpAdbDeviceList) {
                boolean exists = false;
                for (IDevice iDevice : iDevices) {
                    if (adbDev.getIDevice().getSerialNumber().equals(iDevice.getSerialNumber())) {
                        exists = true;
                        break;
                    }
                }
                // 如果在已有列表不存在，添加到已有列表
                if (!exists) {
                    onAdbDeviceDisConnected(adbDev);
                }
            }
    }

    /**
     * 发现安卓设备断开时调用
     */
    private void onAdbDeviceDisConnected(AdbDevice adbDevice) {
        for (Iterator it = adbDeviceList.iterator(); it.hasNext(); ) {
            AdbDevice device = (AdbDevice) it.next();
            if (adbDevice.getUsbDevice() == device.getUsbDevice()) {
                logTools.printLog("Android设备断开：" + adbDevice.getSerialNumber(),"info");
                it.remove();
                listeners.forEach(l -> l.onAdbDeviceDisConnected(device));
                //如果更新一下手机的状态为0
                HashMap args = new HashMap();
                args.put("deviceid",device.getSerialNumber());
                args.put("model",device.getProperty(Constant.PROP_MODEL));
                args.put("status","0");
                args.put("minicapport","");
                args.put("adbkitport","");
                devicesDao.updateDevicesStatusPortByDeviceidModel(args);
            }
        }
    }
    /**
     * 获取ADB命令返回的设备列表
     * @return IDevices
     */
    public IDevice[] getIDevices() {
        return adb.getDevices();
    }

    public boolean getListenerres() {return this.listenerres;}

    public void executePushFile(final IDevice device, final String src, final String dst) {
        final File adbFile = new File(adbPath);
        ExecutorService es = Executors.newCachedThreadPool();
        es.execute(new Runnable() {
            @Override
            public void run() {
                ProcessBuilder pb = new ProcessBuilder(new String[]{adbFile.getPath(), "-s", device.getSerialNumber(), "push", src, dst});
                pb.redirectErrorStream(true);
                try {
                    pb.start();
                } catch (IOException e) {
                    logTools.printLog("推送文件失败","error");
                    return;
                }
            }
        });
    }

    public  String executeShellCommand(IDevice device, String command) {
        CollectingOutputReceiver output = new CollectingOutputReceiver();
        try {
            device.executeShellCommand(command, output, 0);
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (AdbCommandRejectedException e) {
            e.printStackTrace();
        } catch (ShellCommandUnresponsiveException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output.getOutput();
    }
    public void addListener(IAdbServerListener listener) {
        this.listeners.add(listener);
    }
    public List<AdbDevice>  getAdbDeviceList(){
        return this.adbDeviceList;
    }

    public AdbDevice getDevice(String serialNumber) {
        for (AdbDevice device : adbDeviceList) {
            if (device.getSerialNumber().equals(serialNumber)) {
                return device;
            }
        }
        return null;
    }

}
