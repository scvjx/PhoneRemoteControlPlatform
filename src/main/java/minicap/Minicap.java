package minicap;

import adb.AdbDevice;
import com.android.ddmlib.*;
import common.Constant;
import dao.DevicesDao;
import dao.ParameterDao;
import org.springframework.stereotype.Component;
import service.DeviceListenerService;
import tools.LogTools;
import javax.annotation.Resource;
import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by jiaxin on 2019/5/17.
 * minicap的初始化实现
 */
@Component("Minicap")
public class Minicap {
    private static final String MINICAP_BIN = "minicap";
    private static final String MINICAP_SO = "minicap.so";
    private static final String REMOTE_PATH = "/data/local/tmp";
    private String MINICAP_START_COMMAND = "LD_LIBRARY_PATH=/data/local/tmp /data/local/tmp/minicap -P %s@%s/0 -Q %s";
    private String MINICAP_CLEAR_COMMAND = "rm -rf /data/local/tmp/mini*";
    private LogTools logTools = new LogTools();
    private ExecutorService es;
    private int minicapport;
    private String minicapPid = "0";
    private List<MinicapListener> listenerList = new ArrayList<MinicapListener>();

    @Resource
    private DeviceListenerService deviceListenerService;
    @Resource
    DevicesDao devicesDao;
    @Resource
    ParameterDao parameterDao;

    public void installMinicap(AdbDevice device) {
        if (device == null) {
            logTools.printLog("没有找到ADB设备！","error");
        }

//        if (isMinicapInstalled(device)) {
//            logTools.printLog("手机已安装MINICAP","info");
//            return;
//        }
        //先删除手机里存在的minicap文件，避免手机升级等因素造成minicap不匹配
        deviceListenerService.executeShellCommand(device.getIDevice(),MINICAP_CLEAR_COMMAND);
        String sdk = device.getProperty(Constant.PROP_SDK);
        String abi = device.getProperty(Constant.PROP_ABI);
        sdk = sdk.trim();
        abi = abi.trim();
        // minicap
        File minicap_bin = getMinicap(abi);
        if (minicap_bin == null || !minicap_bin.exists()) {
            logTools.printLog("没有找到对应的MINICAP BIN文件！","error");
        }
        try {
            deviceListenerService.executePushFile(device.getIDevice(), minicap_bin.getAbsolutePath(), REMOTE_PATH + "/" + MINICAP_BIN);
        } catch (Exception e) {
            logTools.printLog("MINICAP BIN文件推送手机设备失败！","error");
        }

        // minicap.so
        File minicap_so = getMinicapSo(abi, sdk);
        if (minicap_so == null || !minicap_so.exists()) {
            logTools.printLog("没有找到对应的MINICAP SO文件！","error");
        }

        try {
            deviceListenerService.executePushFile(device.getIDevice(), minicap_so.getAbsolutePath(), REMOTE_PATH + "/" + MINICAP_SO);
        } catch (Exception e) {
            logTools.printLog("MINICAP SO文件推送手机设备失败！","error");
        }
        //等待MINICAP文件传到手机中再继续
        boolean minicap_exist = false;
        while(!minicap_exist){
            String minicapbin = deviceListenerService.executeShellCommand(device.getIDevice(), " ls " + REMOTE_PATH + " | grep " + MINICAP_BIN);
            String minicapso = deviceListenerService.executeShellCommand(device.getIDevice(), " ls " + REMOTE_PATH + " | grep " + MINICAP_SO);
            if(minicapbin!=null && minicapbin.contains(MINICAP_BIN) && minicapso!=null && minicapso.contains(MINICAP_SO)){
                minicap_exist=true;
            }
        }
        deviceListenerService.executeShellCommand(device.getIDevice(), "chmod 777 " + REMOTE_PATH + "/" + MINICAP_BIN);
    }

