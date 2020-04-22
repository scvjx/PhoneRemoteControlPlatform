package websocket;

import adb.AdbDevice;
import dao.DevicesDao;
import minicap.MiniCapDataHandler;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import service.DeviceListenerService;
import tools.LogTools;
import javax.annotation.Resource;
import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * Created by jiaxin on 2019/5/28.
 * websocket链接处理数据实现
 */
public class WebsocketHandler  extends TextWebSocketHandler {
    private LogTools logTools  = new LogTools();
    private AdbDevice adbDevice;
    private MiniCapDataHandler miniCapDataHandler;
    private String serialNumber;
    @Resource
    private DeviceListenerService deviceListenerService;
    @Resource
    private DevicesDao devicesDao;
    @Override
    protected void handleTextMessage(WebSocketSession session,
                                     TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        //接收前台点击事件后调用进行点击处理
        String commandstr = message.getPayload();
        URI sn = session.getUri();
        serialNumber = sn.getQuery().split("=")[1];
        adbDevice = deviceListenerService.getDevice(serialNumber);
        String[] strarray = commandstr.split(" ");
        if("t".equals(strarray[0])){
            String x = strarray[1];
            String y = strarray[2];
            adbDevice.adbTap(x,y);
        }
        if("s".equals(strarray[0])){
            String startx = strarray[1];
            String starty = strarray[2];
            String endx = strarray[3];
            String endy = strarray[4];
            adbDevice.adbswipe(startx,starty,endx,endy,1);
        }
        if("p".equals(strarray[0])){
            String startx = strarray[1];
            String starty = strarray[2];
            String endx = strarray[3];
            String endy = strarray[4];
            adbDevice.adbswipe(startx,starty,endx,endy,2);
        }

    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        //建立websocket链接后直接开启minicap
        logTools.printLog("WebSocket链接已开启！","info");
        URI sn = session.getUri();
        serialNumber = sn.getQuery().split("=")[1];
        adbDevice = deviceListenerService.getDevice(serialNumber);
        getMinicapData(session);
    }

    private void getMinicapData(WebSocketSession session){
        List<Map<String, Object>> mobilephoneList = devicesDao.getDevicesByDeviceidModel(adbDevice.getSerialNumber(), adbDevice.getProperty("ro.product.model"));
        int minicapport = Integer.parseInt((String) mobilephoneList.get(0).get("minicapport"));
        miniCapDataHandler = new MiniCapDataHandler("127.0.0.1",minicapport);
        miniCapDataHandler.setRunning(true);
        miniCapDataHandler.RunImageConverterThread(session);
        miniCapDataHandler.RunImageBinaryFrameCollectorThread();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        logTools.printLog("WebSocket链接已关闭！","info");
        miniCapDataHandler.setRunning(false);
    }
}
