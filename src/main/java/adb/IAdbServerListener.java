package adb;

/**
 * Created by jiaxin on 2019/5/13.
 */
public interface IAdbServerListener {
    void onAdbDeviceConnected(AdbDevice device);
    void onAdbDeviceDisConnected(AdbDevice device);
}