    public boolean isMinicapInstalled(AdbDevice device) {
        if (device == null || device.getIDevice() == null) {
            return false;
        }
        String minicapbin = deviceListenerService.executeShellCommand(device.getIDevice(), " ls " + REMOTE_PATH + " | grep " + MINICAP_BIN);
        String minicapso = deviceListenerService.executeShellCommand(device.getIDevice(), " ls " + REMOTE_PATH + " | grep " + MINICAP_SO);

//        String s = deviceListenerService.executeShellCommand(device.getIDevice(), String.format("LD_LIBRARY_PATH=%s %s/%s -i", REMOTE_PATH, REMOTE_PATH, MINICAP_BIN));
        if(minicapbin!=null && minicapbin.contains(MINICAP_BIN) && minicapso!=null && minicapso.contains(MINICAP_SO)){
           return  true;
        }else{
            return false;
        }
    }

    public File getMinicap(String abi) {
        List<Map<String, Object>> paramlist = parameterDao.getParamByName(Constant.MINIPATH);
        String miniPath = (String) paramlist.get(0).get("value");
        return new File(miniPath+"minicap/bin/" + abi + "/minicap");
    }
    public File getMinicapSo(String abi, String sdk) {
        List<Map<String, Object>> paramlist = parameterDao.getParamByName(Constant.MINIPATH);
        String miniPath = (String) paramlist.get(0).get("value");
        return new File(miniPath+"minicap/shared/android-" + sdk + "/" + abi +  "/minicap.so");
    }

    public void startMinicapThread(final AdbDevice device, String virtualResolution, String quality){
        String size = device.getProperty("SCREEN_SIZE");
        final String startCommand = String.format(MINICAP_START_COMMAND,size, virtualResolution,quality);
        es = Executors.newCachedThreadPool();
        es.execute(new Runnable() {
            @Override
            public void run() {
                startMinicap(startCommand,device);
            }
        });
        List<Map<String, Object>> mobilephoneList = devicesDao.getDevicesByDeviceidModel(device.getSerialNumber(), device.getProperty("ro.product.model"));
        minicapport = Integer.parseInt((String) mobilephoneList.get(0).get("minicapport"));
        try {
            device.getIDevice().createForward(minicapport, "minicap", IDevice.DeviceUnixSocketNamespace.ABSTRACT);
        } catch (TimeoutException e) {
            logTools.printLog(e.getMessage(),"error");
        } catch (AdbCommandRejectedException e) {
            logTools.printLog(e.getMessage(),"error");
        } catch (IOException e) {
            logTools.printLog(e.getMessage(),"error");
        }
    }

    public void  startMinicap(final String startCommandStr, AdbDevice device) {
        try {
            device.getIDevice().executeShellCommand(startCommandStr, new IShellOutputReceiver() {
                @Override
                public void addOutput(byte[] bytes, int i, int i1) {
                    String res = new String(bytes,i , i1);
                    logTools.printLog("MINICAP 启动线程:"+startCommandStr+"返回："+ res,"info");
                    if("0".equals(minicapPid)){
                        int flag = res.indexOf("PID:");
                        String tempstr = res.substring(flag+5);
                        int flag1 = tempstr.indexOf("INFO");
                        minicapPid = tempstr.substring(0,flag1-1);
                        logTools.printLog("MINICAP PID"+ minicapPid,"info");
                    }
                }

                @Override
                public void flush() {
                }

                @Override
                public boolean isCancelled() {
                    return false;
                }
            },0);

        } catch (AdbCommandRejectedException e) {
            logTools.printLog(e.getMessage(),"error");
        } catch (ShellCommandUnresponsiveException e) {
            logTools.printLog(e.getMessage(),"error");
        } catch (IOException e) {
            logTools.printLog(e.getMessage(),"error");
        } catch (TimeoutException e) {
            logTools.printLog(e.getMessage(),"error");
        }
    }

    public void onClose(AdbDevice device,int port) {
        for (MinicapListener listener : listenerList) {
            listener.onClose(this);
        }
        removeForward(device,port);
        if(!"0".equals(minicapPid)){
            device.adbkill(minicapPid);
        }
    }

    public void removeForward(AdbDevice device,int port) {
        try {
            device.getIDevice().removeForward(port, "minicap", IDevice.DeviceUnixSocketNamespace.ABSTRACT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
